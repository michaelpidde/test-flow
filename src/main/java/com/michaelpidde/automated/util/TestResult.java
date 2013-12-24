/*
 * TestFlow test result container
 *
 * Copyright (C) 2013 Michael Pidde <michael.pidde@gmail.com>
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

package com.michaelpidde.automated.util;

import java.util.ArrayList;
import com.michaelpidde.automated.util.TestStep;

public class TestResult {
	private long runStart = System.currentTimeMillis();
	private long runEnd;
	public Boolean passed = false;
	public String testName = "";
	public String error = "";
	public ArrayList<TestStep> steps = new ArrayList<TestStep>();



	/*
	 * Accessors
	 */
	public void setRunEnd() {
		runEnd = System.currentTimeMillis();
	}
	public Boolean getPassed() {
		return passed;
	}
	public String getTestName() {
		return testName;
	}



	public String timeElapsed() {
		return String.valueOf((runEnd - runStart) / 1000);
	}



	public String write() {
		String output = "\nTest: " + testName;
		output += "\nPassed: " + passed.toString();
		output += "\nElapsed (s): " + timeElapsed();
		output += "\nSteps: ";
		int count = 1;
		for(TestStep step : steps) {
			output += "\n\t" + String.valueOf(count) + ": " + step.description;
			output += "\n\t" + step.imagePath;
			count++;
		}
		if(!passed && error.length() > 0) {
			output += "\nError:\n\n" + error;
		}
		return output;
	}
}