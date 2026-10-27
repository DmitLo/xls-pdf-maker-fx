package payment;

import guard.HardwareSigner;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PaymentDialogSwingServ extends JDialog {

    private final WebpayPaymentManager paymentManager = new WebpayPaymentManager();

    public PaymentDialogSwingServ() throws Exception {
        setTitle("Ожидание оплаты...");
        setSize(400, 200);

        // 1. Перехватываем закрытие окна на "крестик"
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                paymentManager.cancelCurrentPayment();
            }
        });

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

        // получение HASH машины
        String hash = HardwareSigner.getHWID();
        //запуск тест оплаты
        String orderNum = WebPayPaymentServiceServ.processPayment(currency, items, total, hash);

        PaymentManagerServ paymentManagerServ = new PaymentManagerServ();
        paymentManagerServ.startPollingPaymentStatus(orderNum);

//        //генерация ключа для локальной программы и сохранение в файле
//        LicenseGeneratorIn.mainStart(hash);

    }
}
