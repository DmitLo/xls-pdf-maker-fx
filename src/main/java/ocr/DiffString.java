package ocr;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.Patch;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Определение различий между строками
 */



public class DiffString {
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

    private static final Pattern TOKEN_PATTERN = Pattern.compile("([\\wА-Яа-яЁё]+|[^\\wА-Яа-яЁё\\s]+|\\s+)");
}
