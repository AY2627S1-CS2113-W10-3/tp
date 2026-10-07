package seedu.duke.ui;

/**
 * Text UI of the application.
 */
public class TextUi {

    /**
     * A decorative prefix added to the beginning of lines printed by AddressBook
     */
    private static final String LINE_PREFIX = "|| ";

    /** A platform independent line separator. */
    private static final String LS = System.lineSeparator();

    private static final String DIVIDER = "===================================================";

    private static final String MESSAGE_GOODBYE = "I hope you will eat well. Bye!";
    private static final String MESSAGE_INIT_FAILED = "BudgetBite could not be launched";
    private static final String MESSAGE_WELCOME = "Welcome to the best NUS campus guide";
    private static final String MESSAGE_USING_STORAGE_FILE = "Data loaded from: ";
    // private static final String MESSAGE_INIT_FAILED = "BudgetBite could not be
    // launched";

    public TextUi() {

    }

    /**
     * Generates and prints the welcome message upon the start of the application.
     * 
     * @param version         current version of the application.
     * @param storageFilePath path to the storage file being used.
     */
    public void showWelcomeMessage(String version, String storageFilePath) {
        String storageFileInfo = String.format(MESSAGE_USING_STORAGE_FILE, storageFilePath);
        showToUser(
                DIVIDER,
                DIVIDER,
                MESSAGE_WELCOME,
                version,
                storageFileInfo,
                DIVIDER);
    }

    public void showGoodbyeMessage() {
        showToUser(MESSAGE_GOODBYE, DIVIDER, DIVIDER);
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
