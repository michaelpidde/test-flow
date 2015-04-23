/*
 * TestFlow Selenium test suite setup and execution
 *
 * Copyright (C) 2014 Michael Pidde <michael.pidde@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.michaelpidde.testflow.engine.util;

import com.michaelpidde.testflow.Cli;
import com.michaelpidde.testflow.engine.util.Logger;
import com.michaelpidde.testflow.engine.util.TestStep;
import com.michaelpidde.testflow.engine.util.TestResult;
import com.michaelpidde.testflow.engine.util.TestException;

import com.michaelpidde.testflow.engine.util.Z;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

public class TestSuite {
	protected WebDriver driver;
	protected String baseUrl;
	protected String browser;
	protected String suite;
	protected Boolean logResults;
	protected Logger logger;
	protected boolean runSuite = true;
	protected ArrayList<TestResult> results = new ArrayList<TestResult>();
	protected String runPath = "";
	protected DriverService service;



	public TestSuite(Logger logger, String browser, String baseUrl, String suite, Boolean logScreens) {
		this.logger = logger;
		this.browser = browser;
		this.baseUrl = baseUrl;
		this.suite = suite;
		this.logResults = logScreens;

		/* TODO: Clean this up. There should be a less gross way to do this. */
		this.runPath = new java.io.File(
			Cli.class.getProtectionDomain().getCodeSource().getLocation().getPath()
		).toString().replace("%20", " ");

		ArrayList<String> pathParts = new ArrayList<String>(Arrays.asList(runPath.split("/")));
		pathParts.remove(pathParts.size()-1);
		String path = "";
		for(String part : pathParts) {
			path += "/" + part;
		}
		this.runPath = path;
	}
	
	
	
	public ArrayList<TestResult> getSuiteResults() {
		return results;
	}



	public void setup() {
		// Figure out system so we can load proper browser drivers.
		String osFamily = "LIN";
		String chromePath = "/chromedriver";
		if(System.getProperty("os.name").contains("Windows")) {
			osFamily = "WIN";
			chromePath = ".\\chromedriver.exe";
		}

		switch(browser) {

			case "ff":
				driver = new FirefoxDriver();
			break;

			case "ie":
				try {
					service = new InternetExplorerDriverService.Builder()
						.usingDriverExecutable(new File(runPath + ".\\IEDriverServer.exe"))
						.usingAnyFreePort()
						.build();
					((InternetExplorerDriverService) service).start();
					DesiredCapabilities ieOptions = DesiredCapabilities.internetExplorer();
					ieOptions.setCapability("ignoreProtectedModeSettings", true);
					driver = new RemoteWebDriver(((InternetExplorerDriverService) service).getUrl(), ieOptions);
					driver = new Augmenter().augment(driver);
				} catch(NullPointerException e) {
					System.out.println(e.toString());
				} catch (IOException e) {
					System.out.println(e.toString());
				}
			break;

			case "chrome":
				try {
					service = new ChromeDriverService.Builder()
						.usingDriverExecutable(new File(runPath + chromePath))
						.usingAnyFreePort()
						.build();
					((ChromeDriverService) service).start();
					DesiredCapabilities chromeOptions = DesiredCapabilities.chrome();
					chromeOptions.setCapability("chrome.switches", Arrays.asList("--start-maximized"));
					driver = new RemoteWebDriver(((ChromeDriverService) service).getUrl(), chromeOptions);
					driver = new Augmenter().augment(driver);
				} catch(NullPointerException e) {
					System.out.println(e.toString());
				} catch (IOException e) {
					System.out.println(e.toString());
				}
			break;
			
		}
		
		// Set a wait period for finding elements, max of 10 seconds.
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// Set up logger
		logger.setDriver(driver);

		// Start test suite run.
		driver.get(baseUrl);
	}


	
	@SuppressWarnings("unchecked")
	public void runTests(HashSet<String> tests) {
		if(runSuite) {
			// Only do this if there are no pre-test errors on the page.
			for(String test : tests) {
				TestResult result = new TestResult();

				Z z = new Z();
				result.testName = "TestSingleFamily";
				result.passed = z.run(driver, baseUrl, logger);
				result.steps = z.getSteps();
	
				// try {
				// 	Class<?> myClass = Class.forName("com.michaelpidde.testflow.client.tests." + suite + "." + test);
				// 	Object testObject = myClass.getConstructor(WebDriver.class, String.class, Logger.class).newInstance(driver, baseUrl, logger);
				// 	Method run = testObject.getClass().getMethod("run");
				// 	Method getSteps = testObject.getClass().getMethod("getSteps");
				// 	result.testName = testObject.getClass().getSimpleName();
				// 	result.passed = (Boolean)run.invoke(testObject);
				// 	result.steps = (ArrayList<TestStep>)getSteps.invoke(testObject);
				// } catch(ClassNotFoundException e) {
				// 	result.passed = false;
				// 	result.testName = test;
				// 	result.error = e.toString();
				// } catch(NoSuchMethodException e) {
				// 	System.out.println(e.toString());
				// } catch(IllegalAccessException e) {
				// 	System.out.println(e.toString());
				// } catch(InstantiationException e) {
				// 	System.out.println(e.toString());
				// } catch(InvocationTargetException e) {
				// 	// This exception occurs when one of the methods invoked via reflection returns an error. We rethrow the error
				// 	// and re-catch the "inner" real exception.
				// 	try {
				// 		throw e.getCause();
				// 	} catch(TestException inner) {
				// 		result.passed = false;
				// 		result.error = inner.toString();
				// 	} catch(Throwable inner) {
				// 		System.out.println(inner.toString());
				// 	}
				// }
	
				result.setRunEnd();
				results.add(result);
			}
		}
	}



	public void teardown() {
		logger.dump();
		driver.quit();
	}
}
