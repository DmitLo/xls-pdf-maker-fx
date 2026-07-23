package button;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import gui.GuiCompare;
import utils.Singleton;

import static ocr.DiffString.generateDiffString;


public class ButtonHandlerCompare implements ActionListener {

    private final JFrame frame;
    private final JTextField textFieldResult;
    private final JTextArea textArea;
    private final JTextField error;
    String last;
    String preLast;

    public ButtonHandlerCompare(JFrame frame, JTextField textFieldResult,
                                JTextArea textArea, JTextField error) {
        this.frame = frame;
        this.textFieldResult = textFieldResult;
        this.textArea = textArea;
        this.error = error;
    }

    public void actionPerformed(ActionEvent e) {

        frame.setTitle("Сравнить");
        System.out.println("Сравнить");

        System.out.println("action occurred for checking");
        List<String> out = Singleton.getInstance().getSharedValue();

        System.out.println("out = " + out);
        try {
            System.out.println("size = " + out.size());
            last = out.get(out.size() - 1).strip()
                    //.replace(" ", "")
                    .replace("«", "\"")
                    .replace("»", "\"");
            System.out.println("l = " + last);
            preLast = out.get(out.size() - 2).strip()
                    //.replace(" ", "")
                    .replace("«", "\"")
                    .replace("»", "\"");
            System.out.println("p = " + preLast);
            if (last.equals(preLast)) {
                error.setText("Названия идентичны");
            } else {
                error.setText("Названия не совпадают");
                windowCorrect(preLast, last);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void windowCorrect(String preLast, String last) {
        GuiCompare.gui(generateDiffString(preLast, last));
    }

}

