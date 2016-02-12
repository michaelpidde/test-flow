/*
 * TestFlow page object compiler
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