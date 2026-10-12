package button;

import gui.GuiAbout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ButtonHandlerClose implements ActionListener {

    private final JFrame frame;

    public ButtonHandlerClose(JFrame frame) {
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent e) {

        String title = frame.getTitle();
        System.out.println(title);

        // Логика команды: корректное завершение работы
        System.out.println("Приложение закрывается...");
        System.exit(0);
    }
}
