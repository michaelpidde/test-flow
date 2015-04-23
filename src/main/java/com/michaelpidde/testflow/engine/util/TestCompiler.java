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