import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class GuiNew {
    JFileChooser frame;
    File folder;

    public static String gui() {
        GuiNew gui = new GuiNew();
        return gui.go();

    }

    public String go() {

        // поля вывода
        frame = new JFileChooser("Выбор файла xls");

        frame.setCurrentDirectory(frame.getCurrentDirectory());
        frame.setFileFilter(new FileNameExtensionFilter("Plain Text Files", "xls"));

        //задание текущей дериктории

        // Open the save dialog
        frame.showOpenDialog(null);

        // получение имени файла
        File curFile = frame.getSelectedFile();
        folder = frame.getCurrentDirectory();
        System.out.println(curFile.getAbsolutePath());
        System.out.println(folder.getAbsolutePath());
        //возврат абсллютного пути файла
        return curFile.getAbsolutePath();

    }
}
