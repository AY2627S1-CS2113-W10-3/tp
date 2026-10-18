package seedu.duke.parser;

import seedu.duke.commands.ByeCommand;
import seedu.duke.commands.Command;
import seedu.duke.commands.HelpCommand;
import seedu.duke.commands.ListExpensesCommand;
import seedu.duke.commands.LogExpenseCommand;
import seedu.duke.commands.RateCommand;
import seedu.duke.commands.UnknownCommand;
import seedu.duke.expenses.ExpensesManager;
import seedu.duke.ratings.RatingsManager;
import seedu.duke.commands.DeleteExpenseCommand;
import seedu.duke.commands.EditExpenseCommand;

public class Parser {
    private final RatingsManager ratingsManager;
    private final ExpensesManager expensesManager;

    public Parser(RatingsManager ratingsManager, ExpensesManager expensesManager) {
        this.ratingsManager = ratingsManager;
        this.expensesManager = expensesManager;
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
        case LogExpenseCommand.COMMAND_WORD:
            return new LogExpenseCommand(arguments, expensesManager);
        case ListExpensesCommand.COMMAND_WORD:
            return new ListExpensesCommand(expensesManager);
        case EditExpenseCommand.COMMAND_WORD:
            return new EditExpenseCommand(arguments, expensesManager);
        case DeleteExpenseCommand.COMMAND_WORD:
            return new DeleteExpenseCommand(arguments, expensesManager);
        default:
            return new UnknownCommand();
        }
    }
}
