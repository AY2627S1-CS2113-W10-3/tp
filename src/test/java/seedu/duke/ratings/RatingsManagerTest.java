package seedu.duke.ratings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.exceptions.BudgetBiteException;

public class RatingsManagerTest {
    private RatingsManager ratingsManager;
    private Rating chickenRice;
    private Rating charSiewRice;
    private Rating beefPho;

    @BeforeEach
    public void setUp() throws BudgetBiteException {
        chickenRice = new Rating("Chicken Rice", "Chicken Rice Stall", 9, "Very delicious");
        charSiewRice = new Rating("Char Siew Rice", "Chicken Rice Stall", 7);
        beefPho = new Rating("Beef Pho", "Vietnamese", 6, "Broth a bit bland");

        ratingsManager = new RatingsManager();
        ratingsManager.addRating(chickenRice);
        ratingsManager.addRating(charSiewRice);
        ratingsManager.addRating(beefPho);
    }

    @Test
    public void addRating_validRating_addedToEnd() throws BudgetBiteException {
        Rating laksa = new Rating("Laksa", "Indian", 5);

        ratingsManager.addRating(laksa);

        assertEquals(4, ratingsManager.getSize());
        assertEquals(laksa, ratingsManager.getRating(4));
    }

    @Test
    public void findRatingIndexes_noFilters_allIndexes() {
        assertEquals(List.of(1, 2, 3), ratingsManager.findRatingIndexes(null, null));
    }

    @Test
    public void findRatingIndexes_foodFilter_matchingIndexes() {
        assertEquals(List.of(1), ratingsManager.findRatingIndexes("Chicken Rice", null));
    }

    @Test
    public void findRatingIndexes_stallFilter_matchingIndexes() {
        assertEquals(List.of(1, 2), ratingsManager.findRatingIndexes(null, "chicken rice stall"));
    }

    @Test
    public void findRatingIndexes_foodAndStallFilters_bothMustMatch() {
        assertEquals(List.of(2), ratingsManager.findRatingIndexes("Char Siew Rice", "Chicken Rice Stall"));
        assertTrue(ratingsManager.findRatingIndexes("Beef Pho", "Chicken Rice Stall").isEmpty());
    }

    @Test
    public void findRatingIndexes_noMatch_emptyAndListUnchanged() {
        assertTrue(ratingsManager.findRatingIndexes("Sushi", null).isEmpty());
        assertEquals(3, ratingsManager.getSize());
    }

    @Test
    public void editRating_scoreOnly_notesPreserved() throws BudgetBiteException {
        Rating updated = ratingsManager.editRating(1, 8, null);

        assertEquals(8, updated.getScore());
        assertEquals("Very delicious", updated.getNotes());
        assertEquals("Chicken Rice", updated.getFoodName());
        assertEquals(updated, ratingsManager.getRating(1));
    }

    @Test
    public void editRating_notesOnly_scorePreserved() throws BudgetBiteException {
        Rating updated = ratingsManager.editRating(1, null, "Better than last time");

        assertEquals(9, updated.getScore());
        assertEquals("Better than last time", updated.getNotes());
    }

    @Test
    public void editRating_invalidScore_ratingUnchanged() {
        assertThrows(BudgetBiteException.class, () -> ratingsManager.editRating(1, 11, "New notes"));
        assertThrows(BudgetBiteException.class, () -> ratingsManager.editRating(1, -1, null));

        assertEquals(List.of(chickenRice, charSiewRice, beefPho), ratingsManager.getRatings());
    }

    @Test
    public void editRating_oneRating_otherRatingsUnchanged() throws BudgetBiteException {
        ratingsManager.editRating(2, 3, "Too dry");

        assertEquals(chickenRice, ratingsManager.getRating(1));
        assertEquals(beefPho, ratingsManager.getRating(3));
    }

    @Test
    public void editRating_indexOutOfRange_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> ratingsManager.editRating(0, 5, null));
        assertThrows(BudgetBiteException.class, () -> ratingsManager.editRating(4, 5, null));
    }

    @Test
    public void deleteRating_validIndex_onlyThatRatingRemoved() throws BudgetBiteException {
        Rating deleted = ratingsManager.deleteRating(2);

        assertEquals(charSiewRice, deleted);
        assertEquals(List.of(chickenRice, beefPho), ratingsManager.getRatings());
    }

    @Test
    public void deleteRating_indexOutOfRange_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> ratingsManager.deleteRating(0));
        assertThrows(BudgetBiteException.class, () -> ratingsManager.deleteRating(4));
        assertEquals(3, ratingsManager.getSize());
    }

    @Test
    public void deleteRating_emptyList_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> new RatingsManager().deleteRating(1));
    }
}
