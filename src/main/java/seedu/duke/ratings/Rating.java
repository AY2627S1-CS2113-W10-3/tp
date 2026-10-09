package seedu.duke.ratings;

import java.util.Objects;

import seedu.duke.exceptions.BudgetBiteException;

/**
 * Represents the user's rating of one food item from one stall.
 * A rating is immutable; editing a rating produces a new {@code Rating} object.
 */
public class Rating {
    public static final int MIN_SCORE = 0;
    public static final int MAX_SCORE = 10;

    private static final String STORAGE_SEPARATOR = "|";
    private static final String STORAGE_SEPARATOR_REGEX = "\\|";
    private static final int STORAGE_FIELD_COUNT = 4;
    private static final String NO_NOTES = "";

    private final String foodName;
    private final String stallName;
    private final int score;
    /** Free-text comments about the food. Empty if the user gave no notes. */
    private final String notes;

    /**
     * Creates a rating with notes.
     *
     * @param foodName  Name of the food rated.
     * @param stallName Name of the stall selling the food.
     * @param score     Score from {@value #MIN_SCORE} to {@value #MAX_SCORE}, inclusive.
     * @param notes     Comments about the food. May be empty, but not {@code null}.
     * @throws BudgetBiteException If any field is invalid.
     */
    public Rating(String foodName, String stallName, int score, String notes) throws BudgetBiteException {
        validateName(foodName, "Food name");
        validateName(stallName, "Stall name");
        validateScore(score);
        validateNotes(notes);
        this.foodName = foodName.trim();
        this.stallName = stallName.trim();
        this.score = score;
        this.notes = notes.trim();
    }

    /**
     * Creates a rating without notes.
     *
     * @see #Rating(String, String, int, String)
     */
    public Rating(String foodName, String stallName, int score) throws BudgetBiteException {
        this(foodName, stallName, score, NO_NOTES);
    }

    /**
     * Creates a rating from a line in the storage file, in the format {@code FOOD|STALL|SCORE|NOTES}.
     * The {@code NOTES} field is empty when the rating has no notes.
     *
     * @param storageLine Line read from the storage file.
     * @return The rating described by the line.
     * @throws BudgetBiteException If the line is not in the expected format.
     */
    public static Rating fromStorageString(String storageLine) throws BudgetBiteException {
        // A limit of -1 keeps the trailing empty NOTES field instead of discarding it.
        String[] fields = storageLine.split(STORAGE_SEPARATOR_REGEX, -1);
        if (fields.length != STORAGE_FIELD_COUNT) {
            throw new BudgetBiteException("Corrupted rating entry: " + storageLine);
        }
        return new Rating(fields[0], fields[1], parseScore(fields[2]), fields[3]);
    }

    /**
     * Parses the given text into a score. Only checks that the text is a whole number;
     * the range is checked when the rating is created.
     *
     * @param scoreText Text containing the score, e.g. {@code "8"}.
     * @return The score as a number.
     * @throws BudgetBiteException If the text is not a whole number.
     */
    public static int parseScore(String scoreText) throws BudgetBiteException {
        try {
            return Integer.parseInt(scoreText.trim());
        } catch (NumberFormatException e) {
            throw new BudgetBiteException("Score must be a whole number from " + MIN_SCORE + " to " + MAX_SCORE
                    + ", e.g. 8");
        }
    }

    public String getFoodName() {
        return foodName;
    }

    public String getStallName() {
        return stallName;
    }

    public int getScore() {
        return score;
    }

    public String getNotes() {
        return notes;
    }

    public boolean hasNotes() {
        return !notes.isEmpty();
    }

    /**
     * Returns a copy of this rating with its score replaced.
     *
     * @param newScore New score of the rating.
     * @return The updated rating.
     * @throws BudgetBiteException If the new score is out of range.
     */
    public Rating withScore(int newScore) throws BudgetBiteException {
        return new Rating(foodName, stallName, newScore, notes);
    }

    /**
     * Returns a copy of this rating with its notes replaced.
     *
     * @param newNotes New notes of the rating.
     * @return The updated rating.
     * @throws BudgetBiteException If the new notes are invalid.
     */
    public Rating withNotes(String newNotes) throws BudgetBiteException {
        return new Rating(foodName, stallName, score, newNotes);
    }

    /**
     * Returns true if this rating matches the given filters. Matching ignores case and
     * surrounding whitespace. A {@code null} filter matches every rating.
     *
     * @param foodFilter  Food name to match, or {@code null} to match any food.
     * @param stallFilter Stall name to match, or {@code null} to match any stall.
     * @return True if the rating matches both filters.
     */
    public boolean matches(String foodFilter, String stallFilter) {
        return matchesFilter(foodName, foodFilter) && matchesFilter(stallName, stallFilter);
    }

    /**
     * Returns this rating in the format used by the storage file.
     *
     * @return The rating as {@code FOOD|STALL|SCORE|NOTES}.
     */
    public String toStorageString() {
        return String.join(STORAGE_SEPARATOR, foodName, stallName, String.valueOf(score), notes);
    }

    @Override
    public String toString() {
        String ratingText = String.format("%s @ %s - %d/%d", foodName, stallName, score, MAX_SCORE);
        return hasNotes() ? ratingText + " | Notes: " + notes : ratingText;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Rating otherRating)) {
            return false;
        }
        return foodName.equals(otherRating.foodName)
                && stallName.equals(otherRating.stallName)
                && score == otherRating.score
                && notes.equals(otherRating.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodName, stallName, score, notes);
    }

    private static boolean matchesFilter(String value, String filter) {
        return filter == null || value.equalsIgnoreCase(filter.trim());
    }

    private static void validateName(String name, String fieldName) throws BudgetBiteException {
        if (name == null || name.isBlank()) {
            throw new BudgetBiteException(fieldName + " cannot be empty.");
        }
        if (name.contains(STORAGE_SEPARATOR)) {
            throw new BudgetBiteException(fieldName + " cannot contain '" + STORAGE_SEPARATOR + "'.");
        }
    }

    private static void validateScore(int score) throws BudgetBiteException {
        boolean isInRange = score >= MIN_SCORE && score <= MAX_SCORE;
        if (!isInRange) {
            throw new BudgetBiteException("Score must be between " + MIN_SCORE + " and " + MAX_SCORE
                    + " (inclusive).");
        }
    }

    private static void validateNotes(String notes) throws BudgetBiteException {
        if (notes == null) {
            throw new BudgetBiteException("Notes cannot be null.");
        }
        if (notes.contains(STORAGE_SEPARATOR)) {
            throw new BudgetBiteException("Notes cannot contain '" + STORAGE_SEPARATOR + "'.");
        }
    }
}
