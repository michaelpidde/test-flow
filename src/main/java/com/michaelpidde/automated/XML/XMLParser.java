package com.michaelpidde.automated.XML;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.*;
import org.w3c.dom.*;

public class XMLParser {
	public static ArrayList<String> getNodeNamesAsList(URL xmlFile, String path) {
		ArrayList<String> collection = new ArrayList<String>();
		NodeList nodes = getNodeList(xmlFile, path);
		
		for(int i = 0; i < nodes.getLength(); i++) {
			collection.add(nodes.item(i).getNodeName());
		}
		
		return collection;
	}
	
	
	
	public static ArrayList<String> getNodeValuesAsList(URL xmlFile, String path) {
		ArrayList<String> collection = new ArrayList<String>();
		NodeList nodes = getNodeList(xmlFile, path);
		
		for(int i = 0; i < nodes.getLength(); i++) {
			collection.add(nodes.item(i).getTextContent());
		}
		
		return collection;
	}
	
	
	
	public static NodeList getNodeList(URL xmlFile, String path) {
		Document xml;
		NodeList nodes = null;
		DocumentBuilderFactory builder = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = builder.newDocumentBuilder();
			xml = db.parse(xmlFile.toString());
			
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expression = xpath.compile(path);
			
			Object result = expression.evaluate(xml, XPathConstants.NODESET);
			nodes = (NodeList)result;
		} catch(ParserConfigurationException e) {
			System.out.println(e.toString());
		} catch(SAXException e) {
			System.out.println(e.toString());
		} catch(XPathExpressionException e) {
			System.out.println(e.toString());
		} catch(IOException e) {
			System.out.println(e.toString());
		}
		
		return nodes;
	}



	public static String getNodeValueAsString(URL xmlFile, String path) {
		String nodeValue = "";
		NodeList nodes = getNodeList(xmlFile, path);

		return nodes.item(0).getTextContent();
	}
}