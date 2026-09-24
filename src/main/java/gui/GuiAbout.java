package gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * О программе
 */

public class GuiAbout {
    JFrame frame;
    JPanel panel;
    JButton button;
    String title;

    public static void gui() throws IOException {
        GuiAbout gui = new GuiAbout();
        gui.go();
    }

    public void go() throws IOException {

        title = "О программе PdfXlsMaker";
        frame = new JFrame(title);
        panel = new JPanel(true);
        //Для растягивания панели Обязательно
        panel.setLayout(new BorderLayout());

        // 1. Создаем кнопку с текстом
        button = new JButton("Закрыть");
        button.setPreferredSize(new Dimension(150, 30));// 2. Меняем цвета (например, белые буквы на синем фоне)

        // Используем JEditorPane и включаем поддержку HTML
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false); // запрещаем редактирование, если это просто для чтения

        JScrollPane areaScrollPane = new JScrollPane(editorPane);
        // Настройка политики скроллинга (по умолчанию они появляются при необходимости)
        areaScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        // 4. Задаем минимальный размер для самой панели скроллинга
        areaScrollPane.setPreferredSize(new Dimension(750, 400));

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(760, 480));
        frame.pack();
        frame.setVisible(true);
        //отменить изменение размеров мышки
        //frame.setResizable(false);

        panel.add(areaScrollPane, BorderLayout.CENTER);
        panel.add(button, BorderLayout.SOUTH);
        frame.add(panel);

        frame.setVisible(true);

        text(editorPane);

        //закрытие окна
        button.addActionListener(e -> {
            frame.dispose(); // Закрывает и освобождает ресурсы конкретного окна frame
        });

    }

    public static void text(JEditorPane editorPane) {
        String nl = System.lineSeparator();
        String htmlText = "<html>" +
                "<h1>Разработано 2026 ©TechL</h1>" +
                "<p><b>Программа работает с XLS файлами (сметы Татарникова), объединяет, выделяет материалы и оборудовние,</b></p>" +
                "<p><b>разбивает и объединяет PDF файлы, вставляет подписи и штампы в XLS, распознает текст (rus) с PDF, DOC, JPG,</b></p>" +
                "<p><b>сравнивает названия и показывает различия, проводит расчет MD5 для файлов.</b></p>" +
                "</html>";
        editorPane.setText(htmlText);
    }
}
