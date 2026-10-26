package seedu.duke.commands;

import java.time.LocalDate;

import seedu.duke.budget.Budget;
import seedu.duke.budget.BudgetManager;
import seedu.duke.budget.BudgetPeriod;
import seedu.duke.exceptions.BudgetBiteException;
import seedu.duke.expenses.ExpensesManager;

/**
 * Sets a food budget for a period, e.g. {@code set-budget weekly 50}.
 * Setting a budget again replaces the old one.
 */
public class SetBudgetCommand extends Command {
    public static final String COMMAND_WORD = "set-budget";
    public static final String MESSAGE_USAGE = "Format: set-budget PERIOD AMOUNT (PERIOD is daily, weekly or monthly)\n"
            + "Example: set-budget weekly 50";

    private static final int ARGUMENT_COUNT = 2;

    private final String arguments;
    private final BudgetManager budgetManager;
    private final ExpensesManager expensesManager;
    private final LocalDate today;

    /**
     * Creates the command with the text typed after {@code set-budget}.
     *
     * @param arguments Text after the command word.
     * @param budgetManager Manager the budget is stored in.
     * @param expensesManager Expenses used to show how much of the new budget is already spent.
     */
    public SetBudgetCommand(String arguments, BudgetManager budgetManager, ExpensesManager expensesManager) {
        this(arguments, budgetManager, expensesManager, LocalDate.now());
    }

    /**
     * Creates the command with a fixed "today", so that tests do not depend on the real date.
     */
    public SetBudgetCommand(String arguments, BudgetManager budgetManager, ExpensesManager expensesManager,
                            LocalDate today) {
        assert budgetManager != null : "Budget manager should not be null";
        assert expensesManager != null : "Expenses manager should not be null";
        this.arguments = arguments == null ? "" : arguments.trim();
        this.budgetManager = budgetManager;
        this.expensesManager = expensesManager;
        this.today = today;
    }

    @Override
    public String execute() {
        try {
            Budget budget = parseBudget();
            budgetManager.setBudget(budget);
            return "Budget set.\n" + budgetManager.getSummary(expensesManager, today);
        } catch (BudgetBiteException e) {
            return e.getMessage() + "\n" + MESSAGE_USAGE;
        }
    }

    private Budget parseBudget() throws BudgetBiteException {
        String[] parts = arguments.split("\\s+");
        if (arguments.isEmpty() || parts.length != ARGUMENT_COUNT) {
            throw new BudgetBiteException("Please give a period and an amount.");
        }
        BudgetPeriod period = BudgetPeriod.fromKeyword(parts[0]);
        double amount = Budget.parseAmount(parts[1]);
        return new Budget(period, amount);
    }
}
