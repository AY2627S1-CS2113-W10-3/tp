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

## Product scope
### Target user profile

{Describe the target user profile}

### Value proposition

{Describe the value proposition: what problem does it solve?}

## User Stories

|Version| As a ... | I want to ... | So that I can ...|
|--------|----------|---------------|------------------|
|v1.0|new user|see usage instructions|refer to them when I forget how to use the application|
|v2.0|user|find a to-do item by name|locate a to-do without having to go through the entire list|

## Non-Functional Requirements

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
