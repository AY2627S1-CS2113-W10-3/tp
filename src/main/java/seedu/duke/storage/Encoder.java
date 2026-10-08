package seedu.duke.storage;

import java.util.ArrayList;
import java.util.List;

import seedu.duke.ratings.RatingsManager;

public class Encoder {
    /**
     * Encodes all the {@code Person} in the {@code toSave} into a list of decodable
     * and readable string presentation
     * for storage.
     */
    public static List<String> encodeRatings(RatingsManager toSave) {
        final List<String> encodedRatings = new ArrayList<>();
        toSave.getAll().forEach(rating -> encodedPersons.add(encodePersonToString(person)));
        return encodedRatings;
    }

    public static List<String> encodeExpenses(RatingsManager toSave) {
        final List<String> encodedExpenses = new ArrayList<>();
        toSave.getAll().forEach(rating -> encodedPersons.add(encodePersonToString(person)));
        return encodedRatings;
    }

    /**
     * Encodes the {@code person} into a decodable and readable string
     * representation.
     */
    private static String encodeRatingToString(String rating) {
        String finalRating = "flavour" + "|" + "pizza" + "|" + "4" + "|" + "24";
        return finalRating;
    }
}
