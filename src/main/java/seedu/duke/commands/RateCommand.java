package seedu.duke.commands;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.food.FoodCourt;
import seedu.duke.food.FoodDirectory;
import seedu.duke.food.FoodItem;
import seedu.duke.food.FoodStall;
import seedu.duke.ratings.Rating;
import seedu.duke.ratings.RatingsManager;

/**
 * Rates a dish from the food catalogue, e.g.
 * {@code rate /location PGPR /stall Chicken Rice /food Roast Chicken Rice /score 8}.
 * The location, stall and food must all exist in the catalogue, so the user cannot rate
 * a place BudgetBite does not know about. Names are matched ignoring letter case,
 * and the rating is saved with the names as they appear in the catalogue.
 *
 * <p>If the arguments end with {@code ?} (or Tab), the valid names for the field being typed
 * are shown instead, e.g. {@code rate /location PGPR /stall ?} lists the stalls in PGPR.
 * See {@link RateSuggestions}.</p>
 */
public class RateCommand extends Command {
    public static final String COMMAND_WORD = "rate";
    public static final String MESSAGE_USAGE =
            "Format: rate /location LOCATION /stall STALL /food FOOD /score SCORE\n"
            + "Example: rate /location PGPR /stall Chicken Rice /food Roast Chicken Rice /score 8\n"
            + "Tip: end with ? (or press Tab) and Enter to see the options, e.g. rate /location PGPR /stall ?";

    static final String FIELD_PREFIX = "/";
    /** Fields the command needs, in the order they are reported as missing. */
    static final List<String> FIELD_NAMES = List.of("location", "stall", "food", "score");

    private final String arguments;
    private final RatingsManager ratingsManager;
    private final FoodDirectory foodDirectory;

    /**
     * Creates the command with the text typed after {@code rate}.
     *
     * @param arguments      Text after the command word.
     * @param ratingsManager Manager the rating is added to.
     * @param foodDirectory  Catalogue the location, stall and food are checked against.
     */
    public RateCommand(String arguments, RatingsManager ratingsManager, FoodDirectory foodDirectory) {
        assert ratingsManager != null : "Ratings manager should not be null";
        assert foodDirectory != null : "Food directory should not be null";
        this.arguments = arguments == null ? "" : arguments.trim();
        this.ratingsManager = ratingsManager;
        this.foodDirectory = foodDirectory;
    }

    @Override
    public String execute() {
        try {
            if (RateSuggestions.isRequest(arguments)) {
                return RateSuggestions.suggest(arguments, foodDirectory);
            }

            Map<String, String> fields = parseFields();
            FoodCourt foodCourt = findFoodCourt(foodDirectory, fields.get("location"));
            FoodStall stall = findStall(foodCourt, fields.get("stall"));
            FoodItem item = findItem(stall, fields.get("food"));
            float score = RatingArguments.parseScore(fields.get("score"));

            Rating rating = new Rating(foodCourt.name(), stall.name(), item.name(), score);
            ratingsManager.addRating(rating);
            return "Rated: " + rating;
        } catch (BudgetBiteException e) {
            return e.getMessage() + "\n" + MESSAGE_USAGE;
        }
    }

    /**
     * Splits the arguments into fields and checks that every field has a value and none is missing.
     *
     * @return Every field in {@link #FIELD_NAMES} mapped to its value.
     * @throws BudgetBiteException If a field is unknown, repeated, empty or missing.
     */
    private Map<String, String> parseFields() throws BudgetBiteException {
        if (arguments.isEmpty()) {
            throw new BudgetBiteException("Please provide /location, /stall, /food and /score.");
        }

        Map<String, String> fields = splitIntoFields(arguments);
        for (Map.Entry<String, String> field : fields.entrySet()) {
            if (field.getValue().isEmpty()) {
                throw new BudgetBiteException("Missing value after " + FIELD_PREFIX + field.getKey() + ".");
            }
        }
        for (String fieldName : FIELD_NAMES) {
            if (!fields.containsKey(fieldName)) {
                throw new BudgetBiteException("Missing " + FIELD_PREFIX + fieldName + ".");
            }
        }
        return fields;
    }

