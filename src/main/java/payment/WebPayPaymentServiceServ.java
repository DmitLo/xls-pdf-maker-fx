package payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import payment.dto.SendFromAppToServ;

import java.awt.*;
import java.io.File;
import java.time.Instant;

/**
 * Проведение оплаты через HTML POST
 */

public class WebPayPaymentServiceServ {

    // Ссылка на платежный шлюз (Обязательно с закрывающим слэшем)
    private static final String WEBPAY_POST_URL = "https://securesandbox.webpay.by/";
    // Тестовый секретный ключ
    private static final String SECRET_KEY = "mzIOhdhgjd652467hgfswjhGHDF";

public static String processPayment(String currency, Item[] items, float total, String hash) {
    String orderNum = "";
        try {
            // 1. ПАРАМЕТРЫ ПЛАТЕЖА (Используем только целые числа для 100% прохождения теста)
            String storeId = "433329306";
            orderNum = "ORDER-" + Instant.now().getEpochSecond(); // Уникальный ID заказа
            int testMode = 1;

            String totalAmount = Float.toString(total);
            String seed = String.valueOf(Instant.now().getEpochSecond());

            // 2. ГЕНЕРИРУЕМ ХЭШ-ПОДПИСЬ
            String signature = WebPaySigner.computeSignature(
                    seed, storeId, orderNum, testMode, currency, totalAmount, SECRET_KEY
            );

            System.out.println("signature " + signature);

            // 3. ФОРМИРУЕМ СТРОГИЙ HTML ПО СПЕЦИФИКАЦИИ WEBPAY
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Перенаправление...</title></head>");
            html.append("<body onload='document.forms[0].submit()'>"); // Гарантированный авто-сабмит
            html.append("<form action='").append(WEBPAY_POST_URL).append("' method='post'>");

            // Основные служебные поля шлюза
            html.append("<input type='hidden' name='*scart'>");
            html.append("<input type='hidden' name='wsb_version' value='2'>");
            html.append("<input type='hidden' name='wsb_language_id' value='russian'>");
            html.append("<input type='hidden' name='wsb_storeid' value='").append(storeId).append("'>");
            html.append("<input type='hidden' name='wsb_order_num' value='").append(orderNum).append("'>");
            html.append("<input type='hidden' name='wsb_test' value='").append(testMode).append("'>");
            html.append("<input type='hidden' name='wsb_currency_id' value='").append(currency).append("'>");
            html.append("<input type='hidden' name='wsb_seed' value='").append(seed).append("'>");

            html.append("<input type='hidden' name='wsb_return_url' value='http://localhost:3000/payment/success'>");
            //html.append("<input type='hidden' name='wsb_return_url' value='http://localhost:8000/payment/success'>");

            for (Item elements: items) {
                html.append("<input type='hidden' name='wsb_invoice_item_name[").append(elements.ordinal).append("]' value='").append(elements.name).append("'>");
                html.append("<input type='hidden' name='wsb_invoice_item_quantity[").append(elements.ordinal).append("]' value='").append(elements.quantity).append("'>");
                html.append("<input type='hidden' name='wsb_invoice_item_price[").append(elements.ordinal).append("]' value='").append(elements.price).append("'>");
            }

            html.append("<input type='hidden' name='wsb_total' value='").append(totalAmount).append("'>");

            html.append("<input type='hidden' name='wsb_signature' value='").append(signature).append("'>");

            html.append("</form></body></html>");

            // 4. ПЕРЕНОСИМ НА ДИСК
            File tempHtmlFile = File.createTempFile("webpay_pay_2", ".html");
            tempHtmlFile.deleteOnExit();

            //передача на внешний сервер страницы
            sendHtml(html, orderNum, hash);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderNum;
    }

    //передача страницы на внешний сервер
    public static void sendHtml(StringBuilder html, String orderNum, String hash) throws JsonProcessingException {
        // URL вашего удаленного сервера Express (замените на свой реальный адрес)
//        String serverUrl = "https://test.gandal.by";
        String serverUrl = "http://127.0.0.1:3000";
        //String secretKey = "my_super_secret_token_123"; // Для защиты от чужих запросов
        String secretKey = SECRET_KEY;

        //Создания объекта для передачи на сервер (html код, номер ордера и хэш машины)
        SendFromAppToServ data = new SendFromAppToServ(html.toString(), orderNum, hash);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(data);
        //проверка
        System.out.println(jsonString);

        try {
            System.out.println("Отправка страницы на удаленный сервер Express...");

            // 1. Создаем HTTP-клиент
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();

            // 2. Формируем POST-запрос с вашим HTML-контентом
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    //.uri(java.net.URI.create(serverUrl + "/upload-html"))
                    .uri(java.net.URI.create(serverUrl + "/process-payment"))
                    .header("Content-Type", "text/html; charset=utf-8")
                    .header("X-Secret-Key", secretKey)
//                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(html.toString(), java.nio.charset.StandardCharsets.UTF_8))
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonString, java.nio.charset.StandardCharsets.UTF_8))
                    .build();

            // 3. Отправляем запрос синхронно
            java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

            // 4. Если сервер принял страницу, открываем её в системном браузере
            if (response.statusCode() == 200) {
                System.out.println("Страница успешно загружена на сервер!");

                String publicUrl = serverUrl + "/view-html";
                System.out.println("Открываем системный браузер по адресу: " + publicUrl);

                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new java.net.URI(publicUrl));
                } else {
                    System.err.println("Ошибка: Среда выполнения не поддерживает открытие браузера.");
                }
            } else {
                System.err.println("Ошибка сервера Express: код " + response.statusCode() + " - " + response.body());
            }

        } catch (Exception e) {
            System.err.println("Не удалось отправить страницу на сервер: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

