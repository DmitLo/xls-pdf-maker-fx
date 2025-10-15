import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.util.List;
import java.util.stream.Collectors;

public class ButtonHandlerTest implements ActionListener {

    private final JFrame frame;
    private final JTextField textFieldResult;
    private final JTextArea textArea;

    public ButtonHandlerTest(JFrame frame, JTextField textFieldResult,
                             JTextArea textArea) {
        this.frame = frame;
        this.textFieldResult = textFieldResult;
        this.textArea = textArea;
    }

    public void actionPerformed(ActionEvent e) {

//        String title = frame.getTitle();
//        System.out.println(title);
//
//        if (title.equals("Вставить подпись в XLS")) {
//            System.out.println("action occurred for checking");
//            //шкала
//            ProgBar.progress();
//
//            //получение списка файлов
//            List<String> strings = textArea.getText().lines().collect(Collectors.toList());
//            try {
//                ReadFromExcelInsertImage.readFromExcelImage(strings.get(0), textFieldResult.getText());
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//
//            //шкала
//            //ProgBar.progress();
//        }

        try {
            boolean complete = textArea.print(); // Displays a print dialog
            if (complete) {
                System.out.println("Printing complete.");
            } else {
                System.out.println("Printing cancelled.");
            }
        } catch (PrinterException pe) {
            System.err.println("Printing failed: " + pe.getMessage());
        }
    }
}
