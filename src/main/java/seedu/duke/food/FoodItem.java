package seedu.duke.food;

import java.math.BigDecimal;

/**
 * Represents one dish sold at a food stall.
 */
public record FoodItem(String name, BigDecimal price) {
}
