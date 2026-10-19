package seedu.duke.ratings;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.commands.DeleteRatingCommand;
import seedu.duke.commands.EditRatingsCommand;
import seedu.duke.commands.ListRatingsCommand;
import seedu.duke.commands.RatingArguments;
import seedu.duke.exceptions.BudgetBiteException;

public class RatingCommandsTest {
    private static final float DELTA = 0.001f;

    private RatingsManager ratingsManager;

    @BeforeEach
    public void setUp() {
        ratingsManager = new RatingsManager();
        ratingsManager.addRating(new Rating("Vietnamese", "Beef Pho", 8));
        ratingsManager.addRating(new Rating("Indian", "Plain Dosai", 6.5f));
    }

    @Test
    public void listRatings_noRatings_emptyMessageShown() {
        String response = new ListRatingsCommand(new RatingsManager()).execute();

        assertTrue(response.contains("No ratings yet."));
    }

    @Test
    public void listRatings_twoRatings_numberedListShown() {
        String response = new ListRatingsCommand(ratingsManager).execute();

        assertTrue(response.contains("1. Stall: Vietnamese, Food: Beef Pho, Score: 8.0"));
        assertTrue(response.contains("2. Stall: Indian, Food: Plain Dosai, Score: 6.5"));
    }

    @Test
    public void editRating_validInput_scoreChanged() throws Exception {
        String response = new EditRatingsCommand("1 /score 4.5", ratingsManager).execute();

        assertEquals(4.5f, ratingsManager.getRating(1).getScore(), DELTA);
        assertTrue(response.contains("Updated rating 1: Stall: Vietnamese, Food: Beef Pho, Score: 4.5"));
    }

    @Test
    public void editRating_missingScore_errorShown() {
        String response = new EditRatingsCommand("1", ratingsManager).execute();

        assertTrue(response.contains("Missing /score."));
        assertTrue(response.contains(EditRatingsCommand.MESSAGE_USAGE));
    }

    @Test
    public void editRating_scoreOutOfRange_errorShownAndScoreUnchanged() throws Exception {
        String response = new EditRatingsCommand("1 /score 11", ratingsManager).execute();

        assertTrue(response.contains("Score must be between 0 and 10."));
        assertEquals(8, ratingsManager.getRating(1).getScore(), DELTA);
    }

    @Test
    public void editRating_indexOutOfRange_errorShown() {
        String response = new EditRatingsCommand("3 /score 5", ratingsManager).execute();

        assertTrue(response.contains("Rating index must be between 1 and 2."));
    }

    @Test
    public void deleteRating_validIndex_ratingRemoved() throws Exception {
        String response = new DeleteRatingCommand("1", ratingsManager).execute();

        assertEquals(1, ratingsManager.getSize());
        assertEquals("Plain Dosai", ratingsManager.getRating(1).getFood());
        assertTrue(response.contains("Deleted: Stall: Vietnamese, Food: Beef Pho, Score: 8.0"));
    }

    @Test
    public void deleteRating_notANumber_errorShownAndNothingRemoved() {
        String response = new DeleteRatingCommand("first", ratingsManager).execute();

        assertEquals(2, ratingsManager.getSize());
        assertTrue(response.contains("Index must be a positive whole number"));
    }

    @Test
    public void deleteRating_emptyList_errorShown() {
        String response = new DeleteRatingCommand("1", new RatingsManager()).execute();

        assertTrue(response.contains("You have no ratings yet."));
    }

    @Test
    public void splitAtScore_validArguments_twoParts() throws BudgetBiteException {
        assertArrayEquals(new String[] {"2", "4.5"}, RatingArguments.splitAtScore("2 /score 4.5"));
    }

    @Test
    public void parseScore_notANumber_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> RatingArguments.parseScore("great"));
    }
}
