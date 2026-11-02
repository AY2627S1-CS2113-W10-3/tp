package seedu.duke.commands;

import java.time.LocalDate;

import seedu.duke.budget.BudgetPeriod;
import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.expenses.ExpensesManager;
import seedu.duke.expenses.Money;

/**
 * Lists logged expenses with their index and the total spent, e.g. {@code list-expenses}.
 * With a period, e.g. {@code list-expenses weekly}, only the expenses of the current day, week or month are listed.
 */
public class ListExpensesCommand extends Command {
    public static final String COMMAND_WORD = "list-expenses";
    public static final String MESSAGE_USAGE = "Format: list-expenses [PERIOD] (PERIOD is daily, weekly or monthly)\n"
            + "Example: list-expenses weekly";

    private final String arguments;
    private final ExpensesManager expensesManager;
    private final LocalDate today;

    /**
     * Creates the command that lists every expense.
     *
     * @param expensesManager Manager whose expenses are listed.
     */
    public ListExpensesCommand(ExpensesManager expensesManager) {
        this("", expensesManager);
    }

    /**
     * Creates the command with the text typed after {@code list-expenses}.
     *
     * @param arguments Text after the command word: empty, or a period such as {@code weekly}.
     * @param expensesManager Manager whose expenses are listed.
     */
    public ListExpensesCommand(String arguments, ExpensesManager expensesManager) {
        this(arguments, expensesManager, LocalDate.now());
    }

    /**
     * Creates the command with a fixed "today", so that tests do not depend on the real date.
     *
     * @param arguments Text after the command word: empty, or a period such as {@code weekly}.
     * @param expensesManager Manager whose expenses are listed.
     * @param today Date to use as today.
     */
    public ListExpensesCommand(String arguments, ExpensesManager expensesManager, LocalDate today) {
        assert expensesManager != null : "Expenses manager should not be null";
        assert today != null : "Today's date should not be null";
        this.arguments = arguments == null ? "" : arguments.trim();
        this.expensesManager = expensesManager;
        this.today = today;
    }

    @Override
    public String execute() {
        if (arguments.isEmpty()) {
            return listAll();
        }
        try {
            return listPeriod(BudgetPeriod.fromKeyword(arguments));
        } catch (BudgetBiteException e) {
            return e.getMessage() + "\n" + MESSAGE_USAGE;
        }
    }

    private String listAll() {
        if (expensesManager.isEmpty()) {
            return "No expenses logged yet. Try: log-expense Chicken Rice /cost 4.50";
        }
        return "Your expenses:" + expensesManager + "\n"
                + "Total spent: " + Money.format(expensesManager.getTotalCost());
    }

    private String listPeriod(BudgetPeriod period) {
        LocalDate startDate = period.getStartDate(today);
        String periodDescription = period.getDescription();
        String expensesText = expensesManager.toStringBetween(startDate, today);
        if (expensesText.isEmpty()) {
            return "No expenses " + periodDescription + ".";
        }
        return "Your expenses " + periodDescription + ":" + expensesText + "\n"
                + "Total spent " + periodDescription + ": "
                + Money.format(expensesManager.getTotalCostBetween(startDate, today));
    }
}
