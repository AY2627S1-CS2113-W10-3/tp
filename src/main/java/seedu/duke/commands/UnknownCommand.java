package seedu.duke.commands;

public class UnknownCommand extends Command {
    @Override
    public String execute() {
        return "Invalid command. Valid inputs:\n"
                + "help\n"
                + "home\n"
                + "location\n"
                + "rate\n"
                + "list-ratings\n"
                + "edit-rating\n"
                + "delete-rating\n"
                + "log-expense\n"
                + "list-expenses\n"
                + "bye";
    }
}
