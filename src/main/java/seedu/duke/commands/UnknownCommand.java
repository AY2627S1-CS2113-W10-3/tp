package seedu.duke.commands;

/**
 * Shown when the user types a command word that does not exist.
 * Points to {@code help} instead of repeating the command list, so the list only needs updating in one place.
 */
public class UnknownCommand extends Command {
    public static final String MESSAGE_UNKNOWN = "Unknown command. Type 'help' to see all commands and their formats.";

    @Override
    public String execute() {
        return MESSAGE_UNKNOWN;
    }
}
