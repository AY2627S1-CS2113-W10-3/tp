package seedu.duke.parser;

import java.util.HashMap;
import java.util.Map;

import seedu.duke.commands.AddRatingCommand;
import seedu.duke.commands.Command;
import seedu.duke.commands.DeleteRatingCommand;
import seedu.duke.commands.EditRatingCommand;
import seedu.duke.commands.IncorrectCommand;
import seedu.duke.commands.ListRatingsCommand;
import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.ratings.Rating;
import seedu.duke.ratings.RatingsManager;

/**
 * Parses the arguments of the ratings commands ({@code rate}, {@code list-ratings}, {@code edit-rating}
 * and {@code delete-rating}) into command objects. Kept separate from {@link Parser} so that the
 * ratings-specific rules live in one place.
 *
 * <p>Arguments use {@code /prefix VALUE} pairs that may appear in any order. The prefix splitting is
 * done by the private helper {@link #splitByPrefixes}; it can be swapped for the team's shared argument
 * parser once that is available.
 */
public class RatingCommandParser {
    public static final String PREFIX_FOOD = "/food";
    public static final String PREFIX_STALL = "/stall";
    public static final String PREFIX_SCORE = "/score";
    public static final String PREFIX_NOTES = "/notes";

    /** Key under which {@link #splitByPrefixes} stores the text before the first prefix. */
    private static final String PREAMBLE = "";

    private final RatingsManager ratings;

    /**
     * Creates a parser whose commands act on the given ratings.
     *
     * @param ratings Ratings the parsed commands will read and change.
     */
    public RatingCommandParser(RatingsManager ratings) {
        this.ratings = ratings;
    }

    /**
     * Parses a ratings command. Invalid input gives an {@link IncorrectCommand} that explains the
     * problem, so the caller never has to handle parse errors itself.
     *
     * @param commandWord One of the ratings command words, e.g. {@code "rate"}.
     * @param arguments   Text after the command word.
     * @return The command to execute.
     */
    public Command parse(String commandWord, String arguments) {
        try {
            switch (commandWord) {
            case AddRatingCommand.COMMAND_WORD:
                return parseAddRating(arguments);
            case ListRatingsCommand.COMMAND_WORD:
                return parseListRatings(arguments);
            case EditRatingCommand.COMMAND_WORD:
                return parseEditRating(arguments);
            case DeleteRatingCommand.COMMAND_WORD:
                return parseDeleteRating(arguments);
            default:
                throw new AssertionError("Not a ratings command: " + commandWord);
            }
        } catch (BudgetBiteException e) {
            return new IncorrectCommand(e.getMessage());
        }
    }

    /**
     * Parses the arguments of {@code rate /food FOOD /stall STALL /score SCORE [/notes NOTES]}.
     *
     * @throws BudgetBiteException If a required field is missing or any field is invalid.
     */
    private Command parseAddRating(String arguments) throws BudgetBiteException {
        String usage = AddRatingCommand.USAGE;
        Map<String, String> args = splitByPrefixes(arguments, PREFIX_FOOD, PREFIX_STALL, PREFIX_SCORE, PREFIX_NOTES);
        checkNoPreamble(args, usage);

        String foodName = getRequiredValue(args, PREFIX_FOOD, usage);
        String stallName = getRequiredValue(args, PREFIX_STALL, usage);
        int score = Rating.parseScore(getRequiredValue(args, PREFIX_SCORE, usage));
        String notes = args.getOrDefault(PREFIX_NOTES, "");
        return new AddRatingCommand(ratings, new Rating(foodName, stallName, score, notes));
    }

    /**
     * Parses the arguments of {@code list-ratings [/food FOOD] [/stall STALL]}.
     *
     * @throws BudgetBiteException If a filter is given without a value.
     */
    private Command parseListRatings(String arguments) throws BudgetBiteException {
        String usage = ListRatingsCommand.USAGE;
        Map<String, String> args = splitByPrefixes(arguments, PREFIX_FOOD, PREFIX_STALL);
        checkNoPreamble(args, usage);

        String foodFilter = getOptionalFilter(args, PREFIX_FOOD, usage);
        String stallFilter = getOptionalFilter(args, PREFIX_STALL, usage);
        return new ListRatingsCommand(ratings, foodFilter, stallFilter);
    }

