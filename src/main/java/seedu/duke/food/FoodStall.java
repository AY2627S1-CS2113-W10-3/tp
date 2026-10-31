package seedu.duke.food;
import java.util.List;

/**
 * Represents a stall in a food court and the dishes on its menu.
 */
public record FoodStall(String name, List<FoodItem> menu) {
    public FoodStall {
        assert name != null && !name.isBlank() : "Stall name should not be blank";
        assert menu != null : "Stall menu should not be null";
    }

    /**
     * Finds a dish on this stall's menu by name, ignoring letter case and surrounding spaces.
     *
     * @param itemName name typed by the user, e.g. {@code "roast chicken rice"}
     * @return the matching dish, or {@code null} if there is none
     */
    public FoodItem findItem(String itemName) {
        for (FoodItem item : menu) {
            if (item.name().equalsIgnoreCase(itemName.trim())) {
                return item;
            }
        }
        return null;
    }
}
