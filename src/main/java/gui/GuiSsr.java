package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GuiSsr {
    //JFileChooser frame;
    File folder;
    JFrame frame;
    JPanel panel;
    JButton button1, button2, button3, button4, button5;
    JTextField textField1, textFieldResult, textFieldResultMd5;
    String title;
    JComboBox<String> petList;
    JTextArea textArea;//

    public static void gui(String result) {
        GuiSsr gui = new GuiSsr();
        gui.go(result);
    }

    public void go(String result) {

        title = "Цифры с ССР";
        frame = new JFrame(title);
        panel = new JPanel(true);
        //Для растягивания панели Обязательно
        panel.setLayout(new BorderLayout());

        // Благодаря этому размер области
        textArea = new JTextArea();//
        textArea.setMinimumSize(new Dimension(200, 600));
        textArea.setLineWrap(true); // Перенос текста на новую строку
        textArea.setWrapStyleWord(true);

        JScrollPane areaScrollPane = new JScrollPane(textArea);
        // Настройка политики скроллинга (по умолчанию они появляются при необходимости)
        areaScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        // 4. Задаем минимальный размер для самой панели скроллинга
        areaScrollPane.setPreferredSize(new Dimension(750, 400));

        textArea.setBackground(Color.white);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(760, 480));
        frame.pack();
        frame.setVisible(true);
        //отменить изменение размеров мышки
        //frame.setResizable(false);

        panel.add(areaScrollPane, BorderLayout.CENTER);
        frame.add(panel);

        System.out.println("result = " + result);
        textArea.setText(result);

    }

}
