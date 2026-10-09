package seedu.duke.expenses;

import java.util.ArrayList;

public class ExpensesManager {
    private static ArrayList<String> expenses = new ArrayList<String>();

    public ExpensesManager() {
        expenses = new ArrayList<String>();
    }

    public ExpensesManager(ArrayList<String> decodedExpenses) {
        expenses = decodedExpenses;
    }

    public String toString() {
        String expensesString = "";

        for (String expense : expenses) {
            expensesString = expensesString + "\n" + expense;
        }

        return expensesString;
    }

    public ArrayList<String> getExpenses() {
        return expenses;
    }
}
