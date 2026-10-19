package seedu.duke.commands;

import seedu.duke.exceptions.BudgetBiteException;

/**
 * Parses the arguments shared by the rating commands.
 * Kept in one place so every rating command reads input the same way.
 * Indexes are parsed with {@link ExpenseArguments#parseIndex(String)}, since the rules are the same.
 */
public final class RatingArguments {
    public static final String SCORE_PREFIX = "/score";

    private RatingArguments() {
        // Utility class; not meant to be instantiated.
    }

    /**
     * Splits arguments such as {@code "2 /score 4.5"} into the text before
     * {@code /score} and the text after it.
     *
     * @param arguments Arguments typed after the command word.
     * @return A two-element array: the text before {@code /score}, then the score text.
     * @throws BudgetBiteException If {@code /score} or the value after it is missing.
     */
    public static String[] splitAtScore(String arguments) throws BudgetBiteException {
        int prefixPosition = arguments.indexOf(SCORE_PREFIX);
        if (prefixPosition < 0) {
            throw new BudgetBiteException("Missing " + SCORE_PREFIX + ".");
        }

        String beforeScore = arguments.substring(0, prefixPosition).trim();
        String scoreText = arguments.substring(prefixPosition + SCORE_PREFIX.length()).trim();
        if (scoreText.isEmpty()) {
            throw new BudgetBiteException("Missing value after " + SCORE_PREFIX + ".");
        }
        return new String[] {beforeScore, scoreText};
    }

    /**
     * Parses the given text into a score. The 0 to 10 range is checked later by {@code Rating}.
     *
     * @param scoreText Text containing the score, e.g. {@code "4.5"}.
     * @return The score as a number.
     * @throws BudgetBiteException If the text is not a number.
     */
    public static float parseScore(String scoreText) throws BudgetBiteException {
        try {
            return Float.parseFloat(scoreText.trim());
        } catch (NumberFormatException e) {
            throw new BudgetBiteException("Score must be a number between 0 and 10, e.g. 4.5");
        }
    }
}
