package seedu.duke.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.duke.expenses.Expense;
import seedu.duke.expenses.ExpensesManager;

public class ListExpensesCommandTest {

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
}
