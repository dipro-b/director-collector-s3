# director-collector-s3
Code to create a file of all the directors, trustees, and key employees of a 501(c) corporation using the IRS database.

## Overview

IRS Tax Returns for tax-exempt corporations can be a rich source for information about different organizations. Being able to parse these returns for interesting information (such as the names of trustees and key employees) is useful for drawing connections between different tax-exempt organizations. Maybe you want to see if a non-profit has stacked its board with executives from a certain industry that benefit from its lobbying, or perhaps you want to check connections between two nonprofits - having a list of all its directors can be a useful tool for investigations. 

This project uses the IRS AWS database to pull information on the directors of any 501(c) corporation.

## Using this project

The easiest way to run this project is by downloading and running the project JAR file (which can be found in the 'exe' folder). 

##Mac Instructions

1. Download the executable jar file, from the "exe" folder.
2. Open terminal.
3. Enter the command "java /Home/your-folder/director-collector.jar", but using the path for the jar file you just downloaded.
4. Follow the instructions on the screen. 