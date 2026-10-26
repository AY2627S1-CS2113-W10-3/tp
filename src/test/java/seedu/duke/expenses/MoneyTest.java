package seedu.duke.expenses;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.duke.exceptions.BudgetBiteException;

public class MoneyTest {
    private static final double DELTA = 0.001;

    @Test
    public void parse_validAmounts_parsed() throws BudgetBiteException {
        assertEquals(4.5, Money.parse("4.50", "Cost", 1000), DELTA);
        assertEquals(5, Money.parse(" 5 ", "Cost", 1000), DELTA);
        assertEquals(1000, Money.parse("1000", "Cost", 1000), DELTA);
        assertEquals(5, Money.parse("5.000", "Cost", 1000), DELTA);
    }

    @Test
    public void parse_tooManyDecimalPlaces_exceptionThrown() {
        BudgetBiteException e = assertThrows(BudgetBiteException.class,
                () -> Money.parse("0.0001", "Cost", 1000));
        assertTrue(e.getMessage().contains("at most 2 decimal places"));
    }

    @Test
    public void parse_aboveMaximum_exceptionThrown() {
        BudgetBiteException e = assertThrows(BudgetBiteException.class,
                () -> Money.parse("1000.01", "Cost", 1000));
        assertEquals("Cost cannot be more than $1000.00.", e.getMessage());
    }

    @Test
    public void parse_zeroOrNegative_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> Money.parse("0", "Cost", 1000));
        assertThrows(BudgetBiteException.class, () -> Money.parse("-3", "Cost", 1000));
    }

    @Test
    public void parse_notANumber_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> Money.parse("abc", "Budget", 1000));
        assertThrows(BudgetBiteException.class, () -> Money.parse("NaN", "Budget", 1000));
        assertThrows(BudgetBiteException.class, () -> Money.parse("", "Budget", 1000));
    }

    @Test
    public void format_twoDecimalPlaces() {
        assertEquals("$4.50", Money.format(4.5));
    }
}
