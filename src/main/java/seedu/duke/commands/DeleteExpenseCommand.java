package seedu.duke.commands;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.expenses.Expense;
import seedu.duke.expenses.ExpensesManager;

/**
 * Deletes a logged expense, e.g. {@code delete-expense 2}.
 */
public class DeleteExpenseCommand extends Command {
    public static final String COMMAND_WORD = "delete-expense";
    public static final String MESSAGE_USAGE = "Format: delete-expense INDEX\n"
            + "Example: delete-expense 1";

    private final String arguments;
    private final ExpensesManager expensesManager;

    /**
     * Creates the command with the text typed after {@code delete-expense}.
     *
     * @param arguments Text after the command word.
     * @param expensesManager Manager holding the expense to delete.
     */
    public DeleteExpenseCommand(String arguments, ExpensesManager expensesManager) {
        assert expensesManager != null : "Expenses manager should not be null";
        this.arguments = arguments == null ? "" : arguments.trim();
        this.expensesManager = expensesManager;
    }

    @Override
    public String execute() {
        try {
            int index = ExpenseArguments.parseIndex(arguments);
            Expense deletedExpense = expensesManager.deleteExpense(index);
            return "Deleted: " + deletedExpense + "\n"
                    + "Expenses left: " + expensesManager.getSize();
        } catch (BudgetBiteException e) {
            return e.getMessage() + "\n" + MESSAGE_USAGE;
        }
    }
}
