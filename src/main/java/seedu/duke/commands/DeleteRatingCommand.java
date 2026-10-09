package seedu.duke.commands;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.ratings.Rating;
import seedu.duke.ratings.RatingsManager;

/**
 * Deletes a rating, identified by its index in {@code list-ratings}.
 */
public class DeleteRatingCommand extends Command {
    public static final String COMMAND_WORD = "delete-rating";
    public static final String USAGE = "delete-rating INDEX";

    private final RatingsManager ratings;
    private final int index;

    public DeleteRatingCommand(RatingsManager ratings, int index) {
        this.ratings = ratings;
        this.index = index;
    }

    @Override
    public String execute() {
        try {
            Rating deletedRating = ratings.deleteRating(index);
            return "Deleted rating " + index + ":\n"
                    + "  " + deletedRating + "\n"
                    + "You now have " + ratings.getSize() + " rating(s).";
        } catch (BudgetBiteException e) {
            return e.getMessage();
        }
    }
}
