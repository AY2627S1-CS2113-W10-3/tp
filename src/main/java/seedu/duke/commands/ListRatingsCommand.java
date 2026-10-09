package seedu.duke.commands;

import java.util.ArrayList;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.ratings.RatingsManager;

/**
 * Lists the user's ratings, optionally filtered by food and/or stall.
 * Each rating is shown with its position in the full list, so the number shown can be passed
 * to {@code edit-rating} or {@code delete-rating} even when the list is filtered.
 */
public class ListRatingsCommand extends Command {
    public static final String COMMAND_WORD = "list-ratings";
    public static final String USAGE = "list-ratings [/food FOOD] [/stall STALL]";

    private final RatingsManager ratings;
    /** Food name to match, or {@code null} to show ratings for any food. */
    private final String foodFilter;
    /** Stall name to match, or {@code null} to show ratings for any stall. */
    private final String stallFilter;

    public ListRatingsCommand(RatingsManager ratings, String foodFilter, String stallFilter) {
        this.ratings = ratings;
        this.foodFilter = foodFilter;
        this.stallFilter = stallFilter;
    }

    @Override
    public String execute() {
        if (ratings.isEmpty()) {
            return "You have no ratings yet. Add one with:\n  " + AddRatingCommand.USAGE;
        }

        ArrayList<Integer> matchingIndexes = ratings.findRatingIndexes(foodFilter, stallFilter);
        if (matchingIndexes.isEmpty()) {
            return "No ratings found matching " + describeFilters() + ".";
        }

        StringBuilder response = new StringBuilder(isFiltered()
                ? "Here are your ratings matching " + describeFilters() + ":"
                : "Here are all your ratings:");
        try {
            for (int index : matchingIndexes) {
                response.append("\n").append(index).append(". ").append(ratings.getRating(index));
            }
        } catch (BudgetBiteException e) {
            // Unreachable: every index came from findRatingIndexes on the same list.
            throw new AssertionError("Index from findRatingIndexes should be valid", e);
        }
        return response.toString();
    }

    private boolean isFiltered() {
        return foodFilter != null || stallFilter != null;
    }

    /**
     * Describes the active filters, e.g. {@code food "Chicken Rice" and stall "Indian"}.
     */
    private String describeFilters() {
        ArrayList<String> descriptions = new ArrayList<>();
        if (foodFilter != null) {
            descriptions.add("food \"" + foodFilter + "\"");
        }
        if (stallFilter != null) {
            descriptions.add("stall \"" + stallFilter + "\"");
        }
        return String.join(" and ", descriptions);
    }
}
