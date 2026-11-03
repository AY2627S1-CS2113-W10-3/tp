package seedu.duke.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.food.FoodDirectory;
import seedu.duke.ratings.Rating;
import seedu.duke.ratings.RatingsManager;
import seedu.duke.ui.MenuNavigator;

/**
 * Tests the home-page {@code rate} command, which only accepts dishes from the food catalogue.
 * Uses the bundled food.txt, e.g. PGPR / Chicken Rice / Roast Chicken Rice.
 */
public class RateCommandTest {
    private static final float DELTA = 0.001f;

    private RatingsManager ratingsManager;
    private FoodDirectory foodDirectory;
    private MenuNavigator navigator;

    @BeforeEach
    public void setUp() {
        ratingsManager = new RatingsManager();
        foodDirectory = new FoodDirectory();
        navigator = new MenuNavigator(foodDirectory, ratingsManager);
    }

    private String rate(String arguments) {
        return new RateCommand(arguments, ratingsManager, foodDirectory, navigator).execute();
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
    public void execute_sameDishAgain_ratingUpdatedNotDuplicated() throws Exception {
        rate("/location PGPR /stall Chicken Rice /food Roast Chicken Rice /score 8");

        String response = rate("/location pgpr /stall chicken rice /food roast chicken rice /score 9.5");

        assertEquals("Updated: Roast Chicken Rice (Chicken Rice, PGPR) - 9.5/10 (was 8.0/10)", response);
        assertEquals(1, ratingsManager.getSize());
        assertEquals(9.5f, ratingsManager.getRating(1).getScore(), DELTA);
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

    // Suggestions: ending with ? opens the numbered list for the field being typed, and rates nothing yet.

    @Test
    public void suggest_nothingTyped_numberedLocationsOpened() {
        String response = rate("?");

        assertTrue(response.contains("Available locations:\n1. PGPR\n2. YIH"));
        assertTrue(response.contains("Enter a number to continue"));
        assertTrue(navigator.isActive());
        assertTrue(ratingsManager.isEmpty());
    }

    @Test
    public void suggest_emptyLocation_allLocationsListed() {
        assertTrue(rate("/location ?").contains("1. PGPR\n2. YIH"));
        // Also without a space before ?
        assertTrue(rate("/location?").contains("1. PGPR\n2. YIH"));
    }

    @Test
    public void suggest_partOfStallName_matchesKeepTheirNumbers() {
        String response = rate("/location pgpr /stall ch ?");

        assertEquals("Stalls in PGPR:\n2. Chicken Rice\n4. Chef Wok\nEnter a number to continue, or 'back'.",
                response);
    }

    @Test
    public void suggest_stall_continueByNumberUntilRated() throws Exception {
        rate("/location PGPR /stall ?");

        assertTrue(navigator.handleInput("2").contains("Menu for Chicken Rice:"));
        assertTrue(navigator.handleInput("2").contains("Rate Roast Chicken Rice"));
        assertTrue(navigator.handleInput("8").contains("Rated: Roast Chicken Rice (Chicken Rice, PGPR) - 8.0/10"));
        assertEquals(1, ratingsManager.getSize());
    }

    @Test
    public void suggest_food_menuOpened() {
        String response = rate("/location PGPR /stall Chicken Rice /food ?");

        assertTrue(response.contains("Menu for Chicken Rice:\n1. Char Siew Rice - $3.30\n2. Roast Chicken Rice"));
        assertTrue(navigator.isNavigationInput("2"));
    }

    @Test
    public void suggest_foodWordInMiddleOfName_found() {
        String response = rate("/location PGPR /stall Chicken Rice /food roast ?");

        assertTrue(response.contains("2. Roast Chicken Rice - $3.30"));
        assertFalse(response.contains("Char Siew Rice"));
    }

    @Test
    public void suggest_noMatch_everythingListed() {
        String response = rate("/location PGPR /stall pizza ?");

        assertTrue(response.contains("No match for 'pizza'."));
        assertTrue(response.contains("1. Vietnamese\n2. Chicken Rice\n3. PGP Western\n4. Chef Wok"));
    }

    @Test
    public void suggest_stallBeforeLocation_askedForLocationFirst() {
        String response = rate("/stall ?");

        assertTrue(response.contains("Give /location first"));
        assertFalse(navigator.isActive());
    }

    @Test
    public void suggest_stallWithUnknownLocation_locationsListed() {
        String response = rate("/location Utown /stall ?");

        assertTrue(response.contains("Unknown location: Utown"));
        assertTrue(response.contains("Available locations: PGPR"));
    }

    @Test
    public void suggest_foodWithoutStall_askedForStallFirst() {
        String response = rate("/location PGPR /food ?");

        assertTrue(response.contains("Give /stall first, e.g. rate /location PGPR /stall ?"));
    }

    @Test
    public void suggest_partOfFieldName_matchingFieldsListed() {
        assertEquals("Fields matching '/st': /stall", rate("/location PGPR /st?"));
    }

    @Test
    public void suggest_scoreForKnownDish_promptOpenedAndRated() {
        String response = rate("/location PGPR /stall Vietnamese /food Beef Pho /score ?");

        assertTrue(response.contains("Rate Beef Pho - $5.50 (Vietnamese, PGPR)"));
        assertTrue(ratingsManager.isEmpty());
        assertTrue(navigator.handleInput("9").contains("Rated: Beef Pho (Vietnamese, PGPR) - 9.0/10"));
    }
}
