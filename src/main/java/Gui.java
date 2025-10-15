import javax.swing.*;
import java.awt.*;

/**
 * Графический интерфейс
 */

public class Gui {

    JFrame frame;
    JPanel panel;
    JButton button1, button2, button3;
    JTextField textField1,textFieldResult;
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

        //frame.setSize(400, 500);
        panel.add(textFieldResult);
        panel.add(petList);
        panel.add(button1);
        panel.add(button2);
        panel.add(button3); //test
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
        frame.setSize(400, 400);
        //panel.setForeground(Color.blue);
        //panel.setBackground(Color.white);
        // frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        // Set's the window to be "always on top"
        //frame.setAlwaysOnTop( true );
        // положение в центре
        frame.setLocationRelativeTo(null);

        textFieldResult.setText("./result.xls");

        //добавление Drag and Drop
        new DragAndDrop().enableDragAndDrop(textArea);

        button1.addActionListener(new ButtonHandlerOpen(textArea));
        //Объединение XLS
        button2.addActionListener(new ButtonHandlerUnionXls(frame, textFieldResult, textArea));
        //Выделение
        button2.addActionListener(new ButtonHandlerSelect(frame, textFieldResult, textArea));
        //Объединение PDF
        button2.addActionListener(new ButtonHandlerUnionPdf(frame, textFieldResult, textArea));
        //Разбиение PDF
        button2.addActionListener(new ButtonHandlerSplittingPdf(frame, textFieldResult, textArea));
        //Добавление подписей в сметы XLS
        button2.addActionListener(new ButtonHandlerInsertImage(frame, textFieldResult, textArea));
        //Добавление подписи и печати в XLS
        button2.addActionListener(new ButtonHandlerInsertStamp(frame, textFieldResult, textArea));


        button3.addActionListener(new ButtonHandlerTest(frame, textFieldResult, textArea));
    }

}