package seedu.duke.commands;

/**
 * Represents a command that could not be parsed. Executing it shows the user what was wrong,
 * so invalid input is reported instead of crashing the application.
 */
public class IncorrectCommand extends Command {
    private final String errorMessage;

    public IncorrectCommand(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String execute() {
        return errorMessage;
    }
}
