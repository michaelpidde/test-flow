package com.michaelpidde.testflow.engine.util;

import com.michaelpidde.testflow.engine.util.Logger;

import org.openqa.selenium.WebDriver;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;

import javax.naming.Binding;
import java.io.File;

public class Z extends GroovyShell {

	public Z(WebDriver driver, String baseUrl, Logger logger) {
		try {
			ClassLoader parent = Z.class.getClassLoader();
			GroovyClassLoader loader = new GroovyClassLoader(parent);
			Class cls = loader.parseClass(new File("./tests/wolfnet/TestZ.groovy"));

			GroovyObject obj = (GroovyObject)cls.newInstance();
			Object[] args = {driver, baseUrl, logger};
			obj.invokeMethod("invoke", args);
		} catch(Exception e) {
			System.out.println(e.toString());
		}
	}

}