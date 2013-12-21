package com.michaelpidde.automated.Formatter;

import java.util.ArrayList;

import com.michaelpidde.automated.util.TestResult;
import com.michaelpidde.automated.util.TestStep;

public class FormatterText implements IResultFormatter {
	@Override
	public String formatResult(TestResult result) {
		String output = "\nTest: " + result.testName;
		output += "\nPassed: " + result.passed.toString();
		output += "\nElapsed (s): " + result.timeElapsed();
		output += "\nSteps: ";
		int count = 1;
		for(TestStep step : result.steps) {
			output += "\n\t" + String.valueOf(count) + ": " + step.description;
			output += (step.imagePath.length() > 0) ? "\n\t" + step.imagePath : "";
			count++;
		}
		if(!result.passed && result.error.length() > 0) {
			output += "\nError:\n\n" + result.error;
		}
		
		return output;
	}

	
	
	@Override
	public String formatSuite(ArrayList<ArrayList<TestResult>> suite) {
		String output = "";
		
		for(ArrayList<TestResult> run : suite) {
			
			for(TestResult result : run) {
				output += formatResult(result) + "\n";
			}
			
			if(suite.iterator().hasNext()) {
				output += "\n---------------------------------------------------------------\n";
			}
			
		}
		
		return output;
	}
}