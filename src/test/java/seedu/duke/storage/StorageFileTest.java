package seedu.duke.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import seedu.duke.budget.Budget;
import seedu.duke.budget.BudgetManager;
import seedu.duke.budget.BudgetPeriod;
import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.expenses.Expense;
import seedu.duke.expenses.ExpensesManager;
import seedu.duke.ratings.Rating;
import seedu.duke.ratings.RatingsManager;

public class StorageFileTest {
    private static final double DELTA = 0.001;

    @Test
    public void load_oldFormatWithoutLocation_unknownLocationGiven() throws Exception {
        RatingsManager actualRatingsManager = new StorageFile(
                "src/test/java/seedu/duke/data/expenses.txt",
                "src/test/java/seedu/duke/data/ratings.txt").loadRatings();

        ArrayList<Rating> expectedRatings = new ArrayList<>();
        expectedRatings.add(new Rating(Rating.UNKNOWN_LOCATION, "fine foods", "korean", 3));
        assertEquals(expectedRatings, actualRatingsManager.getRatings());
    }

    @Test
    public void defaultPaths_insideDataFolder() throws Exception {
        StorageFile storage = new StorageFile();
        assertEquals(Path.of("data", "expenses.txt"), storage.getExpensesPath());
        assertEquals(Path.of("data", "ratings.txt"), storage.getRatingsPath());
    }

    @Test
    public void constructor_notTxtFile_exceptionThrown() {
        assertThrows(BudgetBiteException.class, () -> new StorageFile("data/expenses.json", "data/ratings.txt"));
    }

    @Test
    public void load_noFilesYet_emptyLists() throws Exception {
        Path folder = Files.createTempDirectory("budgetbite-test");
        StorageFile storage = makeStorage(folder.resolve("data"));

        assertTrue(storage.loadExpenses().isEmpty());
        assertTrue(storage.loadRatings().isEmpty());
    }

    @Test
    public void save_dataFolderMissing_folderCreatedAndDataSaved() throws Exception {
        Path dataFolder = Files.createTempDirectory("budgetbite-test").resolve("data");
        StorageFile storage = makeStorage(dataFolder);

        storage.save(new RatingsManager(), new ExpensesManager(), new BudgetManager());

        assertTrue(Files.isDirectory(dataFolder));
        assertTrue(Files.exists(dataFolder.resolve("expenses.txt")));
        assertTrue(Files.exists(dataFolder.resolve("ratings.txt")));
    }

    @Test
    public void saveThenLoad_sameData() throws Exception {
        StorageFile storage = makeStorage(Files.createTempDirectory("budgetbite-test").resolve("data"));
        ExpensesManager expenses = new ExpensesManager();
        expenses.addExpense(new Expense("Chicken Rice", 4.50));
        RatingsManager ratings = new RatingsManager();
        ratings.addRating(new Rating("PGPR", "Chicken Rice", "Roast Chicken Rice", 8));

        storage.save(ratings, expenses, new BudgetManager());
        ExpensesManager loadedExpenses = storage.loadExpenses();
        RatingsManager loadedRatings = storage.loadRatings();

        assertEquals(1, loadedExpenses.getSize());
        assertEquals("Chicken Rice", loadedExpenses.getExpense(1).getFoodName());
        assertEquals(4.50, loadedExpenses.getExpense(1).getCost(), DELTA);
        assertEquals(ratings.getRatings(), loadedRatings.getRatings());
        assertEquals("PGPR|Chicken Rice|Roast Chicken Rice|8.0", Files.readAllLines(storage.getRatingsPath()).get(0));
    }

    @Test
    public void saveThenLoad_budget_sameBudget() throws Exception {
        StorageFile storage = makeStorage(Files.createTempDirectory("budgetbite-test").resolve("data"));
        BudgetManager budget = new BudgetManager(new Budget(BudgetPeriod.WEEKLY, 50));

        storage.save(new RatingsManager(), new ExpensesManager(), budget);
        BudgetManager loaded = storage.loadBudget();

        assertTrue(loaded.hasBudget());
        assertEquals(BudgetPeriod.WEEKLY, loaded.getBudget().getPeriod());
        assertEquals(50, loaded.getBudget().getAmount(), DELTA);
        assertEquals("weekly|50.00", Files.readAllLines(storage.getBudgetPath()).get(0));
    }

    @Test
    public void loadBudget_corruptedFile_noBudget() throws Exception {
        Path dataFolder = Files.createTempDirectory("budgetbite-test").resolve("data");
        StorageFile storage = makeStorage(dataFolder);
        Files.createDirectories(dataFolder);
        Files.write(storage.getBudgetPath(), java.util.List.of("yearly|abc"));

        assertTrue(!storage.loadBudget().hasBudget());
    }

    private StorageFile makeStorage(Path dataFolder) throws BudgetBiteException {
        return new StorageFile(dataFolder.resolve("expenses.txt").toString(),
                dataFolder.resolve("ratings.txt").toString());
    }
}
