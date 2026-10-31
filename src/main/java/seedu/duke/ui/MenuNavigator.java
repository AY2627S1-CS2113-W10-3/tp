package seedu.duke.ui;

import seedu.duke.commands.RatingArguments;
import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.food.FoodCourt;
import seedu.duke.food.FoodDirectory;
import seedu.duke.food.FoodItem;
import seedu.duke.food.FoodStall;
import seedu.duke.ratings.Rating;
import seedu.duke.ratings.RatingsManager;

/**
 * Controls the user's location, stall, and menu selection flow.
 * After choosing a dish from a stall's menu, the user is asked for a score,
 * and the rating is added to the {@code RatingsManager}.
 */
public class MenuNavigator {

    private static final String MESSAGE_SCORE_PROMPT = "Enter a score from 0 to 10, or 'back' to cancel:";
    /**
     * Input that looks like a score, e.g. 8, 7.5, -1 or 7.25. Invalid ones such as -1 are still
     * sent to the navigator, so the user gets a score error instead of "Unknown command".
     */
    private static final String SCORE_LIKE_REGEX = "-?[\\d.]+";

    private enum Stage {
        INACTIVE,
        SELECTING_LOCATION,
        SELECTING_STALL,
        SHOWING_MENU,
        RATING_ITEM
    }

    private final FoodDirectory foodDirectory;
    private final RatingsManager ratingsManager;
    private Stage stage = Stage.INACTIVE;
    private FoodCourt selectedFoodCourt;
    private FoodStall selectedStall;
    private FoodItem selectedItem;

    /**
     * Creates a navigator over the given food catalogue.
     *
     * @param foodDirectory  food courts, stalls and menus to browse
     * @param ratingsManager where ratings made while browsing are added
     */
    public MenuNavigator(FoodDirectory foodDirectory, RatingsManager ratingsManager) {
        assert foodDirectory != null : "Food directory should not be null";
        assert ratingsManager != null : "Ratings manager should not be null";
        this.foodDirectory = foodDirectory;
        this.ratingsManager = ratingsManager;
    }

    public boolean isActive() {
        return stage != Stage.INACTIVE;
    }

    /**
     * Returns true if the input is a step in the current navigation, so it should be handled by
     * {@link #handleInput(String)} instead of being parsed as a command. That is {@code back},
     * or a number: a list choice while browsing, or a score while rating a dish.
     * Other input, e.g. {@code list-ratings}, still runs as a normal command while browsing.
     *
     * @param input trimmed user input
     * @return whether the navigator should handle the input
     */
    public boolean isNavigationInput(String input) {
        if (!isActive()) {
            return false;
        }
        if (input.equalsIgnoreCase("back")) {
            return true;
        }
        if (stage == Stage.RATING_ITEM) {
            return input.matches(SCORE_LIKE_REGEX);
        }
        return input.matches("\\d+");
    }

    /**
     * Starts or restarts the food court selection process.
     *
     * @return list of available food courts
     */
    public String start() {
        stage = Stage.SELECTING_LOCATION;
        selectedFoodCourt = null;
        selectedStall = null;
        selectedItem = null;

        return showFoodCourts();
    }

    /**
     * Handles navigation input after the user enters location.
     *
     * @param input user input
     * @return response for the user
     */
    public String handleInput(String input) {
        assert input != null : "Navigation input should not be null";
        String trimmedInput = input.trim();

        if (trimmedInput.equalsIgnoreCase("back")) {
            return goBack();
        }

        // While rating, the input is a score rather than a list number.
        if (stage == Stage.RATING_ITEM) {
            return rateSelectedItem(trimmedInput);
        }

        Integer index = parseIndex(trimmedInput);

        if (index == null) {
            return "Please enter a valid number or enter 'back'.";
        }

        return switch (stage) {
        case SELECTING_LOCATION -> selectFoodCourt(index);
        case SELECTING_STALL -> selectStall(index);
        case SHOWING_MENU -> selectItem(index);
        case INACTIVE -> "Please enter 'location' first.";
        case RATING_ITEM -> throw new AssertionError("Rating input is handled above");
        };
    }

