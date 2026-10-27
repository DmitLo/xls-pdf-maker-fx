package payment.dto;

public class ReceiveFromServToApp {
        private String success;
        private String hwid;
        private String license;


        public ReceiveFromServToApp() {
        }

        // Обязательны геттеры (или публичные поля)
        public String getSuccess() { return success; }
        public String getHwid() {return  hwid; }
        public String getLicense() { return license; }
}
