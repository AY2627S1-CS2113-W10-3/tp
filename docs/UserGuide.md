# User Guide

# BudgetBite

BudgetBite is a command-line application that helps students decide where to
eat and manage their food-related spending.

Users can browse campus food courts and stalls, view menu items and prices,
record food ratings, track expenses, and manage their food budget.

## Quick Start

### Prerequisites

Java 25 or later must be installed.

Check your Java version with:

```text
java -version
```

### Running the application

1. Download the latest `BudgetBite.jar` file [insert link here]
2. Open a command-line terminal in the folder containing the JAR file.
3. Run the application:

```text
java -jar BudgetBite.jar
```

The application will display a welcome message and wait for a command.

4. Enter `help` to view all available commands:

```text
help
```

5. Enter `bye` to exit the application:

```text
bye
```

## Features

### Food Navigation

#### Browse food courts: `location`

The `location` command shows a list of available food stalls.

Format:

```text
location
```

Example:

```text
location
```

Expected output:

```text
Available locations:
1. PGPR
2. YIH
```

Enter the index of the food court to view its corresponding stalls.

For example:

```text
1
```

Expected output:

```text
Stalls in PGPR:
1. Vietnamese
2. Chicken Rice
```

Enter the index of a stall to view its corresponding menu items.

For example:

```text
1
```

Expected output:

```text
Menu for Vietnamese:
Beef Pho - $5.50
Spring Roll - $2.50
```

### Return to the previous level: `back`

The `back` command returns to the previous browsing level.

When viewing a stall menu, `back` returns to the list of stalls.

When viewing the stalls in a food court, `back` returns to the list of food
courts.

When viewing the list of food courts, `back` returns to the main menu.

Format:

```text
back
```

### Return to the main menu: `home`

The `home` command immediately exits food navigation and returns to the main
menu.

Format:

```text
home
```

This command can be used from any food-navigation stage.

Expected output:

```text
Returned to the main menu.
```

### Direct navigation to a stall

Users can open a stall menu directly without browsing through the numbered
lists.

Format:

```text
FOOD_COURT / STALL
```

Example:

```text
PGPR / Chicken Rice
```

Expected output:

```text
Menu for Chicken Rice:
Char Siew Rice - $3.30
Roast Chicken Rice - $3.30
Toufu Vegetable Rice Set - $2.80
```

Food court and stall names are case-insensitive. For example, the following
inputs refer to the same location:

```text
PGPR / Chicken Rice
pgpr / chicken rice
```

The direct-navigation format must contain exactly one food court and one stall.
Inputs with missing parts or additional path sections are rejected.

### Expense Tracking

#### Log an expense: `log-expense`

Records how much you spent on food, and shows your new total. If you have set
a budget, BudgetBite also shows how much of it is left.

Format:

```text
log-expense FOOD_NAME /cost AMOUNT [/date YYYY-MM-DD]
```

* `AMOUNT` must be more than 0 and at most 1000, with at most 2 decimal places.
* `/date` is optional. Without it, the expense is dated today. The date cannot
  be in the future, and `/date` must come after `/cost`.
* `FOOD_NAME` cannot be empty and cannot contain the `|` character.

Example:

```text
log-expense Chicken Rice /cost 4.50
```

Expected output:

```text
Logged: Chicken Rice - $4.50 (2026-10-27)
Total spent: $4.50
```

To log a meal from an earlier day:

```text
log-expense Teh /cost 1.20 /date 2026-10-20
```

#### List all expenses: `list-expenses`

Shows all your expenses, numbered, with their dates and the total spent.

Format:

```text
list-expenses
```

Expected output:

```text
Your expenses:
1. Chicken Rice - $4.50 (2026-10-27)
2. Teh - $1.20 (2026-10-20)
Total spent: $5.70
```

#### Edit an expense: `edit-expense`

Changes the cost of an expense. Its food name and date stay the same.

Format:

```text
edit-expense INDEX /cost AMOUNT
```

* `INDEX` is the number shown by `list-expenses`. It must be a positive whole
  number.
* `AMOUNT` follows the same rules as in `log-expense`.

Example:

```text
edit-expense 1 /cost 5
```

Expected output:

```text
Updated expense 1: Chicken Rice - $5.00 (2026-10-27)
```

#### Delete an expense: `delete-expense`

Deletes an expense.

Format:

```text
delete-expense INDEX
```

Example:

```text
delete-expense 2
```

Expected output:

```text
Deleted: Teh - $1.20 (2026-10-20)
Expenses left: 1
```

### Budget

#### Set a food budget: `set-budget`

Sets how much you want to spend on food each day, week or month. Setting a
budget again replaces the old one.

Format:

```text
set-budget PERIOD AMOUNT
```

* `PERIOD` is `daily`, `weekly` or `monthly` (not case-sensitive).
    * A daily budget counts today's expenses.
    * A weekly budget counts expenses from Monday of the current week to today.
    * A monthly budget counts expenses from the 1st of the current month to today.
* `AMOUNT` must be more than 0 and at most 100000, with at most 2 decimal places.

Example:

```text
set-budget weekly 50
```

Expected output:

```text
Budget set.
Weekly budget: $50.00
Spent this week: $5.00
Remaining this week: $45.00
```

Once a budget is set, every `log-expense` also shows how much is left. A
`Careful` message appears once you have used 80% of the budget, and a
`Warning` once you go over it:

```text
log-expense Laksa /cost 40
Logged: Laksa - $40.00 (2026-10-27)
Total spent: $45.00
Careful: you have used 90% of your weekly budget.
Remaining this week: $5.00
```

```text
log-expense Mala /cost 10
Logged: Mala - $10.00 (2026-10-27)
Total spent: $55.00
Warning: you are $5.00 over your weekly budget!
```

#### View your budget: `budget`

Shows your budget, how much you have spent in the current period, and how much
is left.

Format:

```text
budget
```

Expected output:

```text
Weekly budget: $50.00
Spent this week: $45.00
Remaining this week: $5.00
```

If no budget is set, BudgetBite shows
`No budget set yet. Try: set-budget weekly 50`.

## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: {your answer here}

## Command Summary

| Command | Format | Purpose |
|---|---|---|
| `help` | `help` | Display all available commands |
| `location` | `location` | Browse available food courts |
| Direct navigation | `FOOD_COURT / STALL` | Open a stall menu directly |
| `back` | `back` | Return to the previous navigation level |
| `home` | `home` | Return to the main menu |
| `rate` | `rate /stall STALL /food FOOD /score SCORE` | Add a food rating |
| `list-ratings` | `list-ratings` | Display all ratings |
| `edit-rating` | `edit-rating INDEX /score SCORE` | Edit a rating |
| `delete-rating` | `delete-rating INDEX` | Delete a rating |
| `log-expense` | `log-expense FOOD_NAME /cost AMOUNT` | Record an expense |
| `list-expenses` | `list-expenses` | Display all expenses |
| `edit-expense` | `edit-expense INDEX /cost AMOUNT` | Edit an expense |
| `delete-expense` | `delete-expense INDEX` | Delete an expense |
| `set-budget` | `set-budget PERIOD AMOUNT` | Set a food budget |
| `budget` | `budget` | Display the current budget status |
| `bye` | `bye` | Exit the application |

## Documentation

* [User Guide](UserGuide.md)
* [Developer Guide](DeveloperGuide.md)
* [About Us](AboutUs.md)
