package payment.test;

import payment.WebpayStatusChecker;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WebpayPaymentManager_test {

    // Ссылки на подключаемые сервисы
    private final WebpayPaymentServ paymentService;
    private final WebpayStatusChecker statusChecker;

    public interface PaymentCallback {
        void onSuccess();

        void onFailed(String reason);
    }

    public WebpayPaymentManager_test() {
        // Конфигурационные данные мерчанта
//        String storeId = "433329306";
        Integer storeId = 433329306;
        String apiUsername = "gandal";
        String apiPassword = "Sb67Yp78E";

        // Секретный ключ в байтах
        byte[] encryptedKey = new byte[]{55, 32, 19, 21, 50, 62, 50, 61, 48, 62, 108, 111, 104, 110, 108, 109, 50, 61, 60, 41, 45, 48, 50, 29, 18, 30, 28};
        byte xorMask = 0x5A;

        // Инициализируем сервисы внутри конструктора менеджера
        this.paymentService = new WebpayPaymentServ(storeId, encryptedKey, xorMask);
        this.statusChecker = new WebpayStatusChecker(apiUsername, apiPassword);
    }

    /**
     * Точка входа для вашего UI
     */
    public void processPayment(String currency, String[] itemName, Integer[] quantity, Float[] itemPrice, Float total, PaymentCallback callback) {

        String orderNum = "INV-" + System.currentTimeMillis();
        Integer testMode = 1; // 1 - тест, 0 - боевой
        Integer version = 2;

        try {
            // 1. Используем первый сервис для генерации ссылки
            //String paymentUrl = paymentService.createPaymentUrl(orderNum, amount, currency, testMode);
            //System.out.println("paymentUrl " + paymentUrl);
            String json = paymentService.createPaymentUrl(orderNum, currency, version, testMode, itemName, quantity, itemPrice, total);
            System.out.println(json);
            HttpResponse<String> response = post(json);

            //открытие в браузе страницы ответа
            openHtmlInBrowser(response.body());


            if (response.statusCode() == 200) {
                String responseBody = response.body();
                System.out.println("Ответ от WebPay получен успешно!");

                String redirectUrl = parseRedirectUrl(responseBody);

                if (redirectUrl != null && !redirectUrl.isEmpty()) {
                    System.out.println("Перенаправляем пользователя на форму: " + redirectUrl);
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().browse(new URI(redirectUrl));
                    }
                } else {
                    System.err.println("В ответе не найден redirectUrl. Ответ: " + responseBody);
                }
            } else {
                // Если код 400 повторится, тут напечатается точная причина ответа WebPay
                System.err.println("Ошибка API WebPay. Код ответа: " + response.statusCode());
                System.err.println("Текст ошибки от сервера: " + response.body());
            }

            // 2. Открываем браузер прямо из менеджера
//            openBrowser(paymentUrl);
            openBrowser(response.body());


            // 3. Используем второй сервис для запуска слежки за статусом
            //statusChecker.startPolling(orderNum, callback);

        } catch (Exception e) {
            callback.onFailed("Ошибка платежной системы: " + e.getMessage());
        }
    }

    private String parseRedirectUrl(String json) {
        if (!json.contains("redirectUrl")) return null;
        int start = json.indexOf("redirectUrl\":\"") + "redirectUrl\":\"".length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end).replace("\\/", "/");
    }


    public static HttpResponse<String> post(String jsonBody) throws IOException, InterruptedException {
        // 1. Создаем клиент
        HttpClient client = HttpClient.newHttpClient();

        // Данные, которые вы хотите отправить (например, JSON)
        //String jsonBody = paymentUrl;

        System.out.println(jsonBody);

        // 2. Строим POST-запрос
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(WebpayPaymentServ.PAYMENT_GATEWAY_URL)) // Укажите ваш URL
                .header("Content-Type", "application/json") // Задаем тип контента
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody)) // Переводим в метод POST и передаем тело
                .build();

        System.out.println(request);

        // 3. Отправляем запрос и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Выводим результат
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
        return response;
    }


    private static void openBrowser(String url) throws IOException, URISyntaxException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(url));
        } else {
            Runtime runtime = Runtime.getRuntime();
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
            else if (os.contains("mac")) runtime.exec("open " + url);
            else if (os.contains("nix") || os.contains("nux")) runtime.exec("xdg-open " + url);
            else throw new IOException("Неподдерживаемая ОС.");
        }
    }

    //отмена платежа
    public void cancelCurrentPayment() {
        if (statusChecker != null) {
            statusChecker.stopPolling();
            System.out.println("⚠️ Опрос статуса принудительно остановлен из интерфейса приложения.");
        }
    }


    //открытие кода html в браузере
    public static void openHtmlInBrowser(String htmlContent) {
        // Строка с HTML-кодом от вашего сервера
//        String htmlContent = "<html><head><meta charset='UTF-8'><title>Ответ сервера</title></head>"
//                + "<body style='font-family:sans-serif; text-align:center; padding-top:50px;'>"
//                + "<h1>Успешно!</h1><p>Эта страница открыта прямо из Java.</p>"
//                + "</body></html>";

        try {
            // 1. Создаем временный HTML-файл (префикс, суффикс)
            File tempFile = File.createTempFile("server_response_", ".html");

            // Удалить файл автоматически при закрытии Java-программы (по желанию)
            tempFile.deleteOnExit();

            // 2. Записываем HTML-код в этот файл
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write(htmlContent);
            }

            // 3. Открываем файл в системном браузере по умолчанию
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(tempFile.toURI());
                System.out.println("Страница успешно открыта в браузере: " + tempFile.getAbsolutePath());
            } else {
                System.out.println("Класс Desktop не поддерживается в данной системе.");
            }

        } catch (IOException e) {
            System.err.println("Ошибка при открытии страницы: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.awt.Desktop;
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.URLEncoder;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.time.Duration;
//import java.util.Base64;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//public class WebpayPaymentManager {
//
//    // === НАСТРОЙКИ ЭНДПОИНТОВ ===
//    // Тестовая среда (Sandbox)
//    private static final String PAYMENT_GATEWAY_URL = "https://webpay.by";
//    private static final String API_STATUS_URL = "https://webpay.by";
//
//    /* Реальная среда (для продакшена смените на эти):
//    private static final String PAYMENT_GATEWAY_URL = "https://webpay.by";
//    private static final String API_STATUS_URL = "https://webpay.by";
//    */
//
//    // === ДАННЫЕ АВТОРИЗАЦИИ ===
//    private final String storeId = "123456789";         // Идентификатор магазина (wsb_storeid)
//    private final String apiUsername = "api_user_login"; // Логин к JSON API (из техподдержки)
//    private final String apiPassword = "api_user_pass";  // Пароль к JSON API (из техподдержки)
//
//    // Замаскированный secret_key для генерации wsb_signature (получен через XOR-скрипт)
//    private static final byte[] ENCRYPTED_SECRET_KEY = new byte[]{ 47, 53, 37, 57, 107, 46, 55, 107, 39, 47 };
//    private static final byte XOR_MASK = 0x5A;
//
//    // === СЛУЖЕБНЫЕ ОБЪЕКТЫ ===
//    private final HttpClient httpClient;
//    private final ObjectMapper objectMapper;
//    private ScheduledExecutorService scheduler;
//
//    public WebpayPaymentManager() {
//        this.httpClient = HttpClient.newBuilder()
//                .connectTimeout(Duration.ofSeconds(10))
//                .build();
//        this.objectMapper = new ObjectMapper();
//    }
//
//    /**
//     * Интерфейс-колбэк для обработки результата оплаты в UI
//     */
//    public interface PaymentCallback {
//        void onSuccess();
//        void onFailed(String reason);
//    }
//
//    /**
//     * ГЛАВНЫЙ МЕТОД: Создает платеж, открывает браузер и начинает следить за статусом.
//     */
//    public void processPayment(String amount, String currency, PaymentCallback callback) {
//        String orderNum = "INV-" + System.currentTimeMillis();
//        String testMode = "1"; // 1 - тест, 0 - боевой
//
//        try {
//            // 1. Формируем платежную ссылку
//            String seed = String.valueOf(System.currentTimeMillis() / 1000);
//            String secretKey = getDecryptedSecretKey();
//
//            String signatureText = seed + storeId + orderNum + testMode + currency + amount + secretKey;
//            String wsbSignature = computeMD5(signatureText);
//
//            Map<String, String> params = new LinkedHashMap<>();
//            params.put("*wsb_seed", seed);
//            params.put("wsb_storeid", storeId);
//            params.put("wsb_order_num", orderNum);
//            params.put("wsb_test", testMode);
//            params.put("wsb_currency_id", currency);
//            params.put("wsb_total", amount);
//            params.put("wsb_signature", wsbSignature);
//
//            // Заглушки (для десктопа они вторичны, так как мы смотрим статус через API)
//            params.put("wsb_return_url", "https://webpay.by");
//            params.put("wsb_cancel_return_url", "https://webpay.by");
//
//            String paymentUrl = buildUrl(PAYMENT_GATEWAY_URL, params);
//
//            // 2. Открываем браузер пользователю
//            System.out.println("Открытие платежной страницы для заказа: " + orderNum);
//            openBrowser(paymentUrl);
//
//            // 3. Запускаем фоновый мониторинг статуса
//            startPolling(orderNum, callback);
//
//        } catch (Exception e) {
//            callback.onFailed("Ошибка инициализации платежа: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Фоновый циклический опрос API WEBPAY
//     */
//    private void startPolling(String orderNumber, PaymentCallback callback) {
//        if (scheduler != null && !scheduler.isShutdown()) {
//            scheduler.shutdownNow();
//        }
//        scheduler = Executors.newSingleThreadScheduledExecutor();
//
//        System.out.println("Запущен фоновый трекер статуса...");
//        scheduler.scheduleAtFixedRate(() -> {
//            try {
//                String status = requestTransactionStatus(orderNumber);
//                System.out.println("Статус заказа " + orderNumber + " из API: " + status);
//
//                if ("completed".equalsIgnoreCase(status) || "approved".equalsIgnoreCase(status)) {
//                    System.out.println("Платеж подтвержден системой WEBPAY.");
//                    stopPolling();
//                    callback.onSuccess(); // Передаем успех в интерфейс
//                } else if ("declined".equalsIgnoreCase(status) || "failed".equalsIgnoreCase(status)) {
//                    System.out.println("Платеж отклонен.");
//                    stopPolling();
//                    callback.onFailed("Оплата отклонена банком или отменена.");
//                }
//
//            } catch (Exception e) {
//                System.err.println("Ошибка опроса статуса: " + e.getMessage());
//            }
//        }, 3, 5, TimeUnit.SECONDS); // Опрос каждые 5 секунд, первая проверка через 3 секунды
//    }
//
//    /**
//     * Прямой HTTP-запрос к JSON API проверки транзакции
//     */
//    private String requestTransactionStatus(String orderNumber) throws Exception {
//        String url = API_STATUS_URL + "/api/v1/transactions/info/invoice/" + orderNumber;
//
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
//            return "pending_payment"; // Клиент еще открывает страницу / вводит карту
//        } else {
//            throw new RuntimeException("HTTP Error: " + statusCode);
//        }
//    }
//
//    public void stopPolling() {
//        if (scheduler != null && !scheduler.isShutdown()) {
//            scheduler.shutdown();
//            System.out.println("Фоновый трекер остановлен.");
//        }
//    }
//
//    // === ВСПОМОГАТЕЛЬНЫЕ КРИПТО-МЕТОДЫ ===
//
//    private String getDecryptedSecretKey() {
//        byte[] decrypted = new byte[ENCRYPTED_SECRET_KEY.length];
//        for (int i = 0; i < ENCRYPTED_SECRET_KEY.length; i++) {
//            decrypted[i] = (byte) (ENCRYPTED_SECRET_KEY[i] ^ XOR_MASK);
//        }
//        return new String(decrypted, StandardCharsets.UTF_8);
//    }
//
//    private static String computeMD5(String input) throws NoSuchAlgorithmException {
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
//        StringBuilder hexString = new StringBuilder();
//        for (byte b : digest) {
//            String hex = Integer.toHexString(0xff & b);
//            if (hex.length() == 1) hexString.append('0');
//            hexString.append(hex);
//        }
//        return hexString.toString();
//    }
//
//    private static String buildUrl(String baseUrl, Map<String, String> params) {
//        StringBuilder urlBuilder = new StringBuilder(baseUrl);
//        if (!params.isEmpty()) {
//            urlBuilder.append("?");
//            params.forEach((key, value) -> {
//                try {
//                    urlBuilder.append(URLEncoder.encode(key, StandardCharsets.UTF_8.name()))
//                            .append("=")
//                            .append(URLEncoder.encode(value, StandardCharsets.UTF_8.name()))
//                            .append("&");
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            });
//            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
//        }
//        return urlBuilder.toString();
//    }
//
//    private static void openBrowser(String url) throws IOException, URISyntaxException {
//        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
//            Desktop.getDesktop().browse(new URI(url));
//        } else {
//            Runtime runtime = Runtime.getRuntime();
//            String os = System.getProperty("os.name").toLowerCase();
//            if (os.contains("win")) runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
//            else if (os.contains("mac")) runtime.exec("open " + url);
//            else if (os.contains("nix") || os.contains("nux")) runtime.exec("xdg-open " + url);
//            else throw new IOException("Неподдерживаемая ОС для открытия браузера.");
//        }
//    }
//
//    // === ДТО КЛАСС ДЛЯ JACKSON ===
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public static class WebpayTransactionInfo {
//        @JsonProperty("status") private String status;
//        public String getStatus() { return status; }
//        public void setStatus(String status) { this.status = status; }
//    }
//}
