# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}

### Model component

![Model Class Diagram](images/ModelClassDiagram.png)

The model holds all user data in memory while the app runs:

* `RatingsManager` holds a list of `Rating`s (stall, food and a score from 0 to 10).
* `ExpensesManager` holds a list of `Expense`s (food name, cost and date). It can
  total all expenses, or only those between two dates.
* `BudgetManager` holds at most one `Budget` (a `BudgetPeriod` and an amount). It
  works out how much of the budget has been spent by asking `ExpensesManager`
  for totals.
* `Money` is the single place for the rules about amounts of money (positive, at
  most 2 decimal places, a maximum) and for formatting amounts as `$4.50`.

Indexes in the public methods of the managers start from 1, matching the
numbers the user sees in `list-ratings` and `list-expenses`. Each manager checks
the index and throws a `BudgetBiteException` with a helpful message if it is
out of range.

### Expense tracking

#### Implementation

The expense feature lets users log, list, edit and delete food expenses
(`log-expense`, `list-expenses`, `edit-expense`, `delete-expense`). Each
`Expense` holds a food name, a cost and a date, and all expenses are kept in an
`ExpensesManager`.

The sequence diagram below shows how `log-expense Laksa /cost 12` is executed
when a budget is set.

![Log Expense Sequence Diagram](images/LogExpenseSequenceDiagram.png)

1. `LogExpenseCommand` uses `ExpenseArguments#splitAtDate` to split off an
   optional `/date`, then `ExpenseArguments#splitAtCost` to separate the food
   name from the cost.
2. `Expense#parseCost` checks the cost using `Money`: it must be a plain number
   more than 0 and at most 1000, with at most 2 decimal places. `BigDecimal` is
   used for this check, because it keeps the digits exactly as typed.
3. If a date was given, `ExpenseArguments#parseDate` checks that it is a real
   date in the format `YYYY-MM-DD` and not in the future. Otherwise, today's
   date is used.
