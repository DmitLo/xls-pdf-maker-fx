package payment;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Создание цифровой подписи
 */

public class WebPaySigner {

    public static String computeSignature(String seed, String storeId, String orderNum,
                                          int test, String currency, String total, String secretKey) {
        try {
            // 1. Склеиваем параметры строго в определенном WebPay порядке
            String rawString = seed + storeId + orderNum + test + currency + total + secretKey;

            System.out.println("rawString " + rawString);

            // 2. Хешируем строку через SHA-1
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = digest.digest(rawString.getBytes(StandardCharsets.UTF_8));

            // 3. Переводим байты в Hex-строку
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
//            return hexString.toString(); // Получаем готовую wsb_signature
            // Измените последнюю строчку в методе computeSignature класса WebPaySigner:
//            return hexString.toString().toUpperCase(); // <-- ОБЯЗАТЕЛЬНО В ВЕРХНЕМ РЕГИСТРЕ!
            //System.out.println(hexString);
            return hexString.toString(); // <-- ОБЯЗАТЕЛЬНО В ВЕРХНЕМ РЕГИСТРЕ!


        } catch (Exception e) {
            throw new RuntimeException("Ошибка при подсчете wsb_signature", e);
        }
    }
}
