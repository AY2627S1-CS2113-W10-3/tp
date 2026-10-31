package seedu.duke.ratings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.exceptions.BudgetBiteException;

public class RatingsManagerTest {
    private static final float DELTA = 0.001f;

    private RatingsManager ratingsManager;

    @BeforeEach
    public void setUp() throws BudgetBiteException {
        ratingsManager = new RatingsManager();
        ratingsManager.addRating(new Rating("PGPR", "Vietnamese", "Beef Pho", 8));
        ratingsManager.addRating(new Rating("YIH", "Indian", "Plain Dosai", 6.5f));
    }

    @Test
    public void constructor_twoManagers_listsAreIndependent() {
        RatingsManager otherManager = new RatingsManager();

        assertTrue(otherManager.isEmpty());
        assertEquals(2, ratingsManager.getSize());
    }

    @Test
    public void editRating_validIndex_onlyScoreUpdated() throws BudgetBiteException {
        ratingsManager.editRating(1, 9.5f);

        Rating editedRating = ratingsManager.getRating(1);
        assertEquals(9.5f, editedRating.getScore(), DELTA);
        assertEquals("Vietnamese", editedRating.getStall());
        assertEquals("Beef Pho", editedRating.getFood());
    }

    @Test
    public void editRating_scoreOutOfRange_exceptionThrownAndScoreUnchanged() throws BudgetBiteException {
        assertThrows(BudgetBiteException.class, () -> ratingsManager.editRating(1, 11));
        assertEquals(8, ratingsManager.getRating(1).getScore(), DELTA);
    }

    @Test
    public void deleteRating_validIndex_ratingRemoved() throws BudgetBiteException {
        Rating deletedRating = ratingsManager.deleteRating(1);

        assertEquals("Beef Pho", deletedRating.getFood());
        assertEquals(1, ratingsManager.getSize());
        assertEquals("Plain Dosai", ratingsManager.getRating(1).getFood());
    }

    @Test
    public void deleteRating_indexOutOfRange_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> ratingsManager.deleteRating(0));
        assertThrows(BudgetBiteException.class, () -> ratingsManager.deleteRating(3));
    }

    @Test
    public void getRating_emptyList_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> new RatingsManager().getRating(1));
    }

    @Test
    public void addOrReplaceRating_newDish_addedAtEnd() throws BudgetBiteException {
        Rating replaced = ratingsManager.addOrReplaceRating(new Rating("PGPR", "Vietnamese", "Spring Roll", 7));

        assertNull(replaced);
        assertEquals(3, ratingsManager.getSize());
        assertEquals("Spring Roll", ratingsManager.getRating(3).getFood());
    }

    @Test
    public void addOrReplaceRating_sameDish_replacedInPlace() throws BudgetBiteException {
        Rating replaced = ratingsManager.addOrReplaceRating(new Rating("PGPR", "Vietnamese", "Beef Pho", 3));

        assertEquals(8, replaced.getScore(), DELTA);
        assertEquals(2, ratingsManager.getSize());
        assertEquals("Beef Pho", ratingsManager.getRating(1).getFood());
        assertEquals(3, ratingsManager.getRating(1).getScore(), DELTA);
    }

    @Test
    public void addOrReplaceRating_sameDishDifferentCase_replaced() throws BudgetBiteException {
        Rating replaced = ratingsManager.addOrReplaceRating(new Rating("pgpr", "VIETNAMESE", "beef pho", 3));

        assertEquals(8, replaced.getScore(), DELTA);
        assertEquals(2, ratingsManager.getSize());
    }

    @Test
    public void addOrReplaceRating_sameStallAndFoodInOtherLocation_added() throws BudgetBiteException {
        Rating replaced = ratingsManager.addOrReplaceRating(new Rating("YIH", "Vietnamese", "Beef Pho", 3));

        assertNull(replaced);
        assertEquals(3, ratingsManager.getSize());
    }

    @Test
    public void toString_twoRatings_numberedList() {
        String expected = "\n1. Beef Pho (Vietnamese, PGPR) - 8.0/10"
                + "\n2. Plain Dosai (Indian, YIH) - 6.5/10";
        assertEquals(expected, ratingsManager.toString());
    }
}
