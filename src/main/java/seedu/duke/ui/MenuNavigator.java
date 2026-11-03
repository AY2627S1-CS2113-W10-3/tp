package seedu.duke.ui;

import java.util.List;
import java.util.function.Function;

import seedu.duke.commands.RateCommand;
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
 * and the rating is added to the {@code RatingsManager}, replacing any earlier rating of that dish.
 */
public class MenuNavigator {

    private static final String MESSAGE_SCORE_PROMPT = "Enter a score from 0 to 10, or 'back' to cancel:";
    private static final String MESSAGE_CONTINUE = "\nEnter a number to continue, or 'back'.";
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

        return showRatingPrompt();
    }

    private String showRatingPrompt() {
        assert selectedItem != null : "A food item must be selected before asking for its score";
        return "Rate " + selectedItem.name() + " - $" + selectedItem.price()
                + " (" + selectedStall.name() + ", " + selectedFoodCourt.name() + ")\n"
                + MESSAGE_SCORE_PROMPT;
    }

    /**
     * Opens the food court list, so the user can continue by number. Used by {@code rate ?}.
     *
     * @param filter text the names must contain, ignoring case, or an empty string for all
     * @return the numbered food courts
     */
    public String openLocations(String filter) {
        stage = Stage.SELECTING_LOCATION;
        selectedFoodCourt = null;
        selectedStall = null;
        selectedItem = null;
        return showFoodCourts(filter) + MESSAGE_CONTINUE;
    }

    /**
     * Opens the stall list of a food court, e.g. for {@code rate /location PGPR /stall ?}.
     *
     * @param foodCourt food court whose stalls are listed
     * @param filter    text the names must contain, ignoring case, or an empty string for all
     * @return the numbered stalls
     */
    public String openStalls(FoodCourt foodCourt, String filter) {
        assert foodCourt != null : "Food court should not be null";
        stage = Stage.SELECTING_STALL;
        selectedFoodCourt = foodCourt;
        selectedStall = null;
        selectedItem = null;
        return showStalls(filter) + MESSAGE_CONTINUE;
    }

    /**
     * Opens a stall's menu, e.g. for {@code rate /location PGPR /stall Chicken Rice /food ?}.
     *
     * @param foodCourt food court the stall is in
     * @param stall     stall whose menu is listed
     * @param filter    text the dish names must contain, ignoring case, or an empty string for all
     * @return the numbered menu
     */
    public String openMenu(FoodCourt foodCourt, FoodStall stall, String filter) {
        assert foodCourt != null && stall != null : "Food court and stall should not be null";
        stage = Stage.SHOWING_MENU;
        selectedFoodCourt = foodCourt;
        selectedStall = stall;
        selectedItem = null;
        return showMenu(filter);
    }

    /**
     * Asks for the score of a dish, e.g. for {@code rate /location PGPR /stall ... /food ... /score ?}.
     *
     * @return the prompt for the score
     */
    public String openRatingPrompt(FoodCourt foodCourt, FoodStall stall, FoodItem item) {
        assert foodCourt != null && stall != null && item != null : "Dish to rate should not be null";
        stage = Stage.RATING_ITEM;
        selectedFoodCourt = foodCourt;
        selectedStall = stall;
        selectedItem = item;
        return showRatingPrompt();
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
            Rating replacedRating = ratingsManager.addOrReplaceRating(rating);

            selectedItem = null;
            stage = Stage.SHOWING_MENU;
            return RateCommand.describeRating(rating, replacedRating) + "\n\n" + showMenu();
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
        return showFoodCourts("");
    }

    private String showFoodCourts(String filter) {
        return numberedList("Available locations:", foodDirectory.getLocations(),
                FoodCourt::name, FoodCourt::name, filter);
    }

    private String showStalls() {
        return showStalls("");
    }

    private String showStalls(String filter) {
        assert selectedFoodCourt != null : "A food court must be selected before showing stalls";
        return numberedList("Stalls in " + selectedFoodCourt.name() + ":", selectedFoodCourt.stalls(),
                FoodStall::name, FoodStall::name, filter);
    }

    private String showMenu() {
        return showMenu("");
    }

    private String showMenu(String filter) {
        assert selectedStall != null : "A stall must be selected before showing its menu";
        return numberedList("Menu for " + selectedStall.name() + ":", selectedStall.menu(),
                FoodItem::name, item -> item.name() + " - $" + item.price(), filter)
                + "\nEnter a food number to rate it, or 'back' to see the stalls.";
    }

    /**
     * Lists the entries numbered from 1. If a filter is given, only entries whose names contain it,
     * ignoring case, are listed, but they keep their original numbers so they can still be chosen.
     * If nothing matches, every entry is listed after a "No match" line.
     *
     * @param title    first line, e.g. {@code "Stalls in PGPR:"}
     * @param entries  food courts, stalls or dishes
     * @param getName  gets the name the filter is matched against
     * @param getLabel gets the text shown for an entry, e.g. a dish with its price
     * @param filter   text typed by the user, or an empty string for all
     */
    private static <T> String numberedList(String title, List<T> entries, Function<T, String> getName,
                                           Function<T, String> getLabel, String filter) {
        String lowerCaseFilter = filter.trim().toLowerCase();
        boolean hasMatch = entries.stream()
                .anyMatch(entry -> getName.apply(entry).toLowerCase().contains(lowerCaseFilter));

        StringBuilder output = new StringBuilder();
        if (!hasMatch) {
            output.append("No match for '").append(filter.trim()).append("'.\n");
        }
        output.append(title);
        for (int index = 0; index < entries.size(); index++) {
            T entry = entries.get(index);
            boolean isShown = !hasMatch || getName.apply(entry).toLowerCase().contains(lowerCaseFilter);
            if (isShown) {
                output.append("\n").append(index + 1).append(". ").append(getLabel.apply(entry));
            }
        }
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
