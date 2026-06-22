import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class GuiFiles {
    JFileChooser frame;
    File folder;

    public static String gui() {
        GuiFiles gui = new GuiFiles();
        return gui.go();

    }

    public String go() {

        // поля вывода
        frame = new JFileChooser("Выбор файла xls, pdf, tif, jpg, doc, png");

        frame.setCurrentDirectory(frame.getCurrentDirectory());
        frame.setFileFilter(new FileNameExtensionFilter("Plain Text & Images Files", "xls", "pdf",
                "tif", "jpg", "jpeg", "doc", "png"));

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
