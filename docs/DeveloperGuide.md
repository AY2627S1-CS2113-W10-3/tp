# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}

### Expense tracking

The expense feature lets users log, list, edit and delete food expenses (`log-expense`, `list-expenses`, `edit-expense`, `delete-expense`).
Each expense is an `Expense` object holding a food name and a positive cost, and all expenses are kept in an `ExpensesManager`, which handles adding, editing, deleting and totalling them.
When the user enters a command, `Parser` creates the matching command object (e.g. `LogExpenseCommand`), which uses `ExpenseArguments` to split the input at `/cost` and read the index.
Indexes shown to the user start from 1; `ExpensesManager` converts them internally and throws a `BudgetBiteException` if an index is out of range, so invalid input never crashes the app.
After every command, `StorageFile` saves all expenses to `data/expenses.txt` in the format `FOOD_NAME|COST`, and they are loaded back when the app starts.

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

**MSS (Main Success Scenario**

1. User enters `location`.
2. BudgetBite displays the available food courts.
3. User selects a food court by entering its number.
4. BudgetBite displays the stalls in the selected food court.
5. User selects a stall by entering its number.
6. BudgetBite displays the stall's menu and prices.

   Use case ends.

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
|v2.0|user|find a to-do item by name|locate a to-do without having to go through the entire list|

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
