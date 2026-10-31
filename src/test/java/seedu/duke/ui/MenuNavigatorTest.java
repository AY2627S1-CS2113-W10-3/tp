package seedu.duke.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.food.FoodDirectory;
import seedu.duke.ratings.Rating;
import seedu.duke.ratings.RatingsManager;

public class MenuNavigatorTest {

    private static final float DELTA = 0.001f;

    private MenuNavigator navigator;
    private RatingsManager ratingsManager;

    @BeforeEach
    public void setUp() {
        ratingsManager = new RatingsManager();
        navigator = new MenuNavigator(new FoodDirectory(), ratingsManager);
    }

    /**
     * Browses to PGPR, then the Chicken Rice stall, whose menu is
     * 1. Char Siew Rice, 2. Roast Chicken Rice, 3. Toufu Vegetable Rice Set.
     */
    private void openChickenRiceMenu() {
        navigator.start();
        navigator.handleInput("1");
        navigator.handleInput("2");
    }

    @Test
    public void start_displaysFoodCourts() {
        String response = navigator.start();

        assertTrue(response.contains("Available locations:"));
        assertTrue(response.contains("1. PGPR"));
        assertTrue(response.contains("2. YIH"));
        assertTrue(navigator.isActive());
    }

    @Test
    public void selectFoodCourt_displaysStalls() {
        navigator.start();

        String response = navigator.handleInput("1");

        assertTrue(response.contains("Stalls in PGPR:"));
        assertTrue(response.contains("1. Vietnamese"));
        assertTrue(response.contains("2. Chicken Rice"));
    }

    @Test
    public void selectStall_displaysFoodMenu() {
        navigator.start();
        navigator.handleInput("1");

        String response = navigator.handleInput("1");

        assertTrue(response.contains("Menu for Vietnamese:"));
        assertTrue(response.contains("Beef Pho"));
        assertTrue(response.contains("Spring Roll"));
        assertTrue(response.contains("$5.50"));
    }

    @Test
    public void backFromMenu_returnsToStallList() {
        navigator.start();
        navigator.handleInput("1");
        navigator.handleInput("1");

        String response = navigator.handleInput("back");

        assertTrue(response.contains("Stalls in PGPR:"));
        assertTrue(response.contains("Vietnamese"));
        assertTrue(navigator.isActive());
    }

    @Test
    public void backFromStallList_returnsToFoodCourtList() {
        navigator.start();
        navigator.handleInput("1");

        String response = navigator.handleInput("back");

        assertTrue(response.contains("Available locations:"));
        assertTrue(response.contains("PGPR"));
        assertTrue(response.contains("YIH"));
    }

    @Test
    public void backAtInitialState_isRejected() {
        String response = navigator.handleInput("back");

        assertTrue(response.contains("Invalid command"));
        assertFalse(navigator.isActive());
    }

    @Test
    public void invalidFoodCourtNumber_returnsError() {
        navigator.start();

        String response = navigator.handleInput("99");

        assertTrue(response.contains("Invalid food court number."));
    }

    @Test
    public void nonNumericSelection_returnsError() {
        navigator.start();

        String response = navigator.handleInput("abc");

        assertTrue(response.contains("valid number"));
    }

    @Test
    public void directNavigationToStall_displaysFoodMenu() {
        String response = navigator.navigateDirectly("PGPR / Chicken Rice");

        assertTrue(response.contains("Menu for Chicken Rice:"));
        assertTrue(response.contains("Char Siew Rice"));
        assertTrue(response.contains("Roast Chicken Rice"));
        assertTrue(response.contains("$3.30"));
        assertTrue(navigator.isActive());
    }

    @Test
    public void directNavigation_isCaseInsensitive() {
        String response = navigator.navigateDirectly("pgpr / chicken rice");

        assertTrue(response.contains("Menu for Chicken Rice:"));
        assertTrue(response.contains("Roast Chicken Rice"));
    }

    @Test
    public void directNavigation_unknownFoodCourt_showsError() {
        String response =
                navigator.navigateDirectly("Unknown Court / Chicken Rice");

        assertTrue(response.contains("Unknown food court: Unknown Court"));
        assertFalse(navigator.isActive());
    }

    @Test
    public void directNavigation_unknownStall_showsError() {
        String response =
                navigator.navigateDirectly("PGPR / Unknown Stall");

        assertTrue(response.contains("Unknown stall in PGPR: Unknown Stall\n"
                + "Please check the stall name and try again."));

        assertFalse(navigator.isActive());
    }

    @Test
    public void directNavigation_withMenuItem_showsFormatError() {
        String response =
                navigator.navigateDirectly("PGPR / Chicken Rice / Roast Chicken Rice");

        assertTrue(response.contains("Format: FOOD_COURT / STALL"));
        assertFalse(navigator.isActive());
    }

    @Test
    public void directNavigation_missingStall_showsFormatError() {
        String response = navigator.navigateDirectly("PGPR /");

        assertTrue(response.contains("Format: FOOD_COURT / STALL"));
        assertFalse(navigator.isActive());
    }

    @Test
    public void homeFromMenu_returnsToMainMenu() {
        navigator.start();
        navigator.handleInput("1");
        navigator.handleInput("2");

        String response = navigator.goHome();

        assertTrue(response.contains("main menu"));
        assertFalse(navigator.isActive());
    }

