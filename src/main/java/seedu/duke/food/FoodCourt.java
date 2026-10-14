package seedu.duke.food;

import java.util.List;

/**
 * Represents a food location containing multiple food stalls.
 */
public record FoodCourt(String name, List<FoodStall> stalls) {

}
