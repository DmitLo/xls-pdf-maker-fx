package utils;

import javax.swing.*;
import java.awt.*;

public class StatusBar {
    public static void start() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Пример Статус-бара");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);

            // Основная метка для статуса
            JLabel statusLabel = new JLabel("Готово");
            statusLabel.setBorder(BorderFactory.createEtchedBorder());

            // Панель статус-бара
            JPanel statusBar = new JPanel();
            statusBar.setLayout(new BorderLayout());
            statusBar.add(statusLabel, BorderLayout.CENTER);

            // Добавляем элементы на форму
            frame.add(new JLabel("Окно приложения", SwingConstants.CENTER), BorderLayout.CENTER);
            frame.add(statusBar, BorderLayout.SOUTH);

            frame.setVisible(true);

            // Имитация фоновой задачи (обновление статус-бара)
            new Thread(() -> {
                try {
                    for (int i = 0; i <= 100; i++) {
                        Thread.sleep(50); // Имитация работы
                        int finalI = i;
                        SwingUtilities.invokeLater(() -> statusLabel.setText("Загрузка... " + finalI + "%"));
                    }
                    SwingUtilities.invokeLater(() -> statusLabel.setText("Задача успешно завершена"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }
}
