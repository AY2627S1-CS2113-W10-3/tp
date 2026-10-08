package seedu.duke.parser;

import seedu.duke.commands.ByeCommand;
import seedu.duke.commands.Command;
import seedu.duke.commands.HelpCommand;
import seedu.duke.commands.UnknownCommand;

public class Parser {
    public Command parseCommand(String userCommandText) {
        String[] args = userCommandText.split(" ");

        switch (args[0].toLowerCase()) {
            case "help":
                return new HelpCommand();
            case "bye":
                return new ByeCommand();
            default:
                return new UnknownCommand();
        }
    }
}
