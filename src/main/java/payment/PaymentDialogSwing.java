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
        String currency = "BYN";
//        String [] itemName = { "Tовар1", "Товар2" };
//        int [] quantity = { 2, 1 };
//        float [] itemPrice = { 10f, 0.5f };
        Item[] items = {
                new Item(1,"Товар1", 2, 10f),
                new Item(2, "Товар2", 1, 0.5f)
        };

        float total = 0f;
        for (Item item : items) {
            System.out.println(item.name + ": " + item.sum());
            total += item.sum();
        }

        //тест оплаты
        WebPayPaymentService.processPayment(currency, items, total);

//        paymentManager.processPayment(currency, itemName, quantity, itemPrice, total, new WebpayPaymentManager.PaymentCallback() {
//            @Override
//            public void onSuccess() {
//                javax.swing.SwingUtilities.invokeLater(() -> dispose());
//                //WebPayPaymentService_test3.processPayment();
//            }
//
//            @Override
//            public void onFailed(String reason) {
//                // обработка ошибки в UI
//            }
//        });
    }
}
