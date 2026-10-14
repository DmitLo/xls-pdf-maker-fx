package payment.test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class WebpayCallbackServer {

    public static void main(String[] args) throws IOException {
        // Создаем сервер на порту 8080 (или любом другом свободном)
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Регистрируем адрес (эндпоинт), на который WEBPAY будет слать уведомления.
        // Этот URL вы должны указать в личном кабинете WEBPAY как "Путь для уведомлений" (postbackURL)
        server.createContext("/webpay-callback", new WebpayNotificationHandler());

        server.setExecutor(null); // использует дефолтный исполнитель
        System.out.println("Сервер запущен на порту 8080. Ожидание нотификаций...");
        server.start();
    }

    // Обработчик входящих запросов от платежной системы
    static class WebpayNotificationHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Проверяем, что запрос пришел методом POST
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {

                // Читаем тело POST-запроса от WEBPAY
                BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));
                StringBuilder requestBody = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
                reader.close();

                // WEBPAY присылает данные в формате x-www-form-urlencoded (как параметры из формы)
                // Парсим эту строку в удобную Map (ключ -> значение)
                Map<String, String> params = parseFormUrlEncoded(requestBody.toString());

                // Извлекаем нужный transaction_id и сопутствующие данные
                String transactionId = params.get("transaction_id");
                String orderId = params.get("order_id"); // Ваш номер заказа/инвойса
                String status = params.get("wsb_payment_type"); // Тип операции или статус
                String signature = params.get("wsb_signature"); // Подпись для проверки безопасности

                System.out.println("\n--- Получено уведомление от WEBPAY ---");
                if (transactionId != null) {
                    System.out.println("ID транзакции (WEBPAY): " + transactionId);
                    System.out.println("Номер заказа на вашем сайте: " + orderId);
                    System.out.println("Статус/Тип: " + status);
                    System.out.println("Электронная подпись: " + signature);

                    // TODO: Здесь вы сохраняете transactionId в свою базу данных к заказу orderId
                    // и проверяете валидность подписи wsb_signature

                } else {
                    System.out.println("Запрос получен, но параметр transaction_id отсутствует.");
                }

                // Отвечаем серверу WEBPAY успешным HTTP-статусом 200 OK
                // Если не ответить, WEBPAY будет слать это уведомление повторно
                String responseText = "OK";
                exchange.sendResponseHeaders(200, responseText.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(responseText.getBytes());
                os.close();

            } else {
                // Если пришел не POST запрос, отдаем ошибку 405 Method Not Allowed
                exchange.sendResponseHeaders(405, -1);
            }
        }

        // Вспомогательный метод для парсинга query-строки (param1=value1&param2=value2)
        private Map<String, String> parseFormUrlEncoded(String query) {
            Map<String, String> result = new HashMap<>();
            if (query == null || query.isEmpty()) return result;

            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                try {
                    String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                    result.put(key, value);
                } catch (Exception e) {
                    // Игнорируем некорректные параметры
                }
            }
            return result;
        }
    }
}

