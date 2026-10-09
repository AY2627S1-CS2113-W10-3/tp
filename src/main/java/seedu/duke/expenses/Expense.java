package seedu.duke.expenses;

import seedu.duke.exceptions.BudgetBiteException;

/**
 * Represents a single food purchase logged by the user.
 */
public class Expense {
    private static final String STORAGE_SEPARATOR = "|";
    private static final String STORAGE_SEPARATOR_REGEX = "\\|";
    private static final int STORAGE_FIELD_COUNT = 2;

    private final String foodName;
    private final double cost;

    /**
     * Creates an expense with the given food name and cost.
     *
     * @param foodName Name of the food purchased.
     * @param cost     Amount spent on the food. Must be positive.
     * @throws BudgetBiteException If the food name or the cost is invalid.
     */
    public Expense(String foodName, double cost) throws BudgetBiteException {
        validateFoodName(foodName);
        validateCost(cost);
        this.foodName = foodName.trim();
        this.cost = cost;
    }

    /**
     * Creates an expense from a line in the storage file, in the format {@code FOOD_NAME|COST}.
     *
     * @param storageLine Line read from the storage file.
     * @return The expense described by the line.
     * @throws BudgetBiteException If the line is not in the expected format.
     */
    public static Expense fromStorageString(String storageLine) throws BudgetBiteException {
        String[] fields = storageLine.split(STORAGE_SEPARATOR_REGEX);
        if (fields.length != STORAGE_FIELD_COUNT) {
            throw new BudgetBiteException("Corrupted expense entry: " + storageLine);
        }
        return new Expense(fields[0], parseCost(fields[1]));
    }

    /**
     * Parses the given text into a cost.
     *
     * @param costText Text containing the cost, e.g. {@code "4.50"}.
     * @return The cost as a number.
     * @throws BudgetBiteException If the text is not a number.
     */
    public static double parseCost(String costText) throws BudgetBiteException {
        try {
            return Double.parseDouble(costText.trim());
        } catch (NumberFormatException e) {
            throw new BudgetBiteException("Cost must be a number, e.g. 4.50");
        }
    }

    public String getFoodName() {
        return foodName;
    }

    public double getCost() {
        return cost;
    }

    /**
     * Returns a copy of this expense with its cost replaced by the given cost.
     *
     * @param newCost New cost of the expense. Must be positive.
     * @return The updated expense.
     * @throws BudgetBiteException If the new cost is invalid.
     */
    public Expense withCost(double newCost) throws BudgetBiteException {
        return new Expense(foodName, newCost);
    }

    /**
     * Returns this expense in the format used by the storage file.
     *
     * @return The expense as {@code FOOD_NAME|COST}.
     */
    public String toStorageString() {
        return foodName + STORAGE_SEPARATOR + String.format("%.2f", cost);
    }

    @Override
    public String toString() {
        return String.format("%s - $%.2f", foodName, cost);
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
        boolean isPositiveNumber = cost > 0 && Double.isFinite(cost);
        if (!isPositiveNumber) {
            throw new BudgetBiteException("Cost must be a positive number.");
        }
    }
}
