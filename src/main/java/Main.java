import javax.swing.*;

/**
 * Выделение оборудования
 */
public class Main {
    public static void main(String[] args) {


        // Запуск Swing-приложения в потоке обработки событий
        SwingUtilities.invokeLater(() -> {
            // MainWindow mw = new MainWindow();
            // mw.setVisible(true);

            System.out.println("start");
            GuiMain.gui();
            System.out.println("stop");


            //вывод результата объекта
//        for (Read z:
//                read) {
//            System.out.println(z);
//        }

        });
    }
}

