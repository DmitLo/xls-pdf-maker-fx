package payment;

/**
 * Объект товара
 */

public class Item {
    int ordinal;
    String name;
    int quantity;
    float price;

    Item(int ordinal, String name, int quantity, float price) {
        this.ordinal = ordinal;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    float sum() {
        return quantity * price;
    }
}
