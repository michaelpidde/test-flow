/*
 * TestFlow JSON config parser
 *
 * This file is part of TestFlow.
 *
 * Copyright (C) 2014-2016 Michael Pidde <michael.pidde@gmail.com>
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

package com.michaelpidde.testflow.engine.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

public class Config {

	JSONObject src;

	public Config(String file) {
		try {
			this.src = new JSONObject(
				new String(Files.readAllBytes(Paths.get(file)))
			);
		} catch(IOException e) {
			System.out.println(e.toString());
		}
	}

	public String getBaseUrl() {
		return (String)src.get("baseUrl");
	}

	public String getLogPath() {
		String path = (String)src.get("logPath");
		if(path.startsWith("~" + File.separator)) {
			return System.getProperty("user.home") + File.separator + path.substring(2);
		} else {
			return path;
		}
	}

	public ArrayList<String> getEmailRecipients() {
		return getConfigList("emailRecipients");
	}

	public ArrayList<String> getSuites() {
		return getConfigList("suites");
	}

	private ArrayList<String> getConfigList(String node) {
		JSONArray jsonArray = src.getJSONArray(node);
		ArrayList<String> result = new ArrayList<String>();
		for(int i = 0; i < jsonArray.length(); i++) {
			result.add(jsonArray.getString(i));
		}
		return result;
	}

}