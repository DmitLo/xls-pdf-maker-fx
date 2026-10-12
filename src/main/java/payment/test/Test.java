package payment.test;

import payment.test.WebPayPaymentService_test2;

public class Test {
        public static void main(String[] args) {
            System.out.println("=== ЗАПУСК ПЛАТЕЖНОЙ СИСТЕМЫ WEBPAY ===");

            // 1. Создаем объект (экземпляр) класса WebPayPaymentService
            WebPayPaymentService_test2 paymentService = new WebPayPaymentService_test2();

            // 2. Вызываем метод запуска оплаты
            // Этот метод сам посчитает подпись, отправит H2H JSON-запрос
            // и автоматически откроет браузер с формой ввода карты
            paymentService.processPayment();

            System.out.println("=======================================");
            System.out.println("Запрос выполнен. Проверьте ваш браузер!");
        }
}
