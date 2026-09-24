package button;

import print.PrintHelper;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Для печати в XLS не доделано
 */

public class ButtonHandlerPrint implements ActionListener {

    private final JFrame frame;
    private final JTextField textFieldResult;
    private final JTextArea textArea;

    public ButtonHandlerPrint(JFrame frame, JTextField textFieldResult,
                              JTextArea textArea) {
        this.frame = frame;
        this.textFieldResult = textFieldResult;
        this.textArea = textArea;
    }

    public void actionPerformed(ActionEvent e) {
        //PaginationPrint.printPage(textFieldResult, textArea);
        PrintHelper.printFile(textFieldResult, textArea);
    }
}

