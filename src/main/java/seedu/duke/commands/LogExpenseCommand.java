package seedu.duke.commands;

import java.time.LocalDate;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.expenses.Expense;
import seedu.duke.expenses.ExpensesManager;
import seedu.duke.expenses.Money;

/**
 * Logs a food expense, e.g. {@code log-expense Chicken Rice /cost 4.50 /date 2026-10-20}.
 * The {@code /date} part is optional; without it the expense is dated today.
 */
public class LogExpenseCommand extends Command {
    public static final String COMMAND_WORD = "log-expense";
    public static final String MESSAGE_USAGE = "Format: log-expense FOOD_NAME /cost AMOUNT [/date YYYY-MM-DD]\n"
            + "Example: log-expense Chicken Rice /cost 4.50";

    private final String arguments;
    private final ExpensesManager expensesManager;
    private final LocalDate today;

    /**
     * Creates the command with the text typed after {@code log-expense}.
     *
     * @param arguments Text after the command word.
     * @param expensesManager Manager the expense is added to.
     */
    public LogExpenseCommand(String arguments, ExpensesManager expensesManager) {
        this(arguments, expensesManager, LocalDate.now());
    }

    /**
     * Creates the command with a fixed "today", so that tests do not depend on the real date.
     *
     * @param arguments Text after the command word.
     * @param expensesManager Manager the expense is added to.
     * @param today Date to use as today.
     */
    public LogExpenseCommand(String arguments, ExpensesManager expensesManager, LocalDate today) {
        assert expensesManager != null : "Expenses manager should not be null";
        assert today != null : "Today's date should not be null";
        this.arguments = arguments == null ? "" : arguments.trim();
        this.expensesManager = expensesManager;
        this.today = today;
    }

    @Override
    public String execute() {
        try {
            Expense expense = parseExpense();
            expensesManager.addExpense(expense);
            return "Logged: " + expense + "\n"
                    + "Total spent: " + Money.format(expensesManager.getTotalCost());
        } catch (BudgetBiteException e) {
            return e.getMessage() + "\n" + MESSAGE_USAGE;
        }
    }

    private Expense parseExpense() throws BudgetBiteException {
        String[] dateParts = ExpenseArguments.splitAtDate(arguments);
        String[] costParts = ExpenseArguments.splitAtCost(dateParts[0]);

        String foodName = costParts[0];
        double cost = Expense.parseCost(costParts[1]);
        LocalDate date = dateParts[1] == null ? today : ExpenseArguments.parseDate(dateParts[1], today);
        return new Expense(foodName, cost, date);
    }
}
