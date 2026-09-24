package button;

import gui.GuiAbout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ButtonHandlerAbout implements ActionListener {

    private final JFrame frame;

    public ButtonHandlerAbout(JFrame frame) {
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent e) {

        String title = frame.getTitle();
        System.out.println(title);

        try {
            GuiAbout.gui();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
