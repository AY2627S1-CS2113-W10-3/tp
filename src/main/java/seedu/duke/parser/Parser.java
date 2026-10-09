package seedu.duke.parser;


import seedu.duke.commands.AddRatingCommand;
import seedu.duke.commands.ByeCommand;
import seedu.duke.commands.Command;
import seedu.duke.commands.DeleteRatingCommand;
import seedu.duke.commands.EditRatingCommand;
import seedu.duke.commands.HelpCommand;
import seedu.duke.commands.ListRatingsCommand;
import seedu.duke.commands.UnknownCommand;
import seedu.duke.ratings.RatingsManager;

public class Parser {
    private final RatingCommandParser ratingCommandParser;

    public Parser(RatingsManager ratings) {
        this.ratingCommandParser = new RatingCommandParser(ratings);
    }

    public Command parseCommand(String userCommandText) {

        // Split into the command word and the rest, e.g. "rate" and "/food Laksa /stall Indian /score 7".
        String[] commandParts = userCommandText.trim().split("\\s+", 2);
        String commandName = commandParts[0].toLowerCase();
        String arguments = commandParts.length > 1 ? commandParts[1] : "";

        switch (commandName) {
            case AddRatingCommand.COMMAND_WORD:
            case ListRatingsCommand.COMMAND_WORD:
            case EditRatingCommand.COMMAND_WORD:
            case DeleteRatingCommand.COMMAND_WORD:
                return ratingCommandParser.parse(commandName, arguments);
            case "help":
                return new HelpCommand();
            case "bye":
                return new ByeCommand();
            default:
                return new UnknownCommand();
        }
    }
}


