package seedu.duke.ui;

import java.io.PrintStream;
import java.util.Scanner;

import seedu.duke.commands.RateCommand;

/**
 * Text UI of the application.
 */
public class TextUi {

    /**
     * A decorative prefix added to the beginning of lines printed by BudgetBite
     */
    private static final String LINE_PREFIX = "|| ";

    private static final String LS = System.lineSeparator();

    private static final String DIVIDER = "===================================================";

    private static final String MESSAGE_GOODBYE = "I hope you will eat well. Bye!";
    private static final String MESSAGE_INIT_FAILED = "BudgetBite could not be launched";
    private static final String MESSAGE_WELCOME = "Welcome to the best NUS campus guide";

    /** A line that is a rate command, e.g. "rate /location PGPR", in any letter case. */
    private static final String RATE_COMMAND_REGEX = "(?i)\\s*" + RateCommand.COMMAND_WORD + "(\\s.*)?";
    /** Tabs, possibly followed by spaces, at the end of a line. */
    private static final String TRAILING_TAB_REGEX = "\t+\\s*$";
    /**
     * What a Tab at the end of a rate command is turned into. The terminal only sends the line after Enter,
     * so a Tab cannot complete a name on the spot; instead, Tab then Enter asks for suggestions,
     * the same as ending a {@code rate} command with {@code ?}. Other commands are left alone,
     * so a stray Tab after e.g. a cost is still ignored as before.
     */
    private static final String TAB_REPLACEMENT = " ?";

    private final Scanner in;
    private final PrintStream out;

    public TextUi() {
        this.in = new Scanner(System.in);
        this.out = System.out;
    }

    public String getUserCommand() {
        out.print(LINE_PREFIX + "Enter command: ");
        String fullInputLine = readLineOrBye();

        // silently consume all ignored lines
        while (shouldIgnore(fullInputLine)) {
            fullInputLine = readLineOrBye();
        }

        if (fullInputLine.matches(RATE_COMMAND_REGEX)) {
            return fullInputLine.replaceAll(TRAILING_TAB_REGEX, TAB_REPLACEMENT);
        }
        return fullInputLine;
    }

    /**
     * Reads the next line typed by the user.
     * If the input has ended (e.g. the user pressed Ctrl+D or Ctrl+Z), returns {@code "bye"}
     * so the app exits normally instead of crashing.
     */
    private String readLineOrBye() {
        if (!in.hasNextLine()) {
            return "bye";
        }
        return in.nextLine();
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

        showToUser(DIVIDER, DIVIDER, MESSAGE_WELCOME, "Version " + version, DIVIDER);
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

    /**
     * Shows message(s) to the user
     */
    public void showToUser(String... message) {
        for (String m : message) {
            System.out.println(LINE_PREFIX + m.replace("\n", LS + LINE_PREFIX));
        }
    }
}
