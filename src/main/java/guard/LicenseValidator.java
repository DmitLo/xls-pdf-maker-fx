package guard;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static guard.GuiCheck.getAndCreateSaveDirectory;
import static guard.GuiCheck.mainPath;

/**
 * сравнение лицензий
 */

public class LicenseValidator {

    // Сюда вставляется сгенерированный публичный ключ
    private static final String PUBLIC_KEY_STR = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtP0xQp4YQn7CA1Gukp8FWzb6anOLqcPWWXFOxDLGMQ/0eXHhZWQ/3tfuAIGHDZvS9abjO7D110ITnJlPn13bnwaFdRjO1QoYPlyxE91/YHxfkuIYZ/7cjSkZpDWdt7vPiP403ehPrzBENHnZhbYcYTzqyiAHWW510Dq1xXz9dbofhUpFdPapsIXjunYbVT/0t54nCg6g5SVhQMFcO6FAQzEs/C3Fz83vYX8Hmb3im1CkxnQBWZb+h2lVKIduL0i0OVhoOeqWpg5ug3HAnnF9VwUJ9UBvwg2CEPNh2NF8hGSQLAoXgWf0j1QtZY6a6/naaFsyBItROgaOpFj4AfwYDwIDAQAB";

    public static boolean checkLicense(String currentHWID) {
        try {

            File licenseFile = new File(getAndCreateSaveDirectory(), "license.lic");
            if (!licenseFile.exists()) return false;

            // Читаем подпись из файла лицензии
            String licenseSignature = new String(Files.readAllBytes(licenseFile.toPath())).trim();

            // Восстанавливаем публичный ключ из строки
            byte[] publicBytes = Base64.getDecoder().decode(PUBLIC_KEY_STR);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // Инициализируем проверку цифровой подписи
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);

            // Проверяем: совпадает ли подпись с текущим HWID компьютера
            sig.update(currentHWID.getBytes());

            byte[] signatureBytes = Base64.getDecoder().decode(licenseSignature);
            return sig.verify(signatureBytes); // вернет true, если файл лицензии валиден для этого ПК

        } catch (Exception e) {
            return false;
        }
    }
}

