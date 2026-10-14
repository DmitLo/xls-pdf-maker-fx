package payment.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

public class WebpayPaymentServ {

    //private static final String PAYMENT_GATEWAY_URL = "https://webpay.by";
//    public static final String PAYMENT_GATEWAY_URL = "https://securesandbox.webpay.by/api/v1/payment/";
    //public static final String PAYMENT_GATEWAY_URL = "https://sandbox.webpay.by/";
//    public static final String PAYMENT_GATEWAY_URL = "https://redirect.gandal.by";
    public static final String PAYMENT_GATEWAY_URL = "http://localhost:3000";

//    private final String storeId;
    private final Integer storeId;
    private final byte[] encryptedSecretKey;
    private final byte xorMask;

//    public WebpayPaymentService(String storeId, byte[] encryptedSecretKey, byte xorMask) {
public WebpayPaymentServ(Integer storeId, byte[] encryptedSecretKey, byte xorMask) {
//        this.storeId = storeId;
        this.storeId = storeId;
        this.encryptedSecretKey = encryptedSecretKey;
        this.xorMask = xorMask;
    }

//    public String createPaymentUrl(String orderNum, String amount, String currency, String testMode) throws Exception {
public String createPaymentUrl(String orderNum, String currency, Integer version, Integer testMode,  String[] itemName, Integer[] quantity,
                               Float[] itemPrice, Float total) throws Exception {
        String seed = String.valueOf(System.currentTimeMillis() / 1000);
        String secretKey = getDecryptedSecretKey();

        String signatureText = seed + storeId + orderNum + testMode + currency + total + secretKey;
        String wsbSignature = computeMD5(signatureText);

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("*scart", "");
        params.put("wsb_version", version);
        params.put("wsb_storeid", storeId);
        params.put("wsb_order_num", orderNum);
        params.put("wsb_test", testMode);
        params.put("wsb_currency_id", currency);
        params.put("wsb_seed", seed);

        params.put("wsb_invoice_item_name", itemName);
        params.put("wsb_invoice_item_quantity", quantity);
        params.put("wsb_invoice_item_price", itemPrice);
        params.put("wsb_total", total);

        params.put("wsb_signature", wsbSignature);
        
        // 2. Инициализируем Jackson ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        // 3. Конвертируем карту в JSON-строку
        String jsonBody = mapper.writeValueAsString(params);

        // Выводим результат
        System.out.println("jsonBody for POST" + jsonBody);

        //return buildUrl(PAYMENT_GATEWAY_URL, params);
        return jsonBody;
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

//    private String buildUrl(String baseUrl, Map<String, String> params) {
//        StringBuilder urlBuilder = new StringBuilder(baseUrl);
//        if (!params.isEmpty()) {
//            urlBuilder.append("?");
//            params.forEach((key, value) -> {
//                try {
//                    urlBuilder.append(URLEncoder.encode(key, StandardCharsets.UTF_8))
//                            .append("=")
//                            .append(URLEncoder.encode(value, StandardCharsets.UTF_8))
//                            .append("&");
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            });
//            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
//        }
//        return urlBuilder.toString();
//    }
}

