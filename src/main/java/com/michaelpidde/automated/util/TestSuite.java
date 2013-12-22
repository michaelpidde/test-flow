package com.michaelpidde.automated.util;

import com.michaelpidde.automated.TestCLI;
import com.michaelpidde.automated.tests.general.CFError;
import com.michaelpidde.automated.util.Logger;
import com.michaelpidde.automated.util.TestStep;
import com.michaelpidde.automated.util.TestResult;
import com.michaelpidde.automated.util.TestException;

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
			TestCLI.class.getProtectionDomain().getCodeSource().getLocation().getPath()
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
		if(browser == "ff") {
			driver = new FirefoxDriver();
		} else if(browser == "ie") {
			try {
				service = new InternetExplorerDriverService.Builder()
					.usingDriverExecutable(new File(runPath + "\\IEDriverServer.exe"))
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
		} else if(browser == "chrome") {
			try {
				service = new ChromeDriverService.Builder()
					.usingDriverExecutable(new File(runPath + "\\chromedriver.exe"))
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
		}
		
		// Set a wait period for finding elements, max of 10 seconds.
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// Set up logger
		logger.setDriver(driver);

		// Start test suite run.
		driver.get(baseUrl);
		
		// Do a quick smoke test to see if the URL has any errors on it.
		CFError cferror = new CFError(driver, baseUrl, logger);
		boolean error = false;
		try {
			error = cferror.run();
		} catch(TestException e) {
			// Don't do anything with this error at this time.
		}
		
		if(error) {
			runSuite = false;
		}
	}


	
	@SuppressWarnings("unchecked")
	public void runTests(HashSet<String> tests) {
		if(runSuite) {
			// Only do this if there are no pre-test errors on the page.
			for(String test : tests) {
				TestResult result = new TestResult();
	
				try {
					Class<?> myClass = Class.forName("com.michaelpidde.automated.tests." + suite + "." + test);
					Object testObject = myClass.getConstructor(WebDriver.class, String.class, Logger.class).newInstance(driver, baseUrl, logger);
					Method run = testObject.getClass().getMethod("run");
					Method getSteps = testObject.getClass().getMethod("getSteps");
					result.testName = testObject.getClass().getSimpleName();
					result.passed = (Boolean)run.invoke(testObject);
					result.steps = (ArrayList<TestStep>)getSteps.invoke(testObject);
				} catch(ClassNotFoundException e) {
					result.passed = false;
					result.testName = test;
					result.error = e.toString();
				} catch(NoSuchMethodException e) {
					System.out.println(e.toString());
				} catch(IllegalAccessException e) {
					System.out.println(e.toString());
				} catch(InstantiationException e) {
					System.out.println(e.toString());
				} catch(InvocationTargetException e) {
					// This exception occurs when one of the methods invoked via reflection returns an error. We rethrow the error
					// and re-catch the "inner" real exception.
					try {
						throw e.getCause();
					} catch(TestException inner) {
						result.passed = false;
						result.error = inner.toString();
					} catch(Throwable inner) {
						System.out.println(inner.toString());
					}
				}
	
				result.setRunEnd();
				results.add(result);
			}
		}
	}



	public void teardown() {
		logger.dump();
		
		if(browser == "chrome") {
			((ChromeDriverService) service).stop();
		} else if(browser == "ie") {
			((InternetExplorerDriverService) service).stop();
		} else if(browser == "ff") {
			driver.quit();
		}
	}
}
