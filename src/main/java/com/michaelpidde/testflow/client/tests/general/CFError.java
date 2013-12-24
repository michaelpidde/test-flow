package com.michaelpidde.testflow.client.tests.general;

import com.michaelpidde.testflow.client.pageObject.google.General;
import com.michaelpidde.testflow.engine.util.Test;
import com.michaelpidde.testflow.engine.util.Logger;
import com.michaelpidde.testflow.engine.util.TestException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class CFError extends Test {
	public CFError(WebDriver driver, String baseUrl, Logger logger) {
		super(driver, baseUrl, logger);
	}

	public boolean run() throws TestException {
		General page = PageFactory.initElements(driver, General.class);
		
		driver.get(baseUrl);
		logStep("Get base URL.");

		boolean cferror = page.isCFError();
		logStep("Check CF error.");
		
		return cferror;
	}
}