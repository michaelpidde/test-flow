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