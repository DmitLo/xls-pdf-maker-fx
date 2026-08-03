package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateUserFolder {
    public static Path create() {
        // Получаем домашнюю директорию пользователя для любой ОС
        String userHome = System.getProperty("user.home");

        // Указываем имя новой папки (например, "MyNewFolder")
        Path folderPath = Paths.get(userHome, "XlsPdfMaker");

        try {
            // Создает директорию и все недостающие родительские каталоги, если их нет
            Files.createDirectories(folderPath);
            System.out.println("Папка успешно создана: " + folderPath);
        } catch (IOException e) {
            System.out.println("Не удалось создать папку: " + e.getMessage());
        }
        return folderPath;
    }
}
