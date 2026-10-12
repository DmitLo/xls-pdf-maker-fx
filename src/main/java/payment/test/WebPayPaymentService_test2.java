package payment.test;

import payment.WebPaySigner;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.time.Instant;

public class WebPayPaymentService_test2 {

    // Ссылка на платежный шлюз (Обязательно с закрывающим слэшем)
//    private static final String WEBPAY_POST_URL = "https://securesandbox.webpay.by/";
    private static final String WEBPAY_POST_URL = "https://redirect.gandal.by/";

    // Тестовый секретный ключ
    private static final String SECRET_KEY = "mzIOhdhgjd652467hgfswjhGHDF";
    //private static final String SECRET_KEY = "1";

    public void processPayment() {
        try {
            // 1. ПАРАМЕТРЫ ПЛАТЕЖА (Используем только целые числа для 100% прохождения теста)
            String storeId = "433329306";
            //String storeId = "111111111";
            //String orderNum = "ORDER-" + Instant.now().getEpochSecond();
            String orderNum = "ORDER-" + Instant.now().getEpochSecond(); // Уникальный ID заказа
            String currency = "BYN";
            int testMode = 1;

            // Использовать строго круглую сумму без точек (например, "20")
            String totalAmount = "30";
            String seed = String.valueOf(Instant.now().getEpochSecond());

            // 2. ГЕНЕРИРУЕМ ХЭШ-ПОДПИСЬ
            String signature = WebPaySigner.computeSignature(
                    seed, storeId, orderNum, testMode, currency, totalAmount, SECRET_KEY
            );

            // 3. ФОРМИРУЕМ СТРОГИЙ HTML ПО СПЕЦИФИКАЦИИ WEBPAY
            StringBuilder html = new StringBuilder();

            html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Перенаправление...</title></head>");
            html.append("<body onload='document.forms[0].submit()'>"); // Гарантированный авто-сабмит
            html.append("<form action='").append(WEBPAY_POST_URL).append("' method='POST'>");

            // Основные служебные поля шлюза
            html.append("<input type='hidden' name='wsb_version' value='2'>");
            html.append("<input type='hidden' name='wsb_language_id' value='russian'>");
            html.append("<input type='hidden' name='wsb_storeid' value='").append(storeId).append("'>");
            html.append("<input type='hidden' name='wsb_order_num' value='").append(orderNum).append("'>");
            html.append("<input type='hidden' name='wsb_currency_id' value='").append(currency).append("'>");
            html.append("<input type='hidden' name='wsb_seed' value='").append(seed).append("'>");
            html.append("<input type='hidden' name='wsb_test' value='").append(testMode).append("'>");
            html.append("<input type='hidden' name='wsb_total' value='").append(totalAmount).append("'>");
            html.append("<input type='hidden' name='wsb_signature' value='").append(signature).append("'>");

            // Корзина товаров (Индексы прописываем жестко: 0, 1, 2... без пустых скобок)
            // Математика: 10 рублей * 2 штуки = Итого 20 рублей. Идеально совпадает с wsb_total!
            html.append("<input type='hidden' name='wsb_invoice_item_name[0]' value='Тестовый Товар'>");
            html.append("<input type='hidden' name='wsb_invoice_item_quantity[0]' value='2'>");
            html.append("<input type='hidden' name='wsb_invoice_item_price[0]' value='15'>");

            html.append("</form></body></html>");

            // 4. ПЕРЕНОСИМ НА ДИСК
            File tempHtmlFile = File.createTempFile("webpay_pay_", ".html");
            tempHtmlFile.deleteOnExit();

            try (FileWriter writer = new FileWriter(tempHtmlFile)) {
                writer.write(html.toString());
            }

            // 5. ЗАПУСКАЕМ БРАУЗЕР
            System.out.println("Файл сгенерирован: " + tempHtmlFile.getAbsolutePath());
            System.out.println("Открываем системный браузер...");
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(tempHtmlFile.toURI());
            } else {
                System.err.println("Ошибка: Среда выполнения не поддерживает открытие браузера.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

