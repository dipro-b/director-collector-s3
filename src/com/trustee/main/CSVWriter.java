package com.trustee.main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CSVWriter {
	
	private ArrayList<Trustee> myTrustees;
	private final String COMMA_DELIMITER = ",";
	private final String LINE_SEPARATOR = "\n";
	
	// headers
	private final String HEADER_ROW = "ein,year,trusteeName,trusteeTitle";
	
	public CSVWriter(ArrayList<Trustee> trustees) {
		myTrustees = trustees;
	}
	
	public void writeCSV(String filename, String ein, String year) throws IOException {
		if (!filename.endsWith(".csv")) {
			throw new IOException("Filename must end with .csv");
		}
		
		FileWriter writer = new FileWriter(filename);
		
		writer.append(HEADER_ROW);
		writer.append(LINE_SEPARATOR);
		
		for (Trustee t : this.myTrustees) {
			writer.append(ein);
			writer.append(COMMA_DELIMITER);
			writer.append(year);
			writer.append(COMMA_DELIMITER);
			String name = t.getName();
			if (name.contains(",")) {
				name.replace(',', ' ');
			}
			writer.append(name);
			writer.append(COMMA_DELIMITER);
			String job = t.getJob();
			if (job.contains(",")) {
				job.replace(',', ' ');
			}
			writer.append(job);
			writer.append(LINE_SEPARATOR);
		}
		
		System.out.println("CSV file " + filename + " was created successfully.");
		writer.flush();
		writer.close();
	}
	
	
}
