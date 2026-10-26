package seedu.duke.budget;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import seedu.duke.exceptions.BudgetBiteException;

/**
 * The length of time a budget covers. Each period knows the first day of the current period,
 * e.g. a weekly budget counts spending from this Monday to today.
 */
public enum BudgetPeriod {
    DAILY("daily", "today"),
    WEEKLY("weekly", "this week"),
    MONTHLY("monthly", "this month");

    private final String keyword;
    private final String description;

    BudgetPeriod(String keyword, String description) {
        this.keyword = keyword;
        this.description = description;
    }

    /**
     * Returns the period matching the word typed by the user, ignoring case.
     *
     * @param text Word typed by the user, e.g. {@code "weekly"}.
     * @return The matching period.
     * @throws BudgetBiteException If the word is not daily, weekly or monthly.
     */
    public static BudgetPeriod fromKeyword(String text) throws BudgetBiteException {
        for (BudgetPeriod period : values()) {
            if (period.keyword.equalsIgnoreCase(text.trim())) {
                return period;
            }
        }
        throw new BudgetBiteException("Period must be daily, weekly or monthly.");
    }

    /**
     * Returns the first day of the period that contains the given day.
     *
     * @param today The current date.
     * @return Today for a daily budget, this Monday for a weekly one, the 1st of the month for a monthly one.
     */
    public LocalDate getStartDate(LocalDate today) {
        switch (this) {
        case DAILY:
            return today;
        case WEEKLY:
            return today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        case MONTHLY:
            return today.withDayOfMonth(1);
        default:
            throw new AssertionError("Unknown budget period: " + this);
        }
    }

    /** Returns the word used in commands and the storage file, e.g. {@code "weekly"}. */
    public String getKeyword() {
        return keyword;
    }

    /** Returns how the current period is described to the user, e.g. {@code "this week"}. */
    public String getDescription() {
        return description;
    }
}
