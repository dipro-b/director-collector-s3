package com.trustee.main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TaxXMLHandler extends DefaultHandler {
	
	// initialize list
	private ArrayList<Trustee> trusteeList = null;
	private Trustee dummyTrustee = null;
	
	// getter method
	public ArrayList<Trustee> getTrustees() {
		return trusteeList;
	}
	
	// boolean vars to keep track of where we are
	boolean isTrustee = false;
	boolean hasName = false;
	boolean hasJob = false;
	
	// set of all XML valid elements
	String[] sections = {"form990partviisectiona", "form990partviisectionagrp", 
			"ofcrdirtrusteesorkeyemployee", "officerdirectortrusteekeyempl", 
			"officerdirectortrusteeemplgrp"};
	HashSet<String> sectionSet = new HashSet<String>(Arrays.asList(sections));
	
	String[] names = {"personnm", "nameperson", "personname"};
	HashSet<String> nameSet = new HashSet<String>(Arrays.asList(names));
	
	String[] jobs = {"title", "titletxt"};
	HashSet<String> jobSet = new HashSet<String>(Arrays.asList(jobs));
	
	@Override
	public void startElement(String uri,  String localName, String qName, Attributes attributes) throws SAXException {
		
		// if trustee section, initialize a trustee
		if (sectionSet.contains(qName.toLowerCase())) {
			isTrustee = true;
			dummyTrustee = new Trustee();
			if (trusteeList == null)
				trusteeList = new ArrayList<Trustee>();
		}
		else if (nameSet.contains(qName.toLowerCase())  && (isTrustee)) {
			hasName = true;
			if (dummyTrustee == null) {
				dummyTrustee = new Trustee();
			}
		}
		else if (jobSet.contains(qName.toLowerCase()) && (isTrustee)) {
			hasJob = true;
			if (dummyTrustee == null) {
				dummyTrustee = new Trustee();
			}
		}
	}
	
	@Override
	public void endElement(String uri,  String localName, String qName) throws SAXException {
		
		// null test is in case xml file is malformed/messy
		if (sectionSet.contains(qName.toLowerCase()) && (dummyTrustee != null)) {
			trusteeList.add(dummyTrustee);
			dummyTrustee = null;
		}
		else if (jobSet.contains(qName.toLowerCase()) && (dummyTrustee != null)) {
			trusteeList.add(dummyTrustee);
			dummyTrustee = null;
		}
	}
	
	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		
		// only add if name is trustee (and not some random person)
		if (isTrustee && hasName) {
			dummyTrustee.setName(new String(ch, start, length));
			hasName = false;
		}
		else if (isTrustee && hasJob) {
			dummyTrustee.setJob(new String(ch, start, length));
			hasJob = false;
		}
	}
	
}
