package seedu.duke.food;

import java.util.List;

/**
 * Represents a food location containing multiple food stalls.
 */
public record FoodCourt(String name, List<FoodStall> stalls) {
    public FoodCourt {
        assert name != null && !name.isBlank() : "Food court name should not be blank";
        assert stalls != null : "Food court stalls should not be null";
    }
}
