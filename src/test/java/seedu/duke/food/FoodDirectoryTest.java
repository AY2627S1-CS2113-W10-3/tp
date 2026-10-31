package seedu.duke.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests the food directory loaded from the food catalogue.
 */
public class FoodDirectoryTest {

    @Test
    public void getLocations_containsExpandedFoodCatalogue() {
        FoodDirectory directory = new FoodDirectory();

        List<String> actualNames = directory.getLocations()
                .stream()
                .map(FoodCourt::name)
                .toList();

        assertTrue(actualNames.containsAll(List.of(
                "PGPR",
                "YIH",
                "TECHNO EDGE",
                "THE TERRACE",
                "THE DECK",
                "FRONTIER",
                "FLAVOURS",
                "FINE FOOD"
        )));

        assertTrue(directory.getLocations()
                .stream()
                .allMatch(foodCourt -> !foodCourt.stalls().isEmpty()));

        assertTrue(directory.getLocations()
                .stream()
                .flatMap(foodCourt -> foodCourt.stalls().stream())
                .allMatch(foodStall -> !foodStall.menu().isEmpty()));
    }

    @Test
    public void getLocations_pgprContainsExpectedStalls() {
        FoodDirectory directory = new FoodDirectory();

        FoodCourt pgpr = findFoodCourt(directory, "PGPR");

        List<String> stallNames = pgpr.stalls()
                .stream()
                .map(FoodStall::name)
                .toList();

        assertTrue(stallNames.containsAll(List.of(
                "Vietnamese",
                "Chicken Rice",
                "PGP Western",
                "Chef Wok"
        )));
    }

    @Test
    public void getLocations_loadsOriginalMenuItemCorrectly() {
        FoodDirectory directory = new FoodDirectory();

        FoodCourt pgpr = findFoodCourt(directory, "PGPR");
        FoodStall vietnamese = findStall(pgpr, "Vietnamese");

        List<String> foodNames = vietnamese.menu()
                .stream()
                .map(FoodItem::name)
                .toList();

        assertEquals(
                List.of("Beef Pho", "Spring Roll"),
                foodNames
        );

        assertEquals(
                new BigDecimal("5.50"),
                vietnamese.menu().get(0).price()
        );
    }

    @Test
    public void getLocations_loadsExpandedMenuItemCorrectly() {
        FoodDirectory directory = new FoodDirectory();

        FoodCourt technoEdge = findFoodCourt(directory, "TECHNO EDGE");
        FoodStall bistroBox = findStall(technoEdge, "Bistro Box");

        FoodItem doubleChickenBurger = bistroBox.menu()
                .stream()
                .filter(item -> item.name().equals("Double Chicken Burger"))
                .findFirst()
                .orElseThrow();

        assertEquals(
                new BigDecimal("9.30"),
                doubleChickenBurger.price()
        );
    }

    private FoodCourt findFoodCourt(
            FoodDirectory directory,
            String foodCourtName) {
        return directory.getLocations()
                .stream()
                .filter(foodCourt -> foodCourt.name().equals(foodCourtName))
                .findFirst()
                .orElseThrow();
    }

    private FoodStall findStall(
            FoodCourt foodCourt,
            String stallName) {
        return foodCourt.stalls()
                .stream()
                .filter(stall -> stall.name().equals(stallName))
                .findFirst()
                .orElseThrow();
    }
}
