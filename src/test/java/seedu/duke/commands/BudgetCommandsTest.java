package seedu.duke.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.budget.BudgetManager;
import seedu.duke.budget.BudgetPeriod;
import seedu.duke.expenses.ExpensesManager;

public class BudgetCommandsTest {
    private static final LocalDate TODAY = LocalDate.of(2026, 10, 28);

    private BudgetManager budgetManager;
    private ExpensesManager expensesManager;

    @BeforeEach
    public void setUp() {
        budgetManager = new BudgetManager();
        expensesManager = new ExpensesManager();
    }

    @Test
    public void setBudget_valid_budgetSet() {
        String response = new SetBudgetCommand("weekly 50", budgetManager, expensesManager, TODAY).execute();

        assertTrue(budgetManager.hasBudget());
        assertEquals(BudgetPeriod.WEEKLY, budgetManager.getBudget().getPeriod());
        assertTrue(response.contains("Weekly budget: $50.00"));
        assertTrue(response.contains("Remaining this week: $50.00"));
    }

    @Test
    public void setBudget_upperCasePeriod_accepted() {
        new SetBudgetCommand("MONTHLY 300", budgetManager, expensesManager, TODAY).execute();
        assertEquals(BudgetPeriod.MONTHLY, budgetManager.getBudget().getPeriod());
    }

    @Test
    public void setBudget_invalidInputs_rejected() {
        String[] invalidInputs = {
                "", "weekly", "50", "yearly 50",
                "weekly abc", "weekly -5", "weekly 0.001", "weekly 50 extra"
        };        for (String input : invalidInputs) {
            String response = new SetBudgetCommand(input, budgetManager, expensesManager, TODAY).execute();
            assertTrue(response.contains(SetBudgetCommand.MESSAGE_USAGE), "No usage shown for: " + input);
        }
        assertFalse(budgetManager.hasBudget());
    }

    @Test
    public void setBudget_twice_replacesOldBudget() {
        new SetBudgetCommand("weekly 50", budgetManager, expensesManager, TODAY).execute();
        new SetBudgetCommand("daily 10", budgetManager, expensesManager, TODAY).execute();

        assertEquals(BudgetPeriod.DAILY, budgetManager.getBudget().getPeriod());
    }

    @Test
    public void budget_noBudgetSet_hint() {
        String response = new BudgetCommand(budgetManager, expensesManager, TODAY).execute();
        assertEquals(BudgetCommand.MESSAGE_NO_BUDGET, response);
    }
}
