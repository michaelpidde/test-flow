/*
 * TestFlow HTML output formatter
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

public class FormatterHTML implements IResultFormatter {
	@Override
	public String formatResult(TestResult result) {
		String output = "<div class=\"test\">";
		output += "<div class=\"title\"><b>Test:</b> " + result.testName + "</div>";
		output += "<div class=\"passed\"><b>Passed:</b> " + result.passed.toString() + "</div>";
		output += "<div class=\"time\"><b>Elapsed (s):</b> " + result.timeElapsed() + "</div>";
		output += "<div class=\"steps\"><b>Steps:</b> </div>";
		int count = 1;
		output += "<ol>";
		for(TestStep step : result.steps) {
			output += "<li>";
			output += step.description + "<br />";
			output += (step.imagePath.length() > 0) ? step.imagePath : "";
			output += "</li>";
			count++;
		}
		output += "</ol>";
		if(!result.passed && result.error.length() > 0) {
			output += "<div class=\"error\">";
			output += "<b>Error:</b><br />";
			output += "<span class=\"errorDetail\">" + result.error + "</span>";
			output += "</div>";
		}
		
		return output;
	}

	
	
	@Override
	public String formatSuite(ArrayList<ArrayList<TestResult>> suite) {
		String output = "";
		
		for(ArrayList<TestResult> run : suite) {
			
			for(TestResult result : run) {
				output += "<hr />";
				output += formatResult(result) + "\n";
			}
			
			if(suite.iterator().hasNext()) {
				output += "<hr />";
			}
			
		}
		
		return output;
	}
}