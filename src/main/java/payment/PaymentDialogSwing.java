package payment;

import javax.swing.JDialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PaymentDialogSwing extends JDialog {

    private final WebpayPaymentManager paymentManager = new WebpayPaymentManager();

    public PaymentDialogSwing() {
        setTitle("Ожидание оплаты...");
        setSize(400, 200);

        // 1. Перехватываем закрытие окна на "крестик"
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                paymentManager.cancelCurrentPayment();
            }
        });

        // 2. Логика для кнопки "Отмена" внутри формы
//        cancelButton.addActionListener(e -> {
//            paymentManager.cancelCurrentPayment();
//            dispose(); // Закрыть окно
//        });

        // Запуск процесса
        paymentManager.processPayment("10.00", "BYN", new WebpayPaymentManager.PaymentCallback() {
            @Override
            public void onSuccess() {
                javax.swing.SwingUtilities.invokeLater(() -> dispose());
            }

            @Override
            public void onFailed(String reason) {
                // обработка ошибки в UI
            }
        });
    }
}
