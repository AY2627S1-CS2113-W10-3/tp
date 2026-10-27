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
