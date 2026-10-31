package seedu.duke.commands;

/**
 * Shows every command with its format.
 */
public class HelpCommand extends Command {
    public static final String COMMAND_WORD = "help";

    @Override
    public String execute() {
        return "Use these commands:\n"
                + "help\n"
                + "home\n"
                + "location\n"
                + "back (while not in home menu)\n"
                + "FOOD_COURT / STALL\n"
                + "rate /location LOCATION /stall STALL /food FOOD /score SCORE\n"
                + "  (end with ? or press Tab, then Enter, to see the options, e.g. rate /location ?)\n"
                + "list-ratings\n"
                + "edit-rating INDEX /score SCORE\n"
                + "delete-rating INDEX\n"
                + "log-expense FOOD_NAME /cost AMOUNT [/date YYYY-MM-DD]\n"
                + "list-expenses\n"
                + "edit-expense INDEX /cost AMOUNT\n"
                + "delete-expense INDEX\n"
                + "set-budget PERIOD AMOUNT (PERIOD is daily, weekly or monthly)\n"
                + "budget\n"
                + "bye";
    }
}
