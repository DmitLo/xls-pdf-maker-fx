package payment;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

public class WebpayPaymentService {

    private static final String PAYMENT_GATEWAY_URL = "https://webpay.by";

    private final String storeId;
    private final byte[] encryptedSecretKey;
    private final byte xorMask;

    public WebpayPaymentService(String storeId, byte[] encryptedSecretKey, byte xorMask) {
        this.storeId = storeId;
        this.encryptedSecretKey = encryptedSecretKey;
        this.xorMask = xorMask;
    }

    public String createPaymentUrl(String orderNum, String amount, String currency, String testMode) throws Exception {
        String seed = String.valueOf(System.currentTimeMillis() / 1000);
        String secretKey = getDecryptedSecretKey();

        String signatureText = seed + storeId + orderNum + testMode + currency + amount + secretKey;
        String wsbSignature = computeMD5(signatureText);

        Map<String, String> params = new LinkedHashMap<>();
        params.put("*wsb_seed", seed);
        params.put("wsb_storeid", storeId);
        params.put("wsb_order_num", orderNum);
        params.put("wsb_test", testMode);
        params.put("wsb_currency_id", currency);
        params.put("wsb_total", amount);
        params.put("wsb_signature", wsbSignature);
        params.put("wsb_return_url", "https://webpay.by");
        params.put("wsb_cancel_return_url", "https://webpay.by");

        return buildUrl(PAYMENT_GATEWAY_URL, params);
    }

    private String getDecryptedSecretKey() {
        byte[] decrypted = new byte[encryptedSecretKey.length];
        for (int i = 0; i < encryptedSecretKey.length; i++) {
            decrypted[i] = (byte) (encryptedSecretKey[i] ^ xorMask);
        }
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private String computeMD5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String buildUrl(String baseUrl, Map<String, String> params) {
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        if (!params.isEmpty()) {
            urlBuilder.append("?");
            params.forEach((key, value) -> {
                try {
                    urlBuilder.append(URLEncoder.encode(key, StandardCharsets.UTF_8))
                            .append("=")
                            .append(URLEncoder.encode(value, StandardCharsets.UTF_8))
                            .append("&");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        return urlBuilder.toString();
    }
}


//import java.awt.Desktop;
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//public class WebpayPaymentService {
//
//    // Тестовый шлюз: https://securesandbox.webpay.by
//    // Реальный шлюз: https://payment.webpay.by
//    private static final String WEBPAY_URL = "https://securesandbox.webpay.by";
//    private static final byte[] ENCRYPTED_KEY = new byte[]{28, 14, 5, 19, 84}; // Это пример
//    private static final byte MASK = 0x5A;
//
//    public static void main(String[] args) {
//        // Данные вашего магазина из личного кабинета WEBPAY
//        String storeId = "433329306";
//        //String secretKey = "your_secret_key_from_cabinet";
//        String secretKey = getSecretKey();
//        // Используем массив байт с XOR-маской (простая защита от авто-сканеров строк)
//
//        // Данные текущего заказа
//        String orderNum = "INV-" + System.currentTimeMillis(); // Уникальный номер счета
//        String totalAmount = "15.50"; // Сумма к оплате (разделитель - точка)
//        String currency = "BYN"; // Валюта счета (BYN, RUB, USD, EUR)
//        String testMode = "1"; // 1 - тестовый режим, 0 - реальный платеж
//
//        try {
//            // 1. Генерируем случайное число (seed) для защиты подписи
//            String seed = String.valueOf(System.currentTimeMillis() / 1000);
//
//            // 2. Вычисляем электронную цифровую подпись (wsb_signature)
//            // Стандартное правило WEBPAY: md5(seed + storeid + order_num + test + currency + total + secret_key)
//            String signatureText = seed + storeId + orderNum + testMode + currency + totalAmount + secretKey;
//            String wsbSignature = computeMD5(signatureText);
//
//            // 3. Формируем мапу параметров для GET-запроса платежной страницы
//            Map<String, String> params = new LinkedHashMap<>();
//            params.put("*wsb_seed", seed);
//            params.put("wsb_storeid", storeId);
//            params.put("wsb_order_num", orderNum);
//            params.put("wsb_test", testMode);
//            params.put("wsb_currency_id", currency);
//            params.put("wsb_total", totalAmount);
//            params.put("wsb_signature", wsbSignature);
//
//            // Ссылки для возврата пользователя (в десктопе обычно ведут на заглушку вашего сайта)
//            params.put("wsb_return_url", "https://yourwebsite.by");
//            params.put("wsb_cancel_return_url", "https://yourwebsite.by");
//            params.put("wsb_notify_url", "https://yourwebsite.by");
//
//            // 4. Собираем полную платежную ссылку
//            String finalPaymentUrl = buildUrl(WEBPAY_URL, params);
//            System.out.println("Сгенерирована платежная ссылка: " + finalPaymentUrl);
//
//            // 5. Открываем стандартную защищенную страницу в браузере ПК
//            openBrowser(finalPaymentUrl);
//
//        } catch (Exception e) {
//            System.err.println("Ошибка при формировании или открытии платежа: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    //получение секретного ключа
//    private static String getSecretKey() {
//        byte[] decrypted = new byte[ENCRYPTED_KEY.length];
//        for (int i = 0; i < ENCRYPTED_KEY.length; i++) {
//            decrypted[i] = (byte) (ENCRYPTED_KEY[i] ^ MASK);
//        }
//        return new String(decrypted, StandardCharsets.UTF_8);
//    }
//
//    /**
//     * Вычисление хэша MD5 (требуется для подписи параметров WEBPAY)
//     */
//    private static String computeMD5(String input) throws NoSuchAlgorithmException {
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
//        StringBuilder hexString = new StringBuilder();
//        for (byte b : messageDigest) {
//            String hex = Integer.toHexString(0xff & b);
//            if (hex.length() == 1) hexString.append('0');
//            hexString.append(hex);
//        }
//        return hexString.toString();
//    }
//
//    /**
//     * Сборка URL-строки с кодированием параметров
//     */
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
//            // Удаляем лишнюю амперсанду в конце
//            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
//        }
//        return urlBuilder.toString();
//    }
//
//    /**
//     * Открытие ссылки в дефолтном браузере операционной системы (Windows/macOS/Linux)
//     */
//    private static void openBrowser(String url) throws IOException, URISyntaxException {
//        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
//            Desktop.getDesktop().browse(new URI(url));
//        } else {
//            // Альтернативный вызов для сред, где Desktop API заблокирован
//            Runtime runtime = Runtime.getRuntime();
//            String os = System.getProperty("os.name").toLowerCase();
//            if (os.contains("win")) {
//                runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
//            } else if (os.contains("mac")) {
//                runtime.exec("open " + url);
//            } else if (os.contains("nix") || os.contains("nux")) {
//                runtime.exec("xdg-open " + url);
//            } else {
//                throw new IOException("Не удалось определить ОС для открытия браузера.");
//            }
//        }
//    }
//}
