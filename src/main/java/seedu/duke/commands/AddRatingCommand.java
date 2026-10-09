package seedu.duke.commands;

import seedu.duke.ratings.Rating;
import seedu.duke.ratings.RatingsManager;

/**
 * Adds a new food rating.
 */
public class AddRatingCommand extends Command {
    public static final String COMMAND_WORD = "rate";
    public static final String USAGE = "rate /food FOOD /stall STALL /score SCORE [/notes NOTES]";

    private final RatingsManager ratings;
    private final Rating ratingToAdd;

    public AddRatingCommand(RatingsManager ratings, Rating ratingToAdd) {
        this.ratings = ratings;
        this.ratingToAdd = ratingToAdd;
    }

    @Override
    public String execute() {
        ratings.addRating(ratingToAdd);
        return "Got it. I've added this rating:\n"
                + "  " + ratingToAdd + "\n"
                + "You now have " + ratings.getSize() + " rating(s).";
    }
}
