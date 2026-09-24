package jna;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MacOsClicker {

    public static void main(String[] args) {
        // Укажите точное название приложения и текст кнопки
        String appName = "Калькулятор"; // Имя процесса/окна
        String buttonText = "1";         // Текст кнопки очистки

        String scriptOpen = String.format("tell application \"Calculator\" to activate");

        String scriptRun = String.format(
                "tell application \"Calculator\" to activate\n" +
                        "\n" +
                        "tell application \"System Events\"\n" +
                        "    tell process \"Calculator\"\n" +
                        "        -- Делаем калькулятор активным и даем ему долю секунды на запуск\n" +
                        "        set frontmost to true\n" +
                        "        delay 0.5\n" +
                        "        \n" +
                        "        -- Вводим первое число (5)\n" +
                        "        keystroke \"5\"\n" +
                        "        delay 0.2\n" +
                        "        \n" +
                        "        -- Вводим знак плюс (+)\n" +
                        "        keystroke \"+\"\n" +
                        "        delay 0.2\n" +
                        "        \n" +
                        "        -- Вводим второе число (5)\n" +
                        "        keystroke \"5\"\n" +
                        "        delay 0.2\n" +
                        "        \n" +
                        "        -- Нажимаем Enter (кнопка «равно»)\n" +
                        "        key code 76\n" +
                        "    end tell\n" +
                        "end tell\n",
                appName, buttonText, buttonText, buttonText
        );

        String scriptLibreDoc = String.format("do shell script \"/Applications/LibreOffice.app/Contents/MacOS/soffice --writer &> /dev/null &\"");
        String scriptLibreXls = String.format("do shell script \"/Applications/LibreOffice.app/Contents/MacOS/soffice --calc &> /dev/null &\"");


        // Выполняем AppleScript через встроенную консольную утилиту osascript
        //executeAppleScript(scriptOpen);
        //executeAppleScript(scriptRun);
        executeAppleScript(scriptLibreXls);
    }

    private static void executeAppleScript(String script) {
        try {
            // Вызываем команду osascript через массив строк, чтобы избежать проблем с пробелами
            String[] command = {"nohup", "osascript", "-e", script};
            Process process = Runtime.getRuntime().exec(command);

            // Читаем возможные ошибки выполнения
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            StringBuilder errorOutput = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Команда клика успешно отправлена!");
            } else {
                System.out.println("Ошибка при выполнении скрипта (Код " + exitCode + "):");
                System.out.println(errorOutput.toString());
                System.out.println("Подсказка: Проверьте правильность имени приложения, кнопки или права доступа.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
