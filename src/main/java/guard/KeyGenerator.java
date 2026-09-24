package guard;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

/**
 * генерация пары ключей (запускается один раз достаточно)
 */

public class KeyGenerator {
    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048); // Длина ключа
        KeyPair kp = kpg.generateKeyPair();

        // Кодируем ключи в строку Base64, чтобы их удобно было хранить
        String publicKey = Base64.getEncoder().encodeToString(kp.getPublic().getEncoded());
        String privateKey = Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded());

        System.out.println("PUBLIC_KEY (вставить в код программы):\n" + publicKey);
        System.out.println("\nPRIVATE_KEY (сохранить у себя в секрете!):\n" + privateKey);
    }
}
