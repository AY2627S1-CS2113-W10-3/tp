package seedu.duke.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seedu.duke.expenses.ExpensesManager;
import seedu.duke.ratings.Rating;
import seedu.duke.ratings.RatingsManager;

public class StorageFileTest {
    @TempDir
    Path tempDir;

    @Test
    public void load_validFormat() throws Exception {
        RatingsManager actualRatingsManager = getStorage().loadRatings();
        RatingsManager expectedRatingsManager = getExpectedRatingsManager();

        assertEquals(expectedRatingsManager.getRatings(), actualRatingsManager.getRatings());
    }

    @Test
    public void saveThenLoad_ratingsWithAndWithoutNotes_sameRatingsRestored() throws Exception {
        StorageFile storage = new StorageFile(
                tempDir.resolve("expenses.txt").toString(), tempDir.resolve("ratings.txt").toString());
        RatingsManager savedRatingsManager = getExpectedRatingsManager();

        storage.save(savedRatingsManager, new ExpensesManager());

        assertEquals(savedRatingsManager.getRatings(), storage.loadRatings().getRatings());
    }

    private RatingsManager getExpectedRatingsManager() throws Exception {
        ArrayList<Rating> ratings = new ArrayList<>();
        ratings.add(new Rating("Chicken Rice", "Chicken Rice Stall", 9, "Very delicious"));
        ratings.add(new Rating("Beef Pho", "Vietnamese", 6));
        return new RatingsManager(ratings);
    }

    private StorageFile getStorage() throws Exception {
        return new StorageFile(
                "src/test/java/seedu/duke/data/expenses.txt", "src/test/java/seedu/duke/data/ratings.txt");
    }
}
