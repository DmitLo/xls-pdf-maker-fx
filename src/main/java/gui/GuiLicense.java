package gui;

import guard.GuiCheck;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Площадка для результата распознования
 */

public class GuiLicense {
    JFrame frame;
    JPanel panel;
    JButton button;
    String title;
    JTextPane textPane;

    public static void gui() throws IOException {
        GuiLicense gui = new GuiLicense();
        gui.go();
    }

    public void go()  {

        title = "Лицензия";
        frame = new JFrame(title);
        panel = new JPanel(true);
        //Для растягивания панели Обязательно
        panel.setLayout(new BorderLayout());

        textPane = new JTextPane();
        // 1. Создаем кнопку с текстом
        button = new JButton("Закрыть");
        button.setPreferredSize(new Dimension(150, 30));// 2. Меняем цвета (например, белые буквы на синем фоне)



        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(660, 180));
        frame.pack();
        // Блокируем изменение размера окна
        frame.setResizable(false);
        frame.setVisible(true);
        //отменить изменение размеров мышки
        //frame.setResizable(false);

        panel.add(textPane, BorderLayout.CENTER);
        panel.add(button, BorderLayout.SOUTH);
        frame.add(panel);
        frame.setVisible(true);

        text(textPane);

        //закрытие окна
        button.addActionListener(e -> {
            frame.dispose(); // Закрывает и освобождает ресурсы конкретного окна frame
        });

    }

    //вывод информации
    public static void text(JTextPane textPane) {
        //String nl = System.lineSeparator();
        textPane.setEditable(false);
        String text1 = "Код системы: \n" + GuiCheck.currentHWID + "\n";
        String text2 = "Ключ: \n";
        textPane.setText(text1 + text2 + spread(readLic()));

        // Создаем набор атрибутов для центрирования
        SimpleAttributeSet centerAttributes = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttributes, StyleConstants.ALIGN_CENTER);

        // Получаем документ и применяем атрибуты ко всей длине текста
        StyledDocument doc = textPane.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), centerAttributes, false);
    }

    //чтение лицензии
    public static String readLic() {
        String content = "";
        try {
            Path pathString = Path.of(GuiCheck.getSaveDirectory());
            Path path = pathString.resolve("license.lic");
            content = Files.readString(path);

            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    //разделение на чвсти
    public static String spread(String text) {
        int chunkSize = 80; // Количество символов в одной строке

        String result = splitWord(text, chunkSize);
        System.out.println(result);
        return result;
    }

    //разделение слова на части (движок)
    public static String splitWord(String word, int length) {
        if (word == null || length <= 0) {
            return word;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i += length) {
            // Если это не первая итерация, добавляем перенос строки перед новой частью
            if (i > 0) {
                sb.append("\n");
            }

            // Вырезаем кусок строки
            int end = Math.min(i + length, word.length());
            sb.append(word, i, end);
        }
        return sb.toString();
    }
}
