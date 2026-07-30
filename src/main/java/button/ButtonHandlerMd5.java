package button;

import utils.Md5;
import utils.ProgBar;
import xls.ReadFromExcelInsertImage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Обработка запуска сохранить
 */
public class ButtonHandlerMd5 implements ActionListener {
    private final JFrame frame;
    private final JTextField textFieldResult;
    private final JTextArea textArea;
    private final JTextField textFieldResultMd5;

    public ButtonHandlerMd5(JFrame frame, JTextField textFieldResult, JTextArea textArea,
                            JTextField textFieldResultMd5) {
        this.frame = frame;
        this.textFieldResult = textFieldResult;
        this.textArea = textArea;
        this.textFieldResultMd5 = textFieldResultMd5;
    }

    public void actionPerformed(ActionEvent e) {

        String title = frame.getTitle();
        System.out.println(title);
        //получение списка файлов
        List<String> strings = textArea.getText().lines().collect(Collectors.toList());

        //запуск задачи в пуле потоков
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> {
            // Код вашей задачи
            System.out.println("Задача в пуле потоков");
            try {
                Md5.createMd5(textFieldResult.getText(), strings, textFieldResultMd5);
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
