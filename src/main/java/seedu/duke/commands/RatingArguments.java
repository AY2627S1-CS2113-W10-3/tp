package seedu.duke.commands;

import java.math.BigDecimal;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.ratings.Rating;

/**
 * Parses the arguments shared by the rating commands.
 * Kept in one place so every rating command reads input the same way.
 * Indexes are parsed with {@link ExpenseArguments#parseIndex(String)}, since the rules are the same.
 */
public final class RatingArguments {
    public static final String SCORE_PREFIX = "/score";

    private static final int MAX_DECIMAL_PLACES = 1;
    /** Plain decimal numbers only, e.g. 7.5 or -3. Rejects forms such as NaN, 5f or 1e1 that Java would accept. */
    private static final String PLAIN_NUMBER_REGEX = "-?\\d+(\\.\\d+)?";
    private static final String MESSAGE_INVALID_SCORE =
            "Score must be a number from 0 to 10 with at most 1 decimal place, e.g. 7.5";

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
     * Parses the given text into a score from 0 to 10 with at most 1 decimal place.
     * Limiting it to 1 decimal place means the score shown and saved is exactly what the user typed.
     *
     * @param scoreText Text containing the score, e.g. {@code "4.5"}.
     * @return The score as a number.
     * @throws BudgetBiteException If the text is not such a number.
     */
    public static float parseScore(String scoreText) throws BudgetBiteException {
        String trimmedText = scoreText.trim();
        if (!trimmedText.matches(PLAIN_NUMBER_REGEX)) {
            throw new BudgetBiteException(MESSAGE_INVALID_SCORE);
        }

        BigDecimal score = new BigDecimal(trimmedText);
        // stripTrailingZeros() makes "7.50" count as 1 decimal place.
        if (score.stripTrailingZeros().scale() > MAX_DECIMAL_PLACES) {
            throw new BudgetBiteException(MESSAGE_INVALID_SCORE);
        }
        boolean isInRange = score.compareTo(BigDecimal.valueOf(Rating.MIN_SCORE)) >= 0
                && score.compareTo(BigDecimal.valueOf(Rating.MAX_SCORE)) <= 0;
        if (!isInRange) {
            throw new BudgetBiteException("Score must be between 0 and 10.");
        }
        return score.floatValue();
    }
}
