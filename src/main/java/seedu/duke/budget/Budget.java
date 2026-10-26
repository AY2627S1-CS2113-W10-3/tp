package seedu.duke.budget;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.expenses.Money;

/**
 * A spending limit for food over a period, e.g. $50 a week.
 */
public class Budget {
    /** Largest budget allowed, to catch typing mistakes such as extra zeros. */
    public static final double MAX_BUDGET = 100000;

    private static final String BUDGET_LABEL = "Budget";
    private static final String STORAGE_SEPARATOR = "|";
    private static final String STORAGE_SEPARATOR_REGEX = "\\|";
    private static final int STORAGE_FIELD_COUNT = 2;

    private final BudgetPeriod period;
    private final double amount;

    /**
     * Creates a budget.
     *
     * @param period Period the budget covers.
     * @param amount Most the user wants to spend in that period. Must be positive.
     * @throws BudgetBiteException If the amount is not positive or is above {@link #MAX_BUDGET}.
     */
    public Budget(BudgetPeriod period, double amount) throws BudgetBiteException {
        assert period != null : "Budget period should not be null";
        Money.validate(amount, BUDGET_LABEL, MAX_BUDGET);
        this.period = period;
        this.amount = amount;
    }

    /**
     * Parses the amount typed by the user for a budget.
     *
     * @param amountText Text such as {@code "50"} or {@code "120.50"}.
     * @return The amount.
     * @throws BudgetBiteException If the text is not a valid amount.
     */
    public static double parseAmount(String amountText) throws BudgetBiteException {
        return Money.parse(amountText, BUDGET_LABEL, MAX_BUDGET);
    }

    /**
     * Creates a budget from its line in the storage file, in the format {@code PERIOD|AMOUNT}.
     *
     * @param storageLine Line read from the storage file, e.g. {@code weekly|50.00}.
     * @return The budget described by the line.
     * @throws BudgetBiteException If the line is not in the expected format.
     */
    public static Budget fromStorageString(String storageLine) throws BudgetBiteException {
        String[] fields = storageLine.split(STORAGE_SEPARATOR_REGEX);
        if (fields.length != STORAGE_FIELD_COUNT) {
            throw new BudgetBiteException("Corrupted budget entry: " + storageLine);
        }
        return new Budget(BudgetPeriod.fromKeyword(fields[0]), parseAmount(fields[1]));
    }

    public BudgetPeriod getPeriod() {
        return period;
    }

    public double getAmount() {
        return amount;
    }

    /**
     * Returns this budget in the format used by the storage file, e.g. {@code weekly|50.00}.
     */
    public String toStorageString() {
        return period.getKeyword() + STORAGE_SEPARATOR + String.format("%.2f", amount);
    }

    @Override
    public String toString() {
        String periodName = period.getKeyword();
        return Character.toUpperCase(periodName.charAt(0)) + periodName.substring(1)
                + " budget: " + Money.format(amount);
    }
}
