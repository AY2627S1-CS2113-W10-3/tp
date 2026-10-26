package seedu.duke.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.expenses.ExpensesManager;

public class LogExpenseCommandTest {
    private ExpensesManager expensesManager;

    @BeforeEach
    public void setUp() {
        expensesManager = new ExpensesManager();
    }

    @Test
    public void execute_validInput_expenseAdded() throws Exception {
        String response = new LogExpenseCommand("Chicken Rice /cost 4.50", expensesManager).execute();

        assertEquals(1, expensesManager.getSize());
        assertEquals("Chicken Rice", expensesManager.getExpense(1).getFoodName());
        assertTrue(response.contains("Logged: Chicken Rice - $4.50"));
        assertTrue(response.contains("Total spent: $4.50"));
    }

    @Test
    public void execute_missingCostPrefix_errorShownAndNothingAdded() {
        String response = new LogExpenseCommand("Chicken Rice 4.50", expensesManager).execute();

        assertEquals(0, expensesManager.getSize());
        assertTrue(response.contains("Missing /cost."));
        assertTrue(response.contains(LogExpenseCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_missingAmount_errorShown() {
        String response = new LogExpenseCommand("Chicken Rice /cost", expensesManager).execute();

        assertEquals(0, expensesManager.getSize());
        assertTrue(response.contains("Missing amount after /cost."));
    }

    @Test
    public void execute_zeroCost_errorShown() {
        String response = new LogExpenseCommand("Teh /cost 0", expensesManager).execute();

        assertEquals(0, expensesManager.getSize());
        assertTrue(response.contains("Cost must be a positive number."));
    }

    @Test
    public void execute_costNotANumber_errorShown() {
        String response = new LogExpenseCommand("Teh /cost abc", expensesManager).execute();

        assertEquals(0, expensesManager.getSize());
        assertTrue(response.contains("Cost must be a number"));
    }

    @Test
    public void execute_blankFoodName_errorShown() {
        String response = new LogExpenseCommand("/cost 4.50", expensesManager).execute();

        assertEquals(0, expensesManager.getSize());
        assertTrue(response.contains("Food name cannot be empty."));
    }
}
