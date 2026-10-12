package payment.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) // Игнорируем все остальные поля, которые нам не нужны
public class WebpayTransactionInfo {

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("order_num")
    private String orderNum;

    @JsonProperty("status")
    private String status;

    @JsonProperty("amount")
    private Double amount;

    // Геттеры и сеттеры
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getOrderNum() { return orderNum; }
    public void setOrderNum(String orderNum) { this.orderNum = orderNum; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}