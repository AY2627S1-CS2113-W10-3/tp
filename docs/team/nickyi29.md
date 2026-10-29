# Ti Kyi Kyaw - Project Portfolio Page

## Overview

BudgetBite is a command-line app for NUS students to find food on campus, rate the dishes they try,
and keep their food spending within a budget.

## Summary of Contributions:

### Code contributed: https://nus-cs2113-ay2627-s1.github.io/tp-dashboard/?search=W10-3&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-09-18T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=Nickyi29&tabRepo=AY2627S1-CS2113-W10-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false))

### Features implemented:

* **Expense tracking** (PRs #19, #27, #28, #30): `log-expense`, `list-expenses`, `edit-expense` and `delete-expense`.
    * What it does: lets users record what they spend on food, see every expense with a running total, and fix
      mistakes by editing or deleting entries.
    * Highlights: all expense commands share one parsing class (`ExpenseArguments`), so they accept input and
      report errors the same way. Indexes and costs are validated, and every error shows the correct format.

* **Food budget** (PR #45): `set-budget PERIOD AMOUNT` and `budget`.
    * What it does: users set a daily, weekly or monthly budget. After every expense, the app shows how much is
      left, warns at 80% used, and warns again when the budget is exceeded.
    * Justification: this is the core "budget" part of BudgetBite. It turns a list of expenses into something
      that helps students control their spending.
    * Highlights: the amount spent is always calculated from the dated expenses rather than stored as a running
      total, so editing, deleting or back-dating an expense updates the budget immediately and a new week or
      month starts automatically.

* **Expense dates** (PR #43): added a date to every expense, with an optional `/date YYYY-MM-DD`, and rejected
  impossible or future dates.

* **Cost limits** (PRs #42, #47): costs must be more than $0, at most $1000 and have at most 2 decimal places,
  checked through a shared `Money` class that budgets also use.

* **Reliable data saving** (PR #35): the app saves to a `data` folder next to the JAR after every command, and no
  longer crashes on unexpected errors or when input ends.

* **Corrupted data handling** (PR #29): loading skips corrupted lines in the data files instead of crashing.

* **Tests** (PRs #19, #45, #47): JUnit tests for expenses, budgets, storage and command parsing, including a
  `ParserTest` that checks every command word.

### Documentation:

* **User Guide:**
    * Wrote the Expense Tracking and Budget sections (`log-expense`, `list-expenses`, `edit-expense`,
      `delete-expense`, `set-budget`, `budget`) (PR #50).

* **Developer Guide:**
    * Wrote the Model component, Expense tracking and Budget tracking sections, with design considerations
      (PRs #36, #50).
    * Drew the Model class diagram and the `log-expense` sequence diagram.
    * Wrote use cases UC02 to UC04, the expense and budget user stories, and the manual testing steps for
      expenses and budgets.

### Project management 

* Renamed the release JAR to `BudgetBite.jar`.
* Fixed CI failures caused by outdated tests and Checkstyle violations (PRs #46, #47).
* Managed releases v1.0 - v2.1

### Community:  

* Reviewed the Developer Guide of team CS2113-W13-1.

### Tools:

* Used an AI assistant (Claude) to help draft parts of the code, tests and documentation. I reviewed, tested
  and edited all of it.
