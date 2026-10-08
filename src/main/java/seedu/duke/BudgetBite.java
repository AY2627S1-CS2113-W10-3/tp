package seedu.duke;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import seedu.duke.commands.ByeCommand;
import seedu.duke.commands.Command;
import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.expenses.ExpensesManager;
import seedu.duke.parser.Parser;
import seedu.duke.ratings.RatingsManager;
import seedu.duke.storage.StorageFile;
import seedu.duke.ui.TextUi;

public class BudgetBite {

    private final String VERSION = "1.0";
    private TextUi ui;
    private StorageFile storage;
    private RatingsManager ratings;
    private ExpensesManager expenses;

    public static void main(String... launchArgs) {
        new BudgetBite().run(launchArgs);
    }

    /** Runs the program until termination. */
    public void run(String[] launchArgs) {
        start(launchArgs);
        runCommandLoopUntilExitCommand();
        exit();
    }

    /**
     * Sets up the required objects, loads up the data from the storage file, and
     * prints the welcome message.
     *
     * @param launchArgs arguments supplied by the user at program launch
     *
     */
    private void start(String[] launchArgs) {
        try {
            this.ui = new TextUi();
            this.storage = initializeStorage(launchArgs);
            this.ratings = storage.loadRatings();
            this.expenses = storage.loadExpenses();
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
        Command command;
        do {
            String userCommandText = ui.getUserCommand();
            command = new Parser().parseCommand(userCommandText);
            String response = executeCommand(command);

            // Delete
            System.out.println(ratings.toString());
            ui.showResponseToUser(response);

        } while (command instanceof ByeCommand);
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
     * the default storage path.
     */
    private StorageFile initializeStorage(String[] launchArgs) throws BudgetBiteException {
        boolean isStorageFileSpecifiedByUser = launchArgs.length > 1;
        return isStorageFileSpecifiedByUser ? new StorageFile(launchArgs[0], launchArgs[1]) : new StorageFile();
    }
}
