package com.michaelpidde.automated.Frontend;

import java.net.URL;
import java.util.ArrayList;

import com.michaelpidde.automated.XML.XMLParser;

public class TestDAO extends XMLParser {
	private static URL testXml = TestDAO.class.getClass().getResource("/Tests.xml");
	
	
	
	public static ArrayList<String> getApps() {
		return getNodeNamesAsList(testXml, "/Apps/*");
	}
	
	
	
	public static ArrayList<String> getSuites(String app) {
		return getNodeNamesAsList(testXml, "/Apps/" + app + "/Suites/*");
	}
	
	
	
	public static ArrayList<String> getTests(String app) {
		return getNodeNamesAsList(testXml, "/Apps/" + app + "/AllTests/*");
	}
}
