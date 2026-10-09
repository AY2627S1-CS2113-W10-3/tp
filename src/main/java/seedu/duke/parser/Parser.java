package seedu.duke.parser;


import seedu.duke.commands.ByeCommand;
import seedu.duke.commands.Command;
import seedu.duke.commands.HelpCommand;
import seedu.duke.commands.UnknownCommand;

public class Parser {
    public Command parseCommand(String userCommandText) {

        String commandName = userCommandText.trim().toLowerCase();

        switch (commandName) {
        case "help":
            return new HelpCommand();
        case "bye":
            return new ByeCommand();
        default:
            return new UnknownCommand();
        }
    }
}


