package com.michaelpidde.automated.tests.mlsfinder;

import com.michaelpidde.automated.PageObject.mlsfinder.SearchPage;
import com.michaelpidde.automated.util.Test;
import com.michaelpidde.automated.util.Logger;
import com.michaelpidde.automated.util.TestException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class HasSingleFamily extends Test {
	public HasSingleFamily(WebDriver driver, String baseUrl, Logger logger) {
		super(driver, baseUrl, logger);
	}

	public boolean run() throws TestException {
		SearchPage page = PageFactory.initElements(driver, SearchPage.class);
		
		driver.get(baseUrl);
		logStep("Get base URL.");

		// Click single family checkbox
		page.checkCheckbox(page.getSingleFamily());
		logStep("Click single family checkbox.");

		int listingCount = page.getListingCount(4);
		logStep("Get listing count.");
		
		return (listingCount > 0)? true : false;
	}
}