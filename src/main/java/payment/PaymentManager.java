package payment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * startLocalServer() - запуск сервера приема ответа от webpay,
 * webPayResponse - проверка ответа,
 * parseQuery - парсинг ответа
 */

public class PaymentManager {

    private HttpServer server;

    // Запуск локального сервера (вызывать при нажатии кнопки "Оплатить")
    public void startLocalServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/payment/success", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                    // 1. Парсим параметры из GET-запроса (урла возврата)
                    URI requestURI = exchange.getRequestURI();
                    Map<String, String> params = parseQuery(requestURI.getQuery());

                    String orderNum = params.get("wsb_order_num");
                    String transactionId = params.get("wsb_tid");

                    String responseText;

                    if (transactionId != null) {
                        System.out.println("GET-ответ получен! Номер транзакции: " + transactionId);

                        // 2. АВТОМАТИЧЕСКИЙ ЗАПУСК ПРОВЕРКИ ПЛАТЕЖА
                        String webPayResponse = checkPaymentInWebPay(transactionId);
                        System.out.println("Ответ от WebPay API:\n" + webPayResponse);

                        // Здесь можно обновить интерфейс Swing (например, вызвать SwingUtilities.invokeLater)

                        responseText = "<h1>Проверка запущена!</h1><p>Ответ сервера WebPay получен. Проверьте консоль приложения.</p>";
                    } else {
                        responseText = "<h1>Ошибка!</h1><p>Не найден ID транзакции (wsb_tid).</p>";
                    }

                    // 3. Отправляем ответ в браузер пользователю
                    byte[] responseBytes = responseText.getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                    exchange.sendResponseHeaders(200, responseBytes.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(responseBytes);
                    os.close();

                    // Внутри метода handle локального сервера:
                    String webPayResponse = checkPaymentInWebPay(transactionId);

                    // 1. Выделяем статус из XML
                    String paymentStatus = XmlParser.getStatusFromXml(webPayResponse);

                    // 2. Реагируем на статус
                    if ("success".equals(paymentStatus)) {
                        System.out.println("ПЛАТЕЖ УСПЕШЕН!");

                        // Здесь обновляем ваше Swing-окно (например, разблокируем функции программы)
                    } else {
                        System.out.println("Платеж отклонен или не найден. Статус: " + paymentStatus);
                    }
                }
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Сервер проверки оплаты запущен на порту 8080...");
    }

    // Тот самый метод API-запроса проверки платежа
    private String checkPaymentInWebPay(String transactionId) {
        try {
            String username = "gandal";
            String rawPassword = "Sb67Yp78E";
            String md5Password = md5(rawPassword);

            // Формируем XML с динамическим transactionId
            String xmlRequest = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" +
                    "<wsb_api_request>\n" +
                    "  <command>get_transaction</command>\n" +
                    "  <authorization>\n" +
                    "    <username>" + username + "</username>\n" +
                    "    <password>" + md5Password + "</password>\n" +
                    "  </authorization>\n" +
                    "  <fields>\n" +
                    "    <transaction_id>" + transactionId + "</transaction_id>\n" +
                    "  </fields>\n" +
                    "</wsb_api_request>";

            String encodedXml = URLEncoder.encode(xmlRequest, StandardCharsets.ISO_8859_1);
            String postData = "*API=&API_XML_REQUEST=" + encodedXml;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://sandbox.webpay.by"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(postData, StandardCharsets.ISO_8859_1))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body(); // Возвращаем XML-ответ от WebPay

        } catch (Exception e) {
            e.printStackTrace();
            return "<error>" + e.getMessage() + "</error>";
        }
    }

    public void stopServer() {
        if (server != null) server.stop(0);
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null) return result;
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            }
        }
        return result;
    }

    private String md5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

