import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

/**
 * Графический интерфейс
 */

public class GuiMain {

    JFrame frame;
    JPanel panelMain, panel1, panel2, panel3, panel4, panel5;
    JButton buttonFile, buttonOk, buttonPrint, buttonMd5, buttonCompare;
    JTextField textField1,textFieldResult, textFieldResultMd5;
    String title;
    JComboBox<String> petList;
    JTextArea textArea;//
    java.util.List<String> compare = new ArrayList<>();


    public static void gui() {
        GuiMain guiMain = new GuiMain();
        guiMain.go();
    }

    public void go() {
        // поля ввода
        title = "Выберите режим работы...";
        frame = new JFrame(title);
        panelMain = new JPanel(true);
        panel1 = new JPanel(true);
        panel2 = new JPanel(true);
        panel3 = new JPanel(true);
        panel4 = new JPanel(true);
        panel5 = new JPanel(true);


        // верхний контейнер для всего
        JPanel topContainer = new JPanel(new GridLayout (4, 1));

        //текстовые поля для отображения выбранных файлов
        textFieldResult = new JTextField(30);
        textFieldResultMd5 = new JTextField(30);

        textArea = new JTextArea(15,40);//
        textArea = new JTextArea();
        //textArea.setMinimumSize(new Dimension(100, 400));
        textArea.setLineWrap(true); // Перенос текста на новую строку

        // добавление прокрутки
        JScrollPane areaScrollPane = new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        String[] petStrings = {"Объединить XLS", "Выделить материалы",
                "Выделить оборудование", "Разбить PDF", "Объединить PDF",
                "Вставить подпись в XLS", "Вставить штамп в XLS", "Распознать PDF, DOC, JPG"};

        // комбобокс
        petList = new JComboBox<>(petStrings);
        petList.setSelectedIndex(0);
        petList.addActionListener(new ComboBoxHandler(petList, frame));

        buttonFile = new JButton("Добавить файлы");
        buttonOk = new JButton("Выполнить BOX");
        buttonPrint = new JButton("Печать"); //test
        buttonMd5 = new JButton("MD5"); //test
        buttonCompare = new JButton("Сравнить"); //test

       // добавление на панель
        panel1.add(textFieldResult);
        panel2.add(textFieldResultMd5);
        panel3.add(petList);
        panel3.add(buttonFile);
        panel4.add(buttonOk);
        panel4.add(buttonPrint); //test
        panel4.add(buttonMd5);
        panel4.add(buttonCompare);
        // добавление в контейнер
        topContainer.add(panel1);
        topContainer.add(panel2);
        topContainer.add(panel3);
        topContainer.add(panel4);

        JPanel bottomPane = new JPanel(new BorderLayout());
        bottomPane.add(areaScrollPane, BorderLayout.CENTER);

        frame.add(topContainer, BorderLayout.NORTH);
        frame.add(bottomPane, BorderLayout.CENTER);
        frame.pack();

        //цвет перха фрейма
        textArea.setBackground(Color.white);
        panel1.setBackground(Color.lightGray);
        panel2.setBackground(Color.lightGray);
        panel3.setBackground(Color.lightGray);
        panel4.setBackground(Color.lightGray);
        panel5.setBackground(Color.lightGray);
        panelMain.setBackground(Color.lightGray);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(500, 460));
        //отменить изменение размеров мышки
        //frame.setResizable(false);
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

        //фиксация по высоте растяжения
        final int fixedWidth = frame.getWidth();
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                frame.setSize(fixedWidth, frame.getHeight());
            }
        });

        buttonFile.addActionListener(new ButtonHandlerOpen(textArea));
        //Объединение XLS
        buttonOk.addActionListener(new ButtonHandlerUnionXls(frame, textFieldResult, textArea, textFieldResultMd5));
        //Выделение
        buttonOk.addActionListener(new ButtonHandlerSelect(frame, textFieldResult, textArea, textFieldResultMd5));
        //Объединение PDF
        buttonOk.addActionListener(new ButtonHandlerUnionPdf(frame, textFieldResult, textArea, textFieldResultMd5));
        //Разбиение PDF
        buttonOk.addActionListener(new ButtonHandlerSplittingPdf(frame, textFieldResult, textArea, textFieldResultMd5));
        //Добавление подписей в сметы XLS
        buttonOk.addActionListener(new ButtonHandlerInsertImage(frame, textFieldResult, textArea, textFieldResultMd5));
        //Добавление подписи и печати в XLS
        buttonOk.addActionListener(new ButtonHandlerInsertStamp(frame, textFieldResult, textArea, textFieldResultMd5));

        buttonPrint.addActionListener(new ButtonHandlerPrintXls(frame, textFieldResult, textArea));
        buttonMd5.addActionListener(new ButtonHandlerMd5(frame, textFieldResult, textArea, textFieldResultMd5));
        //Сравнение
        buttonOk.addActionListener(new ButtonHandlerOcr(frame, textFieldResult, textArea, textFieldResultMd5, compare));
        buttonCompare.addActionListener(new ButtonHandlerCompare(frame, textFieldResult, textArea, textFieldResultMd5));
    }
}