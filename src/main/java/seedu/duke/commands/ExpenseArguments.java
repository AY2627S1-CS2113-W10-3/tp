package seedu.duke.commands;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import seedu.duke.exceptions.BudgetBiteException;

/**
 * Parses the arguments shared by the expense commands.
 * Kept in one place so every expense command reads input the same way.
 */
public final class ExpenseArguments {
    public static final String COST_PREFIX = "/cost";
    public static final String DATE_PREFIX = "/date";

    private static final String WHOLE_NUMBER_REGEX = "\\d+";

    private ExpenseArguments() {
        // Utility class; not meant to be instantiated.
    }

    /**
     * Splits arguments such as {@code "Chicken Rice /cost 4.50"} into the text before
     * {@code /cost} and the text after it.
     *
     * @param arguments Arguments typed after the command word.
     * @return A two-element array: the text before {@code /cost}, then the cost text.
     * @throws BudgetBiteException If {@code /cost} or the amount after it is missing.
     */
    public static String[] splitAtCost(String arguments) throws BudgetBiteException {
        int prefixPosition = arguments.indexOf(COST_PREFIX);
        if (prefixPosition < 0) {
            throw new BudgetBiteException("Missing " + COST_PREFIX + ".");
        }

        String beforeCost = arguments.substring(0, prefixPosition).trim();
        String costText = arguments.substring(prefixPosition + COST_PREFIX.length()).trim();
        if (costText.isEmpty()) {
            throw new BudgetBiteException("Missing amount after " + COST_PREFIX + ".");
        }
        return new String[] {beforeCost, costText};
    }

    /**
     * Parses an expense index as shown in {@code list-expenses}.
     *
     * @param indexText Text that should hold a positive whole number.
     * @return The index, starting from 1.
     * @throws BudgetBiteException If the text is not a positive whole number.
     */
    public static int parseIndex(String indexText) throws BudgetBiteException {
        String trimmedText = indexText.trim();
        if (!trimmedText.matches(WHOLE_NUMBER_REGEX)) {
            throw new BudgetBiteException("Index must be a positive whole number, e.g. 1.");
        }

        try {
            int index = Integer.parseInt(trimmedText);
            if (index < 1) {
                throw new BudgetBiteException("Index must be a positive whole number, e.g. 1.");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new BudgetBiteException("Index is too large.");
        }
    }

    /**
     * Splits off an optional {@code /date} at the end of the arguments, e.g.
     * {@code "Chicken Rice /cost 4.50 /date 2026-10-20"} becomes
     * {@code "Chicken Rice /cost 4.50"} and {@code "2026-10-20"}.
     *
     * @param arguments Arguments typed after the command word.
     * @return A two-element array: the text before {@code /date}, then the date text,
     *         which is {@code null} if no {@code /date} was given.
     * @throws BudgetBiteException If {@code /date} is given without a date after it, or before {@code /cost}.
     */
    public static String[] splitAtDate(String arguments) throws BudgetBiteException {
        int prefixPosition = arguments.indexOf(DATE_PREFIX);
        if (prefixPosition < 0) {
            return new String[] {arguments.trim(), null};
        }

        String beforeDate = arguments.substring(0, prefixPosition).trim();
        String dateText = arguments.substring(prefixPosition + DATE_PREFIX.length()).trim();
        if (dateText.isEmpty()) {
            throw new BudgetBiteException("Missing date after " + DATE_PREFIX + ".");
        }
        if (dateText.contains(COST_PREFIX)) {
            throw new BudgetBiteException(DATE_PREFIX + " must come after " + COST_PREFIX + ".");
        }
        return new String[] {beforeDate, dateText};
    }

    /**
     * Parses a date in the format {@code YYYY-MM-DD}. The date cannot be in the future.
     *
     * @param dateText Text that should hold a date, e.g. {@code "2026-10-20"}.
     * @param today    Today's date, passed in so that tests can fix it.
     * @return The date.
     * @throws BudgetBiteException If the text is not a real date in that format, or is after today.
     */
    public static LocalDate parseDate(String dateText, LocalDate today) throws BudgetBiteException {
        LocalDate date;
        try {
            // LocalDate.parse only accepts YYYY-MM-DD and rejects dates that do not exist, e.g. 2026-02-30.
            date = LocalDate.parse(dateText.trim());
        } catch (DateTimeParseException e) {
            throw new BudgetBiteException("Date must be a real date in the format YYYY-MM-DD, e.g. 2026-10-20.");
        }
        if (date.isAfter(today)) {
            throw new BudgetBiteException("Date cannot be in the future.");
        }
        return date;
    }
}
