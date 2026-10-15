package seedu.duke.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.expenses.Expense;
import seedu.duke.expenses.ExpensesManager;
import seedu.duke.ratings.RatingsManager;
import seedu.duke.ratings.Rating;

public class Decoder {
    private static final Logger LOGGER = Logger.getLogger(Decoder.class.getName());

    // TODO: make <T> for Rating and Expenses function
    public static RatingsManager decodeRatings(List<String> encodedRatings) {
        final ArrayList<Rating> decodedRatings = new ArrayList<>();
        for (String encodedRating : encodedRatings) {
            if (encodedRating.isBlank()) {
                continue;
            }
            try {
                decodedRatings.add(decodeRatingFromString(encodedRating));
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "Skipped invalid rating line: " + encodedRating);
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
     * Blank lines and lines that are not valid expenses are skipped.
     */
    public static ExpensesManager decodeExpenses(List<String> encodedExpenses) {
        final ArrayList<Expense> decodedExpenses = new ArrayList<>();
        for (String encodedExpense : encodedExpenses) {
            if (encodedExpense.isBlank()) {
                continue;
            }
            try {
                decodedExpenses.add(Expense.fromStorageString(encodedExpense.strip()));
            } catch (BudgetBiteException e) {
                LOGGER.log(Level.WARNING, "Skipped invalid expense line: " + encodedExpense);
            }
        }
        return new ExpensesManager(decodedExpenses);
    }
}
