package button;

import pdf.PdfSplitting;
import utils.ProgBar;
import utils.SelectEquipment;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ButtonHandlerSelect implements ActionListener {

    private final JFrame frame;
    private final JTextField textFieldResult;
    private final JTextArea textArea;
    private final JTextField error;
    //статическая переменная файла аналога

    public ButtonHandlerSelect(JFrame frame, JTextField textFieldResult,
                               JTextArea textArea, JTextField error) {
        this.frame = frame;
        this.textFieldResult = textFieldResult;
        this.textArea = textArea;
        this.error = error;
    }

    public void actionPerformed(ActionEvent e) {

        String title = frame.getTitle();
        System.out.println(title);
        error.setText(title);
        // материалы или оборудование "лож" для оборудования
        boolean materialsEquipment;
        // материал аналог
        boolean materialsAnalog = false;
        // удаление аналога
        boolean delAnalog = false;

        if (title.equals("Выделить материалы")) {
            materialsEquipment = true;
        } else {
            materialsEquipment = false;
        }


        if (title.equals("Выделить оборудование") || title.equals("Выделить материалы")) {
            System.out.println("action occurred for checking");
            //получение списка файлов
            List<String> strings = textArea.getText().lines().collect(Collectors.toList());

            //запуск задачи в пуле потоков
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<?> future = executor.submit(() -> {
                // Код вашей задачи
                System.out.println("Задача в пуле потоков");
                try {
                    SelectEquipment.select(strings.get(0), textFieldResult.getText(), materialsEquipment,
                            materialsAnalog, delAnalog);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });

            try {
                future.get(); // Ожидание завершения задачи
            } catch (InterruptedException | ExecutionException exception) {
                // Обработка ошибок
            } finally {
                executor.shutdown();
            }
            //шкала
            ProgBar.progress();
        }
    }
}
