package payment;

import guard.HardwareSigner;
import guard.LicenseGenerator;
import guard.LicenseGeneratorIn;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class PaymentDialogSwing extends JDialog {

    private final WebpayPaymentManager paymentManager = new WebpayPaymentManager();

    public PaymentDialogSwing() throws Exception {
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
        Item[] items = {
                new Item(1,"Товар1", 2, 10f),
                new Item(2, "Товар2", 1, 0.5f)
        };

        float total = 0f;
        for (Item item : items) {
            System.out.println(item.name + ": " + item.sum());
            total += item.sum();
        }

        //запуск тест оплаты
        WebPayPaymentService.processPayment(currency, items, total);

        // Внутри вашего ActionListener для кнопки "Оплатить" или при старте главного окна:
        try {
            PaymentManager paymentManager = new PaymentManager();
            paymentManager.startLocalServer();

            // Дальше ваш код, который открывает браузер с платежной страницей
            // Desktop.getDesktop().browse(new URI("ссылка_на_оплату"));

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Не удалось запустить сервер для проверки оплаты.");
        }

//        //получение HASH машины
//        String hash = HardwareSigner.getHWID();
//        //генерация ключа для локальной программы и сохранение в файле
//        LicenseGeneratorIn.mainStart(hash);

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
