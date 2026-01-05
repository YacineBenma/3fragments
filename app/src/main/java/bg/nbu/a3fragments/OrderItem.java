package bg.nbu.a3fragments;

/**
 * OrderItem - Model class representing a pizza item
 *
 * This is a simple data class (POJO - Plain Old Java Object)
 * that holds information about a pizza.
 *
 * Think of it like a container or box that holds pizza data.
 */
public class OrderItem {

    // Properties of a pizza
    private String name;     // Pizza name (e.g., "Margherita")
    private int price;       // Price in euros
    private String emoji;    // Pizza emoji for display

    /**
     * Constructor - Creates a new OrderItem
     *
     * @param name - Pizza name
     * @param price - Pizza price
     * @param emoji - Pizza emoji
     */
    public OrderItem(String name, int price, String emoji) {
        this.name = name;
        this.price = price;
        this.emoji = emoji;
    }

    // Getter methods - Allow other classes to read the data

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getEmoji() {
        return emoji;
    }

    /**
     * toString() - Converts the object to a readable string
     * Used for displaying the item in the order list
     */
    @Override
    public String toString() {
        return emoji + " " + name + " - €" + price;
    }
}