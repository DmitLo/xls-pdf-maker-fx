package payment.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import payment.WebpayPaymentManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class WebpayStatusChecker {

    private static final String API_STATUS_URL = "https://webpay.by";

    // Настройки ограничений по времени
    private static final int POLL_INTERVAL_SECONDS = 5; // Проверка каждые 5 секунд
    private static final int MAX_WAIT_MINUTES = 15;     // Максимальное время ожидания — 15 минут

    // Вычисляем максимальное число проверок (15 минут * 60 секунд / 5 секунд = 180 итераций)
    private static final int MAX_ITERATIONS = (MAX_WAIT_MINUTES * 60) / POLL_INTERVAL_SECONDS;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private ScheduledExecutorService scheduler;

    // Атомарный счетчик для безопасного подсчета шагов в фоновом потоке
    private final AtomicInteger currentIteration = new AtomicInteger(0);

    private final String apiUsername;
    private final String apiPassword;

    // Добавьте это поле в начало класса WebpayStatusChecker
    private final AtomicInteger consecutiveErrors = new AtomicInteger(0);
    private static final int MAX_CONSECUTIVE_ERRORS = 5; // Максимум 5 ошибок подряд (около 25 секунд без интернета)



    public WebpayStatusChecker(String apiUsername, String apiPassword) {
        this.apiUsername = apiUsername;
        this.apiPassword = apiPassword;
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
        this.objectMapper = new ObjectMapper();
    }

    public void startPolling(String orderNumber, WebpayPaymentManager.PaymentCallback callback) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
        scheduler = Executors.newSingleThreadScheduledExecutor();
        currentIteration.set(0);
        consecutiveErrors.set(0); // Сбрасываем счетчик ошибок при новом старте

        System.out.println("Запущен фоновый трекер статуса с защитой от обрыва сети...");

        scheduler.scheduleAtFixedRate(() -> {
            try {
                // 1. Проверка общего таймаута (15 минут)
                if (currentIteration.incrementAndGet() > MAX_ITERATIONS) {
                    stopPolling();
                    callback.onFailed("Время сессии оплаты истекло. Пожалуйста, попробуйте снова.");
                    return;
                }

                // 2. Попытка запроса к API
                String status = requestTransactionStatus(orderNumber);

                // Если запрос прошел успешно — сбрасываем счетчик сетевых ошибок в 0
                consecutiveErrors.set(0);

                System.out.println("Проверка #" + currentIteration.get() + ". Статус: " + status);

                // 3. Анализ статуса
                if ("completed".equalsIgnoreCase(status) || "approved".equalsIgnoreCase(status)) {
                    stopPolling();
                    callback.onSuccess();
                } else if ("declined".equalsIgnoreCase(status) || "failed".equalsIgnoreCase(status)) {
                    stopPolling();
                    callback.onFailed("Оплата отклонена банком или отменена пользователем.");
                }

            } catch (Exception e) {
                // СЮДА мы попадаем, если пропал интернет (java.net.ConnectException, UnknownHostException и т.д.)
                System.err.println("⚠️ Временная ошибка сети при опросе WEBPAY: " + e.getMessage());

                // Увеличиваем счетчик ошибок подряд
                int errors = consecutiveErrors.incrementAndGet();

                if (errors >= MAX_CONSECUTIVE_ERRORS) {
                    System.out.println("❌ Соединение потеряно. Превышено число попыток подключения.");
                    stopPolling();
                    callback.onFailed("Ошибка соединения с сервером платежей. Проверьте подключение к интернету.");
                } else {
                    System.out.println("Повторное подключение через 5 секунд... (Попытка " + errors + " из " + MAX_CONSECUTIVE_ERRORS + ")");
                }
            }
        }, 3, POLL_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    private String requestTransactionStatus(String orderNumber) throws Exception {
        String url = API_STATUS_URL + "/api/v1/transactions/info/invoice/" + orderNumber;
        String authStr = apiUsername + ":" + apiPassword;
        String encodedAuth = Base64.getEncoder().encodeToString(authStr.getBytes(StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Basic " + encodedAuth)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();

        if (statusCode == 200) {
            WebpayTransactionInfo txInfo = objectMapper.readValue(response.body(), WebpayTransactionInfo.class);
            return txInfo.getStatus() != null ? txInfo.getStatus() : "unknown";
        } else if (statusCode == 404) {
            return "pending_payment"; // Пользователь еще заполняет данные карты на сайте
        } else {
            throw new RuntimeException("HTTP Error: " + statusCode);
        }
    }

    public void stopPolling() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            System.out.println("Фоновый трекер успешно остановлен.");
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class WebpayTransactionInfo {
        @JsonProperty("status") private String status;
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}


//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.nio.charset.StandardCharsets;
//import java.time.Duration;
//import java.util.Base64;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//public class WebpayStatusChecker {
//
//    private static final String API_STATUS_URL = "https://webpay.by";
//
//    private final HttpClient httpClient;
//    private final ObjectMapper objectMapper;
//    private ScheduledExecutorService scheduler;
//
//    private final String apiUsername;
//    private final String apiPassword;
//
//    public WebpayStatusChecker(String apiUsername, String apiPassword) {
//        this.apiUsername = apiUsername;
//        this.apiPassword = apiPassword;
//        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
//        this.objectMapper = new ObjectMapper();
//    }
//
//    public void startPolling(String orderNumber, WebpayPaymentManager.PaymentCallback callback) {
//        if (scheduler != null && !scheduler.isShutdown()) {
//            scheduler.shutdownNow();
//        }
//        scheduler = Executors.newSingleThreadScheduledExecutor();
//
//        scheduler.scheduleAtFixedRate(() -> {
//            try {
//                String status = requestTransactionStatus(orderNumber);
//                System.out.println("Статус из API для " + orderNumber + ": " + status);
//
//                if ("completed".equalsIgnoreCase(status) || "approved".equalsIgnoreCase(status)) {
//                    stopPolling();
//                    callback.onSuccess();
//                } else if ("declined".equalsIgnoreCase(status) || "failed".equalsIgnoreCase(status)) {
//                    stopPolling();
//                    callback.onFailed("Оплата отклонена банком или отменена.");
//                }
//            } catch (Exception e) {
//                System.err.println("Ошибка опроса статуса: " + e.getMessage());
//            }
//        }, 3, 5, TimeUnit.SECONDS);
//    }
//
//    private String requestTransactionStatus(String orderNumber) throws Exception {
//        String url = API_STATUS_URL + "/api/v1/transactions/info/invoice/" + orderNumber;
//        String authStr = apiUsername + ":" + apiPassword;
//        String encodedAuth = Base64.getEncoder().encodeToString(authStr.getBytes(StandardCharsets.UTF_8));
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .header("Authorization", "Basic " + encodedAuth)
//                .header("Accept", "application/json")
//                .GET()
//                .build();
//
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//        int statusCode = response.statusCode();
//
//        if (statusCode == 200) {
//            WebpayTransactionInfo txInfo = objectMapper.readValue(response.body(), WebpayTransactionInfo.class);
//            return txInfo.getStatus() != null ? txInfo.getStatus() : "unknown";
//        } else if (statusCode == 404) {
//            return "pending_payment";
//        } else {
//            throw new RuntimeException("HTTP Error: " + statusCode);
//        }
//    }
//
//    public void stopPolling() {
//        if (scheduler != null && !scheduler.isShutdown()) {
//            scheduler.shutdown();
//        }
//    }
//
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    private static class WebpayTransactionInfo {
//        @JsonProperty("status") private String status;
//        public String getStatus() { return status; }
//        public void setStatus(String status) { this.status = status; }
//    }
//}


//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.time.Duration;
//import java.util.Base64;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;

//public class WebpayStatusChecker {
//
//    // Тестовая среда: https://sandbox.webpay.by
//    // Реальная среда: https://billing.webpay.by
//    private static final String BASE_URL = "https://sandbox.webpay.by";
//
//    private final HttpClient httpClient;
//    private final ScheduledExecutorService scheduler;
//
//    // Данные авторизации из личного кабинета (учетные данные API Управления операциями)
//    private final String apiUsername = "your_api_username";
//    private final String apiPassword = "your_api_password";
//
//    public WebpayStatusChecker() {
//        this.httpClient = HttpClient.newBuilder()
//                .connectTimeout(Duration.ofSeconds(10))
//                .build();
//        this.scheduler = Executors.newSingleThreadScheduledExecutor();
//    }
//
//    /**
//     * Запуск циклической проверки платежа раз в 5 секунд.
//     *
//     * @param orderNumber Номер заказа (тот же самый, что уходил в wsb_order_num)
//     */
//    public void startPollingPaymentStatus(String orderNumber) {
//        System.out.println("Запущен фоновый опрос статуса для заказа: " + orderNumber);
//
//        // Планируем задачу на выполнение каждые 5 секунд
//        scheduler.scheduleAtFixedRate(() -> {
//            try {
//                String status = checkStatus(orderNumber);
//                System.out.println("Текущий статус счета " + orderNumber + ": " + status);
//
//                if ("completed".equalsIgnoreCase(status) || "approved".equalsIgnoreCase(status) || "success".equalsIgnoreCase(status)) {
//                    System.out.println("🎉 Оплата успешна! Активируем функции в приложении.");
//                    stopPolling();
//                    // ТУТ: Вызовите ваш метод разблокировки интерфейса/функций
//                } else if ("declined".equalsIgnoreCase(status) || "failed".equalsIgnoreCase(status)) {
//                    System.out.println("❌ Оплата отклонена банком или отменена пользователем.");
//                    stopPolling();
//                }
//
//                // Если статус "pending" или "waiting" — ничего не делаем, ждем следующей итерации
//
//            } catch (Exception e) {
//                System.err.println("Ошибка при проверке статуса: " + e.getMessage());
//            }
//        }, 0, 5, TimeUnit.SECONDS);
//    }
//
//    /**
//     * Прямой запрос к JSON API WEBPAY
//     */
//    private String checkStatus(String orderNumber) throws Exception {
//        String url = BASE_URL + "/api/v1/transactions/info/invoice/" + orderNumber;
//
//        // Авторизация в API WEBPAY требует заголовок Basic Auth (Base64 от login:password)
//        String authStr = apiUsername + ":" + apiPassword;
//        String encodedAuth = Base64.getEncoder().encodeToString(authStr.getBytes());
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .header("Authorization", "Basic " + encodedAuth)
//                .header("Accept", "application/json")
//                .GET()
//                .build();
//
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//
//        int statusCode = response.statusCode();
//        if (statusCode == 200) {
//            String jsonResponse = response.body();
//            // Возвращает JSON-строку. Для продакшена лучше использовать парсер (Jackson, Gson),
//            // но для примера извлечем значение поля "status" простой строковой обработкой:
//            return parseStatusFromJson(jsonResponse);
//        } else if (statusCode == 404) {
//            return "not_found"; // Счет создан, но пользователь еще не ввел карту на платежной странице
//        } else {
//            throw new RuntimeException("Неожиданный ответ сервера: HTTP " + statusCode);
//        }
//    }
//
//    private String parseStatusFromJson(String json) {
//        // Простейший поиск строки вроде "status":"approved" без тяжелых библиотек
//        if (json.contains("\"status\"")) {
//            int index = json.indexOf("\"status\"");
//            int start = json.indexOf("\"", index + 8) + 1;
//            int end = json.indexOf("\"", start);
//            return json.substring(start, end);
//        }
//        return "unknown";
//    }
//
//    public void stopPolling() {
//        if (!scheduler.isShutdown()) {
//            scheduler.shutdown();
//            System.out.println("Опрос статуса платежа остановлен.");
//        }
//    }
//
//    public static void main(String[] args) {
//        WebpayStatusChecker checker = new WebpayStatusChecker();
//        // Передаем номер счета, который мы сгенерировали для оплаты
//        checker.startPollingPaymentStatus("INV-1711812345");
//    }
//}

//import payment.dto.WebpayTransactionInfo;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//
//public class WebpayStatusChecker {
//
//    private static final String BASE_URL = "https://webpay.by";
//
//    private final HttpClient httpClient;
//    private final ScheduledExecutorService scheduler;
//    private final ObjectMapper objectMapper; // Инициализируем Jackson
//
//    private final String apiUsername = "your_api_username";
//    private final String apiPassword = "your_api_password";
//
//    public WebpayStatusChecker() {
//        this.httpClient = HttpClient.newBuilder()
//                .connectTimeout(Duration.ofSeconds(10))
//                .build();
//        this.scheduler = Executors.newSingleThreadScheduledExecutor();
//        this.objectMapper = new ObjectMapper(); // Инициализация маппера
//    }
//
//    public void startPollingPaymentStatus(String orderNumber) {
//        System.out.println("Запущен фоновый опрос статуса для заказа: " + orderNumber);
//
//        scheduler.scheduleAtFixedRate(() -> {
//            try {
//                String status = checkStatus(orderNumber);
//                System.out.println("Текущий статус счета " + orderNumber + ": " + status);
//
//                // Стандартные успешные статусы в системе WEBPAY: "completed" или "approved"
//                if ("completed".equalsIgnoreCase(status) || "approved".equalsIgnoreCase(status)) {
//                    System.out.println("🎉 Оплата успешна! Активируем функции в приложении.");
//                    stopPolling();
//                    // ТУТ: Логика успешной активации (например, Platform.runLater() для JavaFX UI)
//                } else if ("declined".equalsIgnoreCase(status) || "failed".equalsIgnoreCase(status)) {
//                    System.out.println("❌ Оплата отклонена банком или отменена пользователем.");
//                    stopPolling();
//                }
//
//            } catch (Exception e) {
//                System.err.println("Ошибка при проверке статуса: " + e.getMessage());
//            }
//        }, 0, 5, TimeUnit.SECONDS);
//    }
//
//    private String checkStatus(String orderNumber) throws Exception {
//        String url = BASE_URL + "/api/v1/transactions/info/invoice/" + orderNumber;
//
//        String authStr = apiUsername + ":" + apiPassword;
//        String encodedAuth = Base64.getEncoder().encodeToString(authStr.getBytes());
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .header("Authorization", "Basic " + encodedAuth)
//                .header("Accept", "application/json")
//                .GET()
//                .build();
//
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//
//        int statusCode = response.statusCode();
//        if (statusCode == 200) {
//            // Десериализуем JSON ответ в наш Java-объект с помощью Jackson
//            WebpayTransactionInfo txInfo = objectMapper.readValue(response.body(), WebpayTransactionInfo.class);
//            return txInfo.getStatus() != null ? txInfo.getStatus() : "unknown";
//        } else if (statusCode == 404) {
//            return "pending_payment"; // Клиент еще не дошел до ввода карты, транзакции в базе нет
//        } else {
//            throw new RuntimeException("Неожиданный ответ сервера: HTTP " + statusCode);
//        }
//    }
//
//    public void stopPolling() {
//        if (!scheduler.isShutdown()) {
//            scheduler.shutdown();
//            System.out.println("Опрос статуса платежа остановлен.");
//        }
//    }
//}


