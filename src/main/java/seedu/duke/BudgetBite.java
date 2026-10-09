package seedu.duke;

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

    private static final String VERSION = "1.1";
    private TextUi ui;
    private StorageFile storage;
    private RatingsManager ratings;
    private ExpensesManager expenses;

    private MenuNavigator menuNavigator;

    public static void main(String... launchArgs) {
        new BudgetBite().run(launchArgs);
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

            FoodDirectory foodDirectory = new FoodDirectory();
            this.menuNavigator = new MenuNavigator(foodDirectory);

            ui.showWelcomeMessage(VERSION);

        } catch (BudgetBiteException e) {
            ui.showInitFailedMessage();
            throw new RuntimeException(e);
        }
    }

    /** Prints the Goodbye message and exits. */
    private void exit() {

        // TODO: Improve error handling
        try {
            storage.save(ratings, expenses);
        } catch (BudgetBiteException e) {
            System.out.println(e);
        }
        ui.showGoodbyeMessage();
        System.exit(0);
    }

    /**
     * Reads the user command and executes it, until the user issues the exit
     * command.
     */
    private void runCommandLoopUntilExitCommand() {

        Parser parser = new Parser();
        boolean isExiting = false;

        while (!isExiting) {
            String userInput = ui.getUserCommand();
            String trimmedInput = userInput.trim();
            String response;

            /*
             * The location command always starts or restarts food navigation.
             */
            if (trimmedInput.equalsIgnoreCase("location")) {
                response = menuNavigator.start();
            }

            /*
             * While the navigator is active, numbers are interpreted as
             * location or stall selections.
             *
             * Help and bye are still handled by the normal parser.
             */
            else if (menuNavigator.isActive()
                    && !trimmedInput.equalsIgnoreCase("help")
                    && !trimmedInput.equalsIgnoreCase("bye")) {
                response = menuNavigator.handleInput(trimmedInput);
            }

            /*
             * Normal commands such as help and bye are handled here.
             */
            else {
                Command command = parser.parseCommand(trimmedInput);
                response = executeCommand(command);
                isExiting = command instanceof ByeCommand;
            }

            ui.showResponseToUser(response);
        }
    }


    /**
     * Executes the command and returns the response.
     *
     * @param command user command
     * @return response of the command
     */
    private String executeCommand(Command command) {
        try {
            String response = command.execute();
            return response;
        } catch (Exception e) {
            ui.showToUser(e.getMessage());
            throw new RuntimeException(e);
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
