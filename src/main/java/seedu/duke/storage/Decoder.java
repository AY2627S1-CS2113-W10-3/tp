package seedu.duke.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes.Name;
import java.util.regex.Matcher;

import seedu.duke.ratings.RatingsManager;

public class Decoder {

    public static RatingsManager decodeRatings(List<String> encodedRatings) {
        final List<String> decodedRatings = new ArrayList<>();
        for (String encodedRating : encodedRatings) {
            decodedRatings.add(decodeRatingFromString(encodedRating));
        }
        return new RatingsManager(); // decodedRatings;
        // return new AddressBook(new UniquePersonList(decodedPersons));
    }

    private static String decodeRatingFromString(String encodedRating) {
        return encodedRating.strip(); // paser

    }
}
