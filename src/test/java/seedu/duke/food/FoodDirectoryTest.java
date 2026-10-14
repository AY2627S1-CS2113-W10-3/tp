package seedu.duke.food;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

public class FoodDirectoryTest {

    @Test
    public void getLocations_returnsAllFoodCourts() {
        FoodDirectory directory = new FoodDirectory();

        List<String> actualNames = directory.getLocations()
                .stream()
                .map(FoodCourt::name)
                .toList();

        assertEquals(List.of("PGPR", "YIH"), actualNames);
    }

    @Test
    public void getLocations_firstFoodCourtContainsExpectedStalls() {
        FoodDirectory directory = new FoodDirectory();

        FoodCourt pgpr = directory.getLocations().get(0);

        List<String> actualStallNames = pgpr.stalls()
                .stream()
                .map(FoodStall::name)
                .toList();

        assertEquals(List.of("Vietnamese", "Chicken Rice"), actualStallNames);
    }

    @Test
    public void getLocations_firstStallContainsExpectedFood() {
        FoodDirectory directory = new FoodDirectory();

        FoodStall vietnameseStall =
                directory.getLocations().get(0).stalls().get(0);

        List<String> actualFoodNames = vietnameseStall.menu()
                .stream()
                .map(FoodItem::name)
                .toList();

        assertEquals(
                List.of("Beef Pho", "Spring Roll"),
                actualFoodNames
        );
    }

    @Test
    public void getLocations_foodItemHasCorrectPrice() {
        FoodDirectory directory = new FoodDirectory();

        FoodItem beefPho = directory.getLocations()
                .get(0)
                .stalls()
                .get(0)
                .menu()
                .get(0);

        assertEquals("Beef Pho", beefPho.name());
        assertEquals(new BigDecimal("5.50"), beefPho.price());
    }
}
