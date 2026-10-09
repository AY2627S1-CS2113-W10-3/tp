package seedu.duke.storage;

import java.util.ArrayList;
import java.util.List;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.expenses.Expense;
import seedu.duke.expenses.ExpensesManager;
import seedu.duke.ratings.RatingsManager;

public class Decoder {

    // TODO: make <T> for Rating and Expenses function
    public static RatingsManager decodeRatings(List<String> encodedRatings) {
        final ArrayList<String> decodedRatings = new ArrayList<String>();
        for (String encodedRating : encodedRatings) {
            decodedRatings.add(decodeRatingFromString(encodedRating));
        }
        return new RatingsManager(decodedRatings);
    }

    private static String decodeRatingFromString(String encodedRating) {
        return encodedRating.strip(); // paser
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