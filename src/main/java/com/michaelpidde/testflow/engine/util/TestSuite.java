/*
 * TestFlow Selenium test suite setup and execution
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

import com.michaelpidde.testflow.Cli;
import com.michaelpidde.testflow.engine.util.Logger;
import com.michaelpidde.testflow.engine.util.Test;
import com.michaelpidde.testflow.engine.util.TestStep;
import com.michaelpidde.testflow.engine.util.TestResult;
import com.michaelpidde.testflow.engine.util.TestException;
import com.michaelpidde.testflow.engine.util.TestCompiler;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.control.CompilerConfiguration;

public class TestSuite {
	protected WebDriver driver;
	protected String baseUrl;
	protected String browser;
	protected String app;
	protected Boolean logResults;
	protected Logger logger;
	protected boolean runSuite = true;
	protected ArrayList<TestResult> results = new ArrayList<TestResult>();
	protected String runPath = "";
	protected String osFamily = "LIN";
	protected String separator = "/";
	protected DriverService service;
	protected GroovyClassLoader loader;


	public TestSuite(
		Logger logger, 
		String browser, 
		String baseUrl, 
		String app, 
		Boolean logScreens
	) {

		this.logger = logger;
		this.browser = browser;
		this.baseUrl = baseUrl;
		this.app = app;
		this.logResults = logScreens;

		// Figure out system OS.
		if(System.getProperty("os.name").contains("Windows")) {
			this.osFamily = "WIN";
			this.separator = "\\";
		}

		// Get path based on compiled JAR file.
		this.runPath = new java.io.File(
			Cli.class.getProtectionDomain().getCodeSource().getLocation().getPath()
		).toString().replace("%20", " ");
		runPath = runPath.substring(0, runPath.lastIndexOf(separator));

		// Set up class loader for Groovy compiling
		CompilerConfiguration configuration = new CompilerConfiguration();
		configuration.setScriptBaseClass("com.michaelpidde.testflow.engine.util.Test");
		ClassLoader parent = TestCompiler.class.getClassLoader();
		this.loader = new GroovyClassLoader(parent, configuration);
	}
	
	
	public ArrayList<TestResult> getSuiteResults() {
		return results;
	}


	public void setup() {
		String chromePath = "/chromedriver";
		if(osFamily == "WIN") {
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
					driver = new RemoteWebDriver(
						((InternetExplorerDriverService) service).getUrl(), 
						ieOptions
					);
					driver = new Augmenter().augment(driver);
				} catch(NullPointerException|IOException e) {
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
					chromeOptions.setCapability(
						"chrome.switches", 
						Arrays.asList("--start-maximized")
					);
					driver = new RemoteWebDriver(
						((ChromeDriverService) service).getUrl(), 
						chromeOptions
					);
					driver = new Augmenter().augment(driver);
				} catch(NullPointerException|IOException e) {
					System.out.println(e.toString());
				}
			break;
			
		}
		
		// Set a wait period for finding elements, max of 10 seconds.
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// Set up logger
		logger.setDriver(driver);
	}


	public void runTests(HashSet<String> tests) {
		// Only do this if there are no pre-test errors on the page.
		if(runSuite) {
			/* 
			 * Compile page object(s).
			 * This will return a TestResult object because, though it's not a UI test, 
			 * it's still useful for the end user to see the Groovy compile error
			 * in their test suite results. This also allows us to continue through the 
			 * process and dispose of the webdriver instance so it doesn't become orphaned.
			 */
			File pageDirectory = new File("./tests/" + app + "/pageObject");
			PageObjectCompiler pageCompiler = new PageObjectCompiler(loader);
			TestResult preCompile = pageCompiler.run(pageDirectory);
			preCompile.setRunEnd();

			if(preCompile.getPassed()) {
				// Comile and run tests.
				for(String test : tests) {
					TestCompiler compiler = new TestCompiler(loader);
					String testName = app + "/" + test;
					TestResult result = compiler.run(driver, baseUrl, logger, testName);
					result.steps = compiler.getSteps();
					result.setRunEnd();
					results.add(result);
				}
			} else {
				// Return pre-compile output.
				results.add(preCompile);
			}
		}
	}


	public void teardown() {
		logger.dump();
		switch(browser) {
			case "chrome":
				((ChromeDriverService) service).stop();
			break;
			case "ie":
				((InternetExplorerDriverService) service).stop();
			break;
			case "ff":
				driver.quit();
			break;
		}
	}
}
