/*
 * TestFlow Test XML DAO
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
import java.util.ArrayList;

import com.michaelpidde.testflow.engine.xml.XMLParser;

public class TestDAO extends XMLParser {
	private File testXml;
	
	public TestDAO(String path) {
		this.testXml = loadConfig(path);
	}

	public ArrayList<String> getApps() {
		return getNodeNamesAsList(testXml, "/apps/*");
	}
	
	public ArrayList<String> getSuites(String app) {
		return getNodeNamesAsList(testXml, "/apps/" + app + "/suites/*");
	}
	
	public ArrayList<String> getTests(String app) {
		return getNodeNamesAsList(testXml, "/apps/" + app + "/allTests/*");
	}

	public String getBaseUrl(String app) {
		return getNodeValueAsString(testXml, "/apps/" + app + "/*");
	}
}
