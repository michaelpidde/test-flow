package com.michaelpidde.testflow.client.tests.wolfnet;

import com.michaelpidde.testflow.client.pageObject.wolfnet.Search;
import com.michaelpidde.testflow.engine.util.Test;
import com.michaelpidde.testflow.engine.util.Logger;
import com.michaelpidde.testflow.engine.util.TestException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class TestLoginModal extends Test {
	public TestLoginModal(WebDriver driver, String baseUrl, Logger logger) {
		super(driver, baseUrl, logger);
	}

	public boolean run() throws TestException {
		Search search = PageFactory.initElements(driver, Search.class);
		
		driver.get(baseUrl);
		logStep("Get base URL.");

		search.login();
		logStep("Logging in.");

		// Wait for redirect
		wait(3);

		Boolean hasModal = findElementByClass("modal").isDisplayed();
		logStep("Check login.");

		// If the modal is gone, the login was good...
		return !hasModal;
	}
}