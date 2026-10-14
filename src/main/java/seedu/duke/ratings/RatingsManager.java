package seedu.duke.ratings;

import java.util.ArrayList;

public class RatingsManager {

    private static ArrayList<Rating> ratings;

    public RatingsManager() {
        ratings = new ArrayList<Rating>();
    }

    public RatingsManager(ArrayList<Rating> decodedRatings) {
        ratings = decodedRatings;
    }

    public String toString() {
        String ratingsString = "";

        for (Rating rating : ratings) {
            ratingsString = ratingsString + "\n" + rating;
        }

        return ratingsString;
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }
    public void addRating(Rating rating) {
        ratings.add(rating);
    }
}
