package com.michaelpidde.testflow.engine.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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