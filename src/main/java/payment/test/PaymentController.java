package payment.test;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentController {

    // Аннотация задает URL, который вы указали в поле wsb_return_url
    @GetMapping("/payment/success")
    public String handlePaymentSuccess(
            @RequestParam(name = "wsb_order_num") String orderNum,
            @RequestParam(name = "wsb_tid") String transactionId,
            Model model) {

        // Логика обработки успешной оплаты
        System.out.println("Успешная оплата!");
        System.out.println("Номер заказа (wsb_order_num): " + orderNum);
        System.out.println("Номер транзакции (wsb_tid): " + transactionId);

        // Передаем данные в шаблон (например, Thymeleaf), чтобы показать пользователю
        model.addAttribute("orderNum", orderNum);
        model.addAttribute("transactionId", transactionId);

        // Возвращаем имя HTML-страницы «спасибо за покупку»
        return "success_page";
    }
}
