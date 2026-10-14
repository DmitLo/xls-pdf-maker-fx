package payment;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.time.Instant;
import java.time.LocalTime;


public class WebPayPaymentService {

    // Ссылка на платежный шлюз (Обязательно с закрывающим слэшем)
    private static final String WEBPAY_POST_URL = "https://securesandbox.webpay.by/";
    //private static final String WEBPAY_POST_URL = "https://redirect.gandal.by/";

    // Тестовый секретный ключ
    private static final String SECRET_KEY = "mzIOhdhgjd652467hgfswjhGHDF";
    //private static final String SECRET_KEY = "1";

    public static void processPayment(String currency, Item[] items, float total) {
        try {
            // 1. ПАРАМЕТРЫ ПЛАТЕЖА (Используем только целые числа для 100% прохождения теста)
            String storeId = "433329306";
            String orderNum = "ORDER-" + Instant.now().getEpochSecond(); // Уникальный ID заказа
            //String currency = "BYN";
            int testMode = 1;

            // Использовать строго круглую сумму без точек (например, "20")
            String totalAmount = Float.toString(total);
            //LocalTime startSessTime = LocalTime.now();
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
            //html.append("<input type='hidden' name='wsb_startsesstime' value='").append(startSessTime).append("'>");
            // Основные служебные поля шлюза
            html.append("<input type='hidden' name='*scart'>");
            html.append("<input type='hidden' name='wsb_version' value='2'>");
            html.append("<input type='hidden' name='wsb_language_id' value='russian'>");
            html.append("<input type='hidden' name='wsb_storeid' value='").append(storeId).append("'>");
            html.append("<input type='hidden' name='wsb_order_num' value='").append(orderNum).append("'>");
            html.append("<input type='hidden' name='wsb_test' value='").append(testMode).append("'>");
            html.append("<input type='hidden' name='wsb_currency_id' value='").append(currency).append("'>");
            html.append("<input type='hidden' name='wsb_seed' value='").append(seed).append("'>");

            html.append("<input type='hidden' name='wsb_return_url' value='http://localhost:8080/payment/success'>");

            // Корзина товаров (Индексы прописываем жестко: 0, 1, 2... без пустых скобок)
            // Математика: 10 рублей * 2 штуки = Итого 20 рублей. Идеально совпадает с wsb_total!
//            html.append("<input type='hidden' name='wsb_invoice_item_name[0]' value='Товар1'>");
//            html.append("<input type='hidden' name='wsb_invoice_item_quantity[0]' value='2'>");
//            html.append("<input type='hidden' name='wsb_invoice_item_price[0]' value='10'>");
//            html.append("<input type='hidden' name='wsb_invoice_item_name[1]' value='Товар2'>");
//            html.append("<input type='hidden' name='wsb_invoice_item_quantity[1]' value='1'>");
//            html.append("<input type='hidden' name='wsb_invoice_item_price[1]' value='0.5'>");
//            html.append("<input type='hidden' name='wsb_total' value='").append(totalAmount).append("'>");


            for (Item elements: items) {
                html.append("<input type='hidden' name='wsb_invoice_item_name[").append(elements.ordinal).append("]' value='").append(elements.name).append("'>");
                html.append("<input type='hidden' name='wsb_invoice_item_quantity[").append(elements.ordinal).append("]' value='").append(elements.quantity).append("'>");
                html.append("<input type='hidden' name='wsb_invoice_item_price[").append(elements.ordinal).append("]' value='").append(elements.price).append("'>");
            }

//            for (int i = 0; i < itemName.length; i++) {
//                html.append("<input type='hidden' name='wsb_invoice_item_name[").append(i).append("]' value='").append(itemName[i]).append("'>");
//                html.append("<input type='hidden' name='wsb_invoice_item_quantity[").append(i).append("]' value='").append(quantity[i]).append("'>");
//                html.append("<input type='hidden' name='wsb_invoice_item_price[").append(i).append("]' value='").append(itemPrice[i]).append("'>");
//            }
            html.append("<input type='hidden' name='wsb_total' value='").append(totalAmount).append("'>");

            html.append("<input type='hidden' name='wsb_signature' value='").append(signature).append("'>");

            html.append("</form></body></html>");

            // 4. ПЕРЕНОСИМ НА ДИСК
            File tempHtmlFile = File.createTempFile("webpay_pay_2", ".html");
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

