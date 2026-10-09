package seedu.duke.storage;

import java.util.ArrayList;
import java.util.List;

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

    public static ExpensesManager decodeExpenses(List<String> encodedExpenses) {
        final ArrayList<String> decodedExpenses = new ArrayList<>();
        for (String encodedExpense : encodedExpenses) {
            decodedExpenses.add(decodeExpensesFromString(encodedExpense));
        }
        return new ExpensesManager(decodedExpenses);
    }

    private static String decodeExpensesFromString(String encodedExpense) {
        return encodedExpense.strip(); // paser
    }
}
