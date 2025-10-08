import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Обработка запуска открыть
 */
class ButtonHandlerOpen implements ActionListener {

    private final JTextArea textArea;

    public ButtonHandlerOpen(JTextArea textArea) {

        this.textArea = textArea;
    }

    public void actionPerformed(ActionEvent e) {

        System.out.println("action occurred for checking");
        String filename = GuiNew.gui();

        //добавление в элемент с новой строки
        try {
            textArea.getDocument().insertString(0, filename + "\n", null);
        } catch (BadLocationException badLocationException) {
            badLocationException.printStackTrace();
        }

        //подсчет количество строк
        int numberLines = textArea.getText() // Text obtained from the getText method of the JTextArea
                .lines() // Get the text line by line
                .filter(line -> !line.isBlank()) // Filter all lines removing empty lines
                .collect(Collectors.toList()) // Convert the flattened content to a list
                .size(); // We get the final size being the total number of lines
        System.out.println(numberLines);

        //получение списка файлов
        List<String> strings = textArea.getText().lines().collect(Collectors.toList());
        System.out.println(strings.get(0));
    }
}


