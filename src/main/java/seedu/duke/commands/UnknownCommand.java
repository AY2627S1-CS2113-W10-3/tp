package seedu.duke.commands;

public class UnknownCommand extends Command {
    @Override
    public String execute() {
        return "Invalid command. Valid inputs:\n"
                + "help\n"
                + "location\n"
                + "rate\n"
                + "bye";
    }
}
