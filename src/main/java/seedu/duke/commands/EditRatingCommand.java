package seedu.duke.commands;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.ratings.Rating;
import seedu.duke.ratings.RatingsManager;

/**
 * Edits the score and/or notes of an existing rating, identified by its index in {@code list-ratings}.
 * Fields that are not given are left unchanged.
 */
public class EditRatingCommand extends Command {
    public static final String COMMAND_WORD = "edit-rating";
    public static final String USAGE = "edit-rating INDEX [/score SCORE] [/notes NOTES]";

    private final RatingsManager ratings;
    private final int index;
    /** New score, or {@code null} to keep the current score. */
    private final Integer newScore;
    /** New notes, or {@code null} to keep the current notes. */
    private final String newNotes;

    public EditRatingCommand(RatingsManager ratings, int index, Integer newScore, String newNotes) {
        this.ratings = ratings;
        this.index = index;
        this.newScore = newScore;
        this.newNotes = newNotes;
    }

    @Override
    public String execute() {
        try {
            Rating updatedRating = ratings.editRating(index, newScore, newNotes);
            return "Updated rating " + index + ":\n  " + updatedRating;
        } catch (BudgetBiteException e) {
            return e.getMessage();
        }
    }
}
