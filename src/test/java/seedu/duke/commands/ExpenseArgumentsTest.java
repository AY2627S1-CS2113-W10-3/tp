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

    @Test
    public void splitAtDate_dateBeforeCost_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> ExpenseArguments.splitAtDate("Teh /date 2026-10-20 /cost 2"));
    }

    @Test
    public void splitAtCost_repeatedCost_exceptionThrown() {
        BudgetBiteException e = assertThrows(BudgetBiteException.class,
                () -> ExpenseArguments.splitAtCost("Kopi /cost 3 /cost 4"));
        assertEquals("/cost can only be given once.", e.getMessage());
    }

    @Test
    public void splitAtCost_misspeltPrefix_exceptionThrown() {
        BudgetBiteException e = assertThrows(BudgetBiteException.class,
                () -> ExpenseArguments.splitAtCost("Kopi /costs 3"));
        assertEquals("Unknown parameter /costs. Did you mean /cost?", e.getMessage());
    }

    @Test
    public void splitAtCost_noSpaceBeforeAmount_accepted() throws BudgetBiteException {
        assertArrayEquals(new String[] {"Kopi", "3"}, ExpenseArguments.splitAtCost("Kopi /cost3"));
    }

    @Test
    public void splitAtDate_repeatedDate_exceptionThrown() {
        assertThrows(BudgetBiteException.class,
                () -> ExpenseArguments.splitAtDate("Teh /cost 1 /date 2026-10-20 /date 2026-10-21"));
    }

    @Test
    public void parseDate_beforeYear2000_exceptionThrown() {
        LocalDate today = LocalDate.of(2026, 10, 26);
        BudgetBiteException e = assertThrows(BudgetBiteException.class,
                () -> ExpenseArguments.parseDate("1999-12-31", today));
        assertEquals("Date cannot be before 2000-01-01.", e.getMessage());
        assertEquals(LocalDate.of(2000, 1, 1), assertDoesNotThrowDate("2000-01-01", today));
    }

    private LocalDate assertDoesNotThrowDate(String text, LocalDate today) {
        try {
            return ExpenseArguments.parseDate(text, today);
        } catch (BudgetBiteException e) {
            throw new AssertionError("Date should be accepted: " + text);
        }
    }
}
