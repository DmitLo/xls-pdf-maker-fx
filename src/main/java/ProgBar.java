import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ProgBar {

    /**
     * Progress Bar (Copy)
     */
    // create a frame
    static JFrame frame;

    static JProgressBar progressBar;
//
//    public static void progress() {
//
//        // create a frame
//        frame = new JFrame("Progress...");
//        // create a panel
//        JPanel panel = new JPanel();
//
//        // create a progressbar
//        progressBar = new JProgressBar();
//        // set initial value
//        progressBar.setValue(0);
//        progressBar.setStringPainted(true);
//
//        // add progressbar
//        panel.add(progressBar);
//        // add panel
//        frame.add(panel);
//        // set the size of the frame
//        frame.setSize(150, 50);
//        // положение в центре
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//
//
//        fill();
//        frame.dispose();
//    }
//
//    // function to increase progress
//    public static void fill() {
//        int i = 0;
//        try {
//            while (i <= 100) {
//                // fill the menu bar
//                progressBar.setValue(i + 10);
//
//                // delay the thread
//                Thread.sleep(1000);
//                i += 20;
//            }
//        } catch (Exception ignored) {
//        }
//    }
//
//
//}
    public static void progress() {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Progress...");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 100);
            frame.setLayout(new BorderLayout());

            progressBar = new JProgressBar(0, 100);
            progressBar.setValue(0);
            progressBar.setStringPainted(true); // Показывать процент

            frame.add(progressBar, BorderLayout.CENTER);
            frame.setVisible(true);

            // Используем SwingWorker для обновления прогресс-бара
            new SwingWorker<Void, Integer>() {
                @Override
                protected Void doInBackground() throws Exception {
                    for (int i = 0; i <= 100; i++) {
                        Thread.sleep(50); // Имитация работы
                        publish(i);
                    }
                    return null;
                }

                @Override
                protected void process(java.util.List<Integer> chunks) {
                    int value = chunks.get(chunks.size() - 1);
                    progressBar.setValue(value);
                }
            }.execute();
            //frame.dispose();
        });
    }
}


