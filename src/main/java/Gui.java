import javax.swing.*;
import java.awt.*;

/**
 * Графический интерфейс
 */

public class Gui {

    JFrame frame;
    JPanel panel;
    JButton button1, button2, button3, button4;
    JTextField textField1,textFieldResult, textFieldResultMd5;
    String title;
    JComboBox<String> petList;
    JTextArea textArea;//


    public static void gui() {
        Gui gui = new Gui();
        gui.go();
    }

    public void go() {
        // поля ввода
        title = "Выберите режим работы...";
        frame = new JFrame(title);
        panel = new JPanel(true);

        //текстовые поля для отображения выбранных файлов
        textFieldResult = new JTextField(30);
        textFieldResultMd5 = new JTextField(30);

        textArea = new JTextArea(15,30);//

        //textArea.setLineWrap(true);
        //textArea.setWrapStyleWord(true);

        JScrollPane areaScrollPane = new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //areaScrollPane.setPreferredSize(new Dimension(20, 200));
        //textArea.setEditable(false);
        //textArea.getPreferredScrollableViewportSize();
        //textArea.setLineWrap(true);


        String[] petStrings = {"Объединить XLS", "Выделить материалы",
                "Выделить оборудование", "Разбить PDF", "Объединить PDF",
                "Вставить подпись в XLS", "Вставить штамп в XLS"};
        //Create the combo box, select item at index 4.
        //Indices start at 0, so 1 specifies the pig.
        petList = new JComboBox<>(petStrings);
        petList.setSelectedIndex(0);
        petList.addActionListener(new ComboBoxHandler(petList, frame));

        button1 = new JButton("Добавить файлы");
        button2 = new JButton("Сохранить результат");
        button3 = new JButton("Печать"); //test
        button4 = new JButton("MD5"); //test

        //frame.setSize(400, 500);
        panel.add(textFieldResult);
        panel.add(textFieldResultMd5);
        panel.add(petList);
        panel.add(button1);
        panel.add(button2);
        panel.add(button3); //test
        panel.add(button4);
        //panel.add(textArea);
        panel.add(areaScrollPane, BorderLayout.CENTER);

        //panel.setBackground(Color.blue);
        frame.pack();
        frame.add(panel);
        //цвет перха фрейма
        //frame.setBackground(Color.blue);
        textArea.setBackground(Color.white);
        panel.setBackground(Color.lightGray);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 430);
        //panel.setForeground(Color.blue);
        //panel.setBackground(Color.white);
        // frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        // Set's the window to be "always on top"
        //frame.setAlwaysOnTop( true );
        // положение в центре
        frame.setLocationRelativeTo(null);

        textFieldResult.setText("./result.xls");
        textFieldResultMd5.setText("MD5 result");
        textFieldResultMd5.setEnabled(false);

        //добавление Drag and Drop
        new DragAndDrop().enableDragAndDrop(textArea);

        button1.addActionListener(new ButtonHandlerOpen(textArea));
        //Объединение XLS
        button2.addActionListener(new ButtonHandlerUnionXls(frame, textFieldResult, textArea, textFieldResultMd5));
        //Выделение
        button2.addActionListener(new ButtonHandlerSelect(frame, textFieldResult, textArea, textFieldResultMd5));
        //Объединение PDF
        button2.addActionListener(new ButtonHandlerUnionPdf(frame, textFieldResult, textArea, textFieldResultMd5));
        //Разбиение PDF
        button2.addActionListener(new ButtonHandlerSplittingPdf(frame, textFieldResult, textArea, textFieldResultMd5));
        //Добавление подписей в сметы XLS
        button2.addActionListener(new ButtonHandlerInsertImage(frame, textFieldResult, textArea, textFieldResultMd5));
        //Добавление подписи и печати в XLS
        button2.addActionListener(new ButtonHandlerInsertStamp(frame, textFieldResult, textArea, textFieldResultMd5));

        button3.addActionListener(new ButtonHandlerPrintXls(frame, textFieldResult, textArea));
        button4.addActionListener(new ButtonHandlerMd5(frame, textFieldResult, textArea, textFieldResultMd5));
    }

}