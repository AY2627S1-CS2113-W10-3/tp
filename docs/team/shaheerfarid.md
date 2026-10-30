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

* **Rating management:** Implemented the `list-rating`, `edit-rating`, and
  `delete-rating` commands for viewing, updating, and removing ratings.

* **Rating validation:** Added validation for rating scores, rating indexes,
  and invalid command arguments, with clear error messages for invalid input.

* **Rating storage:** Added support for saving and loading ratings so that
  users' ratings persist between application sessions.

### Tests

* Added JUnit tests for rating creation, listing, editing, deletion, score
  validation, and rating-manager behavior.

### Documentation

* Documented the rating commands and their usage in the User Guide.
* Documented the rating component, rating commands, and relevant design
  decisions in the Developer Guide.
* Added class and sequence diagrams for the rating feature.

### Tools

* Used IntelliJ IDEA, Gradle, Git, and PlantUML during development.
* Used an AI assistant to help refine parts of the code and documentation.
  All generated output was reviewed and edited before being included.
