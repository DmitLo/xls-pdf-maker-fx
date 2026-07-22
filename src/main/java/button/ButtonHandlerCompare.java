package button;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;

import java.util.regex.Pattern;
import com.github.difflib.patch.DeltaType;
import gui.GuiCompare;
import utils.Singleton;

import java.util.ArrayList;
import java.util.regex.Matcher;


public class ButtonHandlerCompare implements ActionListener {

    private final JFrame frame;
    private final JTextField textFieldResult;
    private final JTextArea textArea;
    private final JTextField error;
    String last;
    String preLast;
    private static final Pattern TOKEN_PATTERN = Pattern.compile("([\\wА-Яа-яЁё]+|[^\\wА-Яа-яЁё\\s]+|\\s+)");

    public ButtonHandlerCompare(JFrame frame, JTextField textFieldResult,
                                JTextArea textArea, JTextField error) {
        this.frame = frame;
        this.textFieldResult = textFieldResult;
        this.textArea = textArea;
        this.error = error;
    }

    public void actionPerformed(ActionEvent e) {

        frame.setTitle("Сравнить");
        System.out.println("Сравнить");

        System.out.println("action occurred for checking");
        List<String> out = Singleton.getInstance().getSharedValue();

        System.out.println("out = " + out);
        try {
            System.out.println("size = " + out.size());
            last = out.get(out.size() - 1).strip()
                    //.replace(" ", "")
                    .replace("«", "\"")
                    .replace("»", "\"");
            System.out.println("l = " + last);
            preLast = out.get(out.size() - 2).strip()
                    //.replace(" ", "")
                    .replace("«", "\"")
                    .replace("»", "\"");
            System.out.println("p = " + preLast);
            if (last.equals(preLast)) {
                error.setText("Названия идентичны");
            } else {
                error.setText("Названия не совпадают");
                windowCorrect(preLast, last);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void windowCorrect(String preLast, String last) throws IOException {
        GuiCompare.gui(generateDiffString(preLast, last));

    }

    public static String generateDiffString(String text1, String text2) {
        // 1. Разбиваем строки на списки токенов (слова, знаки препинания)
        List<String> tokens1 = tokenize(text1);
        List<String> tokens2 = tokenize(text2);

        // 2. Находим различия с помощью библиотеки java-diff-utils
        Patch<String> patch = DiffUtils.diff(tokens1, tokens2);

        // 3. Формируем итоговую строку различий
        StringBuilder result = new StringBuilder();
        int position = 0;

        for (AbstractDelta<String> delta : patch.getDeltas()) {
            int startPos = delta.getSource().getPosition();

            // Добавляем неизмененный текст до дельты
            while (position < startPos && position < tokens1.size()) {
                result.append(tokens1.get(position));
                position++;
            }

            // Обработка удаленных элементов
            if (delta.getType() == DeltaType.DELETE || delta.getType() == DeltaType.CHANGE) {
                result.append("[");
                for (String token : delta.getSource().getLines()) {
                    result.append("-").append(token);
                }
                result.append("]");
            }

            // Обработка добавленных элементов
            if (delta.getType() == DeltaType.INSERT || delta.getType() == DeltaType.CHANGE) {
                result.append("[");
                for (String token : delta.getTarget().getLines()) {
                    result.append("+").append(token);
                }
                result.append("]");
            }

            position += delta.getSource().size();
        }

        // Добавляем оставшийся неизмененный текст в конце
        while (position < tokens1.size()) {
            result.append(tokens1.get(position));
            position++;
        }

        return result.toString();
    }

    // Метод для токенизации предложения
    private static List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(text);
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens.stream().filter(t -> !t.isEmpty()).collect(Collectors.toList());
    }

}

