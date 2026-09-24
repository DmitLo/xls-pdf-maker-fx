package guard;

import java.io.FileWriter;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * генератор лицензии
 */

public class LicenseGenerator {
    // Ваш секретный приватный ключ из Шага 1 (никому не показывайте)
    private static final String PRIVATE_KEY_STR = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC0/TFCnhhCfsIDUa6SnwVbNvpqc4upw9ZZcU7EMsYxD/R5ceFlZD/e1+4AgYcNm9L1puM7sPXXQhOcmU+fXdufBoV1GM7VChg+XLET3X9gfF+S4hhn/tyNKRmkNZ23u8+I/jTd6E+vMEQ0edmFthxhPOrKIAdZbnXQOrXFfP11uh+FSkV09qmwheO6dhtVP/S3nicKDqDlJWFAwVw7oUBDMSz8LcXPze9hfweZveKbUKTGdAFZlv6HaVUoh24vSLQ5WGg56pamDm6DccCecX1XBQn1QG/CDYIQ82HY0XyEZJAsCheBZ/SPVC1ljprr+dpoWzIEi1E6Bo6kWPgB/BgPAgMBAAECggEAS83UKPvAW3Ft3ygB/yUdgDPJtHZKo8MDbxjXDbPacMgm9AqvheD0I2i7JXka0KdYN0071U9r2Emznx5TTANqrfN0SDQoxO/icZQvEovuAsJ04TdTU5NaWsFBHRbbxQ2jgdQ+3kajwVNs7bGvWqKIxm1WRWwex+VM8Pb2FQ4htoGWSqpveuQu9wlcsmEI0BZe63t1qskL57wmsDNJMiH9uEUqBC9aU7jHCSejVDcXu3Vs2KPKXt4vqBxAD+vyxWEW6EZe1MGq6WjteaAVQR10Ac4AybJNvlYbRZJILXvpDTgGAikyHqZqp0Fz5oyAWbj+9XA1YRbVM3VdXS4jBnWB8QKBgQDHs9dokbbfMsfW//8wk/hc35lPxkqhVobjcNLIFbis+O4Rr6qOpHHiB7PoTbsrE4QK8BC9o6JV1LEbbSG09n9ZOYKUnNpyYuDSEF3nSoe2FChnzrPycyARr6HfbKidkmANRanG+/IwhL0Na1C606WpZbL3ERBecz10KB6sCSekqQKBgQDoAtWeXEysDDy5I20alsJKgGAUXA3gHrFO402XnRRO408puToQWi1UR0/EVolChpqeM5u+uDxH3moktXAM+qMFdrKNoIvlhejziyJolqyYm7ISUmQq22mKH71Jm7uUS7fWREqBv1gKLcmBLVZUgUG/L+XcWcxPHUXwVZCvZ4UR9wKBgQCwuIIXxFLLAzn7FcEysM7pCOm3wabLTTHC1VWBwE8TqpmqRwSsg/NPbTeUjCzkermvoC+AUezW/YfY2GMqq7OYNp9Ked+Jt9xxqBAhHEAqLX5wFmgBVkV7wSuC9rnj7uNW+125e70fmGdgF6ZyHdcMp7WOk6gjnOE2CEbUQKm3OQKBgDWxijyHAduDTX5PO42+vCI/yXyMvU1T7Rbjte5TlQKYypZOXqiwJfNuOYCnwkZaHmhkazPdMV40k/4A23i1fIrkKHhsYlPxBLNwTLtFu5Xp2nExDit48djcdZMWbnfoehglX9TdMoPQiuTRWDxgWZXkgN7ZDVZ14Ny5AK4HWB2LAoGAcThokoFLrojpUd7cTHfuavmh/4SxpDo8HzonsClcA3NApQy0umtPawSArdvj02NZYISuDYgkity08sR3P8gAy3o7LCEeBkQ6CNrR4AoN2IHXotVbloNpW/4KPVPG6m4+rAT79l5yjN+G6dNWrVurgC8UUIK2sCJ9W3Uhl1yvJOE=";

    public static void generateLicenseFile(String clientHWID) throws Exception {
        // Восстанавливаем приватный ключ
        byte[] privateBytes = Base64.getDecoder().decode(PRIVATE_KEY_STR);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        // Подписываем HWID клиента с помощью приватного ключа
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(privateKey);
        sig.update(clientHWID.getBytes());
        byte[] signatureBytes = sig.sign();

        String licenseContent = Base64.getEncoder().encodeToString(signatureBytes);

        // Записываем зашифрованную подпись в файл license.lic
        try (FileWriter writer = new FileWriter("license.lic")) {
            writer.write(licenseContent);
        }
        System.out.println("Файл license.lic успешно создан для HWID: " + clientHWID);
    }

    public static void main(String[] args) throws Exception {
        // Пример: пришел HWID от клиента. Вставляем его сюда и запускаем!!!!!!!!!!!
        generateLicenseFile("81FBDAB07355DAB9C8055E85BCE306AE61B0A39EBC92E9532DB22186381CC8B6");
    }
}
