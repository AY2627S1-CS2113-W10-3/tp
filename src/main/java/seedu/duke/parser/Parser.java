package seedu.duke.parser;

import seedu.duke.budget.BudgetManager;
import seedu.duke.commands.BudgetCommand;
import seedu.duke.commands.ByeCommand;
import seedu.duke.commands.Command;
import seedu.duke.commands.HelpCommand;
import seedu.duke.commands.ListExpensesCommand;
import seedu.duke.commands.LogExpenseCommand;
import seedu.duke.commands.RateCommand;
import seedu.duke.commands.SetBudgetCommand;
import seedu.duke.commands.UnknownCommand;
import seedu.duke.expenses.ExpensesManager;
import seedu.duke.food.FoodDirectory;
import seedu.duke.ratings.RatingsManager;
import seedu.duke.commands.DeleteExpenseCommand;
import seedu.duke.commands.DeleteRatingCommand;
import seedu.duke.commands.EditExpenseCommand;
import seedu.duke.commands.EditRatingsCommand;
import seedu.duke.commands.ListRatingsCommand;

public class Parser {
    private final RatingsManager ratingsManager;
    private final ExpensesManager expensesManager;
    private final BudgetManager budgetManager;
    private final FoodDirectory foodDirectory;

    /**
     * Creates a parser whose commands work on the given data.
     *
     * @param ratingsManager  Ratings used by the rating commands.
     * @param expensesManager Expenses used by the expense and budget commands.
     * @param budgetManager   Budget used by the budget commands and {@code log-expense}.
     * @param foodDirectory   Food catalogue that {@code rate} checks names against.
     */
    public Parser(RatingsManager ratingsManager, ExpensesManager expensesManager, BudgetManager budgetManager,
                  FoodDirectory foodDirectory) {
        this.ratingsManager = ratingsManager;
        this.expensesManager = expensesManager;
        this.budgetManager = budgetManager;
        this.foodDirectory = foodDirectory;
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
        case RateCommand.COMMAND_WORD:
            return new RateCommand(arguments, ratingsManager, foodDirectory);
        case ListRatingsCommand.COMMAND_WORD:
            return new ListRatingsCommand(ratingsManager);
        case EditRatingsCommand.COMMAND_WORD:
            return new EditRatingsCommand(arguments, ratingsManager);
        case DeleteRatingCommand.COMMAND_WORD:
            return new DeleteRatingCommand(arguments, ratingsManager);
        case LogExpenseCommand.COMMAND_WORD:
            return new LogExpenseCommand(arguments, expensesManager, budgetManager);
        case ListExpensesCommand.COMMAND_WORD:
            return new ListExpensesCommand(expensesManager);
        case EditExpenseCommand.COMMAND_WORD:
            return new EditExpenseCommand(arguments, expensesManager);
        case DeleteExpenseCommand.COMMAND_WORD:
            return new DeleteExpenseCommand(arguments, expensesManager);
        case SetBudgetCommand.COMMAND_WORD:
            return new SetBudgetCommand(arguments, budgetManager, expensesManager);
        case BudgetCommand.COMMAND_WORD:
            return new BudgetCommand(budgetManager, expensesManager);
        default:
            return new UnknownCommand();
        }
    }
}
