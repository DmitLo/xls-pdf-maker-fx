package button;

import utils.Md5;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
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

        //if (title.equals("Объединить XLS")) {
         //   System.out.println("action occurred for checking");
//            if (textFieldResult.getText().isEmpty()) {
//                textFieldResult.setText("./union.xls");
//            }

            //шкала
            //utils.ProgBar.progress();

            //получение списка файлов
            List<String> strings = textArea.getText().lines().collect(Collectors.toList());
            try {
                Md5.createMd5(textFieldResult.getText(), strings, textFieldResultMd5);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            //шкала
            //utils.ProgBar.progress();
       // }
    }
}
