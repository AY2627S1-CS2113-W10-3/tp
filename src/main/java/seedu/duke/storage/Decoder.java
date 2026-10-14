package seedu.duke.storage;

import java.util.ArrayList;
import java.util.List;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.expenses.Expense;
import seedu.duke.expenses.ExpensesManager;
import seedu.duke.ratings.RatingsManager;
import seedu.duke.ratings.Rating;

public class Decoder {

    // TODO: make <T> for Rating and Expenses function
    public static RatingsManager decodeRatings(List<String> encodedRatings) {
        final ArrayList<Rating> decodedRatings = new ArrayList<>();
        for (String encodedRating : encodedRatings) {
            if (!encodedRating.isBlank()) {
                decodedRatings.add(decodeRatingFromString(encodedRating));
            }
        }
        return new RatingsManager(decodedRatings);
    }

    private static Rating decodeRatingFromString(String encodedRating) {
        String[] fields = encodedRating.strip().split("\\|", -1);
        if (fields.length != 3) {
            throw new IllegalArgumentException(
                    "Invalid rating format. Expected: stall|food|score");
        }

        float score;
        try {
            score = Float.parseFloat(fields[2].strip());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Rating score must be a number.", exception);
        }

        return new Rating(fields[0].strip(), fields[1].strip(), score);
    }

    /**
     * Decodes the lines of the expenses storage file into an {@code ExpensesManager}.
     * Blank lines are skipped.
     *
     * @throws BudgetBiteException If any line is not a valid expense.
     */
    public static ExpensesManager decodeExpenses(List<String> encodedExpenses) throws BudgetBiteException {
        final ArrayList<Expense> decodedExpenses = new ArrayList<>();
        for (String encodedExpense : encodedExpenses) {
            if (!encodedExpense.isBlank()) {
                decodedExpenses.add(Expense.fromStorageString(encodedExpense.strip()));
            }
        }
        return new ExpensesManager(decodedExpenses);
    }
}