    private Integer parseIndex(String input) {
        if (!input.matches("\\d+")) {
            return null;
        }

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private String selectFoodCourt(int index) {
        assert stage == Stage.SELECTING_LOCATION
                : "Food court can only be selected from the location stage";

        if (index < 1 || index > foodDirectory.getLocations().size()) {
            return "Invalid food court number.";
        }

        selectedFoodCourt = foodDirectory.getLocations().get(index - 1);
        stage = Stage.SELECTING_STALL;

        assert selectedFoodCourt != null : "Selected food court should not be null";

        return showStalls();
    }

    private String selectStall(int index) {
        assert selectedFoodCourt != null : "A food court must be selected first";

        if (index < 1 || index > selectedFoodCourt.stalls().size()) {
            return "Invalid stall number.";
        }

        selectedStall = selectedFoodCourt.stalls().get(index - 1);
        stage = Stage.SHOWING_MENU;

        assert selectedStall != null : "Selected stall should not be null";

        return showMenu();
    }

    /**
     * Picks a dish from the menu shown and asks the user to score it.
     */
    private String selectItem(int index) {
        assert selectedStall != null : "A stall must be selected first";

        if (index < 1 || index > selectedStall.menu().size()) {
            return "Invalid food number.";
        }

        selectedItem = selectedStall.menu().get(index - 1);
        stage = Stage.RATING_ITEM;

        return "Rate " + selectedItem.name() + " - $" + selectedItem.price()
                + " (" + selectedStall.name() + ", " + selectedFoodCourt.name() + ")\n"
                + MESSAGE_SCORE_PROMPT;
    }

    /**
     * Adds a rating for the selected dish, then shows the stall's menu again so that
     * the user can rate another dish. If the score is invalid, the user is asked again.
     */
    private String rateSelectedItem(String scoreText) {
        assert selectedItem != null : "A food item must be selected before rating it";

        try {
            float score = RatingArguments.parseScore(scoreText);
            Rating rating = new Rating(selectedFoodCourt.name(), selectedStall.name(), selectedItem.name(), score);
            ratingsManager.addRating(rating);

            selectedItem = null;
            stage = Stage.SHOWING_MENU;
            return "Rated: " + rating + "\n\n" + showMenu();
        } catch (BudgetBiteException e) {
            return e.getMessage() + "\n" + MESSAGE_SCORE_PROMPT;
        }
    }

    private String goBack() {
        return switch (stage) {
        case RATING_ITEM -> {
            selectedItem = null;
            stage = Stage.SHOWING_MENU;
            yield "Rating cancelled.\n\n" + showMenu();
        }
        case SHOWING_MENU -> {
            selectedStall = null;
            stage = Stage.SELECTING_STALL;
            yield showStalls();
        }
        case SELECTING_STALL -> {
            selectedFoodCourt = null;
            stage = Stage.SELECTING_LOCATION;
            yield showFoodCourts();
        }
        case SELECTING_LOCATION -> {
            selectedFoodCourt = null;
            selectedStall = null;
            stage = Stage.INACTIVE;
            yield "Returned to the main menu.";
        }
        case INACTIVE -> "Invalid command. Valid inputs:\n"
                + "help\n"
                + "location\n"
                + "bye";
        };
    }

    private String showFoodCourts() {
        StringBuilder output = new StringBuilder("Available locations:");

        for (int index = 0;
             index < foodDirectory.getLocations().size();
             index++) {
            output.append("\n")
                    .append(index + 1)
                    .append(". ")
                    .append(foodDirectory.getLocations().get(index).name());
        }

        return output.toString();
    }

    private String showStalls() {
        assert selectedFoodCourt != null : "A food court must be selected before showing stalls";
        StringBuilder output =
                new StringBuilder("Stalls in " + selectedFoodCourt.name() + ":");

        for (int index = 0;
             index < selectedFoodCourt.stalls().size();
             index++) {
            output.append("\n")
                    .append(index + 1)
                    .append(". ")
                    .append(selectedFoodCourt.stalls().get(index).name());
        }

        return output.toString();
    }

    private String showMenu() {
        assert selectedStall != null : "A stall must be selected before showing its menu";
        StringBuilder output =
                new StringBuilder("Menu for " + selectedStall.name() + ":");

        for (int index = 0;
             index < selectedStall.menu().size();
             index++) {
            FoodItem item = selectedStall.menu().get(index);
            output.append("\n")
                    .append(index + 1)
                    .append(". ")
                    .append(item.name())
                    .append(" - $")
                    .append(item.price());
        }

        output.append("\nEnter a food number to rate it, or 'back' to see the stalls.");
        return output.toString();
    }

    /**
     * Checks whether the input looks like a direct food-court path.
     *
     * <p>The first path component must be a known food court. This prevents
     * commands such as "rate /stall ..." from being mistaken for navigation.</p>
     *
     * @param input user input
     * @return true if the input starts with a known food court and contains a slash
     */
    public boolean isDirectNavigation(String input) {
        String[] path = splitPath(input);
        return path.length >= 2 && foodDirectory.findFoodCourt(path[0]) != null;
    }

    /**
     * Navigates directly to a stall menu or a specific menu item.
     *
     * @param input path in the form FOOD_COURT / STALL
     * @return the menu or item information, or an error message
     */
    public String navigateDirectly(String input) {

        String[] path = splitPath(input);

        if (path.length != 2 || containsBlankPart(path)) {
            return "Invalid Format\nValid Format: FOOD_COURT / STALL" ;
        }

        FoodCourt foodCourt = foodDirectory.findFoodCourt(path[0]);
        if (foodCourt == null) {
            return "Unknown food court: " + path[0];
        }

        FoodStall stall = foodCourt.findStall(path[1]);
        if (stall == null) {
            return "Unknown stall in " + foodCourt.name() + ": " + path[1]
                    + "\nPlease check the stall name and try again.";
        }

        selectedFoodCourt = foodCourt;
        selectedStall = stall;
        selectedItem = null;
        stage = Stage.SHOWING_MENU;

        return showMenu();
    }

    /**
     * Splits a direct path around slash separators.
     */
    private String[] splitPath(String input) {
        if (input == null || input.isBlank()) {
            return new String[0];
        }

        return input.trim().split("\\s*/\\s*", -1);
    }

    /**
     * Checks whether any part of the path is empty.
     */
    private boolean containsBlankPart(String[] path) {
        for (String part : path) {
            if (part.isBlank()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the user to the main menu from any navigation stage.
     *
     * @return confirmation message
     */
    public String goHome() {
        stage = Stage.INACTIVE;
        selectedFoodCourt = null;
        selectedStall = null;
        selectedItem = null;

        return "Returned to the main menu.";
    }
}
