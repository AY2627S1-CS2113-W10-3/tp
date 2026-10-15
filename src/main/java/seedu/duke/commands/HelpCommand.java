package seedu.duke.commands;

public class HelpCommand extends Command {

    @Override
    public String execute() {
        return "Use these commands:\n" +
                "help\n" +
                "location\n" +
                "rate\n" +
                "log-expense FOOD_NAME /" +
                "cost AMOUNT\n" +
                "list-expenses\n" +
                "bye";
    }
}
