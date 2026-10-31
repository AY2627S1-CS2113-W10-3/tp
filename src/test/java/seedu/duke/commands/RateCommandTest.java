package seedu.duke.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.food.FoodDirectory;
import seedu.duke.ratings.Rating;
import seedu.duke.ratings.RatingsManager;

/**
 * Tests the home-page {@code rate} command, which only accepts dishes from the food catalogue.
 * Uses the bundled food.txt, e.g. PGPR / Chicken Rice / Roast Chicken Rice.
 */
public class RateCommandTest {
    private static final float DELTA = 0.001f;

    private RatingsManager ratingsManager;
    private FoodDirectory foodDirectory;

    @BeforeEach
    public void setUp() {
        ratingsManager = new RatingsManager();
        foodDirectory = new FoodDirectory();
    }

    private String rate(String arguments) {
        return new RateCommand(arguments, ratingsManager, foodDirectory).execute();
    }

    @Test
    public void execute_validDish_ratingAdded() throws Exception {
        String response = rate("/location PGPR /stall Chicken Rice /food Roast Chicken Rice /score 8");

        assertEquals("Rated: Roast Chicken Rice (Chicken Rice, PGPR) - 8.0/10", response);
        assertEquals(1, ratingsManager.getSize());
        assertEquals(8f, ratingsManager.getRating(1).getScore(), DELTA);
    }

    @Test
    public void execute_differentCaseAndOrder_savedWithCatalogueNames() throws Exception {
        rate("/score 7.5 /food roast chicken rice /location pgpr /stall CHICKEN RICE");

        Rating rating = ratingsManager.getRating(1);
        assertEquals("PGPR", rating.getLocation());
        assertEquals("Chicken Rice", rating.getStall());
        assertEquals("Roast Chicken Rice", rating.getFood());
    }

    @Test
    public void execute_unknownLocation_rejectedWithAvailableLocations() {
        String response = rate("/location Utown /stall Chicken Rice /food Roast Chicken Rice /score 8");

        assertTrue(response.contains("Unknown location: Utown"));
        assertTrue(response.contains("Available locations: PGPR"));
        assertTrue(ratingsManager.isEmpty());
    }

    @Test
    public void execute_stallFromAnotherLocation_rejectedWithStallList() {
        String response = rate("/location YIH /stall Vietnamese /food Beef Pho /score 8");

        assertTrue(response.contains("Unknown stall in YIH: Vietnamese"));
        assertTrue(response.contains("Stalls in YIH:"));
        assertTrue(ratingsManager.isEmpty());
    }

    @Test
    public void execute_unknownFood_rejectedWithMenu() {
        String response = rate("/location PGPR /stall Vietnamese /food Pizza /score 8");

        assertTrue(response.contains("Unknown food at Vietnamese: Pizza"));
        assertTrue(response.contains("Menu of Vietnamese: Beef Pho, Spring Roll"));
        assertTrue(ratingsManager.isEmpty());
    }

    @Test
    public void execute_missingLocation_errorShown() {
        String response = rate("/stall Vietnamese /food Beef Pho /score 8");

        assertTrue(response.contains("Missing /location."));
        assertTrue(response.contains(RateCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_badScores_errorShownAndNothingAdded() {
        for (String score : new String[] {"NaN", "5f", "7.25", "11", "good"}) {
            String response = rate("/location PGPR /stall Vietnamese /food Beef Pho /score " + score);

            assertTrue(response.contains("Score must be"), "Score " + score + " should be rejected");
        }
        assertTrue(ratingsManager.isEmpty());
    }

    @Test
    public void execute_repeatedField_errorShown() {
        String response = rate("/location PGPR /location YIH /stall Vietnamese /food Beef Pho /score 8");

        assertTrue(response.contains("/location can only be given once."));
    }

    @Test
    public void execute_unknownField_errorShown() {
        String response = rate("/location PGPR /stall Vietnamese /food Beef Pho /price 8");

        assertTrue(response.contains("Unknown field: /price"));
    }

    @Test
    public void execute_emptyFieldValue_errorShown() {
        String response = rate("/location /stall Vietnamese /food Beef Pho /score 8");

        assertTrue(response.contains("Missing value after /location."));
    }

    @Test
    public void execute_textBeforeFirstField_errorShown() {
        String response = rate("PGPR /stall Vietnamese /food Beef Pho /score 8");

        assertTrue(response.contains("Unexpected text before the first field"));
    }

    @Test
    public void execute_noArguments_errorShown() {
        String response = rate("");

        assertTrue(response.contains("Please provide /location, /stall, /food and /score."));
    }

}
