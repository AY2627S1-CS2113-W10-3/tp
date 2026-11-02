package seedu.duke.commands;

import java.time.LocalDate;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.expenses.Expense;
import seedu.duke.expenses.ExpensesManager;

/**
 * Changes the cost, the date, or both, of a logged expense,
 * e.g. {@code edit-expense 1 /cost 5.00} or {@code edit-expense 1 /date 2026-10-20}.
 */
public class EditExpenseCommand extends Command {
    public static final String COMMAND_WORD = "edit-expense";
    public static final String MESSAGE_USAGE = "Format: edit-expense INDEX [/cost AMOUNT] [/date YYYY-MM-DD]\n"
            + "Example: edit-expense 1 /cost 5.00 /date 2026-10-20";

    private final String arguments;
    private final ExpensesManager expensesManager;
    private final LocalDate today;

    /**
     * Creates the command with the text typed after {@code edit-expense}.
     *
     * @param arguments Text after the command word.
     * @param expensesManager Manager holding the expense to edit.
     */
    public EditExpenseCommand(String arguments, ExpensesManager expensesManager) {
        this(arguments, expensesManager, LocalDate.now());
    }

    /**
     * Creates the command with a fixed "today", so that tests do not depend on the real date.
     *
     * @param arguments Text after the command word.
     * @param expensesManager Manager holding the expense to edit.
     * @param today Date to use as today.
     */
    public EditExpenseCommand(String arguments, ExpensesManager expensesManager, LocalDate today) {
        assert expensesManager != null : "Expenses manager should not be null";
        assert today != null : "Today's date should not be null";
        this.arguments = arguments == null ? "" : arguments.trim();
        this.expensesManager = expensesManager;
        this.today = today;
    }

    @Override
    public String execute() {
        try {
            String[] dateParts = ExpenseArguments.splitAtDate(arguments);
            String indexAndCost = dateParts[0];
            String indexText = indexAndCost;
            Double newCost = null;
            if (indexAndCost.contains(ExpenseArguments.COST_PREFIX)) {
                String[] costParts = ExpenseArguments.splitAtCost(indexAndCost);
                indexText = costParts[0];
                newCost = Expense.parseCost(costParts[1]);
            }

            int index = ExpenseArguments.parseIndex(indexText);
            if (newCost == null && dateParts[1] == null) {
                throw new BudgetBiteException("Give a new /cost, a new /date, or both.");
            }
            LocalDate newDate = dateParts[1] == null ? null : ExpenseArguments.parseDate(dateParts[1], today);

            Expense updatedExpense = expensesManager.editExpense(index, newCost, newDate);
            return "Updated expense " + index + ": " + updatedExpense;
        } catch (BudgetBiteException e) {
            return e.getMessage() + "\n" + MESSAGE_USAGE;
        }
    }
}
