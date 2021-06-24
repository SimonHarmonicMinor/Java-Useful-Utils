# Contributing Guide

Do you want to improve the library?
Would you like to see yourself in the `README` *Authors* section?
That's great! Here is a guide how to contribute.


1. Submit a new [issue](https://github.com/SimonHarmonicMinor/Java-Useful-Utils/issues) or choose the existing one.
1. Fork the repository.
1. Checkout a new branch from `master`
1. Make the changes and run `./gradlew build` command. If it succeeds, then you can go ahead.
   Also, you can run some specific isolated commands. `./gradlew runStaticAnalysis` performs all code quality checks,
   and `./gradlew test` run unit tests.
1. Push the changes and create a new pull request. 
   Please, put `Resolves #TASK_ID` in the PR description.

Also you should know that the repository uses [SonarCloud](https://sonarcloud.io/) to measure the test coverage.
If it's lower than 80%, the check doesn't pass.

The commits should be written according to this pattern: `[#TASK_ID] - message`.

For example, `[#96] - Fixed ImmutableList.slice(int fromIndex, int toIndex) javadoc`.

That's basically everything you need to know. Feel free to contribute!