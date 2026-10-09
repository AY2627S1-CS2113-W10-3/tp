package seedu.duke.food;

import java.math.BigDecimal;
import java.util.List;

/**
 * Stores all hardcoded food court, stall, and food item data.
 */
public class FoodDirectory {

    private static final List<FoodCourt> FOOD_COURTS = List.of(
            new FoodCourt(
                    "PGPR",
                    List.of(new FoodStall(
                            "Vietnamese",
                                    List.of(new FoodItem("Beef Pho", new BigDecimal("5.50")),
                                            new FoodItem("Spring Roll", new BigDecimal("2.50"))
                                    )
                            ),
                            new FoodStall(
                                    "Chicken Rice",
                                    List.of(new FoodItem("Char Siew Rice", new BigDecimal("3.30")),
                                            new FoodItem("Roast Chicken Rice", new BigDecimal("3.30")),
                                            new FoodItem("Toufu Vegetable Rice Set", new BigDecimal("2.80"))
                                    )
                            )
                    )
            ),

            new FoodCourt(
                    "YIH",
                    List.of(
                            new FoodStall(
                                    "Tempu Ramen",
                                    List.of(new FoodItem("Bonito Beef Donburi", new BigDecimal("8.90")),
                                            new FoodItem("Black Garlic Ramen", new BigDecimal("7.90")),
                                            new FoodItem("Fire Chicken Ramen", new BigDecimal("8.90"))
                                    )
                            ),
                            new FoodStall(
                                    "Indian",
                                    List.of(new FoodItem("Plain Dosai", new BigDecimal("2.90")),
                                            new FoodItem("Bhel Puri", new BigDecimal("3.00"))
                                    )
                            )
                    )
            )
    );

    /**
     * Returns all food courts.
     *
     * @return list of food courts
     */
    public List<FoodCourt> getLocations() {
        return FOOD_COURTS;
    }
}