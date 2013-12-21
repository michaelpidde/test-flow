package com.michaelpidde.automated.util;

import com.michaelpidde.automated.util.Logger;
import com.michaelpidde.automated.util.TestStep;

import java.util.ArrayList;
import org.openqa.selenium.WebDriver;

public abstract class Test {
	protected WebDriver driver;
	protected String baseUrl;
	protected Logger logger;
	private int screenIndex = 1;
	private String name = this.getClass().getSimpleName();
	private ArrayList<TestStep> steps = new ArrayList<TestStep>();



	public Test(WebDriver driver, String baseUrl, Logger logger) {
		this.driver = driver;
		this.baseUrl = baseUrl;
		this.logger = logger;
	}



	public ArrayList<TestStep> getSteps() {
		return steps;
	}



	public void logStep(String description) {
		steps.add(new TestStep(description, scrnCap()));
	}



	protected String scrnCap() {
		try {
			// Put a short implicit pause to account for UI latency.
			Thread.sleep(500);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		String screenPath = logger.screenshot(screenIndex, name);
		screenIndex++;
		return screenPath;
	}



	protected void log(String message) {
		logger.log(message);
	}
}