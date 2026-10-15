package seedu.duke.commands;

import seedu.duke.exceptions.BudgetBiteException;

/**
 * Parses the arguments shared by the expense commands.
 * Kept in one place so every expense command reads input the same way.
 */
public final class ExpenseArguments {
    public static final String COST_PREFIX = "/cost";

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
}
