package seedu.duke.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.budget.Budget;
import seedu.duke.budget.BudgetManager;
import seedu.duke.budget.BudgetPeriod;
import seedu.duke.expenses.ExpensesManager;

public class LogExpenseCommandTest {
    private static final LocalDate TODAY = LocalDate.of(2026, 10, 26);

    private ExpensesManager expensesManager;
    private BudgetManager budgetManager;

    @BeforeEach
    public void setUp() {
        expensesManager = new ExpensesManager();
        budgetManager = new BudgetManager();
    }

    @Test
    public void execute_validInput_expenseAdded() throws Exception {
        String response = new LogExpenseCommand("Chicken Rice /cost 4.50", expensesManager, budgetManager).execute();

        assertEquals(1, expensesManager.getSize());
        assertEquals("Chicken Rice", expensesManager.getExpense(1).getFoodName());
        assertTrue(response.contains("Logged: Chicken Rice - $4.50"));
        assertTrue(response.contains("Total spent: $4.50"));
    }

    @Test
    public void execute_missingCostPrefix_errorShownAndNothingAdded() {
        String response = new LogExpenseCommand("Chicken Rice 4.50", expensesManager, budgetManager).execute();

        assertEquals(0, expensesManager.getSize());
        assertTrue(response.contains("Missing /cost."));
        assertTrue(response.contains(LogExpenseCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_missingAmount_errorShown() {
        String response = new LogExpenseCommand("Chicken Rice /cost", expensesManager, budgetManager).execute();

        assertEquals(0, expensesManager.getSize());
        assertTrue(response.contains("Missing amount after /cost."));
    }

    @Test
    public void execute_zeroCost_errorShown() {
        String response = new LogExpenseCommand("Teh /cost 0", expensesManager, budgetManager).execute();

        assertEquals(0, expensesManager.getSize());
        assertTrue(response.contains("Cost must be a positive number."));
    }

    @Test
    public void execute_costNotANumber_errorShown() {
        String response = new LogExpenseCommand("Teh /cost abc", expensesManager, budgetManager).execute();

        assertEquals(0, expensesManager.getSize());
        assertTrue(response.contains("Cost must be a number"));
    }

    @Test
    public void execute_blankFoodName_errorShown() {
        String response = new LogExpenseCommand("/cost 4.50", expensesManager, budgetManager).execute();

        assertEquals(0, expensesManager.getSize());
        assertTrue(response.contains("Food name cannot be empty."));
    }

    @Test
    public void execute_noDate_datedToday() throws Exception {
        new LogExpenseCommand("Teh /cost 1.20", expensesManager, budgetManager, TODAY).execute();
        assertEquals(TODAY, expensesManager.getExpense(1).getDate());
    }

    @Test
    public void execute_pastDate_expenseHasThatDate() throws Exception {
        String response = new LogExpenseCommand("Teh /cost 1.20 /date 2026-10-20",
                expensesManager, budgetManager, TODAY).execute();

        assertEquals(LocalDate.of(2026, 10, 20), expensesManager.getExpense(1).getDate());
        assertTrue(response.contains("Logged: Teh - $1.20 (2026-10-20)"));
    }

    @Test
    public void execute_futureDate_rejected() {
        String response = new LogExpenseCommand("Teh /cost 1.20 /date 2026-10-27",
                expensesManager, budgetManager, TODAY).execute();

        assertTrue(response.contains("Date cannot be in the future."));
        assertEquals(0, expensesManager.getSize());
    }

    @Test
    public void execute_impossibleDate_rejected() {
        String response = new LogExpenseCommand("Teh /cost 1.20 /date 2026-02-30",
                expensesManager, budgetManager, TODAY).execute();

        assertTrue(response.contains("format YYYY-MM-DD"));
        assertEquals(0, expensesManager.getSize());
    }

    @Test
    public void execute_tooManyDecimalPlaces_rejected() {
        String response = new LogExpenseCommand("Kopi /cost 0.0001", expensesManager, budgetManager, TODAY).execute();

        assertTrue(response.contains("at most 2 decimal places"));
        assertEquals(0, expensesManager.getSize());
    }

    @Test
    public void execute_noBudget_noBudgetLine() {
        String response = new LogExpenseCommand("Teh /cost 1.20", expensesManager, budgetManager, TODAY).execute();
        assertTrue(!response.contains("Remaining"));
    }

    @Test
    public void execute_withinBudget_showsRemaining() throws Exception {
        budgetManager.setBudget(new Budget(BudgetPeriod.WEEKLY, 20));

        String response = new LogExpenseCommand("Laksa /cost 5", expensesManager, budgetManager, TODAY).execute();

        assertTrue(response.contains("Remaining this week: $15.00"));
    }

    @Test
    public void execute_overBudget_showsWarning() throws Exception {
        budgetManager.setBudget(new Budget(BudgetPeriod.DAILY, 5));

        String response = new LogExpenseCommand("Laksa /cost 6.50", expensesManager, budgetManager, TODAY).execute();

        assertTrue(response.contains("Warning: you are $1.50 over your daily budget!"));
    }
}
