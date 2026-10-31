package seedu.duke.food;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Stores all hardcoded food court, stall, and food item from a text file
 */
public class FoodDirectory {

    private static final Path DEFAULT_FOOD_FILE =
            Paths.get("data", "food.txt");

    private final List<FoodCourt> foodCourts;

    /**
     * Creates a food directory using the default food data file.
     */
    public FoodDirectory() {
        this.foodCourts = FoodDataLoader.load(DEFAULT_FOOD_FILE);
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
