package seedu.duke.storage;

import java.util.ArrayList;

import seedu.duke.expenses.Expense;
import seedu.duke.expenses.ExpensesManager;
import seedu.duke.ratings.RatingsManager;

public class Encoder {

    public static ArrayList<String> encodeRatings(RatingsManager toSave) {
        final ArrayList<String> encodedRatings = new ArrayList<>();
        toSave.getRatings().forEach(rating -> encodedRatings.add(encodeRatingToString(rating)));
        return encodedRatings;
    }

    public static ArrayList<String> encodeExpenses(ExpensesManager toSave) {
        final ArrayList<String> encodedExpenses = new ArrayList<>();
        toSave.getExpenses().forEach(expense -> encodedExpenses.add(encodeExpenseToString(expense)));
        return encodedExpenses;
    }

    /**
     * Encodes the {@code rating} into a decodable and readable string
     * representation.
     */
    private static String encodeRatingToString(String rating) {
        String finalRating = rating;
        return finalRating;
    }

    /**
     * Encodes the {@code expense} into a decodable and readable string
     * representation.
     */
    private static String encodeExpenseToString(Expense expense) {
        return expense.toStorageString();
    }
}
