/*
 * TestFlow Groovy test compiler
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

import com.michaelpidde.testflow.engine.util.Logger;
import com.michaelpidde.testflow.engine.util.TestStep;
import com.michaelpidde.testflow.engine.util.TestResult;
import com.michaelpidde.testflow.engine.util.TestException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.NoSuchElementException;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

import javax.naming.Binding;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class TestCompiler extends GroovyShell {

	GroovyClassLoader loader;
	GroovyObject obj;

	public TestCompiler(GroovyClassLoader loader) {
		this.loader = loader;
	}

	public TestResult run(WebDriver driver, String baseUrl, Logger logger, String test) {
		TestResult result = new TestResult();
		result.testName = test;
		result.passed = false;
		try {
			Class cls = loader.parseClass(new File("./tests/" + test + ".groovy"));

			this.obj = (GroovyObject)cls.newInstance();
			Object[] setupArgs = {driver, baseUrl, logger};
			obj.invokeMethod("setup", setupArgs);

			Object[] runArgs = {};
			result.passed = (boolean)obj.invokeMethod("run", runArgs);
		} catch(IllegalAccessException|InstantiationException|IOException|
			NoSuchElementException e) {

			result.error = e.toString();
		} catch(Exception e) {
			result.error = e.toString();
			System.out.println(e.toString());
		} finally {
			return result;
		}
	}

	public ArrayList<TestStep> getSteps() {
		ArrayList<TestStep> result = new ArrayList<TestStep>();
		try {
			Method getSteps = obj.getClass().getMethod("getSteps");
			result = (ArrayList<TestStep>)getSteps.invoke(obj);
		} catch(NoSuchMethodException|IllegalAccessException|InvocationTargetException e) {
			System.out.println(e.toString());
		} finally {
			return result;
		}
	}

}