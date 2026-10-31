package seedu.duke.commands;

import seedu.duke.ratings.RatingsManager;

/**
 * Lists every rating with its index, stall, food and score.
 */
public class ListRatingsCommand extends Command {
    public static final String COMMAND_WORD = "list-ratings";

    private final RatingsManager ratingsManager;

    /**
     * Creates the command.
     *
     * @param ratingsManager Manager whose ratings are listed.
     */
    public ListRatingsCommand(RatingsManager ratingsManager) {
        assert ratingsManager != null : "Ratings manager should not be null";
        this.ratingsManager = ratingsManager;
    }

    @Override
    public String execute() {
        if (ratingsManager.isEmpty()) {
            return "No ratings yet. Try: rate /location PGPR /stall Chicken Rice /food Roast Chicken Rice /score 8";
        }
        return "Your ratings:" + ratingsManager;
    }
}
