package seedu.duke.food;
import java.util.List;

public record FoodStall(String name, List<FoodItem> menu) {
    public FoodStall {
        assert name != null && !name.isBlank() : "Stall name should not be blank";
        assert menu != null : "Stall menu should not be null";
    }
}