4. The new `Expense` is added to `ExpensesManager`.
5. `BudgetManager#getStatusAfterSpending` adds a line about the budget to the
   response (see [Budget tracking](#budget-tracking)).

`Expense` objects cannot be changed after creation. `edit-expense` creates a
copy with the new cost (`Expense#withCost`), which keeps the original date, and
puts it in place of the old one. Expenses are saved to `data/expenses.txt` in
the format `FOOD_NAME|COST|DATE`.

#### Design considerations

**Aspect: Where the input of expense commands is parsed**

* **Alternative 1 (current choice):** In a shared helper class, `ExpenseArguments`.
    * Pros: `log-expense`, `edit-expense` and `delete-expense` reject bad input
      with the same messages, and the parsing is tested once.
    * Cons: One more class.
* **Alternative 2:** Inside each command.
    * Pros: Each command is self-contained.
    * Cons: Duplicated code, and the commands can drift apart in what they accept.

**Aspect: How costs are stored**

* **Alternative 1 (current choice):** As `double`, limited to 2 decimal places
  when typed, and always shown rounded to 2 decimal places.
    * Pros: Simple arithmetic and formatting.
    * Cons: Tiny rounding errors in sums, e.g. `0.1 + 0.2`, which must be allowed
      for when comparing amounts.
* **Alternative 2:** As `BigDecimal` everywhere.
    * Pros: Exact amounts.
    * Cons: Every calculation and comparison becomes more verbose. At the
      amounts involved, `double` is accurate enough.

**Aspect: How commands find out today's date**

* **Alternative 1 (current choice):** Commands take an optional `LocalDate today`
  in their constructor; the app passes the real date.
    * Pros: Tests pass a fixed date, so they give the same result on any day.
    * Cons: An extra constructor in each date-dependent command.
* **Alternative 2:** Call `LocalDate.now()` inside the commands.
    * Pros: Simpler constructors.
    * Cons: Tests of dates and budgets would pass or fail depending on the day
      they are run.

### Budget tracking

#### Implementation

A `Budget` holds a `BudgetPeriod` (`DAILY`, `WEEKLY` or `MONTHLY`) and an
amount. `BudgetManager` holds the user's one budget, if set.

* `set-budget PERIOD AMOUNT` (`SetBudgetCommand`) reads the period with
  `BudgetPeriod#fromKeyword` and the amount with `Budget#parseAmount`, then
  replaces any existing budget.
* `budget` (`BudgetCommand`) shows `BudgetManager#getSummary`: the budget, what
  was spent in the current period, and what is left.

**Working out what was spent.** Each `BudgetPeriod` knows the first day of the
current period: today for `DAILY`, this Monday for `WEEKLY`
(`TemporalAdjusters.previousOrSame(MONDAY)`), and the 1st of the month for
`MONTHLY`. `BudgetManager#getSpent` then calls
`ExpensesManager#getTotalCostBetween(startDate, today)`, which adds up the
expenses dated in that range.

**Warnings.** After each `log-expense`, `BudgetManager#getStatusAfterSpending`
returns:

* `Warning: you are $X over your ... budget!` if spending is over the budget;
* otherwise `Careful: you have used N% of your ... budget.` plus the remaining
  amount, if 80% (`NEAR_LIMIT_RATIO`) or more is used;
* otherwise just the remaining amount.

A tiny tolerance is allowed when checking whether the user is over the budget,
so floating-point errors never count as being over. The budget is saved to
`data/budget.txt` in the format `PERIOD|AMOUNT`, e.g. `weekly|50.00`.

#### Design considerations

**Aspect: How the amount spent is tracked**

* **Alternative 1 (current choice):** Calculate it each time from the dated
  expenses in `ExpensesManager`.
    * Pros: Editing, deleting or back-dating an expense is reflected in the
      budget straight away, and the budget and the expense list can never
      disagree. A new period starts automatically.
    * Cons: Goes through all expenses each time; fast enough for thousands of
      expenses.
* **Alternative 2:** Keep a running total in `BudgetManager`, updated by each
  expense command.
    * Pros: No need to go through the expenses.
    * Cons: Every expense command must update the total correctly, and the total
      must be reset when a new period starts. Easy to get wrong.

**Aspect: How many budgets a user can have**

* **Alternative 1 (current choice):** One budget at a time; `set-budget`
  replaces it.
    * Pros: Simple commands and messages.
    * Cons: Users cannot track, for example, a weekly and a monthly limit together.
* **Alternative 2:** One budget per period.
    * Pros: More flexible.
    * Cons: Needs extra commands to choose and delete budgets, and longer
      messages after each expense.

### Navigation component

**API:** `MenuNavigator.java`, `FoodDirectory.java`

The navigation component allows users to browse food courts, food stalls, and
menu items.

`FoodDirectory` stores the food data as a list of `FoodCourt` records. Each
`FoodCourt` contains a list of `FoodStall` records, and each stall contains a
list of `FoodItem` records. A `FoodItem` stores the dish name and its price.

`MenuNavigator` controls the user's current browsing position. It stores the
current navigation stage, the selected food court, and the selected stall.
The food data is read-only during execution.

The navigator is used by `BudgetBite`. When the user enters `location`, the
application starts the navigation flow. While navigation is active, numeric
inputs and `back` are sent to `MenuNavigator`. The `home` command returns the
user to the main menu from any navigation stage.

### Food navigation

#### Implementation

`MenuNavigator` uses four stages to represent the user's current position:

* `INACTIVE`: The user is not browsing food locations.
* `SELECTING_LOCATION`: The user is choosing a food court.
* `SELECTING_STALL`: The user is choosing a stall within the selected food court.
* `SHOWING_MENU`: The menu of the selected stall is displayed.

The normal browsing flow is as follows:

1. The user enters `location`.
2. `MenuNavigator#start()` changes the stage to `SELECTING_LOCATION` and
   displays the available food courts.
3. The user enters a number to select a food court.
4. The navigator changes the stage to `SELECTING_STALL` and displays the
   stalls in that food court.
5. The user enters a number to select a stall.
6. The navigator changes the stage to `SHOWING_MENU` and displays the stall's
   menu and prices.

The `back` command moves the user one level backwards:

* From `SHOWING_MENU`, it returns to the stall list.
* From `SELECTING_STALL`, it returns to the food-court list.
* From `SELECTING_LOCATION`, it returns to the main menu.

The `home` command clears the selected food court and stall, changes the stage
to `INACTIVE`, and returns the user to the main menu.

Users can also navigate directly to a stall using the format:

`FOOD_COURT / STALL`

For example:

`PGPR / Chicken Rice`

The food court and stall names are matched without regard to letter case. The
input is split around the slash, and the navigator checks that the food court
and stall exist before displaying the menu. Inputs with missing parts or more
than one slash are rejected with a format error.

Invalid numeric selections do not change the current navigation stage. Instead,
the navigator returns an error message and waits for another input.

#### Design considerations

**Aspect: Representing the browsing position**

* **Alternative 1 (current choice):** Use an explicit `Stage` enum with
  `INACTIVE`, `SELECTING_LOCATION`, `SELECTING_STALL`, and `SHOWING_MENU`.
    * Pros: Each input is interpreted according to the user's current position,
      making the navigation flow clear and easy to test.
    * Cons: Adding another navigation level requires adding another stage.
* **Alternative 2:** Store only the selected food court and stall, without an
  explicit stage.
    * Pros: Fewer state variables.
    * Cons: The application would need to infer the current position from nullable
      fields, which makes invalid states easier to create.

The explicit-stage design was chosen because the navigation flow is hierarchical
and each stage accepts different inputs.

**Aspect: Selecting a stall**

* **Alternative 1 (current choice):** Let users select food courts and stalls
  using numbered lists.
    * Pros: Users do not need to remember the exact spelling of names.
    * Cons: Users must go through multiple steps.
* **Alternative 2:** Require users to type the food court and stall names every
  time.
    * Pros: Direct input can be faster.
    * Cons: Names are harder to remember and more prone to typing errors.

The current implementation supports both numbered browsing and direct
navigation using `FOOD_COURT / STALL`.

### Use case: UC01 - Browse food menus

**MSS (Main Success Scenario)**

1. User enters `location`.
2. BudgetBite displays the available food courts.
3. User selects a food court by entering its number.
4. BudgetBite displays the stalls in the selected food court.
5. User selects a stall by entering its number.
6. BudgetBite displays the stall's menu and prices.

   Use case ends.

### Use case: UC02 - Log an expense

**MSS (Main Success Scenario)**

1. User enters the food name and cost, and optionally the date.
2. BudgetBite records the expense and shows the new total.

   Use case ends.

**Extensions**

* 1a. The cost is missing, not a positive number, has more than 2 decimal
  places, or is more than $1000.

    * 1a1. BudgetBite shows an error message and the correct format.

      Use case ends.

* 1b. The date is not a real date in the format YYYY-MM-DD, or is in the future.

    * 1b1. BudgetBite shows an error message and the correct format.

      Use case ends.

* 2a. The user has set a budget.

    * 2a1. BudgetBite also shows how much of the budget is left, with a warning
      if 80% or more is used, or if the budget is exceeded.

      Use case ends.

### Use case: UC03 - Set a food budget

**MSS (Main Success Scenario)**

1. User enters a period (daily, weekly or monthly) and an amount.
2. BudgetBite saves the budget, replacing any previous one, and shows how much
   was already spent and is left in the current period.

   Use case ends.

**Extensions**

* 1a. The period is not daily, weekly or monthly, or the amount is invalid.

    * 1a1. BudgetBite shows an error message and the correct format.

      Use case ends.

### Use case: UC04 - Delete an expense

**MSS (Main Success Scenario)**

1. User requests to list expenses.
2. BudgetBite shows the expenses, numbered.
3. User requests to delete a specific expense in the list.
4. BudgetBite deletes the expense and shows how many are left.

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is not a positive whole number, or is not in the list.

    * 3a1. BudgetBite shows an error message.

      Use case resumes at step 2.

**Extensions**

* 1a. User enters a food court and stall directly using
  `FOOD_COURT / STALL`.

    * 1a1. BudgetBite displays the selected stall's menu.

      Use case ends.

* 3a. The selected food court number is invalid.

    * 3a1. BudgetBite displays an error message.

      Use case resumes at step 3.

* 5a. The selected stall number is invalid.

    * 5a1. BudgetBite displays an error message.

      Use case resumes at step 5.

* 1b. The direct-navigation format is invalid, or the stall does not exist.

    * 1b1. BudgetBite displays an error message.

      Use case ends.

## Product scope

### Target user profile

BudgetBite is designed for students who eat at campus food courts and want to
make quick food-related decisions.

The target users:

* are familiar with basic command-line applications;
* want to browse available food courts and stalls;
* want to view menu items and prices before choosing a meal;
* may want to record food ratings and expenses; and
* may want to manage their food spending with a budget.

### Value proposition

BudgetBite brings campus food navigation, menu prices, food ratings, expense
tracking, and budget tracking into one command-line application.

For the food-navigation feature, users can browse food courts and stalls,
inspect available menu items and prices, or jump directly to a stall using its
name. This helps students decide where and what to eat without manually
searching through separate menus or websites.

### Food-navigation scope

The food-navigation feature includes:

* browsing the available food courts;
* browsing the stalls inside a selected food court;
* viewing menu items and their prices;
* navigating backwards through the browsing levels;
* returning to the main menu with `home`; and
* directly opening a stall menu using `FOOD_COURT / STALL`.

The feature does not include live menu updates, stall opening hours, food
ordering, payment, delivery, GPS directions, or real-time stock availability.

### Value proposition

{Describe the value proposition: what problem does it solve?}

## User Stories

|Version| As a ... | I want to ... | So that I can ...|
|--------|----------|---------------|------------------|
|v1.0|new user|see usage instructions|refer to them when I forget how to use the application|
| v1.0 | student looking for food | browse the food courts and stalls on campus | decide where to eat |
| v1.0 | student looking for food | view the dishes and prices at a stall | choose a dish within my budget |
| v1.0 | frequent user | jump directly to a stall's menu by name | skip the step-by-step browsing process |
|| v1.0 | student | log what I spend on food | know where my money goes |
| v1.0 | student | see all my expenses and the total | know how much I have spent |
| v1.0 | user who makes mistakes | edit or delete an expense | fix wrong entries |
| v2.0 | student on a budget | set a daily, weekly or monthly food budget | control my spending |
| v2.0 | student on a budget | see how much of my budget is left | decide how much I can spend on my next meal |
| v2.0 | student on a budget | be warned when I am close to or over my budget | cut back before I overspend |
| v2.0 | student who forgets to log | log an expense with an earlier date | keep my records and budget accurate ||

## Non-Functional Requirementsß

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

### Browsing food

1. Enter `location`.

   Expected: `Available locations:` is shown, followed by `PGPR` and `YIH`.

2. Enter `1`.

   Expected: `Stalls in PGPR:` is shown, followed by `Vietnamese` and
   `Chicken Rice`.

3. Enter `1`.

   Expected: `Menu for Vietnamese:` is shown, including `Beef Pho - $5.50`
   and `Spring Roll - $2.50`.

4. Enter `back`.

   Expected: The stall list for PGPR is shown again.

5. Enter `back`.

   Expected: The food-court list is shown again.

6. Enter `home`.

   Expected: `Returned to the main menu.` is shown.

7. Enter `pgpr / chicken rice`.

   Expected: The Chicken Rice menu is displayed. This confirms that direct
   navigation is case-insensitive.

8. Test invalid inputs by entering:

    * `location`, then `99`
    * `location`, then `abc`
    * `PGPR /`
    * `PGPR / Unknown Stall`
    * `PGPR / Chicken Rice / Roast Chicken Rice`

   Expected: An appropriate error message is shown and the application
   continues running.

### Tracking expenses

1. Enter `log-expense Chicken Rice /cost 4.50`, then
   `log-expense Teh /cost 1.20 /date 2026-10-20`, then `list-expenses`.

   Expected: Both expenses are listed with their dates, and the total is $5.70.

2. Enter `edit-expense 1 /cost 5`, then `delete-expense 2`.

   Expected: Expense 1 now costs $5.00 and keeps its date; expense 2 is deleted.

3. Test invalid inputs by entering:

    * `log-expense Kopi /cost 0.0001`
    * `log-expense Kopi /cost 1001`
    * `log-expense Kopi /cost -2`
    * `log-expense Kopi /cost 1e2`
    * `log-expense /cost 2`
    * `log-expense A|B /cost 2`
    * `log-expense Kopi /cost 2 /date 2099-01-01`
    * `log-expense Kopi /cost 2 /date 2026-02-30`
    * `log-expense Kopi /date 2026-10-20 /cost 2`
    * `edit-expense 99 /cost 2`
    * `delete-expense 0`

   Expected: An error message with the correct format is shown for each, and no
   expense is added or changed.

### Budget

1. Enter `budget`.

   Expected: `No budget set yet. Try: set-budget weekly 50`

2. Enter `set-budget weekly 20`.

   Expected: The weekly budget, what was spent since Monday, and what is left.

3. Log expenses dated today until you pass $16, then $20.

   Expected: After each one, the remaining amount is shown; a `Careful` message
   appears once 80% is used, and a `Warning` once over $20.

4. Enter `log-expense Old Meal /cost 10 /date` followed by a date in an earlier
   week.

   Expected: The expense is logged, but the remaining weekly budget does not change.

5. Close the app without `bye`, start it again, and enter `budget`.

   Expected: The budget and the amount spent are the same as before.

6. Test invalid inputs by entering:

    * `set-budget yearly 50`
    * `set-budget weekly`
    * `set-budget weekly abc`
    * `set-budget weekly 0`
    * `set-budget weekly 100001`

   Expected: An error message with the correct format is shown for each, and the
   budget is unchanged.
