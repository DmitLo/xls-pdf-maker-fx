package payment;

/**
 * Объект товара
 */

public class Item {
    public int ordinal;
    public String name;
    public int quantity;
    public float price;

    public Item(int ordinal, String name, int quantity, float price) {
        this.ordinal = ordinal;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public float sum() {
        return quantity * price;
    }
}
