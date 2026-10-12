import guard.GuiCheck;
import gui.GuiMain;

import javax.swing.*;

/**
 * Выделение оборудования
 */
public class AppStart {
    public static void main(String[] args) {


        // Запуск Swing-приложения в потоке обработки событий
        SwingUtilities.invokeLater(() -> {
            System.out.println("start");
            //GuiMain.gui();
            GuiCheck.mainCheck();
            System.out.println("stop");
        });
    }
}




