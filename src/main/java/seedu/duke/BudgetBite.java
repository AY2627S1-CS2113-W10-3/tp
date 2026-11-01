package seedu.duke;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.duke.budget.BudgetManager;
import seedu.duke.commands.ByeCommand;
import seedu.duke.commands.Command;
import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.expenses.ExpensesManager;
import seedu.duke.parser.Parser;
import seedu.duke.ratings.RatingsManager;
import seedu.duke.storage.StorageFile;
import seedu.duke.ui.MenuNavigator;
import seedu.duke.ui.TextUi;
import seedu.duke.food.FoodDirectory;

public class BudgetBite {

    private static final String VERSION = "2.0";
    private static final Logger LOGGER = Logger.getLogger(BudgetBite.class.getName());
    private static final String MESSAGE_UNEXPECTED_ERROR =
            "Sorry, something went wrong while running that command. Your data is safe; please try again.";
    private TextUi ui;
    private StorageFile storage;
    private RatingsManager ratings;
    private ExpensesManager expenses;
    private BudgetManager budget;
    private FoodDirectory foodDirectory;

    private MenuNavigator menuNavigator;

    public static void main(String... launchArgs) {
        hideLogsFromConsole();
        new BudgetBite().run(launchArgs);
    }

    /**
     * Stops log messages below {@code SEVERE} from being printed to the console, where they would
     * look like errors to the user. Problems the user needs to know about are shown through
     * {@code TextUi} instead.
     */
    private static void hideLogsFromConsole() {
        for (Handler handler : Logger.getLogger("").getHandlers()) {
            handler.setLevel(Level.SEVERE);
        }
    }


    /** Runs the program until user enters bye */
    public void run(String[] launchArgs) {
        start(launchArgs);
        runCommandLoopUntilExitCommand();
        exit();
    }

    /**
     * Sets up the required objects, loads up the data from the storage file, and
     * prints the welcome
     * message.
     *
     * @param launchArgs arguments supplied by the user at program launch
     */
    private void start(String[] launchArgs) {
        try {
            this.ui = new TextUi();
            this.storage = initializeStorage(launchArgs);
            this.ratings = storage.loadRatings();
            this.expenses = storage.loadExpenses();
            this.budget = storage.loadBudget();

            this.foodDirectory = new FoodDirectory();
            this.menuNavigator = new MenuNavigator(foodDirectory, ratings);

            ui.showWelcomeMessage(VERSION);
            for (String warning : storage.getLoadWarnings()) {
                ui.showToUser(warning);
            }

        } catch (BudgetBiteException e) {
            ui.showInitFailedMessage();
            throw new RuntimeException(e);
        }
    }

    /**
     * Prints the Goodbye message and exits.
     * Data does not need saving here because it is already saved after every command.
     */
    private void exit() {
        ui.showGoodbyeMessage();
        System.exit(0);
    }

    /**
     * Reads the user command and executes it, until the user issues the exit
     * command.
     */
    private void runCommandLoopUntilExitCommand() {

        Parser parser = new Parser(ratings, expenses, budget, foodDirectory);
        boolean isExiting = false;

        while (!isExiting) {
            String trimmedInput = ui.getUserCommand().trim();
            String response;

            if (isNavigationInput(trimmedInput)) {
                response = navigate(trimmedInput);
            } else {
                // Normal commands such as help and bye are handled here.
                Command command = parser.parseCommand(trimmedInput);
                response = executeCommand(command);
                isExiting = command instanceof ByeCommand;
            }

            // Saved after navigation too, since a rating can be added while browsing the menus.
            ui.showResponseToUser(saveData(response));
        }
    }

    /**
     * Returns true if the input should be handled by the menu navigator rather than the parser.
     */
    private boolean isNavigationInput(String input) {
        return input.equalsIgnoreCase("home")
                || input.equalsIgnoreCase("location")
                || menuNavigator.isDirectNavigation(input)
                || menuNavigator.isNavigationInput(input);
    }

    /**
     * Passes navigation input to the menu navigator and returns its response.
     */
    private String navigate(String input) {
        if (input.equalsIgnoreCase("home")) {
            // home always returns to the main menu.
            return menuNavigator.goHome();
        } else if (input.equalsIgnoreCase("location")) {
            // location always starts or restarts food navigation.
            return menuNavigator.start();
        } else if (menuNavigator.isDirectNavigation(input)) {
            // A direct path jumps straight to a stall menu.
            return menuNavigator.navigateDirectly(input);
        }
        return menuNavigator.handleInput(input);
    }


    /**
     * Executes the command and returns the response.
     * Any unexpected error is caught here so that one bad command cannot crash the app
     * and lose the user's data.
     *
     * @param command user command
     * @return response of the command
     */
    private String executeCommand(Command command) {
        try {
            return command.execute();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unexpected error while running " + command.getClass().getSimpleName(), e);
            return MESSAGE_UNEXPECTED_ERROR;
        }
    }

    /**
     * Saves ratings, expenses and the budget so that no data is lost if the app is closed suddenly.
     * Called after every input, as the User Guide promises.
     *
     * @param response Response of the command that was just run.
     * @return The same response, with a warning added if saving failed.
     */
    private String saveData(String response) {
        try {
            storage.save(ratings, expenses, budget);
            return response;
        } catch (BudgetBiteException e) {
            return response + "\nWarning: " + e.getMessage();
        }
    }

    /**
     * Creates the StorageFile object based on the user specified path (if any) or
     * the default storage
     * path.
     */
    private StorageFile initializeStorage(String[] launchArgs) throws BudgetBiteException {
        boolean isStorageFileSpecifiedByUser = launchArgs.length > 1;
        return isStorageFileSpecifiedByUser
                ? new StorageFile(launchArgs[0], launchArgs[1])
                : new StorageFile();
    }
}
