package seedu.duke.expenses;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.commands.DeleteExpenseCommand;
import seedu.duke.commands.EditExpenseCommand;

public class EditDeleteExpenseCommandTest {
    private static final double DELTA = 0.001;

    private ExpensesManager expensesManager;

    @BeforeEach
    public void setUp() throws Exception {
        expensesManager = new ExpensesManager();
        expensesManager.addExpense(new Expense("Chicken Rice", 4.50));
        expensesManager.addExpense(new Expense("Teh", 1.20));
    }

    @Test
    public void editExpense_validInput_costChanged() throws Exception {
        String response = new EditExpenseCommand("1 /cost 5.00", expensesManager).execute();

        assertEquals(5.00, expensesManager.getExpense(1).getCost(), DELTA);
        assertTrue(response.contains("Updated expense 1: Chicken Rice - $5.00"));
    }

    @Test
    public void editExpense_indexOutOfRange_errorShown() throws Exception {
        String response = new EditExpenseCommand("3 /cost 5.00", expensesManager).execute();

        assertTrue(response.contains("Expense index must be between 1 and 2."));
        assertEquals(4.50, expensesManager.getExpense(1).getCost(), DELTA);
    }

    @Test
    public void editExpense_missingCost_errorShown() {
        String response = new EditExpenseCommand("1", expensesManager).execute();

        assertTrue(response.contains("Missing /cost."));
    }

    @Test
    public void deleteExpense_validIndex_expenseRemoved() throws Exception {
        String response = new DeleteExpenseCommand("1", expensesManager).execute();

        assertEquals(1, expensesManager.getSize());
        assertEquals("Teh", expensesManager.getExpense(1).getFoodName());
        assertTrue(response.contains("Deleted: Chicken Rice - $4.50"));
    }

    @Test
    public void deleteExpense_notANumber_errorShownAndNothingRemoved() {
        String response = new DeleteExpenseCommand("first", expensesManager).execute();

        assertEquals(2, expensesManager.getSize());
        assertTrue(response.contains("Index must be a positive whole number"));
    }

    @Test
    public void deleteExpense_emptyList_errorShown() {
        String response = new DeleteExpenseCommand("1", new ExpensesManager()).execute();

        assertTrue(response.contains("You have no expenses yet."));
    }
}
