package seedu.duke.ratings;

import java.util.ArrayList;

public class RatingsManager {

    private static ArrayList<String> ratings;

    public RatingsManager() {
        ratings = new ArrayList<String>();
    }

    public RatingsManager(ArrayList<String> decodedRatings) {
        ratings = decodedRatings;
    }

    public String toString() {
        String ratingsString = "";

        for (String rating : ratings) {
            ratingsString = ratingsString + "\n" + rating;
        }

        return ratingsString;

    }

    public ArrayList<String> getRatings() {
        return ratings;
    }

}
