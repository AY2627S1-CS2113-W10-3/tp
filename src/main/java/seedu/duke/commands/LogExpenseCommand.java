package seedu.duke.commands;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.expenses.Expense;
import seedu.duke.expenses.ExpensesManager;

/**
 * Logs a food expense, e.g. {@code log-expense Chicken Rice /cost 4.50}.
 */
public class LogExpenseCommand extends Command {
    public static final String COMMAND_WORD = "log-expense";
    public static final String MESSAGE_USAGE = "Format: log-expense FOOD_NAME /cost AMOUNT\n"
            + "Example: log-expense Chicken Rice /cost 4.50";

    private final String arguments;
    private final ExpensesManager expensesManager;

    /**
     * Creates the command with the text typed after {@code log-expense}.
     *
     * @param arguments Text after the command word.
     * @param expensesManager Manager the expense is added to.
     */
    public LogExpenseCommand(String arguments, ExpensesManager expensesManager) {
        assert expensesManager != null : "Expenses manager should not be null";
        this.arguments = arguments == null ? "" : arguments.trim();
        this.expensesManager = expensesManager;
    }

    @Override
    public String execute() {
        try {
            Expense expense = parseExpense();
            expensesManager.addExpense(expense);
            return "Logged: " + expense + "\n"
                    + String.format("Total spent: $%.2f", expensesManager.getTotalCost());
        } catch (BudgetBiteException e) {
            return e.getMessage() + "\n" + MESSAGE_USAGE;
        }
    }

    private Expense parseExpense() throws BudgetBiteException {
        String[] parts = ExpenseArguments.splitAtCost(arguments);
        String foodName = parts[0];
        double cost = Expense.parseCost(parts[1]);
        return new Expense(foodName, cost);
    }
}
