package seedu.duke.commands;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.duke.exceptions.BudgetBiteException;

public class ExpenseArgumentsTest {

    @Test
    public void splitAtCost_validArguments_twoParts() throws BudgetBiteException {
        String[] parts = ExpenseArguments.splitAtCost("Chicken Rice /cost 4.50");

        assertArrayEquals(new String[] {"Chicken Rice", "4.50"}, parts);
    }

    @Test
    public void splitAtCost_noPrefix_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> ExpenseArguments.splitAtCost("Chicken Rice 4.50"));
    }

    @Test
    public void parseIndex_positiveNumber_parsed() throws BudgetBiteException {
        assertEquals(3, ExpenseArguments.parseIndex(" 3 "));
    }

    @Test
    public void parseIndex_invalidText_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> ExpenseArguments.parseIndex("abc"));
        assertThrows(BudgetBiteException.class, () -> ExpenseArguments.parseIndex("-1"));
        assertThrows(BudgetBiteException.class, () -> ExpenseArguments.parseIndex("0"));
        assertThrows(BudgetBiteException.class, () -> ExpenseArguments.parseIndex(""));
        assertThrows(BudgetBiteException.class, () -> ExpenseArguments.parseIndex("99999999999"));
    }
}
