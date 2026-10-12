package button;

import gui.GuiAbout;
import payment.PaymentDialogSwing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonHandlerPay implements ActionListener {

    private final JFrame frame;

    public ButtonHandlerPay(JFrame frame) {
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent e) {

        String title = frame.getTitle();
        System.out.println(title);

        new PaymentDialogSwing();
    }
}
