package seedu.duke.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.parser.Parser;
import seedu.duke.ratings.RatingsManager;

/**
 * Tests the ratings commands end to end: user input is parsed by {@link Parser} and the resulting
 * command is executed, as in the main application loop.
 */
public class RatingCommandsTest {
    private RatingsManager ratingsManager;
    private Parser parser;

    @BeforeEach
    public void setUp() {
        ratingsManager = new RatingsManager();
        parser = new Parser(ratingsManager);
    }

    private String run(String userInput) {
        return parser.parseCommand(userInput).execute();
    }

    private void addSampleRatings() {
        run("rate /food Chicken Rice /stall Chicken Rice Stall /score 9 /notes Very delicious");
        run("rate /food Char Siew Rice /stall Chicken Rice Stall /score 7");
        run("rate /food Beef Pho /stall Vietnamese /score 6");
    }

    @Test
    public void rate_allFields_ratingAdded() {
        String response = run("rate /food Chicken Rice /stall Chicken Rice Stall /score 9 /notes Very delicious");

        assertEquals("Got it. I've added this rating:\n"
                + "  Chicken Rice @ Chicken Rice Stall - 9/10 | Notes: Very delicious\n"
                + "You now have 1 rating(s).", response);
        assertEquals(1, ratingsManager.getSize());
    }

    @Test
    public void rate_prefixesInAnyOrderWithoutNotes_ratingAdded() {
        run("rate /score 6 /stall Vietnamese /food Beef Pho");

        assertEquals("Beef Pho @ Vietnamese - 6/10", ratingsManager.getRatings().get(0).toString());
    }

    @Test
    public void rate_scoreOutOfRange_rejected() {
        assertEquals("Score must be between 0 and 10 (inclusive).",
                run("rate /food Laksa /stall Indian /score 11"));
        assertEquals("Score must be between 0 and 10 (inclusive).",
                run("rate /food Laksa /stall Indian /score -1"));
        assertTrue(ratingsManager.isEmpty());
    }

    @Test
    public void rate_nonNumericScore_rejected() {
        assertEquals("Score must be a whole number from 0 to 10, e.g. 8",
                run("rate /food Laksa /stall Indian /score great"));
        assertTrue(ratingsManager.isEmpty());
    }

    @Test
    public void rate_missingOrDuplicateFields_rejected() {
        assertTrue(run("rate /food Laksa /stall Indian").startsWith("Missing /score."));
        assertTrue(run("rate /stall Indian /score 5").startsWith("Missing /food."));
        assertEquals("/food can only be given once.", run("rate /food A /food B /stall Indian /score 5"));
        assertTrue(ratingsManager.isEmpty());
    }

    @Test
    public void listRatings_noRatings_helpfulMessage() {
        assertTrue(run("list-ratings").startsWith("You have no ratings yet."));
    }

    @Test
    public void listRatings_noFilters_allRatingsShown() {
        addSampleRatings();

        assertEquals("Here are all your ratings:\n"
                + "1. Chicken Rice @ Chicken Rice Stall - 9/10 | Notes: Very delicious\n"
                + "2. Char Siew Rice @ Chicken Rice Stall - 7/10\n"
                + "3. Beef Pho @ Vietnamese - 6/10", run("list-ratings"));
    }

    @Test
    public void listRatings_foodFilter_onlyMatchingShownWithOriginalIndex() {
        addSampleRatings();

        assertEquals("Here are your ratings matching food \"Beef Pho\":\n"
                + "3. Beef Pho @ Vietnamese - 6/10", run("list-ratings /food Beef Pho"));
    }

    @Test
    public void listRatings_stallFilter_onlyMatchingShown() {
        addSampleRatings();

        assertEquals("Here are your ratings matching stall \"Chicken Rice Stall\":\n"
                + "1. Chicken Rice @ Chicken Rice Stall - 9/10 | Notes: Very delicious\n"
                + "2. Char Siew Rice @ Chicken Rice Stall - 7/10", run("list-ratings /stall Chicken Rice Stall"));
    }

    @Test
    public void listRatings_foodAndStallFilters_bothMustMatch() {
        addSampleRatings();

        assertEquals("Here are your ratings matching food \"Char Siew Rice\" and stall \"Chicken Rice Stall\":\n"
                + "2. Char Siew Rice @ Chicken Rice Stall - 7/10",
                run("list-ratings /stall Chicken Rice Stall /food Char Siew Rice"));
    }

    @Test
    public void listRatings_noMatch_helpfulMessageAndRatingsKept() {
        addSampleRatings();

        assertEquals("No ratings found matching food \"Beef Pho\" and stall \"Chicken Rice Stall\".",
                run("list-ratings /food Beef Pho /stall Chicken Rice Stall"));
        assertEquals(3, ratingsManager.getSize());
    }

    @Test
    public void listRatings_emptyFilter_rejected() {
        assertTrue(run("list-ratings /food").startsWith("/food needs a value."));
    }

    @Test
    public void editRating_scoreAndNotes_updatedAndOthersUnchanged() {
        addSampleRatings();

        assertEquals("Updated rating 2:\n  Char Siew Rice @ Chicken Rice Stall - 8/10 | Notes: Better than last time",
                run("edit-rating 2 /score 8 /notes Better than last time"));
        assertEquals("Chicken Rice @ Chicken Rice Stall - 9/10 | Notes: Very delicious",
                ratingsManager.getRatings().get(0).toString());
        assertEquals("Beef Pho @ Vietnamese - 6/10", ratingsManager.getRatings().get(2).toString());
    }

    @Test
    public void editRating_invalidEdits_rejectedAndRatingUnchanged() {
        addSampleRatings();

        assertEquals("Score must be between 0 and 10 (inclusive).", run("edit-rating 1 /score 15"));
        assertEquals("Score must be a whole number from 0 to 10, e.g. 8", run("edit-rating 1 /score high"));
        assertTrue(run("edit-rating 1").startsWith("Nothing to edit."));
        assertTrue(run("edit-rating 1 /notes").startsWith("/notes needs a value."));
        assertEquals("Chicken Rice @ Chicken Rice Stall - 9/10 | Notes: Very delicious",
                ratingsManager.getRatings().get(0).toString());
    }

    @Test
    public void deleteRating_validIndex_onlyThatRatingRemoved() {
        addSampleRatings();

        assertEquals("Deleted rating 2:\n"
                + "  Char Siew Rice @ Chicken Rice Stall - 7/10\n"
                + "You now have 2 rating(s).", run("delete-rating 2"));
        assertEquals("Here are all your ratings:\n"
                + "1. Chicken Rice @ Chicken Rice Stall - 9/10 | Notes: Very delicious\n"
                + "2. Beef Pho @ Vietnamese - 6/10", run("list-ratings"));
    }

    @Test
    public void editAndDeleteRating_invalidIndexes_rejectedWithoutChanges() {
        addSampleRatings();

        assertEquals("Rating index must be between 1 and 3.", run("delete-rating 4"));
        assertEquals("Rating index must be between 1 and 3.", run("edit-rating 9 /score 5"));
        assertTrue(run("delete-rating 0").startsWith("Rating index must be a positive whole number"));
        assertTrue(run("delete-rating abc").startsWith("Rating index must be a positive whole number"));
        assertTrue(run("delete-rating").startsWith("Missing rating index."));
        assertEquals(3, ratingsManager.getSize());
    }
}
