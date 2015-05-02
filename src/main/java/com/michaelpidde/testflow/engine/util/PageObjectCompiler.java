package com.michaelpidde.testflow.engine.util;

import com.michaelpidde.testflow.engine.util.Directory;
import com.michaelpidde.testflow.engine.util.TestResult;

import java.io.File;
import java.io.IOException;
import java.lang.Object;
import java.util.ArrayList;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;

public class PageObjectCompiler extends GroovyShell {

	GroovyClassLoader loader;

	public PageObjectCompiler(GroovyClassLoader loader) {
		this.loader = loader;
	}

	public TestResult run(File directory) {
		TestResult result = new TestResult();
		result.passed = true;
		result.testName = "None [PageObject pre-compile]";

		try {
			ArrayList<String> files = Directory.listDirectoryFiles(directory, ".groovy");
			GroovyCodeSource code;

			for(String file : files) {
				code = new GroovyCodeSource(new File(directory.getAbsolutePath() + "/" + file));
				loader.parseClass(code, true);
			}
		} catch(IOException e) {
			result.passed = false;
			result.error = e.toString();
			System.out.println(e.toString());
		} catch (Exception e) {
			result.passed = false;
			result.error = e.toString();
			System.out.println(e.toString());
		} finally {
			return result;
		}
	}

}