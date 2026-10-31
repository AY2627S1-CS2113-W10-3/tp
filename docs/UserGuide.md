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

1. Download the latest `BudgetBite.jar` from [here](https://github.com/AY2627S1-CS2113-W10-3/tp/releases).
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

The `location` command shows a list of available food courts.

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
3. TECHNO EDGE
4. THE TERRACE
5. THE DECK
6. FRONTIER
7. FLAVOURS
8. FINE FOOD
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
3. PGP Western
4. Chef Wok
```

Enter the index of a stall to view its corresponding menu items.

For example:

```text
1
```

Expected output:

```text
Menu for Vietnamese:
1. Beef Pho - $5.50
2. Spring Roll - $2.50
Enter a food number to rate it, or 'back' to see the stalls.
```

#### Rate a dish while browsing

While a stall's menu is shown, enter the number of a dish to rate it.
BudgetBite then asks for a score from 0 to 10, with at most 1 decimal place.
After you rate it, the menu is shown again so you can rate another dish.

For example, after opening the Vietnamese menu above:

```text
1
```

Expected output:

```text
Rate Beef Pho - $5.50 (Vietnamese, PGPR)
Enter a score from 0 to 10, or 'back' to cancel:
```

Then enter the score:

```text
8
```

Expected output:

```text
Rated: Beef Pho (Vietnamese, PGPR) - 8.0/10

Menu for Vietnamese:
1. Beef Pho - $5.50
2. Spring Roll - $2.50
Enter a food number to rate it, or 'back' to see the stalls.
```

* If the score is invalid, BudgetBite shows an error and asks again.
* If you have already rated the dish, its rating is updated, and BudgetBite
  shows `Updated: ...` with the old score.
* Enter `back` to cancel without rating, or `home` to leave browsing.
* Other commands, such as `list-ratings`, still work while you are asked for a
  score.

This gives the same result as the `rate` command, without typing the names.

### Return to the previous level: `back`

The `back` command returns to the previous browsing level.

When asked for a score, `back` cancels the rating and shows the stall menu again.

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
1. Char Siew Rice - $3.30
2. Roast Chicken Rice - $3.30
3. Toufu Vegetable Rice Set - $2.80
Enter a food number to rate it, or 'back' to see the stalls.
```

You can then rate a dish by its number, as in
[Rate a dish while browsing](#rate-a-dish-while-browsing).

If the food court exists but the stall does not, BudgetBite shows:

```text
Unknown stall in PGPR: Unknown Stall
Please check the stall name and try again.
```

Food court and stall names are case-insensitive. For example, the following
inputs refer to the same location:

```text
PGPR / Chicken Rice
pgpr / chicken rice
```

The direct-navigation format must contain exactly one food court and one stall.
Inputs with missing parts or additional path sections are rejected.

### Food Ratings

#### Rate a food item: `rate`

Gives a score from 0 to 10 to a food item from a stall, so you remember what
you liked. If you don't know the exact names, browse with `location` and
[rate the dish from its menu](#rate-a-dish-while-browsing) instead.

Format:

```text
rate /location LOCATION /stall STALL /food FOOD /score SCORE
```

* All four fields are needed, and each can be given only once. They can be in
  any order.
* `LOCATION`, `STALL` and `FOOD` must be a food court, a stall in that food
  court, and a dish on that stall's menu, as shown by `location`. You cannot
  rate places that are not in BudgetBite. Letter case does not matter, e.g.
  `pgpr` works for `PGPR`.
* If a name is not found, BudgetBite lists the valid names so you can try
  again.
* `SCORE` must be a number from 0 to 10 with at most 1 decimal place, e.g. `7.5`.
* If you have already rated that dish, its rating is updated instead of a
  second one being added, e.g.
  `Updated: Roast Chicken Rice (Chicken Rice, PGPR) - 9.0/10 (was 8.0/10)`.
  This also applies when rating from a menu.

Example:

```text
rate /location PGPR /stall Chicken Rice /food Roast Chicken Rice /score 8
```

Expected output:

```text
Rated: Roast Chicken Rice (Chicken Rice, PGPR) - 8.0/10
```

**Not sure of a name?** End the command with `?`, or press `Tab`, and then
press Enter. BudgetBite lists what you can type for the field you are on,
keeping only names that contain what you have typed so far. Nothing is rated;
type the full command afterwards.

```text
rate /location ?
Locations: PGPR, YIH, TECHNO EDGE, THE TERRACE, THE DECK, FRONTIER, FLAVOURS, FINE FOOD

rate /location PGPR /stall ch ?
Stalls in PGPR matching 'ch': Chicken Rice, Chef Wok

rate /location PGPR /stall Chicken Rice /food ?
Menu of Chicken Rice: Char Siew Rice, Roast Chicken Rice, Toufu Vegetable Rice Set
```

Unlike a terminal, `Tab` does not fill in the name for you; you still need to
press Enter to see the list.

Example of a name that is not found:

```text
rate /location YIH /stall Chicken Rice /food Roast Chicken Rice /score 8
```

Expected output:

```text
Unknown stall in YIH: Chicken Rice
Stalls in YIH: Tempu Ramen, Indian, Wok Express, Mini Wok
Format: rate /location LOCATION /stall STALL /food FOOD /score SCORE
Example: rate /location PGPR /stall Chicken Rice /food Roast Chicken Rice /score 8
Tip: end with ? (or press Tab) and Enter to see the options, e.g. rate /location PGPR /stall ?
```

#### List all ratings: `list-ratings`

Shows all your ratings, numbered.

Format:

```text
list-ratings
```

Expected output:

```text
Your ratings:
1. Roast Chicken Rice (Chicken Rice, PGPR) - 8.0/10
2. Beef Pho (Vietnamese, PGPR) - 7.5/10
```

If you have no ratings yet, BudgetBite shows
`No ratings yet. Try: rate /location PGPR /stall Chicken Rice /food Roast Chicken Rice /score 8`.

Ratings saved by older versions of BudgetBite did not record the food court.
They are kept, and shown with `Unknown location`.

#### Edit a rating: `edit-rating`

Changes the score of a rating. Its location, stall and food stay the same.

Format:

```text
edit-rating INDEX /score SCORE
```

* `INDEX` is the number shown by `list-ratings`. It must be a positive whole
  number.
* `SCORE` follows the same rules as in `rate`.

Example:

```text
edit-rating 1 /score 9
```

Expected output:

```text
Updated rating 1: Roast Chicken Rice (Chicken Rice, PGPR) - 9.0/10
```

#### Delete a rating: `delete-rating`

Deletes a rating.

Format:

```text
delete-rating INDEX
```

Example:

```text
delete-rating 2
```

Expected output:

```text
Deleted: Beef Pho (Vietnamese, PGPR) - 7.5/10
Ratings left: 1
```

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

## Known issues

* **Ratings saved by older versions of BudgetBite** are shown with
  `Unknown location`, and any repeated ratings among them are kept. Use
  `delete-rating` to remove the ones you no longer want.

## Command Summary

| Command | Format | Purpose |
|---|---|---|
| `help` | `help` | Display all available commands |
| `location` | `location` | Browse available food courts |
| Direct navigation | `FOOD_COURT / STALL` | Open a stall menu directly |
| `back` | `back` | Return to the previous navigation level |
| `home` | `home` | Return to the main menu |
| `rate` | `rate /location LOCATION /stall STALL /food FOOD /score SCORE` | Add a food rating (end with `?` or Tab to see the options) |
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
