package seedu.duke.ratings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.duke.exceptions.BudgetBiteException;

public class RatingTest {

    @Test
    public void constructor_validFieldsWithNotes_fieldsStored() throws BudgetBiteException {
        Rating rating = new Rating(" Chicken Rice ", "Chicken Rice Stall", 9, "Very delicious");

        assertEquals("Chicken Rice", rating.getFoodName());
        assertEquals("Chicken Rice Stall", rating.getStallName());
        assertEquals(9, rating.getScore());
        assertEquals("Very delicious", rating.getNotes());
        assertTrue(rating.hasNotes());
    }

    @Test
    public void constructor_noNotes_notesEmpty() throws BudgetBiteException {
        Rating rating = new Rating("Laksa", "Indian", 6);

        assertFalse(rating.hasNotes());
        assertEquals("Laksa @ Indian - 6/10", rating.toString());
    }

    @Test
    public void constructor_boundaryScores_accepted() throws BudgetBiteException {
        assertEquals(0, new Rating("Laksa", "Indian", 0).getScore());
        assertEquals(10, new Rating("Laksa", "Indian", 10).getScore());
    }

    @Test
    public void constructor_scoreOutOfRange_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> new Rating("Laksa", "Indian", -1));
        assertThrows(BudgetBiteException.class, () -> new Rating("Laksa", "Indian", 11));
    }

    @Test
    public void constructor_blankNames_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> new Rating(" ", "Indian", 5));
        assertThrows(BudgetBiteException.class, () -> new Rating("Laksa", "", 5));
    }

    @Test
    public void constructor_separatorInField_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> new Rating("Laksa|Mee", "Indian", 5));
        assertThrows(BudgetBiteException.class, () -> new Rating("Laksa", "Indian", 5, "a|b"));
    }

    @Test
    public void parseScore_notAWholeNumber_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> Rating.parseScore("nine"));
        assertThrows(BudgetBiteException.class, () -> Rating.parseScore("7.5"));
        assertThrows(BudgetBiteException.class, () -> Rating.parseScore(""));
    }

    @Test
    public void matches_filtersIgnoreCase_correctResult() throws BudgetBiteException {
        Rating rating = new Rating("Chicken Rice", "Chicken Rice Stall", 9);

        assertTrue(rating.matches(null, null));
        assertTrue(rating.matches("chicken rice", null));
        assertTrue(rating.matches(null, "CHICKEN RICE STALL"));
        assertFalse(rating.matches("Chicken", null));
        assertFalse(rating.matches("Chicken Rice", "Indian"));
    }

    @Test
    public void fromStorageString_savedRating_sameRatingRestored() throws BudgetBiteException {
        Rating withNotes = new Rating("Chicken Rice", "Chicken Rice Stall", 9, "Very delicious");
        Rating withoutNotes = new Rating("Laksa", "Indian", 6);

        assertEquals(withNotes, Rating.fromStorageString(withNotes.toStorageString()));
        assertEquals(withoutNotes, Rating.fromStorageString(withoutNotes.toStorageString()));
    }

    @Test
    public void fromStorageString_wrongFieldCount_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> Rating.fromStorageString("Laksa|Indian|6"));
    }
}
