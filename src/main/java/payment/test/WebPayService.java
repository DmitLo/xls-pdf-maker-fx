package payment.test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

//тест

public class WebPayService {

    // Используем тестовую среду. Для боевой замените на https://webpay.by
    private static final String BASE_URL = "https://webpay.by";

    private final HttpClient httpClient;

    public WebPayService() {
        // Создаем клиент с отключенными редиректами, чтобы при ошибке
        // не "проглатывать" HTML-страницы, а видеть реальный код ответа
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();
    }

    /**
     * ШАГ 1: Получение авторизационного токена (auth_token)
     */
    public String getAuthToken(String username, String password) throws Exception {
        // ВНИМАНИЕ: Сверьте точный URL и структуру JSON-тела для авторизации с вашей документацией WebPay!
        String authUrl = BASE_URL + "/api/v1/auth/login";
        String jsonBody = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(authUrl))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // Здесь необходимо распарсить JSON (например, через библиотеки Jackson или Gson)
            // И вытащить поле с токеном. Для примера вернем тело ответа целиком
            return response.body();
        } else {
            throw new RuntimeException("Ошибка авторизации. Код: " + response.statusCode());
        }
    }

    /**
     * ШАГ 2: Получение статуса транзакции (Host-to-Host)
     */
    public String getTransactionStatus(String authToken, long transactionNumber) throws Exception {
        // Строим корректный URL согласно правилам WebPay управления операциями
        // ВНИМАНИЕ: Уточните точный эндпоинт (/api/v1/transactions/...) в вашем полном мануале
        String statusUrl = BASE_URL + "/api/v1/transactions/" + transactionNumber;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(statusUrl))
                .header("Authorization", "Bearer " + authToken) // Передаем токен в заголовке
                .header("Accept", "application/json")          // Требуем от сервера строго JSON
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Валидируем заголовок ответа, чтобы гарантировать отсутствие HTML
        String contentType = response.headers().firstValue("Content-Type").orElse("");
        if (contentType.contains("text/html")) {
            throw new IOException("Критическая ошибка: Сервер прислал веб-страницу вместо данных! Проверьте URL.");
        }

        if (response.statusCode() == 200) {
            return response.body(); // Ура! Здесь придет чистый JSON со статусом транзакции
        } else {
            return "Ошибка сервера. Код: " + response.statusCode() + ", Тело: " + response.body();
        }
    }
}
