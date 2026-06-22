import javax.swing.*;
import java.util.List;

// 1. Параметр: тип результата (Void)
// 2. Параметр: тип для промежуточных обновлений (Integer)
class MyWorker extends SwingWorker<Void, Integer> {
    private final JLabel statusLabel;
    private final JProgressBar progressBar;
    private final JButton startButton;

    public MyWorker(JLabel statusLabel, JProgressBar progressBar, JButton startButton) {
        this.statusLabel = statusLabel;
        this.progressBar = progressBar;
        this.startButton = startButton;
    }

    // Выполняется в фоновом потоке (ДЕЛАТЬ ТУТ: запросы к БД, вычисления, сеть)
    @Override
    protected Void doInBackground() throws Exception {
        for (int i = 0; i <= 100; i += 10) {
            Thread.sleep(300); // Имитация долгой работы
            publish(i); // Отправка промежуточных данных в Event Dispatch Thread (EDT)
        }
        return null;
    }

    // Выполняется в потоке EDT для обновления UI (промежуточные результаты)
    @Override
    protected void process(List<Integer> chunks) {
        int progress = chunks.get(chunks.size() - 1);
        progressBar.setValue(progress);
        statusLabel.setText("Выполнено: " + progress + "%");
    }

    // Выполняется в потоке EDT после завершения фонового процесса
    @Override
    protected void done() {
        startButton.setEnabled(true);
        statusLabel.setText("Задача завершена!");
    }
}