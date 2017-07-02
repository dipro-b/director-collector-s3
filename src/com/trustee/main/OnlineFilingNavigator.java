package com.trustee.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class OnlineFilingNavigator {
	
	protected static String MY_EIN;
	
	/**
	 * @param ein Employee Identification Number of entity
	 */
	public OnlineFilingNavigator(String ein) {
		MY_EIN = ein;
	}
	
	/**
	 * Generates URL from year given.
	 * @param year - tax year
	 * @return url in string form
	 * @throws IOException if year is before 2010
	 */
	public static String getIndexURL(String year) throws IOException {
		if (Integer.parseInt(year) <= 2010) throw new IOException("Year must be 2011 or later!");
		String ans = "https://s3.amazonaws.com/irs-form-990/index_" + year + ".json";
		System.out.println("URL Generated! \nURL = " + ans + "\n");
		return ans;
	}
	
	/**
	 * Get url of XML document using the index file
	 * @param ein - EIN of entity OR filename containing multiple EINs
	 * @param indexURL - URL of index file
	 * @return - URL of tax return
	 * @throws IOException if EIN cannot be found in index file or if EIN file is malformed
	 * TODO: Add logic to parse EINs
	 */
	public static String getIdentifier(String ein, String indexURL) throws Exception {
		URL index = new URL(indexURL);
		boolean isFile = false;
		
		if (ein.endsWith(".txt")) {
			isFile = true;
		}
		
		// make set of EINs
		HashSet<String> einSet = new HashSet<String>();
		if (isFile) {
			ArrayList<String> eins = readEINFile(ein);
			einSet.addAll(eins);
		}
		else {
			einSet.add(ein);
		}
		
		// open stream of JSON index file
		BufferedReader in = new BufferedReader(new InputStreamReader(index.openStream()));
		Scanner reader = new Scanner(in);
		reader.useDelimiter("\"");
		
		boolean found = false;
		String token = "";
		long sysTime = System.currentTimeMillis();
		long now;
		while (reader.hasNext()) {
			// timer
			now = System.currentTimeMillis();
			if ((now - sysTime) > 5000) {
				System.out.println("Working...");
				sysTime = System.currentTimeMillis();
			}
			
			// parsing logic
			token = reader.next();
			if (token.equals("EIN")) {
				reader.next();
				token = reader.next();
				if (token.equalsIgnoreCase(ein)) {
					found = true;
					break;
				}
			}
		}
		
		// throw exception if parsed whole document
		if (found == false) {
			reader.close();
			in.close();
			throw new IOException("Entity does not exist! Check EIN number and try again.");
		}
		
		while (!token.equals("URL")) {
			if (!reader.hasNext()) break;
			token = reader.next();
		}
		reader.next();
		String ans = reader.next();
		
		reader.close();
        in.close();
		
        System.out.println("Identifier found! \nId = " + ans + "\n");
		return ans;
	}
	
	/**
	 * Parses XML document (tax return) and returns a list of trustees and their positions
	 * @param url - URL of XML Document
	 * @return list of trustees
	 * @throws Exception if document is malformed or URL does not exist
	 */
	public static ArrayList<Trustee> getTrustees(String url) throws Exception {
		// initialize parser
		TaxXMLParserSAX parser = new TaxXMLParserSAX(url);
		
		// look through XML doc for <Form990PartVIISectionAGrp> and <PersonNm>
		ArrayList<Trustee> trustees = parser.parse();
		
		return trustees;
	}
	/**
	 * Helper function to read file of EINs
	 * 
	 * Logic: Since index file is so large, it makes more sense to look for multiple entities at once
	 * @param filename file of EINs, separated by line breaks
	 * @return ArrayList of all EINs (in string form)
	 */
	public static ArrayList<String> readEINFile(String filename) throws Exception {
		Scanner scan = new Scanner(new File(filename));
		ArrayList<String> ans = new ArrayList<String>();
		
		while (scan.hasNextLine()) {
			String ein = scan.nextLine();
			ans.add(ein);
		}
		
		scan.close();
		return ans;
	}
	
	public static void main(String[] args) {
		try {
//			String EIN = "860292099"; // Safari Club Foundation
			String EIN = "860974183"; // Safari Club International
			String url = getIndexURL("2014");
			String id = getIdentifier(EIN, url);
//			System.out.println(id);
			
//			String id = "https://s3.amazonaws.com/irs-form-990/201441359349303644_public.xml";
			getTrustees(id);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