    @Test
    public void homeClearsCurrentNavigation() {
        navigator.start();
        navigator.handleInput("1");
        navigator.handleInput("2");

        navigator.goHome();

        String response = navigator.handleInput("1");

        assertTrue(response.contains("Please enter 'location' first."));
        assertFalse(navigator.isActive());
    }

    @Test
    public void homeWhenAlreadyAtHome_staysAtHome() {
        String response = navigator.goHome();

        assertTrue(response.contains("main menu"));
        assertFalse(navigator.isActive());
    }

    @Test
    public void directNavigationToExpandedStall_displaysMenu() {
        String response =
                navigator.navigateDirectly("TECHNO EDGE / Bistro Box");

        assertTrue(response.contains("Menu for Bistro Box:"));
        assertTrue(response.contains("Double Chicken Burger - $9.30"));
    }

    @Test
    public void menu_itemsAreNumberedWithHint() {
        String response = navigator.navigateDirectly("PGPR / Chicken Rice");

        assertTrue(response.contains("1. Char Siew Rice - $3.30"));
        assertTrue(response.contains("2. Roast Chicken Rice - $3.30"));
        assertTrue(response.contains("Enter a food number to rate it"));
    }

    @Test
    public void selectItem_asksForScore() {
        openChickenRiceMenu();

        String response = navigator.handleInput("2");

        assertTrue(response.contains("Rate Roast Chicken Rice - $3.30 (Chicken Rice, PGPR)"));
        assertTrue(response.contains("Enter a score from 0 to 10"));
    }

    @Test
    public void selectItem_invalidNumber_returnsError() {
        openChickenRiceMenu();

        String response = navigator.handleInput("99");

        assertTrue(response.contains("Invalid food number."));
    }

    @Test
    public void rateItem_validScore_ratingAddedAndMenuShownAgain() throws Exception {
        openChickenRiceMenu();
        navigator.handleInput("2");

        String response = navigator.handleInput("7.5");

        assertTrue(response.contains("Rated: Roast Chicken Rice (Chicken Rice, PGPR) - 7.5/10"));
        assertTrue(response.contains("Menu for Chicken Rice:"));
        assertEquals(1, ratingsManager.getSize());
        Rating rating = ratingsManager.getRating(1);
        assertEquals("PGPR", rating.getLocation());
        assertEquals(7.5f, rating.getScore(), DELTA);

        // Back on the menu, so a number picks a dish again.
        assertTrue(navigator.handleInput("1").contains("Rate Char Siew Rice"));
    }

    @Test
    public void rateItem_sameDishTwice_ratingUpdated() throws Exception {
        openChickenRiceMenu();
        navigator.handleInput("2");
        navigator.handleInput("6");
        navigator.handleInput("2");

        String response = navigator.handleInput("9");

        assertTrue(response.contains("Updated: Roast Chicken Rice (Chicken Rice, PGPR) - 9.0/10 (was 6.0/10)"));
        assertEquals(1, ratingsManager.getSize());
        assertEquals(9f, ratingsManager.getRating(1).getScore(), DELTA);
    }

    @Test
    public void rateItem_invalidScore_askedAgainAndNothingAdded() {
        openChickenRiceMenu();
        navigator.handleInput("2");

        String response = navigator.handleInput("11");

        assertTrue(response.contains("Score must be between 0 and 10."));
        assertTrue(response.contains("Enter a score from 0 to 10"));
        assertTrue(ratingsManager.isEmpty());

        // Still waiting for a score for the same dish.
        assertTrue(navigator.handleInput("8").contains("Rated: Roast Chicken Rice"));
    }

    @Test
    public void rateItem_back_cancelledAndMenuShown() {
        openChickenRiceMenu();
        navigator.handleInput("2");

        String response = navigator.handleInput("back");

        assertTrue(response.contains("Rating cancelled."));
        assertTrue(response.contains("Menu for Chicken Rice:"));
        assertTrue(ratingsManager.isEmpty());
    }

    @Test
    public void rateItem_afterDirectNavigation_ratingAdded() {
        navigator.navigateDirectly("TECHNO EDGE / Bistro Box");
        navigator.handleInput("1");

        String response = navigator.handleInput("9");

        assertTrue(response.contains("Rated:"));
        assertTrue(response.contains("(Bistro Box, TECHNO EDGE)"));
    }

    @Test
    public void isNavigationInput_dependsOnStage() {
        assertFalse(navigator.isNavigationInput("1"));

        openChickenRiceMenu();
        assertTrue(navigator.isNavigationInput("2"));
        assertTrue(navigator.isNavigationInput("back"));
        assertFalse(navigator.isNavigationInput("7.5"));
        assertFalse(navigator.isNavigationInput("list-ratings"));

        navigator.handleInput("2");
        assertTrue(navigator.isNavigationInput("7.5"));
        assertTrue(navigator.isNavigationInput("-1"));
        assertFalse(navigator.isNavigationInput("list-ratings"));
    }

    @Test
    public void home_whileRating_clearsRating() {
        openChickenRiceMenu();
        navigator.handleInput("2");

        navigator.goHome();

        assertFalse(navigator.isActive());
        assertFalse(navigator.isNavigationInput("8"));
    }
}
