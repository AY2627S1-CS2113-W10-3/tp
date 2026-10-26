package seedu.duke.commands;

import java.time.LocalDate;

import seedu.duke.budget.BudgetManager;
import seedu.duke.expenses.ExpensesManager;

/**
 * Shows the budget, how much was spent in the current period and how much is left.
 */
public class BudgetCommand extends Command {
    public static final String COMMAND_WORD = "seedu/duke/budget";
    public static final String MESSAGE_NO_BUDGET = "No budget set yet. Try: set-budget weekly 50";

    private final BudgetManager budgetManager;
    private final ExpensesManager expensesManager;
    private final LocalDate today;

    /**
     * Creates the command.
     *
     * @param budgetManager Manager holding the budget.
     * @param expensesManager Expenses counted against the budget.
     */
    public BudgetCommand(BudgetManager budgetManager, ExpensesManager expensesManager) {
        this(budgetManager, expensesManager, LocalDate.now());
    }

    /**
     * Creates the command with a fixed "today", so that tests do not depend on the real date.
     */
    public BudgetCommand(BudgetManager budgetManager, ExpensesManager expensesManager, LocalDate today) {
        assert budgetManager != null : "Budget manager should not be null";
        assert expensesManager != null : "Expenses manager should not be null";
        this.budgetManager = budgetManager;
        this.expensesManager = expensesManager;
        this.today = today;
    }

    @Override
    public String execute() {
        if (!budgetManager.hasBudget()) {
            return MESSAGE_NO_BUDGET;
        }
        return budgetManager.getSummary(expensesManager, today);
    }
}
