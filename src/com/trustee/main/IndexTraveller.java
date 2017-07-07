package com.trustee.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Class to traverse one (or multiple) index files in the IRS 990 database.
 * @author diprobhowmik
 *
 */
public class IndexTraveller {

	private List<String> myYears; //years being searched
	private List<String> myEINS; //Employer Identification Numbers
	private HashMap<String, String> myInfo; //map for EIns and assets
	
	public IndexTraveller() {
		myYears = new ArrayList<String>();
		myInfo = new HashMap<String, String>();
	}
	
	public List<String> getYears() {
		return myYears;
	}
	
	public List<String> getEINS() {
		return myEINS;
	}
	
	public Map<String, String> getIndices() {
		return myInfo;
	}
	
	/**
	 * Get url of XML document using the index file
	 * @param ein - EIN of entity OR filename containing multiple EINs
	 * @param indexURL - URL of index file
	 * @return - map of tax return assets (URLs)
	 * @throws IOException if EIN cannot be found in index file or if EIN file is malformed
	 */
	public HashMap<String, String> getIdentifier(String ein, String indexURL) throws Exception {
		URL index = new URL(indexURL);
		
		// open stream of JSON index file
		BufferedReader in = new BufferedReader(new InputStreamReader(index.openStream()));
		Scanner reader = new Scanner(in);
		reader.useDelimiter("\"");
		
		System.out.println("Searching for asset now.");
		// look for EIN within index file
		HashMap<String, String> ans = new HashMap<String, String>();
		if (ein.endsWith(".txt")) {
			ans = findMultipleAssets(ein, reader);
		}
		else {
			String asset = findAsset(ein, reader);
			ans.put(ein, asset);
		}
		
		// throw exception if ans is null (ie, the program has parsed the whole doc)
		if (ans.isEmpty()) {
			reader.close();
			in.close();
			throw new IOException("Entity does not exist! Check EIN number and try again.");
		}
		
		if ((ans.size() == 1) && (ans.containsValue(null))) {
			return null;
		}
		
		reader.close();
        in.close();
		
        System.out.println("Asset found! \nId = " + ans + "\n");
		return ans;
	}
	

	
	/**
	 * Helper function to find EIN within index file
	 * @param EIN token to find
	 * @param scan
	 * @return URL of asset
	 */
	private String findAsset(String EIN, Scanner reader) {
		boolean found = false;
		boolean hasURL = false;
		String token = null;
		long sysTime = System.currentTimeMillis();
		long now;
		
		String ans = "";
		
		// find EIN
		while (reader.hasNext()) {
			// print updates every 5 seconds
			now = System.currentTimeMillis();
			if ((now - sysTime) > 5000) {
				System.out.println("Looking for asset...");
				sysTime = System.currentTimeMillis();
			}
		
			token = reader.next();
			if (token.equals("EIN")) {
				reader.next();
				token = reader.next();
				if (token.equalsIgnoreCase(EIN)) {
					found = true;
					break;
				}
			}
		}
		if (!found) {
			System.out.println("Entity is not in the database. Check your EIN. "
					+ "If the EIN is correct, then the corporation may not have "
					+ "filed an online tax return.");
			return null;
		}
		
		// get asset URL
		while (reader.hasNext()) {
			token = reader.next();
			if (token.equalsIgnoreCase("EIN")) { // parsed through entry
				break;
			}
			if (token.equalsIgnoreCase("URL")) {
				hasURL = true;
			}
			if (isURL(token) && hasURL) { // found entry
				ans = token;
				break;
			}
			
		}
		
		if ((found) && (hasURL)) {
			return ans;
		}
		else if ((found) && (!hasURL)) {
			System.out.println("Entity exists, but it has no data!");
			return null;
		}
		else return null;
	}
	
	/**
	 * Method to find multiple XML assets given a file of EIN numbers.
	 * 
	 * Returns an arraylist with the URLs of the corresponding 
	 * 
	 * @param file - file of XML assets.
	 * @param reader - scanner object that is parsing the index file.
	 * @return
	 * TODO: fix logic (perhaps by using helper methods to get "URL" and "EIN" tags)
	 */
	private HashMap<String, String> findMultipleAssets(String file, Scanner reader) throws IOException {
		
		// get EINs from file
		HashSet<String> einSet;
		einSet = readEINFile(file);
		
		//Initialize map for assets
		HashMap<String, String> assetMap = new HashMap<String, String>();
		
		// initialize variables
		boolean found = false;
		String token = null;
		String ein = null;
		String asset = null;
		long sysTime = System.currentTimeMillis();
		long now;
		
		// find EIN
		while (reader.hasNext()) { 
			// print updates every 5 seconds
			now = System.currentTimeMillis();
			if ((now - sysTime) > 5000) {
				System.out.println("Looking for asset...");
				sysTime = System.currentTimeMillis();
			}
		
			token = reader.next();
			if (token.equalsIgnoreCase("EIN")) {
				reader.next();
				token = reader.next();
				if (einSet.contains(token)) { 
					found = true;
					ein = token;
					continue;
				}
			}
			
			if (token.equalsIgnoreCase("URL") && found) {
				reader.next();
				asset = reader.next();
				assetMap.put(ein, asset);
				found = false;
			}
			
			if (assetMap.keySet().size() == einSet.size()) break;
		}
		if (!found) return null;
		
		// get asset URL
		while (!token.equalsIgnoreCase("URL")) {
			if (!reader.hasNext()) break;
			token = reader.next();
		}
		if (reader.hasNext()) {
			token = reader.next();
			token = reader.next();
		}
		
		if (found) return null;
		else return null;
	}

	/**
	 * Helper function to read file of EINs
	 * 
	 * Logic: Since index file is so large, it makes more sense to look for multiple entities at once
	 * @param filename file of EINs, separated by line breaks
	 * @return ArrayList of all EINs (in string form)
	 */
	private HashSet<String> readEINFile(String filename) throws IOException {
		File f = new File(filename);
		Scanner scan = new Scanner(f);
		
		HashSet<String> ans = new HashSet<String>();
		
		while (scan.hasNextLine()) {
			String ein = scan.nextLine();
			
			// check for alphabetical
			if (ein.toLowerCase().equals(ein.toUpperCase())) {
				scan.close();
				throw new IOException("Check your file!");
			}
			if (ein.length() != 9) {
				scan.close();
				throw new IOException("Check your EINs!");
			}
			ans.add(ein);
		}
		
		scan.close();
		return ans;
	}
	
	private boolean isURL(String url) {
		if (url.endsWith(".xml")) return true;
		else return false;
	}
	
	
}
