package com.michaelpidde.automated.XML;

import java.net.URL;
import java.util.ArrayList;

import com.michaelpidde.automated.Frontend.TestDAO;
import com.michaelpidde.automated.XML.XMLParser;

public class ConfigParser extends XMLParser {
	private static URL configXml = TestDAO.class.getClass().getResource("/Config.xml");
	
	public static ArrayList<String> getEmailRecipients() {
		return getNodeValuesAsList(configXml, "/Config/global/emailRecipients/*");
	}
}