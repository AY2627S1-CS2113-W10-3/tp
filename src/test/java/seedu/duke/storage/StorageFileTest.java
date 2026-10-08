package seedu.duke.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import seedu.duke.ratings.RatingsManager;

public class StorageFileTest {
    @Test
    public void load_validFormat() throws Exception {
        RatingsManager actualRatingsManager = getStorage().loadRatings();
        RatingsManager expectedRatingsMangaer = getExpectedRatingsManager();

        // ensure loaded AddressBook is properly constructed with test data
        // TODO: overwrite equals method in AddressBook class and replace with equals
        // method below
        assertEquals(actualRatingsManager.getRatings(), expectedRatingsMangaer.getRatings());
    }

    private RatingsManager getExpectedRatingsManager() {
        ArrayList<String> ratings = new ArrayList<String>();
        ratings.add("fine foods|korean|3|6.5");
        return new RatingsManager(ratings);

    }

    private StorageFile getStorage() throws Exception {
        return new StorageFile("src/test/java/seedu/duke/data/expenses.txt",
                "src/test/java/seedu/duke/data/expenses.txt");
    }
}
