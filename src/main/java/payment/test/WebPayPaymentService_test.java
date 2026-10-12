package payment.test;

import payment.WebPaySigner;

import java.awt.Desktop;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

public class WebPayPaymentService_test {

    //    private static final String WEBPAY_API_URL = "https://securesandbox.webpay.by/api/v1/payment";
    private static final String WEBPAY_API_URL = "http://localhost:3000";
    private static final String SECRET_KEY = "mzIOhdhgjd652467hgfswjhGHDF";

    public void processPayment() {
        try {
            // Параметры платежа
            String storeId = "433329306"; // Идентификатор магазина
            String orderNum = "ORDER-" + Instant.now().getEpochSecond(); // Уникальный ID заказа
            String currency = "BYN";
            int testMode = 1;
            String totalAmount = "31.4"; // Строка для хэша подписи
            String seed = String.valueOf(Instant.now().getEpochSecond());

            // Считаем подпись через наш WebPaySigner
            String signature = WebPaySigner.computeSignature(
                    seed, storeId, orderNum, testMode, currency, totalAmount, SECRET_KEY
            );

            // ИСПРАВЛЕННЫЙ ВАЛИДНЫЙ JSON-ПАКЕТ ПО СПЕЦИФИКАЦИИ WEBPAY
            String jsonBody = "{\n" +
                    "  \"wsb_storeid\": " + storeId + ",\n" + // Передаем как число (без кавычек)
                    "  \"wsb_order_num\": \"" + orderNum + "\",\n" +
                    "  \"wsb_currency_id\": \"" + currency + "\",\n" +
                    "  \"wsb_version\": 2,\n" +
                    "  \"wsb_seed\": \"" + seed + "\",\n" +
                    "  \"wsb_test\": " + testMode + ",\n" +
                    "  \"wsb_invoice_item_name\": [\"Товар 1\", \"Товар 2\"],\n" +
                    "  \"wsb_invoice_item_quantity\": [2, 1],\n" + // Исправлен синтаксис массива
                    "  \"wsb_invoice_item_price\": [\"10\", \"0.5\"],\n" + // Цены передаются строками
                    "  \"wsb_total\": " + totalAmount + ",\n" + // Передаем как число (без кавычек)
                    "  \"wsb_signature\": \"" + signature + "\"\n" +
                    "}";

            // Вывод сформированного JSON в консоль для визуальной самопроверки
            System.out.println("Сформированный JSON отправки:\n" + jsonBody);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(WEBPAY_API_URL))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            System.out.println("\nОтправка H2H запроса...");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String parseRedirectUrl(String json) {
        if (!json.contains("redirectUrl")) return null;
        int start = json.indexOf("redirectUrl\":\"") + "redirectUrl\":\"".length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end).replace("\\/", "/");
    }
}

