package seedu.duke.ui;

import java.io.PrintStream;
import java.util.Scanner;

/** Text UI of the application. */
public class TextUi {

    /** A decorative prefix added to the beginning of lines printed by BudgetBite */
    private static final String LINE_PREFIX = "|| ";

    private static final String LS = System.lineSeparator();

    private static final String DIVIDER = "===================================================";

    private static final String MESSAGE_GOODBYE = "I hope you will eat well. Bye!";
    private static final String MESSAGE_INIT_FAILED = "BudgetBite could not be launched";
    private static final String MESSAGE_WELCOME = "Welcome to the best NUS campus guide";

    private final Scanner in;
    private final PrintStream out;

    public TextUi() {
        this.in = new Scanner(System.in);
        this.out = System.out;
    }

    public String getUserCommand() {
        out.print(LINE_PREFIX + "Enter command: ");
        String fullInputLine = in.nextLine();

        // silently consume all ignored lines
        while (shouldIgnore(fullInputLine)) {
            fullInputLine = in.nextLine();
        }

        showToUser("[Command entered:" + fullInputLine + "]");
        return fullInputLine;
    }

    private boolean shouldIgnore(String rawInputLine) {
        return rawInputLine.trim().isEmpty();
    }

    /**
     * Generates and prints the welcome message upon the start of the application.
     *
     * @param version current version of the application.
     */
    public void showWelcomeMessage(String version) {

        showToUser(DIVIDER, DIVIDER, MESSAGE_WELCOME, "version: ", version, DIVIDER);
    }

    public void showGoodbyeMessage() {
        showToUser(MESSAGE_GOODBYE, DIVIDER, DIVIDER);
    }

    public void showResponseToUser(String response) {
        showToUser(response, DIVIDER);
    }

    public void showInitFailedMessage() {
        showToUser(MESSAGE_INIT_FAILED, DIVIDER, DIVIDER);
    }

    /** Shows message(s) to the user */
    public void showToUser(String... message) {
        for (String m : message) {
            System.out.println(LINE_PREFIX + m.replace("\n", LS + LINE_PREFIX));
        }
    }
}
