import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GuiCompare {
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
        GuiCompare gui = new GuiCompare();
        gui.go(result);
    }

    public void go(String result) {

        title = "Сопоставленный текст";
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

        // Основная метка для статуса
        //JLabel statusLabel = new JLabel("Готово");
        //statusLabel.setBorder(BorderFactory.createEtchedBorder());

//        // Панель статус-бара
//        JPanel statusBar = new JPanel();
//        statusBar.setLayout(new BorderLayout());
//        statusBar.add(statusLabel, BorderLayout.CENTER);
//        // Добавляем элементы на форму
//        frame.add(statusBar, BorderLayout.SOUTH);
//        frame.setVisible(true);

        //compareAndFormat(last, preLast);



        //запуск статус-бара
      //  status(statusLabel);

//        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
//
//            @Override
//            protected Void doInBackground() throws Exception {
//                // ВЫПОЛНЯЕТСЯ В ФОНЕ: ваш сложный процесс
//                process(fileNameResult, strings, error, compare);
//                return null;
//            }
//
//            private void process(String fileNameResult, List<String> strings, JTextField error, List<String> compare) {
//                // Запуск распознавания в отдельном потоке
//                CompletableFuture<String> futureText = CompletableFuture.supplyAsync(() -> {
//                    try {
//                        return Ocr.osr(strings);
//                    } catch (IOException e) {
//                        throw new RuntimeException("Ошибка распознавания", e);
//                    }
//                });
//
//                // Действия, которые нужно выполнить после завершения OCR
//                futureText.thenAccept(result -> {
//                    textArea.setText(result);
//
//                    // Отслеживаем любые изменения текста (ввод, удаление, вставка)
//                    textArea.getDocument().addDocumentListener(new DocumentListener() {
//                        @Override
//                        public void insertUpdate(DocumentEvent e) {
//                            sendText();
//                        }
//
//                        @Override
//                        public void removeUpdate(DocumentEvent e) {
//                            //sendText();
//                        }
//
//                        @Override
//                        public void changedUpdate(DocumentEvent e) {
//                            //sendText();
//                        }
//
//                        // Вспомогательный метод для отправки данных родительскому окну
//                        private void sendText() {
//                            //parent.updateText(textArea.getText());
//                            error.setText("OK");
//                            compare.add(textArea.getText());
//                            // Сохранить данные из окна
//                            Singleton.getInstance().setSharedValue(compare);
//                        }
//                    });
//                }).join(); // join нужен для демонстрации, чтобы программа не закрылась сразу
//            }
//        };
//
//        worker.execute(); // Запуск сложного процесса
    }

//    //статус бар
//    public static void status(JLabel statusLabel) {
//        new Thread(() -> {
//            try {
//                for (int i = 0; i <= 100; i = i + 1) {
//                    Thread.sleep(100); // Имитация работы
//                    int finalI = i;
//                    SwingUtilities.invokeLater(() -> statusLabel.setText("Распознование... " + finalI + "%"));
//                }
//                SwingUtilities.invokeLater(() -> statusLabel.setText("Распознование успешно завершено"));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }

}
