package payment;

import javafx.stage.Stage;

public class PaymentWindowController {

    private final WebpayPaymentManager paymentManager = new WebpayPaymentManager();

    public void openPaymentDialog(Stage paymentStage) {

        // 1. Вешаем слушатель на крестик (закрытие окна)
        paymentStage.setOnCloseRequest(event -> {
            // Принудительно тушим фоновый поток опроса WEBPAY
            paymentManager.cancelCurrentPayment();
        });

        // 2. Если внутри окна есть обычная кнопка "Отмена"
//        cancelButton.setOnAction(event -> {
//            paymentManager.cancelCurrentPayment();
//            paymentStage.close(); // Закрываем окно
//        });

        // Запуск процесса оплаты
        paymentManager.processPayment("10.00", "BYN", new WebpayPaymentManager.PaymentCallback() {
            @Override
            public void onSuccess() {
                // Опрос сам остановится внутри менеджера, нам нужно только закрыть UI
                javafx.application.Platform.runLater(paymentStage::close);
            }

            @Override
            public void onFailed(String reason) {
                javafx.application.Platform.runLater(() -> {
                    // Показываем ошибку пользователю
                });
            }
        });

        paymentStage.show();
    }
}

