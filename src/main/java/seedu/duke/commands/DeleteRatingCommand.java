package seedu.duke.commands;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.ratings.Rating;
import seedu.duke.ratings.RatingsManager;

/**
 * Deletes a rating, e.g. {@code delete-rating 2}.
 */
public class DeleteRatingCommand extends Command {
    public static final String COMMAND_WORD = "delete-rating";
    public static final String MESSAGE_USAGE = "Format: delete-rating INDEX\n"
            + "Example: delete-rating 1";

    private final String arguments;
    private final RatingsManager ratingsManager;

    /**
     * Creates the command with the text typed after {@code delete-rating}.
     *
     * @param arguments Text after the command word.
     * @param ratingsManager Manager holding the rating to delete.
     */
    public DeleteRatingCommand(String arguments, RatingsManager ratingsManager) {
        assert ratingsManager != null : "Ratings manager should not be null";
        this.arguments = arguments == null ? "" : arguments.trim();
        this.ratingsManager = ratingsManager;
    }

    @Override
    public String execute() {
        try {
            int index = ExpenseArguments.parseIndex(arguments);
            Rating deletedRating = ratingsManager.deleteRating(index);
            return "Deleted: " + deletedRating + "\n"
                    + "Ratings left: " + ratingsManager.getSize();
        } catch (BudgetBiteException e) {
            return e.getMessage() + "\n" + MESSAGE_USAGE;
        }
    }
}
