package payment.dto;

// Объект для передачи на сервер, html и код компьютера
public class SendFromAppToServ {

        private final String htmlString;
        private final String orderNum;
        private final String hashString;

        public SendFromAppToServ(String htmlString, String orderNum, String hashString) {
            this.htmlString = htmlString;
            this.orderNum = orderNum;
            this.hashString = hashString;
        }

        // Обязательны геттеры (или публичные поля)
        public String getHtmlString() { return htmlString; }
        public String getOrderNum() { return orderNum; }
        public String getHashString() { return hashString; }
}
