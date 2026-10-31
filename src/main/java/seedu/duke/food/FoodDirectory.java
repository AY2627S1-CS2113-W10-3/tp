package seedu.duke.food;

import java.util.List;

/**
 * Stores food court, stall, and food item data loaded from a text file.
 */
public class FoodDirectory {

    private final List<FoodCourt> foodCourts;

    /**
     * Creates a food directory using the food data resource.
     */
    public FoodDirectory() {
        this.foodCourts = FoodDataLoader.loadFoodData();
    }

    /**
     * Returns all food courts.
     *
     * @return list of food courts
     */
    public List<FoodCourt> getLocations() {
        return foodCourts;
    }
}
