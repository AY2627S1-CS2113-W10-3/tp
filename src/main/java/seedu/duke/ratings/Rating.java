package seedu.duke.ratings;

import java.util.Objects;

import seedu.duke.exceptions.BudgetBiteException;

/**
 * A score from 0 to 10 that the user gave to one dish at a stall in a food court.
 */
public class Rating {
    /** Lowest score allowed. */
    public static final float MIN_SCORE = 0;
    /** Highest score allowed. */
    public static final float MAX_SCORE = 10;
    /**
     * Location given to ratings saved by older versions, which did not store the food court.
     * Such ratings are kept so the user does not lose them.
     */
    public static final String UNKNOWN_LOCATION = "Unknown location";

    private static final String STORAGE_SEPARATOR = "|";
    private static final String STORAGE_SEPARATOR_REGEX = "\\|";
    private static final int OLD_STORAGE_FIELD_COUNT = 3;
    private static final int STORAGE_FIELD_COUNT = 4;

    private final String location;
    private final String stall;
    private final String food;
    private final float score;

    /**
     * Creates a rating.
     *
     * @param location Food court the stall is in, e.g. {@code "PGPR"}.
     * @param stall    Stall the food is from.
     * @param food     Dish being rated.
     * @param score    Score from 0 to 10.
     * @throws BudgetBiteException If a name is blank or contains '|', or the score is out of range.
     */
    public Rating(String location, String stall, String food, float score) throws BudgetBiteException {
        validateName(location, "Location");
        validateName(stall, "Stall");
        validateName(food, "Food");
        // NaN fails every comparison, so it has to be checked on its own.
        if (Float.isNaN(score) || score < MIN_SCORE || score > MAX_SCORE) {
            throw new BudgetBiteException("Score must be between 0 and 10.");
        }

        this.location = location.trim();
        this.stall = stall.trim();
        this.food = food.trim();
        this.score = score;
    }

    /**
     * Creates a rating from its line in the storage file, in the format {@code LOCATION|STALL|FOOD|SCORE}.
     * Lines saved by older versions have no location ({@code STALL|FOOD|SCORE});
     * they are given {@link #UNKNOWN_LOCATION}.
     *
     * @param storageLine Line read from the storage file, e.g. {@code PGPR|Chicken Rice|Roast Chicken Rice|8.0}.
     * @return The rating described by the line.
     * @throws BudgetBiteException If the line is not in the expected format.
     */
    public static Rating fromStorageString(String storageLine) throws BudgetBiteException {
        String[] fields = storageLine.split(STORAGE_SEPARATOR_REGEX, -1);
        if (fields.length == OLD_STORAGE_FIELD_COUNT) {
            return new Rating(UNKNOWN_LOCATION, fields[0], fields[1], parseStoredScore(fields[2], storageLine));
        }
        if (fields.length != STORAGE_FIELD_COUNT) {
            throw new BudgetBiteException("Corrupted rating entry: " + storageLine);
        }
        return new Rating(fields[0], fields[1], fields[2], parseStoredScore(fields[3], storageLine));
    }

    public String getLocation() {
        return location;
    }

    public String getStall() {
        return stall;
    }

    public String getFood() {
        return food;
    }

    public float getScore() {
        return score;
    }

    /**
     * Returns a copy of this rating with its score replaced. The location, stall and food stay the same.
     *
     * @param newScore New score from 0 to 10.
     * @return The updated rating.
     * @throws BudgetBiteException If the new score is out of range.
     */
    public Rating withScore(float newScore) throws BudgetBiteException {
        return new Rating(location, stall, food, newScore);
    }

    /**
     * Returns true if both ratings are for the same dish, i.e. the same location, stall and food,
     * ignoring letter case. The scores do not matter.
     *
     * @param other Rating to compare with.
     */
    public boolean isSameDish(Rating other) {
        return location.equalsIgnoreCase(other.location)
                && stall.equalsIgnoreCase(other.stall)
                && food.equalsIgnoreCase(other.food);
    }

    /**
     * Returns the score as shown to the user, e.g. {@code 8.0/10}.
     */
    public String formatScore() {
        return String.format("%.1f/10", score);
    }

    /**
     * Returns the rating as shown to the user, e.g. {@code Roast Chicken Rice (Chicken Rice, PGPR) - 8.0/10}.
     */
    @Override
    public String toString() {
        return food + " (" + stall + ", " + location + ") - " + formatScore();
    }

    /**
     * Returns this rating in the format used by the storage file,
     * e.g. {@code PGPR|Chicken Rice|Roast Chicken Rice|8.0}.
     */
    public String toStorageString() {
        return String.join(STORAGE_SEPARATOR, location, stall, food, String.format("%.1f", score));
    }

    /**
     * Two ratings are equal if they have the same location, stall, food and score.
     * Without this, {@code equals} would only be true for the exact same object in memory.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Rating otherRating)) {
            return false;
        }
        return location.equals(otherRating.location)
                && stall.equals(otherRating.stall)
                && food.equals(otherRating.food)
                && Float.compare(score, otherRating.score) == 0;
    }

    /**
     * Must be overridden together with {@code equals} so that equal ratings
     * also behave correctly in hash-based collections such as {@code HashSet}.
     */
    @Override
    public int hashCode() {
        return Objects.hash(location, stall, food, score);
    }

    /**
     * Checks that a name is not blank and does not contain the storage separator,
     * which would break the line when it is saved.
     */
    private static void validateName(String name, String label) throws BudgetBiteException {
        if (name == null || name.isBlank()) {
            throw new BudgetBiteException(label + " name cannot be blank.");
        }
        if (name.contains(STORAGE_SEPARATOR)) {
            throw new BudgetBiteException(label + " name cannot contain '" + STORAGE_SEPARATOR + "'.");
        }
    }

    private static float parseStoredScore(String scoreText, String storageLine) throws BudgetBiteException {
        try {
            return Float.parseFloat(scoreText.trim());
        } catch (NumberFormatException e) {
            throw new BudgetBiteException("Corrupted rating score: " + storageLine);
        }
    }
}
