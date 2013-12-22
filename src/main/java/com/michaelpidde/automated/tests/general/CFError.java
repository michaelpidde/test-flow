package com.michaelpidde.automated.tests.general;

import com.michaelpidde.automated.PageObject.Google.General;
import com.michaelpidde.automated.util.Test;
import com.michaelpidde.automated.util.Logger;
import com.michaelpidde.automated.util.TestException;

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