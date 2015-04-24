/*
 * TestFlow Groovy test compiler
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

	GroovyObject obj;

	public TestResult run(WebDriver driver, String baseUrl, Logger logger, String test) {
		TestResult result = new TestResult();
		result.testName = test;
		try {
			CompilerConfiguration configuration = new CompilerConfiguration();
			configuration.setScriptBaseClass("Test");
			ClassLoader parent = TestCompiler.class.getClassLoader();
			GroovyClassLoader loader = new GroovyClassLoader(parent, configuration);
			Class cls = loader.parseClass(new File("./tests/" + test + ".groovy"));

			this.obj = (GroovyObject)cls.newInstance();
			Object[] setupArgs = {driver, baseUrl, logger};
			obj.invokeMethod("setup", setupArgs);

			Object[] runArgs = {};
			result.passed = (boolean)obj.invokeMethod("run", runArgs);
		} catch(IllegalAccessException e) {
			result.passed = false;
			result.error = e.toString();
			System.out.println(e.toString());
		} catch(InstantiationException e) {
			result.passed = false;
			result.error = e.toString();
			System.out.println(e.toString());
		} catch(IOException e) {
			result.passed = false;
			result.error = e.toString();
			System.out.println(e.toString());
		} catch (NoSuchElementException e) {
			result.passed = false;
			result.error = e.toString();
			System.out.println(e.toString());
		}
		return result;
	}

	public ArrayList<TestStep> getSteps() {
		ArrayList<TestStep> result = new ArrayList<TestStep>();
		try {
			Method getSteps = obj.getClass().getMethod("getSteps");
			result = (ArrayList<TestStep>)getSteps.invoke(obj);
		} catch(NoSuchMethodException e) {
			System.out.println(e.toString());
		} catch(IllegalAccessException e) {
			System.out.println(e.toString());
		} catch(InvocationTargetException e) {
			System.out.println(e.toString());
		}
		return result;
	}

}