package com.michaelpidde.testflow.engine.util;

import com.michaelpidde.testflow.engine.util.Logger;
import com.michaelpidde.testflow.engine.util.TestStep;

import org.openqa.selenium.WebDriver;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

import javax.naming.Binding;
import java.io.File;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class Z extends GroovyShell {

	GroovyObject obj;

	public boolean run(WebDriver driver, String baseUrl, Logger logger) {
		try {
			CompilerConfiguration configuration = new CompilerConfiguration();
			configuration.setScriptBaseClass("Test");
			ClassLoader parent = Z.class.getClassLoader();
			GroovyClassLoader loader = new GroovyClassLoader(parent, configuration);
			Class cls = loader.parseClass(new File("./tests/wolfnet/TestZ.groovy"));

			this.obj = (GroovyObject)cls.newInstance();
			Object[] setupArgs = {driver, baseUrl, logger};
			obj.invokeMethod("setup", setupArgs);
			Object[] runArgs = {};
			return (boolean)obj.invokeMethod("run", runArgs);
		} catch(Exception e) {
			System.out.println(e.toString());
			return false;
		}
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