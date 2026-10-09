package seedu.duke.storage;

import java.util.ArrayList;
import java.util.List;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.expenses.Expense;
import seedu.duke.expenses.ExpensesManager;
import seedu.duke.ratings.Rating;
import seedu.duke.ratings.RatingsManager;

public class Decoder {

    /**
     * Decodes the lines of the ratings storage file into a {@code RatingsManager}.
     * Blank lines are skipped.
     *
     * @throws BudgetBiteException If any line is not a valid rating.
     */
    public static RatingsManager decodeRatings(List<String> encodedRatings) throws BudgetBiteException {
        final ArrayList<Rating> decodedRatings = new ArrayList<>();
        for (String encodedRating : encodedRatings) {
            if (!encodedRating.isBlank()) {
                decodedRatings.add(Rating.fromStorageString(encodedRating.strip()));
            }
        }
        return new RatingsManager(decodedRatings);
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
