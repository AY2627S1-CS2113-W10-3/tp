package seedu.duke.commands;

import seedu.duke.expenses.ExpensesManager;

/**
 * Lists every logged expense with its index and the total spent.
 */
public class ListExpensesCommand extends Command {
    public static final String COMMAND_WORD = "list-expenses";

    private final ExpensesManager expensesManager;

    /**
     * Creates the command.
     *
     * @param expensesManager Manager whose expenses are listed.
     */
    public ListExpensesCommand(ExpensesManager expensesManager) {
        assert expensesManager != null : "Expenses manager should not be null";
        this.expensesManager = expensesManager;
    }

    @Override
    public String execute() {
        if (expensesManager.isEmpty()) {
            return "No expenses logged yet. Try: log-expense Chicken Rice /cost 4.50";
        }
        return "Your expenses:" + expensesManager + "\n"
                + String.format("Total spent: $%.2f", expensesManager.getTotalCost());
    }
}
