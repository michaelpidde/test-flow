package com.michaelpidde.automated.Formatter;

import java.util.ArrayList;

import com.michaelpidde.automated.util.TestResult;

public interface IResultFormatter {
	String formatResult(TestResult result);
	String formatSuite(ArrayList<ArrayList<TestResult>> suite);
}