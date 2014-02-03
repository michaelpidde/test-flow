/*
 * TestFlow configuration XML parser
 *
 * Copyright (C) 2014 Michael Pidde <michael.pidde@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.michaelpidde.testflow.engine.xml;

import java.net.URL;
import java.util.ArrayList;

import com.michaelpidde.testflow.engine.xml.TestDAO;
import com.michaelpidde.testflow.engine.xml.XMLParser;

public class ConfigParser extends XMLParser {
	private static URL configXml = TestDAO.class.getClass().getResource("/Config.xml");
	
	public static ArrayList<String> getEmailRecipients() {
		return getNodeValuesAsList(configXml, "/config/global/emailRecipients/*");
	}
}