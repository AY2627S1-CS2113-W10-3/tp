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

    /**
     * Decodes the lines of the ratings storage file into a {@code RatingsManager}.
     * Blank lines and lines that are not valid ratings are skipped.
     */
    public static RatingsManager decodeRatings(List<String> encodedRatings) {
        final ArrayList<Rating> decodedRatings = new ArrayList<>();
        for (String encodedRating : encodedRatings) {
            if (encodedRating.isBlank()) {
                continue;
            }
            try {
                decodedRatings.add(Rating.fromStorageString(encodedRating.strip()));
            } catch (BudgetBiteException e) {
                LOGGER.log(Level.WARNING, "Skipped invalid rating line: " + encodedRating);
            }
        }
        return new RatingsManager(decodedRatings);
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
