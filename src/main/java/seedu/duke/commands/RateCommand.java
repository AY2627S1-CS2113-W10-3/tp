package seedu.duke.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import seedu.duke.ratings.Rating;
import seedu.duke.ratings.RatingsManager;

public class RateCommand extends Command {
    private final String arguments;
    private final RatingsManager ratingsManager;

    public RateCommand(String arguments, RatingsManager ratingsManager) {
        this.arguments = arguments == null ? "" : arguments.trim();
        if (ratingsManager == null) {
            throw new IllegalArgumentException("Ratings manager cannot be null.");
        }
        this.ratingsManager = ratingsManager;
    }

    @Override
    public String execute() {
        if (arguments.isEmpty()) {
            return "Please provide /stall, /food and /score.";
        }

        Map<String, String> fields = parseFields();
        if (fields == null) {
            return "Invalid format. Use: rate /stall <stall> /food <food> /score <0-10>.";
        }

        String stall = fields.get("stall");
        String food = fields.get("food");
        String scoreText = fields.get("score");

        if (stall == null || food == null || scoreText == null) {
            return "Please provide /stall, /food and /score.";
        }

        float score;
        try {
            score = Float.parseFloat(scoreText);
        } catch (NumberFormatException exception) {
            return "Invalid score. Please provide a whole number between 0 and 10.";
        }

        if (score < 0 || score > 10) {
            return "Invalid score. Please provide a whole number between 0 and 10.";
        }

        Rating rating = new Rating(stall, food, score);
        ratingsManager.addRating(rating);

        return "Stall: " + stall + ", Food: " + food + ", Score: " + score;
    }

    private Map<String, String> parseFields() {
        Set<String> validFields = Set.of("stall", "food", "score");
        Map<String, String> fields = new HashMap<>();
        String currentField = null;
        StringBuilder currentValue = new StringBuilder();

        String[] tokens = arguments.split("\\s+");
        for (String token : tokens) {
            if (token.startsWith("/")) {
                if (currentField != null && !saveField(fields, currentField, currentValue.toString())) {
                    return null;
                }

                currentField = token.substring(1).toLowerCase();
                if (!validFields.contains(currentField) || fields.containsKey(currentField)) {
                    return null;
                }
                currentValue.setLength(0);
            } else {
                if (currentField == null) {
                    return null;
                }
                if (currentValue.length() > 0) {
                    currentValue.append(" ");
                }
                currentValue.append(token);
            }
        }

        if (currentField == null || !saveField(fields, currentField, currentValue.toString())) {
            return null;
        }
        return fields;
    }

    private boolean saveField(Map<String, String> fields, String field, String value) {
        String trimmedValue = value.trim();
        if (trimmedValue.isEmpty() || fields.containsKey(field)) {
            return false;
        }
        fields.put(field, trimmedValue);
        return true;
    }
}
