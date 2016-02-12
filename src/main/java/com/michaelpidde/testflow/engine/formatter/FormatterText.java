/*
 * TestFlow text output formatter
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

package com.michaelpidde.testflow.engine.formatter;

import java.util.ArrayList;

import com.michaelpidde.testflow.engine.util.TestResult;
import com.michaelpidde.testflow.engine.util.TestStep;

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
				output += "\n---------------------------------------------------------------\n";
				output += formatResult(result) + "\n";
			}
			
			if(suite.iterator().hasNext()) {
				output += "\n---------------------------------------------------------------\n";
			}
			
		}
		
		return output;
	}
}