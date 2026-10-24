package seedu.duke.food;

import java.math.BigDecimal;

/**
 * Represents one dish sold at a food stall.
 */
public record FoodItem(String name, BigDecimal price) {
    public FoodItem {
        assert name != null && !name.isBlank() : "Food item name should not be blank";
        assert price != null && price.signum() >= 0
                : "Food item price should not be negative";
    }
}
