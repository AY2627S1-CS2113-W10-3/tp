package seedu.duke.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.budget.BudgetManager;
import seedu.duke.commands.BudgetCommand;
import seedu.duke.commands.ByeCommand;
import seedu.duke.commands.DeleteExpenseCommand;
import seedu.duke.commands.DeleteRatingCommand;
import seedu.duke.commands.EditExpenseCommand;
import seedu.duke.commands.EditRatingsCommand;
import seedu.duke.commands.HelpCommand;
import seedu.duke.commands.ListExpensesCommand;
import seedu.duke.commands.ListRatingsCommand;
import seedu.duke.commands.LogExpenseCommand;
import seedu.duke.commands.RateCommand;
import seedu.duke.commands.SetBudgetCommand;
import seedu.duke.commands.UnknownCommand;
import seedu.duke.expenses.ExpensesManager;
import seedu.duke.ratings.RatingsManager;

/**
 * Checks that every command word reaches the right command.
 */
public class ParserTest {
    private Parser parser;

    @BeforeEach
    public void setUp() {
        parser = new Parser(new RatingsManager(), new ExpensesManager(), new BudgetManager());
    }

    @Test
    public void parseCommand_everyCommandWord_correctCommand() {
        assertTrue(parser.parseCommand("help") instanceof HelpCommand);
        assertTrue(parser.parseCommand("bye") instanceof ByeCommand);
        assertTrue(parser.parseCommand("rate /stall A /food B /score 5") instanceof RateCommand);
        assertTrue(parser.parseCommand("list-ratings") instanceof ListRatingsCommand);
        assertTrue(parser.parseCommand("edit-rating 1 /score 5") instanceof EditRatingsCommand);
        assertTrue(parser.parseCommand("delete-rating 1") instanceof DeleteRatingCommand);
        assertTrue(parser.parseCommand("log-expense Teh /cost 1") instanceof LogExpenseCommand);
        assertTrue(parser.parseCommand("list-expenses") instanceof ListExpensesCommand);
        assertTrue(parser.parseCommand("edit-expense 1 /cost 2") instanceof EditExpenseCommand);
        assertTrue(parser.parseCommand("delete-expense 1") instanceof DeleteExpenseCommand);
        assertTrue(parser.parseCommand("set-budget weekly 50") instanceof SetBudgetCommand);
        assertTrue(parser.parseCommand("budget") instanceof BudgetCommand);
    }

    @Test
    public void parseCommand_upperCaseWord_correctCommand() {
        assertTrue(parser.parseCommand("BUDGET") instanceof BudgetCommand);
    }

    @Test
    public void parseCommand_unknownWord_unknownCommand() {
        assertTrue(parser.parseCommand("foo") instanceof UnknownCommand);
    }
}
