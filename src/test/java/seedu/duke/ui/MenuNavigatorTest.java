package seedu.duke.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.food.FoodDirectory;

public class MenuNavigatorTest {

    private MenuNavigator navigator;

    @BeforeEach
    public void setUp() {
        navigator = new MenuNavigator(new FoodDirectory());
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
        String response = navigator.navigateDirectly("Unknown Court / Chicken Rice");

        assertTrue(response.contains("Unknown food court"));
        assertFalse(navigator.isActive());
    }

    @Test
    public void directNavigation_unknownStall_showsError() {
        String response = navigator.navigateDirectly("PGPR / Unknown Stall");

        assertTrue(response.contains("Unknown stall"));
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
}
