package seedu.duke.commands;

public class HelpCommand extends Command {

    @Override
    public String execute() {
        return "Use these commands:\n" +
                "help\n" +
                "location\n" +
                "rate /stall STALL /food FOOD /score SCORE\n" +
                "list-ratings\n" +
                "edit-rating INDEX /score SCORE\n" +
                "delete-rating INDEX\n" +
                "log-expense FOOD_NAME /" +
                "cost AMOUNT\n" +
                "list-expenses\n" +
                 "edit-expense\n" +
                "delete-expense\n" +
                "bye";
    }
}
