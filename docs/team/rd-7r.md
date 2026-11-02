# Mun Jia Hao - Project Portfolio Page

## Overview

I contributed to BudgetBite's food-navigation feature. BudgetBite is a
command-line application that helps students browse campus food courts, food
stalls, menus, and prices.

## Summary of Contributions

#### Code contributed: [Here](https://nus-cs2113-ay2627-s1.github.io/tp-dashboard/?search=W10-3&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-09-18T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=Rd-7r&tabRepo=AY2627S1-CS2113-W10-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

### Features implemented

* **Food navigation:** Implemented the `location` command, allowing users to
  browse food courts, stalls, and menus using numbered selections.

* **Direct stall navigation:** Implemented direct navigation using the format
  `FOOD_COURT / STALL`, such as `PGPR / Chicken Rice`.

* **Navigation controls:** Implemented the `back` command for returning to the
  previous browsing level and the `home` command for returning directly to the
  main menu.

* **Navigation state management:** Designed the `MenuNavigator` using the
  following stages:
  `INACTIVE`, `SELECTING_LOCATION`, `SELECTING_STALL`, and `SHOWING_MENU`.

  This system was later integrated with the rating system to add a new `RATING_ITEM` stage
  that handles rating a selected dish and validating the user's score.

* **Food data loading:** Implemented `FoodDataLoader` to load food-court, stall,
  menu-item, and price data from `src/main/resources/food.txt`. Added validation
  for malformed records, blank fields, invalid prices, and negative prices.

  the data was previously hardcoded into the code, but a text file was used to accommodate a larger data set

* **Food data model:** Added the food-navigation classes:
  `FoodDirectory`, `FoodCourt`, `FoodStall`, and `FoodItem`.

* **Input validation:** Added validation for invalid food-court numbers,
  invalid stall numbers, malformed direct-navigation paths, and unknown stalls.
  Invalid navigation input displays an error message without terminating the
  application.

* **Testing:** Added and updated JUnit tests for `FoodDataLoader`,
  `FoodDirectory`, and `MenuNavigator`, covering data loading, validation, and
  the food-navigation flow.

### Documentation

* **User Guide:**
    * Wrote the introduction, prerequisites and Quick Start sections.
    * Documented the food-navigation commands: `location`,
      `FOOD_COURT / STALL`, `back`, and `home`.
    * Added examples, expected outputs, as well as the command summary.

* **Developer Guide:**
    * Wrote the Navigation component and Food navigation sections
    * Documented `FoodDataLoader`, the `food.txt` format, navigation states,
      transitions, input validation, and food-navigation design decisions.
    * Added the `NavigationStateDiagram`.
    * Added the food-navigation use case, user stories, and manual-testing steps.

### Other Contributions

* **Discussions/suggestions:** 
    * Discussed the overall system structure with the team and suggested changes where needed
    * Recommended changes to the rating system to improve user experience as it links closely to the food-navigation system

### Tools

* Used IntelliJ IDEA and PlantUML to create and render the navigation diagram.
* Used an AI assistant (codex) to refine parts of the documentation and code testing. I have reviewed and edited all outputs to fit my own judgement.