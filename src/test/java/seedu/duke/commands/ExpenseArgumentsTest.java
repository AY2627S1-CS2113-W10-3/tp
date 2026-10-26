package seedu.duke.commands;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

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

    @Test
    public void splitAtDate_noDate_dateIsNull() throws Exception {
        String[] parts = ExpenseArguments.splitAtDate("Teh /cost 1.20");

        assertEquals("Teh /cost 1.20", parts[0]);
        assertNull(parts[1]);
    }

    @Test
    public void splitAtDate_withDate_split() throws Exception {
        String[] parts = ExpenseArguments.splitAtDate("Teh /cost 1.20 /date 2026-10-20");

        assertEquals("Teh /cost 1.20", parts[0]);
        assertEquals("2026-10-20", parts[1]);
    }

    @Test
    public void parseDate_today_accepted() throws Exception {
        LocalDate today = LocalDate.of(2026, 10, 26);
        assertEquals(today, ExpenseArguments.parseDate("2026-10-26", today));
    }
}
