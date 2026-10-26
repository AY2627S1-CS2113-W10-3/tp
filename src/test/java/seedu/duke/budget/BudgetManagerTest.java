package seedu.duke.budget;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.expenses.Expense;
import seedu.duke.expenses.ExpensesManager;

public class BudgetManagerTest {
    private static final double DELTA = 0.001;
    /** A Wednesday, so this week runs from Monday 2026-10-26. */
    private static final LocalDate TODAY = LocalDate.of(2026, 10, 28);

    private ExpensesManager expenses;
    private BudgetManager budgetManager;

    @BeforeEach
    public void setUp() throws BudgetBiteException {
        expenses = new ExpensesManager();
        expenses.addExpense(new Expense("Last Sunday", 10.00, LocalDate.of(2026, 10, 25)));
        expenses.addExpense(new Expense("Monday", 4.00, LocalDate.of(2026, 10, 26)));
        expenses.addExpense(new Expense("Today", 6.00, TODAY));
        budgetManager = new BudgetManager();
    }

    @Test
    public void periodStartDates_correct() {
        assertEquals(TODAY, BudgetPeriod.DAILY.getStartDate(TODAY));
        assertEquals(LocalDate.of(2026, 10, 26), BudgetPeriod.WEEKLY.getStartDate(TODAY));
        assertEquals(LocalDate.of(2026, 10, 1), BudgetPeriod.MONTHLY.getStartDate(TODAY));
    }

    @Test
    public void weeklyStart_onMonday_isSameDay() {
        LocalDate monday = LocalDate.of(2026, 10, 26);
        assertEquals(monday, BudgetPeriod.WEEKLY.getStartDate(monday));
    }

    @Test
    public void getSpent_eachPeriod_countsOnlyThatPeriod() throws BudgetBiteException {
        budgetManager.setBudget(new Budget(BudgetPeriod.DAILY, 50));
        assertEquals(6.00, budgetManager.getSpent(expenses, TODAY), DELTA);

        budgetManager.setBudget(new Budget(BudgetPeriod.WEEKLY, 50));
        assertEquals(10.00, budgetManager.getSpent(expenses, TODAY), DELTA);

        budgetManager.setBudget(new Budget(BudgetPeriod.MONTHLY, 50));
        assertEquals(20.00, budgetManager.getSpent(expenses, TODAY), DELTA);
    }

    @Test
    public void getStatusAfterSpending_noBudget_empty() {
        assertEquals("", budgetManager.getStatusAfterSpending(expenses, TODAY));
    }

    @Test
    public void getStatusAfterSpending_underLimit_remainingOnly() throws BudgetBiteException {
        budgetManager.setBudget(new Budget(BudgetPeriod.WEEKLY, 50));
        assertEquals("Remaining this week: $40.00", budgetManager.getStatusAfterSpending(expenses, TODAY));
    }

    @Test
    public void getStatusAfterSpending_nearLimit_careful() throws BudgetBiteException {
        budgetManager.setBudget(new Budget(BudgetPeriod.WEEKLY, 12));
        String status = budgetManager.getStatusAfterSpending(expenses, TODAY);

        assertTrue(status.contains("Careful: you have used 83% of your weekly budget."));
        assertTrue(status.contains("Remaining this week: $2.00"));
    }

    @Test
    public void getStatusAfterSpending_exactlyAtLimit_notOver() throws BudgetBiteException {
        budgetManager.setBudget(new Budget(BudgetPeriod.WEEKLY, 10));
        String status = budgetManager.getStatusAfterSpending(expenses, TODAY);

        assertFalse(status.contains("Warning"));
        assertTrue(status.contains("Remaining this week: $0.00"));
    }

    @Test
    public void getStatusAfterSpending_overLimit_warning() throws BudgetBiteException {
        budgetManager.setBudget(new Budget(BudgetPeriod.DAILY, 5));
        assertEquals("Warning: you are $1.00 over your daily budget!",
                budgetManager.getStatusAfterSpending(expenses, TODAY));
    }

    @Test
    public void getSummary_weekly_threeLines() throws BudgetBiteException {
        budgetManager.setBudget(new Budget(BudgetPeriod.WEEKLY, 50));
        assertEquals("Weekly budget: $50.00\nSpent this week: $10.00\nRemaining this week: $40.00",
                budgetManager.getSummary(expenses, TODAY));
    }

    @Test
    public void budgetStorageString_roundTrip() throws BudgetBiteException {
        Budget budget = Budget.fromStorageString(new Budget(BudgetPeriod.MONTHLY, 300).toStorageString());

        assertEquals(BudgetPeriod.MONTHLY, budget.getPeriod());
        assertEquals(300, budget.getAmount(), DELTA);
    }

    @Test
    public void periodFromKeyword_invalid_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> BudgetPeriod.fromKeyword("yearly"));
    }

    @Test
    public void budget_aboveMaximum_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> Budget.parseAmount("100000.01"));
    }
}
