package com.trustee.main;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class TaxXMLParserSAX {
	
	private SAXParserFactory myFactory;
	private SAXParser myParser;
	private TaxXMLHandler myHandler;
	private URL myURL;
	
	
	public TaxXMLParserSAX(String url) throws Exception {
		myFactory = SAXParserFactory.newInstance();
		myParser = myFactory.newSAXParser();
		myHandler = new TaxXMLHandler();
		myURL = new URL(url);
	}
	
	public ArrayList<Trustee> parse() {
		try {
			InputStream in = myURL.openStream();
			myParser.parse(in, myHandler);
			ArrayList<Trustee> tList = myHandler.getTrustees();
			for (Trustee t : tList) {
				System.out.println("Name = " + t.getName());
				System.out.println("Title = " + t.getJob());
			}
			return tList;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
