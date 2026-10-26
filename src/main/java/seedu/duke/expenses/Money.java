package seedu.duke.expenses;

import java.math.BigDecimal;

import seedu.duke.exceptions.BudgetBiteException;

/**
 * Reads amounts of money typed by the user, such as costs and budgets.
 * Kept in one place so every amount follows the same rules:
 * a positive number, at most 2 decimal places, and not more than a given maximum.
 */
public final class Money {
    private static final int MAX_DECIMAL_PLACES = 2;

    private Money() {
        // Utility class; not meant to be instantiated.
    }

    /**
     * Parses the given text into an amount of money.
     *
     * @param amountText Text typed by the user, e.g. {@code "4.50"}.
     * @param label      What the amount is, used in error messages, e.g. {@code "Cost"}.
     * @param maxAmount  Largest amount allowed.
     * @return The amount as a number.
     * @throws BudgetBiteException If the text is not a valid amount.
     */
    public static double parse(String amountText, String label, double maxAmount) throws BudgetBiteException {
        assert label != null && !label.isBlank() : "Label should describe the amount";
        BigDecimal amount = toBigDecimal(amountText, label);

        if (amount.signum() <= 0) {
            throw new BudgetBiteException(label + " must be a positive number.");
        }
        // stripTrailingZeros() makes "4.50" count as 1 decimal place and "5.000" as 0.
        if (amount.stripTrailingZeros().scale() > MAX_DECIMAL_PLACES) {
            throw new BudgetBiteException(label + " can have at most 2 decimal places, e.g. 4.50");
        }
        checkNotAboveMax(amount.doubleValue(), label, maxAmount);
        return amount.doubleValue();
    }

    /**
     * Checks that an amount is positive and not more than the maximum.
     * Used when an amount is created directly in code rather than typed by the user.
     *
     * @throws BudgetBiteException If the amount is not positive or is above the maximum.
     */
    public static void validate(double amount, String label, double maxAmount) throws BudgetBiteException {
        boolean isPositiveNumber = amount > 0 && Double.isFinite(amount);
        if (!isPositiveNumber) {
            throw new BudgetBiteException(label + " must be a positive number.");
        }
        checkNotAboveMax(amount, label, maxAmount);
    }

    /**
     * Formats an amount the way it is shown to the user, e.g. {@code 4.5} becomes {@code "$4.50"}.
     */
    public static String format(double amount) {
        return String.format("$%.2f", amount);
    }

    private static BigDecimal toBigDecimal(String amountText, String label) throws BudgetBiteException {
        try {
            return new BigDecimal(amountText.trim());
        } catch (NumberFormatException e) {
            throw new BudgetBiteException(label + " must be a number, e.g. 4.50");
        }
    }

    private static void checkNotAboveMax(double amount, String label, double maxAmount) throws BudgetBiteException {
        if (amount > maxAmount) {
            throw new BudgetBiteException(label + " cannot be more than " + format(maxAmount) + ".");
        }
    }
}
