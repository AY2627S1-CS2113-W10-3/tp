package seedu.duke.expenses;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import seedu.duke.exceptions.BudgetBiteException;

/**
 * Represents a single food purchase logged by the user: what was bought, how much it cost and when.
 */
public class Expense {
    /** Largest cost allowed for one expense, to catch typing mistakes such as an extra zero. */
    public static final double MAX_COST = 1000;

    private static final String STORAGE_SEPARATOR = "|";
    private static final String STORAGE_SEPARATOR_REGEX = "\\|";
    private static final int OLD_STORAGE_FIELD_COUNT = 2;
    private static final int STORAGE_FIELD_COUNT = 3;
    private static final String COST_LABEL = "Cost";

    private final String foodName;
    private final double cost;
    private final LocalDate date;

    /**
     * Creates an expense dated today.
     *
     * @param foodName Name of the food purchased.
     * @param cost     Amount spent on the food. Must be positive.
     * @throws BudgetBiteException If the food name or the cost is invalid.
     */
    public Expense(String foodName, double cost) throws BudgetBiteException {
        this(foodName, cost, LocalDate.now());
    }

    /**
     * Creates an expense with the given food name, cost and date.
     *
     * @param foodName Name of the food purchased.
     * @param cost     Amount spent on the food. Must be positive.
     * @param date     Date the food was bought.
     * @throws BudgetBiteException If the food name or the cost is invalid.
     */
    public Expense(String foodName, double cost, LocalDate date) throws BudgetBiteException {
        assert date != null : "Expense date should not be null";
        validateFoodName(foodName);
        validateCost(cost);
        this.foodName = foodName.trim();
        this.cost = cost;
        this.date = date;
    }

    /**
     * Creates an expense from a line in the storage file, in the format {@code FOOD_NAME|COST|DATE}.
     * Lines saved by v1.0 have no date ({@code FOOD_NAME|COST}); they are given today's date.
     *
     * @param storageLine Line read from the storage file.
     * @return The expense described by the line.
     * @throws BudgetBiteException If the line is not in the expected format.
     */
    public static Expense fromStorageString(String storageLine) throws BudgetBiteException {
        String[] fields = storageLine.split(STORAGE_SEPARATOR_REGEX);
        if (fields.length == OLD_STORAGE_FIELD_COUNT) {
            return new Expense(fields[0], parseCost(fields[1]));
        }
        if (fields.length != STORAGE_FIELD_COUNT) {
            throw new BudgetBiteException("Corrupted expense entry: " + storageLine);
        }
        try {
            return new Expense(fields[0], parseCost(fields[1]), LocalDate.parse(fields[2].trim()));
        } catch (DateTimeParseException e) {
            throw new BudgetBiteException("Corrupted expense date: " + storageLine);
        }
    }

    /**
     * Parses the given text into a cost.
     *
     * @param costText Text containing the cost, e.g. {@code "4.50"}.
     * @return The cost as a number.
     * @throws BudgetBiteException If the text is not a positive number with at most 2 decimal places,
     *                             or is more than {@link #MAX_COST}.
     */
    public static double parseCost(String costText) throws BudgetBiteException {
        return Money.parse(costText, COST_LABEL, MAX_COST);
    }

    public String getFoodName() {
        return foodName;
    }

    public double getCost() {
        return cost;
    }

    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns true if this expense was made between the two dates, including both of them.
     *
     * @param startDate First day of the range.
     * @param endDate   Last day of the range.
     * @return Whether the expense's date is in the range.
     */
    public boolean isBetween(LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Returns a copy of this expense with its cost replaced by the given cost. The date stays the same.
     *
     * @param newCost New cost of the expense. Must be positive.
     * @return The updated expense.
     * @throws BudgetBiteException If the new cost is invalid.
     */
    public Expense withCost(double newCost) throws BudgetBiteException {
        return new Expense(foodName, newCost, date);
    }

    /**
     * Returns this expense in the format used by the storage file.
     *
     * @return The expense as {@code FOOD_NAME|COST|DATE}, e.g. {@code Chicken Rice|4.50|2026-10-26}.
     */
    public String toStorageString() {
        return foodName + STORAGE_SEPARATOR + String.format("%.2f", cost) + STORAGE_SEPARATOR + date;
    }

    @Override
    public String toString() {
        return foodName + " - " + Money.format(cost) + " (" + date + ")";
    }

    private static void validateFoodName(String foodName) throws BudgetBiteException {
        if (foodName == null || foodName.isBlank()) {
            throw new BudgetBiteException("Food name cannot be empty.");
        }
        if (foodName.contains(STORAGE_SEPARATOR)) {
            throw new BudgetBiteException("Food name cannot contain '" + STORAGE_SEPARATOR + "'.");
        }
    }

    private static void validateCost(double cost) throws BudgetBiteException {
        Money.validate(cost, COST_LABEL, MAX_COST);
    }
}
