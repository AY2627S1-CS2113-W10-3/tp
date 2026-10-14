package seedu.duke.parser;


import seedu.duke.commands.ByeCommand;
import seedu.duke.commands.Command;
import seedu.duke.commands.HelpCommand;
import seedu.duke.commands.UnknownCommand;
import seedu.duke.commands.RateCommand;
import seedu.duke.ratings.RatingsManager;

public class Parser {
    private final RatingsManager ratingsManager;

    public Parser(RatingsManager ratingsManager) {
        this.ratingsManager = ratingsManager;
    }

    public Command parseCommand(String userCommandText) {

        String trimmedInput = userCommandText.trim();
        String[] parts = trimmedInput.split("\\s+", 2);
        String commandName = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1] : "";
        

        switch (commandName) {
        case "help":
            return new HelpCommand();
        case "bye":
            return new ByeCommand();
        case "rate":
            return new RateCommand(arguments, ratingsManager);
        default:
            return new UnknownCommand();
        }
    }
}
