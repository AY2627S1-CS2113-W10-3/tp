package seedu.duke.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.expenses.ExpensesManager;
import seedu.duke.ratings.RatingsManager;

/**
 * Represents the files used to store ratings and expenses.
 * By default the files are kept in a {@code data} folder next to wherever the app is run from,
 * e.g. next to the JAR file, so the app works on any computer and not just inside the project folder.
 */
public class StorageFile {

    /** Folder that holds the data files when the user does not give their own paths. */
    public static final String DEFAULT_DATA_FOLDER = "data";

    public static final String DEFAULT_EXPENSES_STORAGE_FILEPATH = DEFAULT_DATA_FOLDER + "/expenses.txt";

    public static final String DEFAULT_RATINGS_STORAGE_FILEPATH = DEFAULT_DATA_FOLDER + "/ratings.txt";

    private static final Logger LOGGER = Logger.getLogger(StorageFile.class.getName());

    private static final String FILE_EXTENSION = ".txt";

    private final Path expensesPath;
    private final Path ratingsPath;

    /**
     * Creates storage that uses the default file paths inside the {@code data} folder.
     *
     * @throws BudgetBiteException If the default paths are invalid.
     */
    public StorageFile() throws BudgetBiteException {
        this(DEFAULT_EXPENSES_STORAGE_FILEPATH, DEFAULT_RATINGS_STORAGE_FILEPATH);
    }

    /**
     * Creates storage that uses the given file paths.
     *
     * @param expensesFilePath Path of the file holding expenses. Must end with {@code .txt}.
     * @param ratingsFilePath  Path of the file holding ratings. Must end with {@code .txt}.
     * @throws BudgetBiteException If either path does not end with {@code .txt}.
     */
    public StorageFile(String expensesFilePath, String ratingsFilePath) throws BudgetBiteException {
        this.expensesPath = Paths.get(expensesFilePath);
        this.ratingsPath = Paths.get(ratingsFilePath);
        if (!isValidPath(expensesPath) || !isValidPath(ratingsPath)) {
            throw new BudgetBiteException("Storage file should end with '" + FILE_EXTENSION + "'");
        }
    }

    /**
     * Returns true if the given path is acceptable as a storage file,
     * i.e. it ends with {@code .txt}.
     */
    private static boolean isValidPath(Path filePath) {
        return filePath.toString().endsWith(FILE_EXTENSION);
    }

    /**
     * Saves all ratings and expenses to their files.
     * Creates the {@code data} folder first if it does not exist yet, e.g. on the very first run.
     *
     * @param ratings  Ratings to save.
     * @param expenses Expenses to save.
     * @throws BudgetBiteException If the files could not be written.
     */
    public void save(RatingsManager ratings, ExpensesManager expenses) throws BudgetBiteException {
        assert ratings != null : "Ratings to save should not be null";
        assert expenses != null : "Expenses to save should not be null";

        writeLines(expensesPath, Encoder.encodeExpenses(expenses));
        writeLines(ratingsPath, Encoder.encodeRatings(ratings));
        LOGGER.log(Level.FINE, "Saved data to " + expensesPath + " and " + ratingsPath);
    }

    /**
     * Loads the saved ratings, or returns an empty list of ratings if there is no file yet.
     *
     * @return The loaded ratings.
     * @throws BudgetBiteException If the file exists but could not be read.
     */
    public RatingsManager loadRatings() throws BudgetBiteException {
        if (!Files.isRegularFile(ratingsPath)) {
            LOGGER.log(Level.FINE, "No ratings file at " + ratingsPath + ", starting with no ratings");
            return new RatingsManager();
        }
        return Decoder.decodeRatings(readLines(ratingsPath));
    }

    /**
     * Loads the saved expenses, or returns an empty list of expenses if there is no file yet.
     *
     * @return The loaded expenses.
     * @throws BudgetBiteException If the file exists but could not be read.
     */
    public ExpensesManager loadExpenses() throws BudgetBiteException {
        if (!Files.isRegularFile(expensesPath)) {
            LOGGER.log(Level.FINE, "No expenses file at " + expensesPath + ", starting with no expenses");
            return new ExpensesManager();
        }
        return Decoder.decodeExpenses(readLines(expensesPath));
    }

    public Path getExpensesPath() {
        return expensesPath;
    }

    public Path getRatingsPath() {
        return ratingsPath;
    }

    /**
     * Writes the lines to the file, creating any missing parent folders first.
     */
    private static void writeLines(Path filePath, ArrayList<String> lines) throws BudgetBiteException {
        try {
            Path parentFolder = filePath.toAbsolutePath().getParent();
            if (parentFolder != null) {
                // Does nothing if the folder already exists.
                Files.createDirectories(parentFolder);
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not write to " + filePath, e);
            throw new BudgetBiteException("Could not save your data to " + filePath
                    + ". Check that the folder is not read-only.");
        }
    }

    /**
     * Reads every line of the file.
     */
    private static List<String> readLines(Path filePath) throws BudgetBiteException {
        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not read " + filePath, e);
            throw new BudgetBiteException("Could not read your saved data from " + filePath + ".");
        }
    }
}
