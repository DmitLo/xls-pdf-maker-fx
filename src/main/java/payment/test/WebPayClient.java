package payment.test;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class WebPayClient {

    public static void webPayBack (String[] args) {
        try {
            // 1. Подготовка ваших данных доступа и ID транзакции
            String username = "gandal";
            String rawPassword = "Sb67Yp78E"; // пароль в чистом виде
            String md5Password = md5(rawPassword);      // шифруем в MD5, как требует WebPay
            String transactionId = "221623638";         // ID из GET-запроса (wsb_tid)

            // 2. Формируем XML строку
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

            // 3. Кодируем XML в формат urlencode (аналог urlencode в PHP)
            String encodedXml = URLEncoder.encode(xmlRequest, StandardCharsets.ISO_8859_1);

            // Собираем postdata (обратите внимание на префикс *API=&API_XML_REQUEST=)
            String postData = "*API=&API_XML_REQUEST=" + encodedXml;

            // 4. Создаем HTTP-клиент и отправляем POST-запрос (аналог cURL)
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://sandbox.webpay.by"))
                    .header("Content-Type", "application/x-www-form-urlencoded") // обязательный заголовок для POST-форм
                    .POST(HttpRequest.BodyPublishers.ofString(postData, StandardCharsets.ISO_8859_1))
                    .build();

            // Получаем ответ сервера
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 5. Выводим XML-ответ от WebPay в консоль
            System.out.println("Ответ от WebPay:\n" + response.body());

        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка при отправке запроса: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Вспомогательный метод для хэширования пароля в MD5 (аналог md5() в PHP)
     */
    private static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Алгоритм MD5 не найден", e);
        }
    }
}

