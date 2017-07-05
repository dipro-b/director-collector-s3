# director-collector-s3
Code to create a file of all the directors, trustees, and key employees of a 501(c) corporation using the IRS database.

## Overview

IRS Tax Returns for tax-exempt corporations can be a rich source for information about different organizations. Being able to parse these returns for interesting information (such as the names of trustees and key employees) is useful for drawing connections between different tax-exempt organizations. Maybe you want to see if a non-profit has stacked its board with executives from a certain industry that benefit from its lobbying, or perhaps you want to check connections between two nonprofits - having a list of all its directors can be a useful tool for investigations. 

This project uses the IRS AWS database to pull information on the directors of any 501(c) corporation, and save it in a spreadsheet.

## Using this project

The easiest way to run this project is by downloading and running the project JAR file (which can be found in the 'exe' folder). 

### Mac Instructions

1. Download the executable jar file (.jar), from the "exe" folder on Github.
2. Open terminal/console.
3. Enter the command "java -jar /Home/your-folder/director-collector.jar", but using the path for the  file you just downloaded. 
	1. For example, if you saved the file in "Downloads," then the path might look like this: "user-name/Home/Downloads/director-collector.jar".
	2. If you are having trouble, you can drag the file from your finder window into the console (the path will appear in the command line).
4. Follow the instructions on the screen. 
	1. Enter the EIN of the entity: enter the 9-digit Employer Identification Number (without dashes) into the terminal window. Press enter.
	2. Enter the year you want to look up: enter a year from 2011 or later (eg: "2014"). Press enter.
	3. Let the program run.
	4. Enter the name of your CSV file: enter a valid filename that includes the folder it is in. Press enter. 
	
	You may want to enter the same path you entered before. For example, if you ran "/Home/your-folder/director-collector.jar", then your filename might be "/Home/Your-folder/directors.csv". 

## Versions:
0.8 - Current version, allows search of 1 entity at a time, 1 year at a time.

## Future improvements

- Allow user to search for multiple entities at once.
- Allow user to search for all years from 2011 - 2017 at once. 