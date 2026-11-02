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

/**
 * Lists the valid names for the field being typed in a {@code rate} command,
 * similar to pressing Tab in a terminal. The user ends the command with {@code ?}
 * (a Tab at the end of the line is turned into {@code ?} by {@code TextUi}), e.g.
 * <ul>
 *     <li>{@code rate /location ?} lists every food court,</li>
 *     <li>{@code rate /location PGPR /stall ch?} lists the stalls in PGPR whose names contain "ch",</li>
 *     <li>{@code rate /location PGPR /stall Chicken Rice /food ?} lists that stall's menu.</li>
 * </ul>
 * The field being typed is the last one in the command. Nothing is rated; the user then
 * types the full command.
 */
final class RateSuggestions {
    /** Ending a {@code rate} command with this asks for suggestions instead of rating. */
    static final String MARKER = "?";

    /** The markers and spaces at the end of a request, e.g. the " ?" in "/stall ch ?". */
    private static final String TRAILING_MARKERS_REGEX = "[\\s?]+$";

    private RateSuggestions() {
        // Utility class; not meant to be instantiated.
    }

    /**
     * Returns true if the arguments of a {@code rate} command ask for suggestions.
     *
     * @param arguments Trimmed text after the command word.
     */
    static boolean isRequest(String arguments) {
        return arguments.endsWith(MARKER);
    }

    /**
     * Lists the valid names for the field being typed, keeping only those that contain
     * the text typed so far, ignoring letter case.
     *
     * @param arguments     Text after the command word, ending with {@link #MARKER}.
     * @param foodDirectory Catalogue the names come from.
     * @return The suggestions to show.
     * @throws BudgetBiteException If an earlier field is missing or not in the catalogue,
     *                             e.g. a stall is asked for before a valid location is given.
     */
    static String suggest(String arguments, FoodDirectory foodDirectory) throws BudgetBiteException {
        assert isRequest(arguments) : "Only suggestion requests should be passed in";
        String typedSoFar = arguments.replaceAll(TRAILING_MARKERS_REGEX, "");

        String[] words = typedSoFar.split("\\s+");
        String lastWord = words[words.length - 1];
        boolean isTypingFieldName = lastWord.startsWith(RateCommand.FIELD_PREFIX)
                && !RateCommand.FIELD_NAMES.contains(lastWord.substring(1).toLowerCase());
        if (isTypingFieldName) {
            List<String> fieldWords = RateCommand.FIELD_NAMES.stream()
                    .map(name -> RateCommand.FIELD_PREFIX + name).toList();
            return listMatches("Fields", fieldWords, Function.identity(), lastWord);
        }

        LinkedHashMap<String, String> fields = RateCommand.splitIntoFields(typedSoFar);
        if (fields.isEmpty()) {
            return "Start with /location.\n"
                    + listMatches("Locations", foodDirectory.getLocations(), FoodCourt::name, "");
        }

        Map.Entry<String, String> currentField = fields.lastEntry();
        String typedValue = currentField.getValue();
        return switch (currentField.getKey()) {
        case "location" -> listMatches("Locations", foodDirectory.getLocations(), FoodCourt::name, typedValue);
        case "stall" -> {
            FoodCourt foodCourt = getEarlierFoodCourt(fields, foodDirectory);
            yield listMatches("Stalls in " + foodCourt.name(), foodCourt.stalls(), FoodStall::name, typedValue);
        }
        case "food" -> {
            FoodStall stall = getEarlierStall(fields, foodDirectory);
            yield listMatches("Menu of " + stall.name(), stall.menu(), FoodItem::name, typedValue);
        }
        case "score" -> "Score: a number from 0 to 10 with at most 1 decimal place, e.g. 7.5";
        default -> throw new AssertionError("Unknown fields are rejected by splitIntoFields");
        };
    }

    /**
     * Returns the food court given earlier in the command, which the stall and food suggestions need.
     */
    private static FoodCourt getEarlierFoodCourt(Map<String, String> fields, FoodDirectory foodDirectory)
            throws BudgetBiteException {
        String location = fields.get("location");
        if (location == null || location.isEmpty()) {
            throw new BudgetBiteException("Give /location first, e.g. rate /location PGPR /stall ?");
        }
        return RateCommand.findFoodCourt(foodDirectory, location);
    }

    /**
     * Returns the stall given earlier in the command, which the food suggestions need.
     */
    private static FoodStall getEarlierStall(Map<String, String> fields, FoodDirectory foodDirectory)
            throws BudgetBiteException {
        FoodCourt foodCourt = getEarlierFoodCourt(fields, foodDirectory);
        String stall = fields.get("stall");
        if (stall == null || stall.isEmpty()) {
            throw new BudgetBiteException("Give /stall first, e.g. rate /location "
                    + foodCourt.name() + " /stall ?");
        }
        return RateCommand.findStall(foodCourt, stall);
    }

    /**
     * Lists the names that contain the typed text, ignoring letter case.
     * If nothing is typed, or nothing matches, every name is listed.
     *
     * @param title   What the names are, e.g. {@code "Stalls in PGPR"}.
     * @param entries Food courts, stalls, dishes or field words.
     * @param getName Gets the name of one entry, e.g. {@code FoodStall::name}.
     * @param typed   Text typed so far for this field.
     */
    private static <T> String listMatches(String title, List<T> entries, Function<T, String> getName,
                                          String typed) {
        String allNames = RateCommand.joinNames(entries, getName);
        if (typed.isEmpty()) {
            return title + ": " + allNames;
        }

        String lowerCaseTyped = typed.toLowerCase();
        List<T> matches = entries.stream()
                .filter(entry -> getName.apply(entry).toLowerCase().contains(lowerCaseTyped))
                .toList();
        if (matches.isEmpty()) {
            return "No match for '" + typed + "'.\n" + title + ": " + allNames;
        }
        return title + " matching '" + typed + "': " + RateCommand.joinNames(matches, getName);
    }
}
