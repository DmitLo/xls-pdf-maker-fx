package button;

import gui.GuiOcr;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Распознование прнажатию кнопки
 */

public class ButtonHandlerOcr implements ActionListener {

    private final JFrame frame;
    private final JTextField textFieldResult;
    private final JTextArea textArea;
    private final JTextField error;
    private final List<String> compare;

    public ButtonHandlerOcr(JFrame frame, JTextField textFieldResult,
                            JTextArea textArea, JTextField error, List<String> compare) {
        this.frame = frame;
        this.textFieldResult = textFieldResult;
        this.textArea = textArea;
        this.error = error;
        this.compare = compare;
    }

    public void actionPerformed(ActionEvent e) {

        String title = frame.getTitle();
        System.out.println(title);
        error.setText(title);

        if (title.equals("Распознать PDF, DOC, JPG")) {
            System.out.println("action occurred for checking");

            //получение списка файлов
            List<String> strings = textArea.getText().lines().collect(Collectors.toList());
            try {
                GuiOcr.gui(textFieldResult.getText(), strings, error, compare);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
