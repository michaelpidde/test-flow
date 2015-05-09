/*
 * TestFlow database base class
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

package com.michaelpidde.testflow.engine.database;

import com.michaelpidde.testflow.engine.util.TestStep;
import com.michaelpidde.testflow.engine.util.TestResult;

import java.util.ArrayList;

public class Database {
	public String resultStepsToJSON(ArrayList<TestStep> steps) {
		String json = "{\"steps\":[";
		for(int i = 0; i < steps.size(); i++) {
			json += "{";
			json += "\"description\":\"" + steps.get(i).getDescription() + "\",";
			json += "\"imagePath\":\"" + steps.get(i).getImagePath() + "\"";
			json += "}";
			if(i < steps.size() - 1) {
				json += ",";
			}
		}
		json += "]}";

		return json;
	}

	public void insertResult(TestResult result) {}

	public void insertSuite(ArrayList<ArrayList<TestResult>> suite) {}
}