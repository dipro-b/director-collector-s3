package com.trustee.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class OnlineFilingNavigator {
	
	private String MY_EIN;
	private boolean isFile = false;
	private boolean allYears = false;
	private IndexTraveller myTraveller;
	private ArrayList<Trustee> myTrustees;
	
	public OnlineFilingNavigator() {
		myTraveller = new IndexTraveller();
	}
	
	/**
	 * @param ein Employee Identification Number of entity
	 */
	public OnlineFilingNavigator(String ein) {
		MY_EIN = ein;
		if (ein.endsWith(".txt")) isFile = true;
	}
	
	
	// setter and getter methods
	// TODO: fix EIN and object passing between 
	// Navigator class and Traveller class
	public void setEIN(String ein) {
		MY_EIN = ein;
		if (ein.endsWith(".txt")) isFile = true;
	}
	
	public String getEIN() {
		return MY_EIN;
	}
	
	public boolean isFile() {
		return this.isFile;
	}
	
	public boolean isAllYears() {
		return this.allYears;
	}
	
	public void setTrustees(ArrayList<Trustee> t) {
		this.myTrustees = t;
	}
	
	/**
	 * Generates URL from year given.
	 * @param year - tax year
	 * @return url in string form
	 * @throws IOException if year is before 2010
	 */
	private String getIndexURL(String year) throws IOException {
		if (Integer.parseInt(year) <= 2010) 
			throw new IOException("Year must be 2011 or later!");
		String ans = "https://s3.amazonaws.com/irs-form-990/index_" + year + ".json";
		System.out.println("URL Generated! \nURL = " + ans);
		return ans;
	}
	
	/**
	 * Parses XML document (tax return) and returns a list of trustees and their positions
	 * @param url - URL of XML Document
	 * @return list of trustees
	 * @throws Exception if document is malformed or URL does not exist
	 */
	private ArrayList<Trustee> getTrustees(String url) throws Exception {
		// initialize parser
		TaxXMLParserSAX parser = new TaxXMLParserSAX(url);
		
		// look through XML doc for <Form990PartVIISectionAGrp> and <PersonNm>
		ArrayList<Trustee> trustees = parser.parse();
		this.setTrustees(trustees);
		
		return trustees;
	}
	
	public void writeCSV(String filename, String EIN, String year) {
		CSVWriter write = new CSVWriter(this.myTrustees);
		try {
			write.writeCSV(filename, EIN, year);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Main method that calls other methods
	 * @param year
	 * TODO: figure out how to process multiple EINs
	 * TODO: figure out how this fits in
	 */
	public ArrayList<Trustee> processSingleEIN(String year) throws Exception{
		String EIN = this.getEIN();
		String url = getIndexURL(year);
		HashMap<String, String> assetMap = myTraveller.getIdentifier(EIN, url);
		String id = assetMap.get(EIN);
		ArrayList<Trustee> trustees = getTrustees(id);

		return trustees;
		
	}
}
