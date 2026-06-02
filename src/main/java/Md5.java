import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Md5 {
    public static void createMd5 (String fileNameResult, List<String> strings, JTextField textFieldResultMd5) throws IOException {
        String originalText = "Hello World";
        boolean ok = false;
        byte[] fileBytes = new byte[0];

        //FileInputStream fis = new FileInputStream(fileNameResult);
        System.out.println("fileNameResult = " + strings.get(0));
        try {
            //originalText = Files.readString(Path.of(strings.get(0)));
            ok = true;
            // 1. Читаем все байты файла
            fileBytes = Files.readAllBytes(Path.of(strings.get(0)));
        } catch (Exception e) {
            System.out.println("Выберите файл...");
            textFieldResultMd5.setText("Выберите файл...");
        }

        try {
            // 2. Инициализируем алгоритм MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(fileBytes);

            // 3. Переводим байты хэша в шестнадцатеричную (Hex) строку
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            // Выводим результат в консоль
            if (ok) {
                System.out.println("Исходный текст: " + originalText);
                System.out.println("MD5-хеш:       " + hexString.toString());
                textFieldResultMd5.setText(hexString.toString());
                textFieldResultMd5.setEnabled(true);
            }

        } catch (NoSuchAlgorithmException e) {
            // Исключение сработает, если алгоритм MD5 не поддерживается системой
            System.err.println("Алгоритм MD5 не найден: " + e.getMessage());
        }
    }
}