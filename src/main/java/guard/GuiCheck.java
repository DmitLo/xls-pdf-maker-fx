package guard;

import gui.GuiMain;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;

public class GuiCheck {

    public static String currentHWID;
    // Укажите здесь название вашей компании и приложения (без пробелов, на английском)
    public static String COMPANY_NAME = "MyCompany";
    public static String APP_NAME = "XPMaker";
    public static String mainPath;

    public static void main(String[] args) {
        // 1. Получаем HWID текущего компьютера (код из предыдущего ответа с библиотекой OSHI)
        currentHWID = HardwareSigner.getHWID();

        // 2. Проверяем наличие валидной лицензии рядом с файлом программы
        if (LicenseValidator.checkLicense(currentHWID)) {
            // Лицензия верна! Запускаем программу
            SwingUtilities.invokeLater(GuiMain::gui);
        } else {
            // Лицензии нет или она от другого ПК. Показываем окно активации
            SwingUtilities.invokeLater(() -> showActivationWindow(currentHWID));
        }
    }

    private static void showActivationWindow(String hwid) {
        JFrame frame = new JFrame("Требуется активация");
        frame.setSize(600, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        File folder = getAndCreateSaveDirectory();
        System.out.println("Безопасный путь для сохранения настроек: " + folder.getAbsolutePath());
        mainPath = folder.getAbsolutePath();


        JTextArea text = new JTextArea("Программа не активирована.\n" +
                "Отправьте этот код разработчику mail.retech@gmail.com:\n" + hwid +
                "\n\nПоместите файл license.lic в папку с программой и перезапустите её.\n" +
                "Путь: " + folder);

        text.setEditable(false); // Делаем нередактируемым, как мы учились ранее!
        text.setLineWrap(true);
        text.setWrapStyleWord(true);

        frame.add(new JScrollPane(text));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Вспомогательный метод, который не просто возвращает путь,
     * но и сразу создает эти папки на диске, если их еще нет.
     */
    public static File getAndCreateSaveDirectory() {
        File dir = new File(getSaveDirectory());
        if (!dir.exists()) {
            dir.mkdirs(); // Создает всю цепочку папок (и компанию, и приложение)
        }
        return dir;
    }

    public static String getSaveDirectory() {
        String os = System.getProperty("os.name").toLowerCase();
        String baseDir;

        if (os.contains("win")) {
            // Windows: C:\Users\Имя\AppData\Roaming
            baseDir = System.getenv("AppData");
            if (baseDir == null) {
                baseDir = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Roaming";
            }
            // Итоговый путь: ...\AppData\Roaming\MyCompany\MyCoolApp
            return baseDir + File.separator + COMPANY_NAME + File.separator + APP_NAME;

        } else if (os.contains("mac")) {
            // macOS: /Users/Имя/Library/Application Support
            baseDir = System.getProperty("user.home") + "/Library/Application Support";
            // Итоговый путь: .../Application Support/MyCompany/MyCoolApp
            return baseDir + File.separator + COMPANY_NAME + File.separator + APP_NAME;

        } else {
            // Linux / Unix: /home/Имя/.config
            baseDir = System.getenv("XDG_CONFIG_HOME");
            if (baseDir == null) {
                baseDir = System.getProperty("user.home") + File.separator + ".config";
            }
            // Итоговый путь: .../.config/mycoolapp
            return baseDir + File.separator + APP_NAME.toLowerCase();
        }
    }


}
