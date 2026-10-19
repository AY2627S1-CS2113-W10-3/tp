package seedu.duke.commands;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.ratings.Rating;
import seedu.duke.ratings.RatingsManager;

/**
 * Changes the score of a food item you already rated, e.g. {@code edit-rating 1 /score 4.5}.
 */
public class EditRatingsCommand extends Command {
    public static final String COMMAND_WORD = "edit-rating";
    public static final String MESSAGE_USAGE = "Format: edit-rating INDEX /score RATING\n"
            + "Example: edit-rating 1 /score 4.5";

    private final String arguments;
    private final RatingsManager ratingsManager;

    /**
     * Creates the command with the text typed after {@code edit-rating}.
     *
     * @param arguments Text after the command word.
     * @param ratingsManager Manager holding the rating to edit.
     */
    public EditRatingsCommand(String arguments, RatingsManager ratingsManager) {
        assert ratingsManager != null : "Ratings manager should not be null";
        this.arguments = arguments == null ? "" : arguments.trim();
        this.ratingsManager = ratingsManager;
    }

    @Override
    public String execute() {
        try {
            String[] parts = RatingArguments.splitAtScore(arguments);
            int index = ExpenseArguments.parseIndex(parts[0]);
            float newScore = RatingArguments.parseScore(parts[1]);

            Rating updatedRating = ratingsManager.editRating(index, newScore);
            return "Updated rating " + index + ": " + updatedRating;
        } catch (BudgetBiteException e) {
            return e.getMessage() + "\n" + MESSAGE_USAGE;
        }
    }
}
