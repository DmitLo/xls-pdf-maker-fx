package button;

import gui.GuiLicense;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ButtonHandlerLicense implements ActionListener {

    private final JFrame frame;

    public ButtonHandlerLicense(JFrame frame) {
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent e) {

        String title = frame.getTitle();
        System.out.println(title);

        try {
            GuiLicense.gui();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
