package com.trustee.main;

import java.util.ArrayList;
import java.util.Scanner;

public class TrusteeCollectorMain {
	
	public static void textEntryInterface(OnlineFilingNavigator nav) {
		Scanner input = new Scanner(System.in);
		ArrayList<Trustee> trustees = new ArrayList<Trustee>();
		
		// intro
		System.out.println("Welcome to the IRS Trustee Collector!");
		
		// ask for EIN
		System.out.println("Please enter the Employer Identification Number "
				+ "(EIN) of the entity you want to research.");
		String EIN;
		while (true) {
			EIN = input.next();
			if ((EIN.length() == 9) && (EIN.toLowerCase().equals(EIN.toUpperCase())))
				break;
			else System.out.println("Please enter a valid EIN.");
		}
		nav.setEIN(EIN);
		System.out.println("EIN entered: " + EIN);
		
		// ask for year
		System.out.println("Please enter the year you want to look up.");
		String year;
		while (true) {
			year = input.next();
			if (Integer.parseInt(year) >= 2011) {
				break;
			}
			else System.out.println("Please enter a year 2011 or later.");
		}
		
		// call parse method
		if (!year.equals("0")) {
			trustees = nav.processSingleEIN(year);
		}
		
		// call CSVwrite method
		System.out.println("Enter a filename for the CSV file. Don't forget to include the directory!");
		String fname = input.next();
		nav.writeCSV(fname, EIN, year);
		
		input.close();
	}
	
	public static void main(String[] args) {
		
		// set EINs
//		String EIN = "860292099"; // Safari Club Foundation
//		String EIN = "860974183"; // Safari Club International
//		String EIN = "/data/EIN_File.txt";
		
		// create new navigator
		OnlineFilingNavigator mainNavigator = new OnlineFilingNavigator();
		
		textEntryInterface(mainNavigator);
		
	}
}
