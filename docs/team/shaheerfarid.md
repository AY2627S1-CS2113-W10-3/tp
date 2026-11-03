# Muhammad Shaheer Bin Farid - Project Portfolio Page

## Overview

BudgetBite is a command-line application that helps NUS students discover
food on campus, rate the dishes they try, and keep track of their food
expenses.

## Summary of Contributions

### Code contributed

[View my code contributions on the TP dashboard](https://nus-cs2113-ay2627-s1.github.io/tp-dashboard/?search=W10-3&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-09-18T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=shaheerfarid&tabRepo=AY2627S1-CS2113-W10-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

### Features implemented

* **Food ratings:** Implemented the `rate` command, allowing users to rate a
  food item with a score from 0 to 10.

* **Rating management:** Implemented the `list-ratings`, `edit-rating`, and
  `delete-rating` commands for viewing, updating, and removing ratings.

* **Ratings checked against the food catalogue:** `rate` now takes
  `/location`, `/stall` and `/food`, and only accepts names that exist in the
  food data, matched ignoring case. Unknown names show the valid options at
  that level, so ratings always match what users see when browsing.

* **Rating while browsing:** Extended the navigation flow so stall menus are
  numbered, and choosing a dish asks for a score, then returns to the menu.
  Added the `RATING_ITEM` navigation stage for this.

* **Rating suggestions:** Ending a `rate` command with `?` (or Tab) opens the
  numbered list of locations, stalls or dishes for the field being typed, e.g.
  `rate /location PGPR /stall ch ?`. Users then continue by number instead of
  retyping the command.

* **No duplicate ratings:** Rating a dish again updates its score instead of
  adding a second rating.

* **Rating validation:** Added validation for rating scores, rating indexes,
  and invalid command arguments, with clear error messages for invalid input.
  Scores must be plain numbers from 0 to 10 with at most 1 decimal place.

* **Rating storage:** Added support for saving and loading ratings so that
  users' ratings persist between application sessions. Ratings are saved as
  `LOCATION|STALL|FOOD|SCORE`, and older files without a location still load.

* **Bug fixes:** Fixed `NaN` scores crashing `rate`, a `|` in a name
  corrupting the ratings file, and scores such as `7.25` being saved as `7.3`.

### Tests

* Added JUnit tests for rating creation, listing, editing, deletion, score
  validation, and rating-manager behavior.
* Added tests for catalogue validation, rating from the menu, suggestions and
  re-rating (`RateCommandTest`, `MenuNavigatorTest`).

### Documentation

* Documented the rating commands and their usage in the User Guide, including
  rating while browsing and the `?`/Tab suggestions.
* Documented the rating component, rating commands, and relevant design
  decisions in the Developer Guide, e.g. why ratings are checked against the
  catalogue and why suggestions use `?`/Tab instead of live Tab completion.
* Added class and sequence diagrams for the rating feature, and the rating
  stage in the navigation state diagram.

### Tools

* Used IntelliJ IDEA, Gradle, Git, and PlantUML during development.
* Used an AI assistant to help refine parts of the code and documentation.
  All generated output was reviewed and edited before being included.
