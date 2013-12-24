package com.michaelpidde.testflow.client.pageObject.google;

import com.michaelpidde.testflow.engine.util.TestException;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;

public class Search {
	@FindBy(how = How.ID, using = "resultStats")
	private WebElement resultStats;

	@FindBy(how = How.ID, using = "gbqfq")
	private WebElement searchBox;

	@FindBy(how = How.ID, using = "gbqfb")
	private WebElement searchButton;

	/*
	 * Implicit fail element (using for testing)
	 */
	@FindBy(how = How.ID, using = "implicit_fail")
	public WebElement implicitFail;


	/*
	 * Actions
	 */
	public void doSearch(String criteria) throws TestException {
		try {
			searchBox.sendKeys(criteria);
		} catch(NoSuchElementException e) {
			throw new TestException(e.toString());
		}
	}

	public String getResults() throws TestException {
		return getResults(0);
	}

	public String getResults(int seconds) throws TestException {
		// Account for Ajax latency if count is updating.
		try {
			// Multiply for milliseconds
			Thread.sleep(seconds*1000);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		String searchResults;
		try {
			searchResults = resultStats.getText();
		} catch(NoSuchElementException e) {
			return "";
			//throw new TestException(e.toString());
		}

		return searchResults;
	}
}