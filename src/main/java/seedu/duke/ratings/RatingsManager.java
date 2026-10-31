package seedu.duke.ratings;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.duke.exceptions.BudgetBiteException;

/**
 * Manages the list of ratings made by the user.
 * Indexes given to the public methods start from 1, matching the numbers shown to the user.
 */
public class RatingsManager {
    private static final Logger LOGGER = Logger.getLogger(RatingsManager.class.getName());

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
     * @param decodedRatings Ratings to manage.
     */
    public RatingsManager(ArrayList<Rating> decodedRatings) {
        assert decodedRatings != null : "Ratings list should not be null";
        ratings = decodedRatings;
    }

    /**
     * Adds a rating to the end of the list.
     *
     * @param rating Rating to add.
     */
    public void addRating(Rating rating) {
        assert rating != null : "Rating should not be null";
        int sizeBefore = ratings.size();

        ratings.add(rating);

        assert ratings.size() == sizeBefore + 1 : "List should grow by one after adding";
        LOGGER.log(Level.FINE, "Added rating: " + rating);
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
     * Changes the score of the rating at the given index.
     * Since {@code Rating} is immutable, the old rating is replaced by a new one
     * with the same location, stall and food but the new score.
     *
     * @param index    Index of the rating, starting from 1.
     * @param newScore New score, between 0 and 10.
     * @return The updated rating.
     * @throws BudgetBiteException If the index is out of range or the score is invalid.
     */
    public Rating editRating(int index, float newScore) throws BudgetBiteException {
        Rating updatedRating = getRating(index).withScore(newScore);
        ratings.set(toZeroBased(index), updatedRating);
        LOGGER.log(Level.FINE, "Edited rating " + index + " to: " + updatedRating);
        return updatedRating;
    }

    /**
     * Deletes the rating at the given index.
     *
     * @param index Index of the rating, starting from 1.
     * @return The deleted rating.
     * @throws BudgetBiteException If the index is out of range.
     */
    public Rating deleteRating(int index) throws BudgetBiteException {
        checkIndex(index);
        int sizeBefore = ratings.size();

        Rating deletedRating = ratings.remove(toZeroBased(index));

        assert ratings.size() == sizeBefore - 1 : "List should shrink by one after deleting";
        LOGGER.log(Level.FINE, "Deleted rating: " + deletedRating);
        return deletedRating;
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

    /**
     * Returns the ratings as a numbered list, one per line, e.g. {@code "\n1. Stall: ..."}.
     */
    @Override
    public String toString() {
        StringBuilder ratingsText = new StringBuilder();
        for (int i = 0; i < ratings.size(); i++) {
            ratingsText.append("\n").append(i + 1).append(". ").append(ratings.get(i));
        }
        return ratingsText.toString();
    }

    private void checkIndex(int index) throws BudgetBiteException {
        if (ratings.isEmpty()) {
            LOGGER.log(Level.FINE, "Rejected rating index " + index + " because the list is empty");
            throw new BudgetBiteException("You have no ratings yet.");
        }
        boolean isInRange = index >= 1 && index <= ratings.size();
        if (!isInRange) {
            LOGGER.log(Level.FINE, "Rejected rating index " + index + " (list size " + ratings.size() + ")");
            throw new BudgetBiteException("Rating index must be between 1 and " + ratings.size() + ".");
        }
    }

    private int toZeroBased(int index) {
        return index - 1;
    }
}
