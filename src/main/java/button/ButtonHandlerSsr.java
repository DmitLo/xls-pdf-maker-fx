package button;

import pdf.PdfUnion;
import utils.ProgBar;
import utils.SelectEquipment;
import utils.SelectSsr;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ButtonHandlerSsr implements ActionListener {

    private final JFrame frame;
    private final JTextField textFieldResult;
    private final JTextArea textArea;
    private final JTextField error;
    //статическая переменная файла аналога

    public ButtonHandlerSsr(JFrame frame, JTextField textFieldResult,
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
        boolean materialsEquipment = false;
        // материал аналог
        boolean materialsAnalog = false;
        // удаление аналога
        boolean delAnalog = false;

        if (title.equals("Цифры с ССР")) {
            materialsEquipment = true;
        }


        if (title.equals("Цифры с ССР")) {
            System.out.println("action occurred for checking");
            //получение списка файлов
            List<String> strings = textArea.getText().lines().collect(Collectors.toList());

            //запуск задачи в пуле потоков
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<?> future = executor.submit(() -> {
                // Код вашей задачи
                System.out.println("Задача в пуле потоков");
                try {
                    SelectSsr.select(strings.get(0), textFieldResult.getText());
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
