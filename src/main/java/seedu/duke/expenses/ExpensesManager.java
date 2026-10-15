package seedu.duke.expenses;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.duke.exceptions.BudgetBiteException;

/**
 * Manages the list of expenses logged by the user.
 * Indexes given to the public methods start from 1, matching the numbers shown to the user.
 */
public class ExpensesManager {
    private static final Logger LOGGER = Logger.getLogger(ExpensesManager.class.getName());

    private final ArrayList<Expense> expenses;

    /**
     * Creates a manager with no expenses.
     */
    public ExpensesManager() {
        this(new ArrayList<>());
    }

    /**
     * Creates a manager holding the given expenses, e.g. those loaded from the storage file.
     *
     * @param expenses Expenses to manage.
     */
    public ExpensesManager(ArrayList<Expense> expenses) {
        assert expenses != null : "Expense list should not be null";
        this.expenses = expenses;
    }

    /**
     * Adds an expense to the end of the list.
     *
     * @param expense Expense to add.
     */
    public void addExpense(Expense expense) {
        assert expense != null : "Expense to add should not be null";
        int sizeBefore = expenses.size();

        expenses.add(expense);

        assert expenses.size() == sizeBefore + 1 : "List should grow by one after adding";
        LOGGER.log(Level.FINE, "Added expense: " + expense);
    }

    /**
     * Returns the expense at the given index.
     *
     * @param index Index of the expense, starting from 1.
     * @return The expense at that index.
     * @throws BudgetBiteException If the index is out of range.
     */
    public Expense getExpense(int index) throws BudgetBiteException {
        checkIndex(index);
        return expenses.get(toZeroBased(index));
    }

    /**
     * Changes the cost of the expense at the given index.
     *
     * @param index   Index of the expense, starting from 1.
     * @param newCost New cost of the expense. Must be positive.
     * @return The updated expense.
     * @throws BudgetBiteException If the index is out of range or the cost is invalid.
     */
    public Expense editExpenseCost(int index, double newCost) throws BudgetBiteException {
        Expense updatedExpense = getExpense(index).withCost(newCost);
        expenses.set(toZeroBased(index), updatedExpense);
        LOGGER.log(Level.FINE, "Edited expense " + index + " to: " + updatedExpense);
        return updatedExpense;
    }

    /**
     * Deletes the expense at the given index.
     *
     * @param index Index of the expense, starting from 1.
     * @return The deleted expense.
     * @throws BudgetBiteException If the index is out of range.
     */
    public Expense deleteExpense(int index) throws BudgetBiteException {
        checkIndex(index);
        int sizeBefore = expenses.size();

        Expense deletedExpense = expenses.remove(toZeroBased(index));

        assert expenses.size() == sizeBefore - 1 : "List should shrink by one after deleting";
        LOGGER.log(Level.FINE, "Deleted expense: " + deletedExpense);
        return deletedExpense;
    }

    /**
     * Returns the total amount spent across all expenses.
     *
     * @return Sum of the costs of all expenses.
     */
    public double getTotalCost() {
        double totalCost = 0;
        for (Expense expense : expenses) {
            totalCost += expense.getCost();
        }
        assert totalCost >= 0 : "Total cost should never be negative";
        return totalCost;
    }

    public int getSize() {
        return expenses.size();
    }

    public boolean isEmpty() {
        return expenses.isEmpty();
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    @Override
    public String toString() {
        StringBuilder expensesText = new StringBuilder();
        for (int i = 0; i < expenses.size(); i++) {
            expensesText.append("\n").append(i + 1).append(". ").append(expenses.get(i));
        }
        return expensesText.toString();
    }

    private void checkIndex(int index) throws BudgetBiteException {
        if (expenses.isEmpty()) {
            LOGGER.log(Level.FINE, "Rejected expense index " + index + " because the list is empty");
            throw new BudgetBiteException("You have no expenses yet.");
        }
        boolean isInRange = index >= 1 && index <= expenses.size();
        if (!isInRange) {
            LOGGER.log(Level.FINE, "Rejected expense index " + index + " (list size " + expenses.size() + ")");
            throw new BudgetBiteException("Expense index must be between 1 and " + expenses.size() + ".");
        }
    }

    private int toZeroBased(int index) {
        return index - 1;
    }
}
