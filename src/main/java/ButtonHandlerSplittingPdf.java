import com.aspose.cells.AbstractCalculationEngine;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class ButtonHandlerSplittingPdf implements ActionListener {


    private final JFrame frame;
    private final JTextField textFieldResult;
    private final JTextArea textArea;

    public ButtonHandlerSplittingPdf( JFrame frame, JTextField textFieldResult, JTextArea textArea) {
        this.frame = frame;
        this.textFieldResult = textFieldResult;
        this.textArea = textArea;
    }

    public void actionPerformed(ActionEvent e) {

        String title = frame.getTitle();
        System.out.println(title);

        if (title.equals("Разбить PDF")) {
            System.out.println("action occurred for checking");
//            if (textFieldResult.getText().isEmpty()) {
//                textFieldResult.setText("./union.xls");
//            }

            //шкала
            ProgBar.progress();

            //получение списка файлов
            List<String> strings = textArea.getText().lines().collect(Collectors.toList());
            try {
                PdfSplitting.pdfSomePages( textFieldResult.getText(), strings);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            //шкала
            //ProgBar.progress();
        }
    }
}
