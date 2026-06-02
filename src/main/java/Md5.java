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

        //FileInputStream fis = new FileInputStream(fileNameResult);
        System.out.println("fileNameResult = " + strings);
        try {
            originalText = Files.readString(Path.of(strings.get(0)));
            ok = true;
        } catch (Exception e) {
            System.out.println("Выберите файл...");
            textFieldResultMd5.setText("Выберите файл...");
        }


        try {
            // 1. Создаем экземпляр генератора хешей с алгоритмом MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 2. Превращаем текст в массив байтов и передаем его алгоритму
            byte[] hashBytes = md.digest(originalText.getBytes(StandardCharsets.UTF_8));

            // 3. Конвертируем полученные байты в привычную шестнадцатеричную (Hex) строку
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                // Преобразуем байт в 16-ричный вид и убираем лишние знаки
                String hex = Integer.toHexString(0xff & b);

                // Добавляем лидирующий ноль, если число получилось однозначным
                if (hex.length() == 1) {
                    hexString.append('0');
                }
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