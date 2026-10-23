package payment.test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class PaymentServer {

    private HttpServer server;

    public void startServer() throws IOException {
        // Запускаем сервер на порту 8080
        server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Настраиваем путь /payment/success (тот самый wsb_return_url)
        server.createContext("/payment/success", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                    // Получаем параметры из строки запроса
                    URI requestURI = exchange.getRequestURI();
                    String query = requestURI.getQuery();
                    Map<String, String> params = parseQuery(query);

                    String orderNum = params.get("wsb_order_num");
                    String transactionId = params.get("wsb_tid");

                    String responseText;
                    if (orderNum != null && transactionId != null) {
                        // Данные успешно получены!
                        System.out.println("Номер заказа: " + orderNum);
                        System.out.println("ID транзакции: " + transactionId);

                        // TODO: Здесь вы можете вызвать метод вашего Swing-окна,
                        // например: myJFrame.showSuccessMessage(orderNum);

                        responseText = "<h1>Оплата успешна!</h1><p>Вы можете закрыть эту вкладку браузера и вернуться в приложение.</p>";
                    } else {
                        responseText = "<h1>Ошибка!</h1><p>Неверные параметры платежа.</p>";
                    }

                    // Отправляем ответ в браузер пользователя
                    byte[] responseBytes = responseText.getBytes("UTF-8");
                    exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                    exchange.sendResponseHeaders(200, responseBytes.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(responseBytes);
                    os.close();
                }
            }
        });

        server.setExecutor(null); // Создает дефолтный поток выполнения
        server.start();
        System.out.println("Локальный платежный сервер запущен на порту 8080...");
    }

    public void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    // Вспомогательный метод для парсинга GET-параметров
    private Map<String, String> parseQuery(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null) return result;
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
