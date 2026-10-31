package seedu.duke.food;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads food court, stall, and menu data from text file.
 *
 * <p> Each non-blank line must follow this format:
 * {@code food court|stall|food item|price}</p>
 */
public final class FoodDataLoader {

    private static final String FIELD_SEPARATOR = "\\|";
    private static final int EXPECTED_FIELD_COUNT = 4;

    private FoodDataLoader() {
        // Prevent instantiation of this utility class.
    }

    /**
     * Loads food data from the given file.
     *
     * @param filePath path to the food data file
     * @return food courts in the same order as they appear in the file
     * @throws IllegalStateException if the file cannot be read or contains invalid data
     */
    public static List<FoodCourt> load(Path filePath) {
        try {
            return loadLines(Files.readAllLines(filePath));
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not read food data from " + filePath,
                    exception
            );
        }
    }

    /**
     * Converts file lines into the food-court hierarchy.
     */
    private static List<FoodCourt> loadLines(List<String> lines) {
        Map<String, Map<String, List<FoodItem>>> foodData =
                new LinkedHashMap<>();

        for (int index = 0; index < lines.size(); index++) {
            String line = lines.get(index).strip();

            if (line.isBlank()) {
                continue;
            }

            String[] fields = line.split(FIELD_SEPARATOR, -1);

            if (fields.length != EXPECTED_FIELD_COUNT) {
                throw invalidLine(index + 1,
                        "expected food court|stall|item|price");
            }

            String foodCourtName = fields[0].strip();
            String stallName = fields[1].strip();
            String foodItemName = fields[2].strip();
            String priceText = fields[3].strip();

            if (foodCourtName.isBlank()
                    || stallName.isBlank()
                    || foodItemName.isBlank()
                    || priceText.isBlank()) {
                throw invalidLine(index + 1, "no field may be blank");
            }

            BigDecimal price;

            try {
                price = new BigDecimal(priceText);
            } catch (NumberFormatException exception) {
                throw invalidLine(index + 1,
                        "price must be a valid number");
            }

            if (price.signum() < 0) {
                throw invalidLine(index + 1,
                        "price cannot be negative");
            }

            FoodItem foodItem = new FoodItem(foodItemName, price);

            foodData
                    .computeIfAbsent(foodCourtName, name -> new LinkedHashMap<>())
                    .computeIfAbsent(stallName, name -> new ArrayList<>())
                    .add(foodItem);
        }

        List<FoodCourt> foodCourts = new ArrayList<>();

        for (Map.Entry<String, Map<String, List<FoodItem>>> courtEntry
                : foodData.entrySet()) {

            List<FoodStall> stalls = new ArrayList<>();

            for (Map.Entry<String, List<FoodItem>> stallEntry
                    : courtEntry.getValue().entrySet()) {

                stalls.add(new FoodStall(
                        stallEntry.getKey(),
                        List.copyOf(stallEntry.getValue())
                ));
            }

            foodCourts.add(new FoodCourt(
                    courtEntry.getKey(),
                    List.copyOf(stalls)
            ));
        }

        return List.copyOf(foodCourts);
    }

    /**
     * Creates an informative exception for an invalid data line.
     */
    private static IllegalStateException invalidLine(
            int lineNumber,
            String reason) {
        return new IllegalStateException(
                "Invalid food data on line "
                        + lineNumber
                        + ": "
                        + reason
        );
    }
}
