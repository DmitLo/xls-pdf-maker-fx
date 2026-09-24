package print;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;

/**
 * Печать всех документов
 */

public class PrintHelper {

    public static void printFile(JTextField textFieldResult, JTextArea textArea) {

        //получение списка файлов
        List<String> strings = textArea.getText().lines().collect(Collectors.toList());
        // Проверяем, поддерживает ли текущая ОС отправку на печать через Desktop API
        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.PRINT)) {
            JOptionPane.showMessageDialog(null,
                    "Печать через систему не поддерживается на данном компьютере.",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String filePath = strings.get(0);
        File file = new File(filePath);

        // Проверяем, существует ли вообще файл
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null,
                    "Файл не найден: " + filePath,
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Отправляем файл на системную печать
            Desktop.getDesktop().print(file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Не удалось отправить файл на печать: " + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