    /**
     * Parses the arguments of {@code edit-rating INDEX [/score SCORE] [/notes NOTES]}.
     * At least one of {@code /score} and {@code /notes} must be given.
     *
     * @throws BudgetBiteException If the index is invalid, nothing is being edited, the score is not a number,
     *                             or {@code /notes} is given without any text.
     */
    private Command parseEditRating(String arguments) throws BudgetBiteException {
        String usage = EditRatingCommand.USAGE;
        Map<String, String> args = splitByPrefixes(arguments, PREFIX_SCORE, PREFIX_NOTES);
        int index = parseIndex(args.get(PREAMBLE), usage);

        String scoreText = args.get(PREFIX_SCORE);
        String newNotes = args.get(PREFIX_NOTES);
        if (scoreText == null && newNotes == null) {
            throw new BudgetBiteException("Nothing to edit. Give a new " + PREFIX_SCORE + " and/or "
                    + PREFIX_NOTES + ".\nUsage: " + usage);
        }
        if (newNotes != null && newNotes.isEmpty()) {
            throw new BudgetBiteException(PREFIX_NOTES + " needs a value.\nUsage: " + usage);
        }
        Integer newScore = scoreText == null ? null : Rating.parseScore(scoreText);
        return new EditRatingCommand(ratings, index, newScore, newNotes);
    }

    /**
     * Parses the arguments of {@code delete-rating INDEX}.
     *
     * @throws BudgetBiteException If the index is missing or not a positive whole number.
     */
    private Command parseDeleteRating(String arguments) throws BudgetBiteException {
        int index = parseIndex(arguments.trim(), DeleteRatingCommand.USAGE);
        return new DeleteRatingCommand(ratings, index);
    }

    /**
     * Splits arguments of the form {@code PREAMBLE /prefix VALUE /prefix VALUE ...} into their parts.
     * For example, {@code "1 /score 8 /notes Too salty"} becomes
     * {@code {"" -> "1", "/score" -> "8", "/notes" -> "Too salty"}}.
     *
     * <p>Prefixes may appear in any order but at most once each. A word only counts as a prefix if it
     * exactly matches one of the given prefixes, so text such as {@code 50/50} inside a value is left
     * untouched.
     *
     * @param arguments Text after the command word.
     * @param prefixes  Prefixes to recognise.
     * @return Map from each prefix that was given to its value, plus the text before the first prefix
     *         stored under {@link #PREAMBLE}. A prefix given with nothing after it maps to an empty string.
     * @throws BudgetBiteException If a prefix appears more than once.
     */
    private static Map<String, String> splitByPrefixes(String arguments, String... prefixes)
            throws BudgetBiteException {
        Map<String, String> valuesByPrefix = new HashMap<>();
        String currentPrefix = PREAMBLE;
        StringBuilder currentValue = new StringBuilder();

        for (String word : arguments.trim().split("\\s+")) {
            String prefix = findPrefix(word, prefixes);
            if (prefix == null) {
                appendWord(currentValue, word);
                continue;
            }
            // A new prefix ends the value (or preamble) collected so far.
            valuesByPrefix.put(currentPrefix, currentValue.toString());
            if (valuesByPrefix.containsKey(prefix)) {
                throw new BudgetBiteException(prefix + " can only be given once.");
            }
            currentPrefix = prefix;
            currentValue = new StringBuilder();
        }
        valuesByPrefix.put(currentPrefix, currentValue.toString());
        return valuesByPrefix;
    }

    private static String findPrefix(String word, String... prefixes) {
        for (String prefix : prefixes) {
            if (prefix.equals(word)) {
                return prefix;
            }
        }
        return null;
    }

    private static void appendWord(StringBuilder text, String word) {
        if (word.isEmpty()) {
            return;
        }
        if (!text.isEmpty()) {
            text.append(" ");
        }
        text.append(word);
    }

    /**
     * Parses a rating index. Only checks that it is a positive whole number; whether a rating
     * exists at that index is checked when the command runs.
     */
    private static int parseIndex(String indexText, String usage) throws BudgetBiteException {
        if (indexText.isEmpty()) {
            throw new BudgetBiteException("Missing rating index.\nUsage: " + usage);
        }
        try {
            int index = Integer.parseInt(indexText);
            if (index >= 1) {
                return index;
            }
        } catch (NumberFormatException e) {
            // Falls through to the error below.
        }
        throw new BudgetBiteException("Rating index must be a positive whole number, e.g. 1.\nUsage: " + usage);
    }

    private static String getRequiredValue(Map<String, String> args, String prefix, String usage)
            throws BudgetBiteException {
        String value = args.get(prefix);
        if (value == null) {
            throw new BudgetBiteException("Missing " + prefix + ".\nUsage: " + usage);
        }
        return value;
    }

    /**
     * Returns the filter given after the prefix, or {@code null} if the prefix was not given.
     */
    private static String getOptionalFilter(Map<String, String> args, String prefix, String usage)
            throws BudgetBiteException {
        String filter = args.get(prefix);
        if (filter != null && filter.isEmpty()) {
            throw new BudgetBiteException(prefix + " needs a value.\nUsage: " + usage);
        }
        return filter;
    }

    private static void checkNoPreamble(Map<String, String> args, String usage) throws BudgetBiteException {
        String preamble = args.get(PREAMBLE);
        if (!preamble.isEmpty()) {
            throw new BudgetBiteException("Unexpected text: \"" + preamble + "\".\nUsage: " + usage);
        }
    }
}
