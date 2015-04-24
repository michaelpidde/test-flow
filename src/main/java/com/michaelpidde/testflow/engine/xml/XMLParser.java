/*
 * TestFlow generic xml parser
 *
 * This file is part of TestFlow.
 *
 * Copyright (C) 2014-2015 Michael Pidde <michael.pidde@gmail.com>
 *
 * TestFlow is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * TestFlow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TestFlow; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.michaelpidde.testflow.engine.xml;

import java.io.File;
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
	public static File loadConfig(String path) {
		return new File(path);
	}


	public static ArrayList<String> getNodeNamesAsList(File xmlFile, String path) {
		ArrayList<String> collection = new ArrayList<String>();
		NodeList nodes = getNodeList(xmlFile, path);
		
		for(int i = 0; i < nodes.getLength(); i++) {
			collection.add(nodes.item(i).getNodeName());
		}
		
		return collection;
	}
	
	
	public static ArrayList<String> getNodeValuesAsList(File xmlFile, String path) {
		ArrayList<String> collection = new ArrayList<String>();
		NodeList nodes = getNodeList(xmlFile, path);
		
		for(int i = 0; i < nodes.getLength(); i++) {
			collection.add(nodes.item(i).getTextContent());
		}
		
		return collection;
	}
	
	
	public static NodeList getNodeList(File xmlFile, String path) {
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


	public static String getNodeValueAsString(File xmlFile, String path) {
		String nodeValue = "";
		NodeList nodes = getNodeList(xmlFile, path);

		return nodes.item(0).getTextContent();
	}
}