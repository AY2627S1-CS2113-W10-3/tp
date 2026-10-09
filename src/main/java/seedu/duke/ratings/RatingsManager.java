package seedu.duke.ratings;

import java.util.ArrayList;

import seedu.duke.exceptions.BudgetBiteException;

/**
 * Manages the list of food ratings recorded by the user.
 * Indexes given to and returned by the public methods start from 1, matching the numbers shown to the user.
 */
public class RatingsManager {
    private final ArrayList<Rating> ratings;

    /**
     * Creates a manager with no ratings.
     */
    public RatingsManager() {
        this(new ArrayList<>());
    }

    /**
     * Creates a manager holding the given ratings, e.g. those loaded from the storage file.
     *
     * @param ratings Ratings to manage.
     */
    public RatingsManager(ArrayList<Rating> ratings) {
        assert ratings != null : "Ratings list should not be null";
        this.ratings = ratings;
    }

    /**
     * Adds a rating to the end of the list.
     *
     * @param rating Rating to add.
     */
    public void addRating(Rating rating) {
        assert rating != null : "Rating to add should not be null";
        ratings.add(rating);
    }

    /**
     * Returns the rating at the given index.
     *
     * @param index Index of the rating, starting from 1.
     * @return The rating at that index.
     * @throws BudgetBiteException If the index is out of range.
     */
    public Rating getRating(int index) throws BudgetBiteException {
        checkIndex(index);
        return ratings.get(toZeroBased(index));
    }

    /**
     * Edits the score and/or notes of the rating at the given index. Fields given as {@code null}
     * are left unchanged. The rating is only replaced if every new value is valid, so a failed
     * edit never leaves the rating half-updated.
     *
     * @param index    Index of the rating, starting from 1.
     * @param newScore New score, or {@code null} to keep the current score.
     * @param newNotes New notes, or {@code null} to keep the current notes.
     * @return The updated rating.
     * @throws BudgetBiteException If the index is out of range or a new value is invalid.
     */
    public Rating editRating(int index, Integer newScore, String newNotes) throws BudgetBiteException {
        Rating updatedRating = getRating(index);
        if (newScore != null) {
            updatedRating = updatedRating.withScore(newScore);
        }
        if (newNotes != null) {
            updatedRating = updatedRating.withNotes(newNotes);
        }
        ratings.set(toZeroBased(index), updatedRating);
        return updatedRating;
    }

    /**
     * Deletes the rating at the given index. Ratings after it move up by one place.
     *
     * @param index Index of the rating, starting from 1.
     * @return The deleted rating.
     * @throws BudgetBiteException If the index is out of range.
     */
    public Rating deleteRating(int index) throws BudgetBiteException {
        checkIndex(index);
        return ratings.remove(toZeroBased(index));
    }

    /**
     * Returns the indexes of the ratings matching the given filters, in list order.
     * Indexes are returned (rather than the ratings themselves) so that a filtered list can show
     * the same numbers that {@code edit-rating} and {@code delete-rating} expect.
     *
     * @param foodFilter  Food name to match, or {@code null} to match any food.
     * @param stallFilter Stall name to match, or {@code null} to match any stall.
     * @return Indexes of the matching ratings, starting from 1.
     * @see Rating#matches(String, String)
     */
    public ArrayList<Integer> findRatingIndexes(String foodFilter, String stallFilter) {
        ArrayList<Integer> matchingIndexes = new ArrayList<>();
        for (int i = 0; i < ratings.size(); i++) {
            if (ratings.get(i).matches(foodFilter, stallFilter)) {
                matchingIndexes.add(i + 1);
            }
        }
        return matchingIndexes;
    }

    public int getSize() {
        return ratings.size();
    }

    public boolean isEmpty() {
        return ratings.isEmpty();
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    private void checkIndex(int index) throws BudgetBiteException {
        if (ratings.isEmpty()) {
            throw new BudgetBiteException("You have no ratings yet.");
        }
        boolean isInRange = index >= 1 && index <= ratings.size();
        if (!isInRange) {
            throw new BudgetBiteException("Rating index must be between 1 and " + ratings.size() + ".");
        }
    }

    private int toZeroBased(int index) {
        return index - 1;
    }
}
