package seedu.duke.commands;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.expenses.Expense;
import seedu.duke.expenses.ExpensesManager;

/**
 * Changes the cost of a logged expense, e.g. {@code edit-expense 1 /cost 5.00}.
 */
public class EditExpenseCommand extends Command {
    public static final String COMMAND_WORD = "edit-expense";
    public static final String MESSAGE_USAGE = "Format: edit-expense INDEX /cost AMOUNT\n"
            + "Example: edit-expense 1 /cost 5.00";

    private final String arguments;
    private final ExpensesManager expensesManager;

    /**
     * Creates the command with the text typed after {@code edit-expense}.
     *
     * @param arguments Text after the command word.
     * @param expensesManager Manager holding the expense to edit.
     */
    public EditExpenseCommand(String arguments, ExpensesManager expensesManager) {
        assert expensesManager != null : "Expenses manager should not be null";
        this.arguments = arguments == null ? "" : arguments.trim();
        this.expensesManager = expensesManager;
    }

    @Override
    public String execute() {
        try {
            String[] parts = ExpenseArguments.splitAtCost(arguments);
            int index = ExpenseArguments.parseIndex(parts[0]);
            double newCost = Expense.parseCost(parts[1]);

            Expense updatedExpense = expensesManager.editExpenseCost(index, newCost);
            return "Updated expense " + index + ": " + updatedExpense;
        } catch (BudgetBiteException e) {
            return e.getMessage() + "\n" + MESSAGE_USAGE;
        }
    }
}
