package seedu.duke.expenses;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.exceptions.BudgetBiteException;

public class ExpensesManagerTest {
    private static final double DELTA = 0.001;
    private static final LocalDate MONDAY = LocalDate.of(2026, 10, 19);
    private static final LocalDate WEDNESDAY = LocalDate.of(2026, 10, 21);

    private ExpensesManager expensesManager;

    @BeforeEach
    public void setUp() throws BudgetBiteException {
        expensesManager = new ExpensesManager();
        expensesManager.addExpense(new Expense("Chicken Rice", 4.50, MONDAY));
        expensesManager.addExpense(new Expense("Teh", 1.20, WEDNESDAY));
    }

    @Test
    public void addExpense_validExpense_sizeIncreases() throws BudgetBiteException {
        expensesManager.addExpense(new Expense("Laksa", 5.00));

        assertEquals(3, expensesManager.getSize());
        assertEquals("Laksa", expensesManager.getExpense(3).getFoodName());
    }

    @Test
    public void getTotalCost_twoExpenses_returnsSum() {
        assertEquals(5.70, expensesManager.getTotalCost(), DELTA);
    }

    @Test
    public void getTotalCost_noExpenses_returnsZero() {
        ExpensesManager emptyManager = new ExpensesManager();

        assertTrue(emptyManager.isEmpty());
        assertEquals(0, emptyManager.getTotalCost(), DELTA);
    }

    @Test
    public void editExpenseCost_validIndex_onlyCostUpdated() throws BudgetBiteException {
        expensesManager.editExpenseCost(1, 5.00);

        assertEquals(5.00, expensesManager.getExpense(1).getCost(), DELTA);
        assertEquals("Chicken Rice", expensesManager.getExpense(1).getFoodName());
    }

    @Test
    public void editExpenseCost_zeroCost_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> expensesManager.editExpenseCost(1, 0));
    }

    @Test
    public void deleteExpense_validIndex_expenseRemoved() throws BudgetBiteException {
        Expense deletedExpense = expensesManager.deleteExpense(1);

        assertEquals("Chicken Rice", deletedExpense.getFoodName());
        assertEquals(1, expensesManager.getSize());
        assertEquals("Teh", expensesManager.getExpense(1).getFoodName());
    }

    @Test
    public void deleteExpense_indexOutOfRange_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> expensesManager.deleteExpense(0));
        assertThrows(BudgetBiteException.class, () -> expensesManager.deleteExpense(3));
    }

    @Test
    public void constructor_blankFoodName_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> new Expense("  ", 4.50));
    }

    @Test
    public void constructor_negativeCost_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> new Expense("Chicken Rice", -1));
    }

    @Test
    public void parseCost_notANumber_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> Expense.parseCost("four"));
    }

    @Test
    public void fromStorageString_savedExpense_sameExpenseRestored() throws BudgetBiteException {
        Expense originalExpense = new Expense("Chicken Rice", 4.50);

        Expense restoredExpense = Expense.fromStorageString(originalExpense.toStorageString());

        assertEquals(originalExpense.getFoodName(), restoredExpense.getFoodName());
        assertEquals(originalExpense.getCost(), restoredExpense.getCost(), DELTA);
    }

    @Test
    public void fromStorageString_missingCost_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> Expense.fromStorageString("Chicken Rice"));
    }

    @Test
    public void getExpense_indexOutOfRange_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> expensesManager.getExpense(0));
        assertThrows(BudgetBiteException.class, () -> expensesManager.getExpense(3));
    }

    @Test
    public void toString_twoExpenses_numberedList() {
        String expected = "\n1. Chicken Rice - $4.50 (2026-10-19)\n2. Teh - $1.20 (2026-10-21)";
        assertEquals(expected, expensesManager.toString());
    }

    @Test
    public void getTotalCostBetween_rangeCoversOneExpense_returnsItsCost() {
        assertEquals(1.20, expensesManager.getTotalCostBetween(WEDNESDAY, WEDNESDAY.plusDays(3)), DELTA);
    }

    @Test
    public void getTotalCostBetween_rangeIncludesBothEnds_returnsSum() {
        assertEquals(5.70, expensesManager.getTotalCostBetween(MONDAY, WEDNESDAY), DELTA);
    }

    @Test
    public void storageString_withDate_restoredWithSameDate() throws BudgetBiteException {
        Expense original = new Expense("Chicken Rice", 4.50, MONDAY);
        Expense restored = Expense.fromStorageString(original.toStorageString());

        assertEquals("Chicken Rice|4.50|2026-10-19", original.toStorageString());
        assertEquals(MONDAY, restored.getDate());
    }

    @Test
    public void fromStorageString_oldFormatWithoutDate_datedToday() throws BudgetBiteException {
        Expense restored = Expense.fromStorageString("Teh|1.20");

        assertEquals(LocalDate.now(), restored.getDate());
        assertEquals(1.20, restored.getCost(), DELTA);
    }

    @Test
    public void fromStorageString_invalidDate_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> Expense.fromStorageString("Teh|1.20|yesterday"));
    }

    @Test
    public void withCost_keepsDate() throws BudgetBiteException {
        Expense edited = new Expense("Teh", 1.20, MONDAY).withCost(2.00);
        assertEquals(MONDAY, edited.getDate());
    }
}
