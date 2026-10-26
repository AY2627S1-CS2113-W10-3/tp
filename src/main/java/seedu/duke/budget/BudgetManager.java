package seedu.duke.budget;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.duke.expenses.ExpensesManager;
import seedu.duke.expenses.Money;

/**
 * Holds the user's budget, if they have set one, and works out how much of it is used.
 * There is at most one budget at a time; setting a new one replaces the old one.
 */
public class BudgetManager {
    /** Share of the budget used at which the user is warned that they are close to the limit. */
    public static final double NEAR_LIMIT_RATIO = 0.8;

    private static final Logger LOGGER = Logger.getLogger(BudgetManager.class.getName());
    private static final double ROUNDING_TOLERANCE = 0.000001;

    private Budget budget;

    /**
     * Creates a manager with no budget set.
     */
    public BudgetManager() {
        this.budget = null;
    }

    /**
     * Creates a manager holding the given budget, e.g. one loaded from the storage file.
     *
     * @param budget Budget to hold.
     */
    public BudgetManager(Budget budget) {
        assert budget != null : "Use the no-argument constructor when there is no budget";
        this.budget = budget;
    }

    /**
     * Sets the budget, replacing any existing one.
     *
     * @param newBudget Budget to set.
     */
    public void setBudget(Budget newBudget) {
        assert newBudget != null : "Budget to set should not be null";
        this.budget = newBudget;
        LOGGER.log(Level.FINE, "Budget set to " + newBudget);
    }

    public boolean hasBudget() {
        return budget != null;
    }

    public Budget getBudget() {
        return budget;
    }

    /**
     * Returns how much was spent in the current budget period, from its first day up to today.
     *
     * @param expenses All expenses.
     * @param today    The current date.
     * @return The amount spent in the current period.
     */
    public double getSpent(ExpensesManager expenses, LocalDate today) {
        assert hasBudget() : "A budget must be set before working out what is spent";
        LocalDate startDate = budget.getPeriod().getStartDate(today);
        return expenses.getTotalCostBetween(startDate, today);
    }

    /**
     * Returns how much is left to spend in the current period. Negative when over the budget.
     *
     * @param expenses All expenses.
     * @param today    The current date.
     * @return The budget minus what was spent in the current period.
     */
    public double getRemaining(ExpensesManager expenses, LocalDate today) {
        return budget.getAmount() - getSpent(expenses, today);
    }

    /**
     * Describes the budget, what was spent in the current period and what is left.
     *
     * @param expenses All expenses.
     * @param today    The current date.
     * @return Several lines summarising the budget.
     */
    public String getSummary(ExpensesManager expenses, LocalDate today) {
        assert hasBudget() : "A budget must be set before it is summarised";
        String periodDescription = budget.getPeriod().getDescription();
        return budget + "\n"
                + "Spent " + periodDescription + ": " + Money.format(getSpent(expenses, today)) + "\n"
                + getRemainingLine(expenses, today);
    }

    /**
     * Returns one line telling the user how their budget stands, shown after they log an expense.
     * Warns them when they are close to the limit or over it.
     *
     * @param expenses All expenses, including the one just logged.
     * @param today    The current date.
     * @return The line to show, or an empty string if no budget is set.
     */
    public String getStatusAfterSpending(ExpensesManager expenses, LocalDate today) {
        if (!hasBudget()) {
            return "";
        }
        double usedRatio = getSpent(expenses, today) / budget.getAmount();
        if (usedRatio >= NEAR_LIMIT_RATIO && !isOverBudget(expenses, today)) {
            int usedPercent = (int) Math.floor(usedRatio * 100);
            return "Careful: you have used " + usedPercent + "% of your "
                    + budget.getPeriod().getKeyword() + " budget.\n" + getRemainingLine(expenses, today);
        }
        return getRemainingLine(expenses, today);
    }

    private String getRemainingLine(ExpensesManager expenses, LocalDate today) {
        String periodDescription = budget.getPeriod().getDescription();
        if (isOverBudget(expenses, today)) {
            double overBy = -getRemaining(expenses, today);
            return "Warning: you are " + Money.format(overBy) + " over your "
                    + budget.getPeriod().getKeyword() + " budget!";
        }
        return "Remaining " + periodDescription + ": " + Money.format(getRemaining(expenses, today));
    }

    private boolean isOverBudget(ExpensesManager expenses, LocalDate today) {
        // The tolerance stops tiny floating-point errors from counting as being over, e.g. 0.1 + 0.2.
        return getRemaining(expenses, today) < -ROUNDING_TOLERANCE;
    }
}
