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
}