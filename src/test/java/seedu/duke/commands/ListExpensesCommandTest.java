package seedu.duke.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.duke.expenses.Expense;
import seedu.duke.expenses.ExpensesManager;

public class ListExpensesCommandTest {
    /** A Wednesday, so this week runs from Monday 2026-10-26. */
    private static final LocalDate TODAY = LocalDate.of(2026, 10, 28);

    @Test
    public void execute_noExpenses_emptyMessageShown() {
        String response = new ListExpensesCommand(new ExpensesManager()).execute();

        assertTrue(response.contains("No expenses logged yet."));
    }

    @Test
    public void execute_twoExpenses_numberedListWithTotal() throws Exception {
        ExpensesManager expensesManager = new ExpensesManager();
        expensesManager.addExpense(new Expense("Chicken Rice", 4.50));
        expensesManager.addExpense(new Expense("Teh", 1.20));

        String response = new ListExpensesCommand(expensesManager).execute();

        assertTrue(response.contains("1. Chicken Rice - $4.50"));
        assertTrue(response.contains("2. Teh - $1.20"));
        assertTrue(response.contains("Total spent: $5.70"));
    }

    private ExpensesManager makeExpenses() throws Exception {
        ExpensesManager expensesManager = new ExpensesManager();
        expensesManager.addExpense(new Expense("Mala", 9.00, LocalDate.of(2026, 10, 23)));
        expensesManager.addExpense(new Expense("Chicken Rice", 4.50, LocalDate.of(2026, 10, 26)));
        expensesManager.addExpense(new Expense("Laksa", 12.00, TODAY));
        return expensesManager;
    }

    @Test
    public void execute_weekly_onlyThisWeekWithOriginalIndexes() throws Exception {
        String response = new ListExpensesCommand("weekly", makeExpenses(), TODAY).execute();

        assertTrue(response.contains("Your expenses this week:"));
        assertFalse(response.contains("Mala"));
        assertTrue(response.contains("2. Chicken Rice - $4.50 (2026-10-26)"));
        assertTrue(response.contains("3. Laksa - $12.00 (2026-10-28)"));
        assertTrue(response.contains("Total spent this week: $16.50"));
    }

    @Test
    public void execute_daily_onlyToday() throws Exception {
        String response = new ListExpensesCommand("DAILY", makeExpenses(), TODAY).execute();

        assertTrue(response.contains("3. Laksa"));
        assertFalse(response.contains("Chicken Rice"));
        assertTrue(response.contains("Total spent today: $12.00"));
    }

    @Test
    public void execute_monthly_wholeMonth() throws Exception {
        String response = new ListExpensesCommand("monthly", makeExpenses(), TODAY).execute();
        assertTrue(response.contains("Total spent this month: $25.50"));
    }

    @Test
    public void execute_periodWithNoExpenses_message() {
        String response = new ListExpensesCommand("weekly", new ExpensesManager(), TODAY).execute();
        assertEquals("No expenses this week.", response);
    }

    @Test
    public void execute_unknownPeriod_errorAndUsage() throws Exception {
        String response = new ListExpensesCommand("yearly", makeExpenses(), TODAY).execute();

        assertTrue(response.contains("Period must be daily, weekly or monthly."));
        assertTrue(response.contains(ListExpensesCommand.MESSAGE_USAGE));
    }
}
