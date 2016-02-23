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

package com.michaelpidde.testflow.engine.config;

import com.michaelpidde.testflow.engine.database.Datasource;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

public class ConfigJson implements IConfig {

	JSONObject src;

	public ConfigJson(String file) {
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
			path = System.getProperty("user.home") + File.separator + path.substring(2);
		}
		
		File filePath = new File(path);
		if(!filePath.exists()) {
			filePath.mkdir();
		}

		return path;
	}

	public ArrayList<String> getEmailRecipients() {
		return getConfigList("emailRecipients");
	}

	public ArrayList<String> getSuites() {
		return getConfigList("suites");
	}

	public String getLogType() {
		return (String)src.get("logType");
	}

	public Datasource getLoggingDatasource() {
		JSONObject loggingConfig = src.getJSONObject("loggingDatasource");
		return new Datasource(
			(String)loggingConfig.get("type"),
			(String)loggingConfig.get("url"),
			(String)loggingConfig.get("username"),
			(String)loggingConfig.get("password"),
			(String)loggingConfig.get("database")
		);
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