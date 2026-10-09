package seedu.duke.storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.expenses.ExpensesManager;
import seedu.duke.ratings.RatingsManager;

/**
 * Represents the file used to store ratings and expenses.
 */
public class StorageFile {

    /**
     * Default file path used if the user doesn't provide the file name.
     */
    public static final String DEFAULT_EXPENSES_STORAGE_FILEPATH =
            "src/main/java/seedu/duke/data/expenses.txt";

    public static final String DEFAULT_RATINGS_STORAGE_FILEPATH =
            "src/main/java/seedu/duke/data/ratings.txt";

    public Map<String, Path> paths = new HashMap<>();

    /**
     * @throws BudgetBiteException if the default path is invalid
     */
    public StorageFile() throws BudgetBiteException {
        this(DEFAULT_EXPENSES_STORAGE_FILEPATH, DEFAULT_RATINGS_STORAGE_FILEPATH);
    }

    /**
     * @throws BudgetBiteException if the given file path is invalid
     */
    public StorageFile(String expensesFilePath, String ratingsFilePath) throws BudgetBiteException {
        paths.put("expenses", Paths.get(expensesFilePath));
        paths.put("ratings", Paths.get(ratingsFilePath));
        if (!isValidPath(paths.get("expenses")) || !isValidPath(paths.get("ratings"))) {
            throw new BudgetBiteException("Storage file should end with '.txt'");
        }
    }

    /**
     * Returns true if the given path is acceptable as a storage file. The file path is considered
     * acceptable if it ends with '.txt'
     */
    private static boolean isValidPath(Path filePath) {
        return filePath.toString().endsWith(".txt");
    }

    /**
     * Saves the data to the storage file.
     *
     * @throws StorageOperationException if there were errors converting and/or storing data to file.
     */
    public void save(RatingsManager ratings, ExpensesManager expenses) throws BudgetBiteException {
        try {
            ArrayList<String> encodedRatings = Encoder.encodeRatings(ratings);
            ArrayList<String> encodedExpenses = Encoder.encodeExpenses(expenses);
            Files.write(paths.get("expenses"), encodedExpenses);
            Files.write(paths.get("ratings"), encodedRatings);

        } catch (IOException ioe) {
            throw new BudgetBiteException("Error writing to files ");
        }
    }

    public RatingsManager loadRatings() throws BudgetBiteException {

        if (!Files.exists(paths.get("ratings")) || !Files.isRegularFile(paths.get("ratings"))) {

            System.out.println("no old ratings");
            return new RatingsManager();
        }

        try {
            return Decoder.decodeRatings(Files.readAllLines(paths.get("ratings")));
        } catch (FileNotFoundException fnfe) {
            throw new BudgetBiteException("A non-existent file scenario is already handled earlier.");
            // other errors
        } catch (IOException ioe) {
            throw new BudgetBiteException("Error writing to file: " + paths.get("ratings"));
        }
    }

    public ExpensesManager loadExpenses() throws BudgetBiteException {

        if (!Files.exists(paths.get("expenses")) || !Files.isRegularFile(paths.get("expenses"))) {
            return new ExpensesManager();
        }

        try {
            return Decoder.decodeExpenses(Files.readAllLines(paths.get("expenses")));
        } catch (FileNotFoundException fnfe) {
            throw new BudgetBiteException("A non-existent file scenario is already handled earlier.");
            // other errors
        } catch (IOException ioe) {
            throw new BudgetBiteException("Error writing to file: " + paths.get("expenses"));
        }
    }
}
