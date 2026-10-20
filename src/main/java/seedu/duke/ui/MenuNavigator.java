package seedu.duke.ui;

import seedu.duke.food.FoodCourt;
import seedu.duke.food.FoodDirectory;
import seedu.duke.food.FoodItem;
import seedu.duke.food.FoodStall;

/**
 * Controls the user's location, stall, and menu selection flow.
 */
public class MenuNavigator {

    private enum Stage {
        INACTIVE,
        SELECTING_LOCATION,
        SELECTING_STALL,
        SHOWING_MENU
    }

    private final FoodDirectory foodDirectory;
    private Stage stage = Stage.INACTIVE;
    private FoodCourt selectedFoodCourt;
    private FoodStall selectedStall;

    public MenuNavigator(FoodDirectory foodDirectory) {
        this.foodDirectory = foodDirectory;
    }

    public boolean isActive() {
        return stage != Stage.INACTIVE;
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

        return showFoodCourts();
    }

    /**
     * Handles navigation input after the user enters location.
     *
     * @param input user input
     * @return response for the user
     */
    public String handleInput(String input) {
        String trimmedInput = input.trim();

        if (trimmedInput.equalsIgnoreCase("back")) {
            return goBack();
        }

        Integer index = parseIndex(trimmedInput);

        if (index == null) {
            return "Please enter a valid number or enter 'back'.";
        }

        return switch (stage) {
        case SELECTING_LOCATION -> selectFoodCourt(index);
        case SELECTING_STALL -> selectStall(index);
        case SHOWING_MENU -> "Enter 'back' to return to the stall list.";
        case INACTIVE -> "Please enter 'location' first.";
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
        if (index < 1 || index > foodDirectory.getLocations().size()) {
            return "Invalid food court number.";
        }

        selectedFoodCourt = foodDirectory.getLocations().get(index - 1);
        stage = Stage.SELECTING_STALL;

        return showStalls();
    }

    private String selectStall(int index) {
        if (index < 1 || index > selectedFoodCourt.stalls().size()) {
            return "Invalid stall number.";
        }

        selectedStall = selectedFoodCourt.stalls().get(index - 1);
        stage = Stage.SHOWING_MENU;

        return showMenu();
    }

    private String goBack() {
        return switch (stage) {
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
        StringBuilder output =
                new StringBuilder("Menu for " + selectedStall.name() + ":");

        for (FoodItem item : selectedStall.menu()) {
            output.append("\n")
                    .append(item.name())
                    .append(" - $")
                    .append(item.price());
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
        return path.length >= 2 && findFoodCourt(path[0]) != null;
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

        FoodCourt foodCourt = findFoodCourt(path[0]);
        if (foodCourt == null) {
            return "Unknown food court: " + path[0];
        }

        FoodStall stall = findStall(foodCourt, path[1]);
        if (stall == null) {
            return "Unknown stall in " + foodCourt.name() + ": " + path[1];
        }

        selectedFoodCourt = foodCourt;
        selectedStall = stall;
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
     * Finds a food court by name, ignoring letter case.
     */
    private FoodCourt findFoodCourt(String name) {
        for (FoodCourt foodCourt : foodDirectory.getLocations()) {
            if (foodCourt.name().equalsIgnoreCase(name.trim())) {
                return foodCourt;
            }
        }

        return null;
    }

    /**
     * Finds a stall inside a food court by name, ignoring letter case.
     */
    private FoodStall findStall(FoodCourt foodCourt, String name) {
        for (FoodStall stall : foodCourt.stalls()) {
            if (stall.name().equalsIgnoreCase(name.trim())) {
                return stall;
            }
        }

        return null;
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

        return "Returned to the main menu.";
    }
}
