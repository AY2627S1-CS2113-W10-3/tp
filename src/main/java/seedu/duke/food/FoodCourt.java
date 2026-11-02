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

    /**
     * Finds a stall in this food court by name, ignoring letter case and surrounding spaces.
     *
     * @param stallName name typed by the user, e.g. {@code "chicken rice"}
     * @return the matching stall, or {@code null} if there is none
     */
    public FoodStall findStall(String stallName) {
        for (FoodStall stall : stalls) {
            if (stall.name().equalsIgnoreCase(stallName.trim())) {
                return stall;
            }
        }
        return null;
    }
}