    /**
     * Splits the arguments into fields, e.g. {@code "/stall Chicken Rice /score 8"} gives
     * {@code {stall=Chicken Rice, score=8}}. A word starting with '/' starts a new field,
     * and the words after it, up to the next field, are its value. Fields can be in any order.
     * Used both for rating and for suggestions, so values may be empty and fields may be missing.
     *
     * @param arguments Text after the command word.
     * @return The fields in the order they were typed, mapped to their values.
     * @throws BudgetBiteException If a field is unknown or repeated, or there is text before the first field.
     */
    static LinkedHashMap<String, String> splitIntoFields(String arguments) throws BudgetBiteException {
        LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        String currentField = null;
        StringBuilder currentValue = new StringBuilder();

        for (String word : arguments.trim().split("\\s+")) {
            if (word.isEmpty()) {
                // Splitting blank arguments gives one empty word.
                continue;
            }
            if (word.startsWith(FIELD_PREFIX)) {
                saveField(fields, currentField, currentValue);
                currentField = word.substring(FIELD_PREFIX.length()).toLowerCase();
                checkNewField(fields, currentField, word);
                currentValue.setLength(0);
            } else if (currentField == null) {
                throw new BudgetBiteException("Unexpected text before the first field: " + word);
            } else {
                currentValue.append(currentValue.length() > 0 ? " " : "").append(word);
            }
        }
        saveField(fields, currentField, currentValue);
        return fields;
    }

    private static void checkNewField(Map<String, String> fields, String fieldName, String typedWord)
            throws BudgetBiteException {
        if (!FIELD_NAMES.contains(fieldName)) {
            throw new BudgetBiteException("Unknown field: " + typedWord);
        }
        if (fields.containsKey(fieldName)) {
            throw new BudgetBiteException(FIELD_PREFIX + fieldName + " can only be given once.");
        }
    }

    private static void saveField(Map<String, String> fields, String fieldName, StringBuilder value) {
        if (fieldName != null) {
            fields.put(fieldName, value.toString().trim());
        }
    }

    /**
     * Finds a food court in the catalogue.
     *
     * @throws BudgetBiteException If there is none with that name; the message lists the valid names.
     */
    static FoodCourt findFoodCourt(FoodDirectory foodDirectory, String name) throws BudgetBiteException {
        FoodCourt foodCourt = foodDirectory.findFoodCourt(name);
        if (foodCourt == null) {
            throw new BudgetBiteException("Unknown location: " + name + "\n"
                    + "Available locations: " + joinNames(foodDirectory.getLocations(), FoodCourt::name));
        }
        return foodCourt;
    }

    /**
     * Finds a stall in the given food court.
     *
     * @throws BudgetBiteException If there is none with that name; the message lists the valid names.
     */
    static FoodStall findStall(FoodCourt foodCourt, String name) throws BudgetBiteException {
        FoodStall stall = foodCourt.findStall(name);
        if (stall == null) {
            throw new BudgetBiteException("Unknown stall in " + foodCourt.name() + ": " + name + "\n"
                    + "Stalls in " + foodCourt.name() + ": " + joinNames(foodCourt.stalls(), FoodStall::name));
        }
        return stall;
    }

    /**
     * Finds a dish on the given stall's menu.
     *
     * @throws BudgetBiteException If there is none with that name; the message lists the valid names.
     */
    static FoodItem findItem(FoodStall stall, String name) throws BudgetBiteException {
        FoodItem item = stall.findItem(name);
        if (item == null) {
            throw new BudgetBiteException("Unknown food at " + stall.name() + ": " + name + "\n"
                    + "Menu of " + stall.name() + ": " + joinNames(stall.menu(), FoodItem::name));
        }
        return item;
    }

    /**
     * Joins the names of the given catalogue entries with commas, e.g. {@code "PGPR, YIH"}.
     *
     * @param entries Food courts, stalls or dishes.
     * @param getName Gets the name of one entry, e.g. {@code FoodCourt::name}.
     */
    static <T> String joinNames(List<T> entries, Function<T, String> getName) {
        return String.join(", ", entries.stream().map(getName).toList());
    }
}
