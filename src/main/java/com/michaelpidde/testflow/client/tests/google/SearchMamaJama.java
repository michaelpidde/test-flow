package com.michaelpidde.testflow.client.tests.google;

import com.michaelpidde.testflow.client.pageObject.google.Search;
import com.michaelpidde.testflow.engine.util.Test;
import com.michaelpidde.testflow.engine.util.Logger;
import com.michaelpidde.testflow.engine.util.TestException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class SearchMamaJama extends Test {
	public SearchMamaJama(WebDriver driver, String baseUrl, Logger logger) {
		super(driver, baseUrl, logger);
	}

	public boolean run() throws TestException {
		Search search = PageFactory.initElements(driver, Search.class);
		
		driver.get(baseUrl);
		logStep("Get base URL.");


		search.doSearch("Mama Jama");
		logStep("Search for 'Mama Jama'");

		String results = search.getResults();
		logStep("Search result: " + results);
		
		return (results.length() > 0)? true : false;
	}
}