package com.michaelpidde.automated.PageObject.mlsfinder;

import com.michaelpidde.automated.util.TestException;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;

public class SearchPage {
	@FindBy(how = How.CLASS_NAME, using = "wntResultsCount")
	private WebElement resultsCount;

	/*
	 * Property Types
	 */
	@FindBy(how = How.ID, using = "ptype_single_family")
	private WebElement singleFamily;

	@FindBy(how = How.ID, using = "ptype_condo")
	private WebElement condo;

	@FindBy(how = How.ID, using = "ptype_townhouse")
	private WebElement townhouse;

	@FindBy(how = How.ID, using = "ptype_duplex")
	private WebElement duplex;

	/*
	 * Implicit fail element (using for testing)
	 */
	@FindBy(how = How.ID, using = "implicit_fail")
	public WebElement implicitFail;

	/*
	 * Accessors
	 */
	public WebElement getSingleFamily() {
		return singleFamily;
	}


	/*
	 * Actions
	 */
	public void checkCheckbox(WebElement checkbox) throws TestException {
		try {
			checkbox.click();
		} catch(NoSuchElementException e) {
			throw new TestException(e.toString());
		}
	}

	public int getListingCount() throws TestException {
		return getListingCount(0);
	}

	public int getListingCount(int seconds) throws TestException {
		// Account for Ajax latency if count is updating.
		try {
			// Multiply for milliseconds
			Thread.sleep(seconds*1000);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		String listings;
		try {
			listings = resultsCount.getText();
		} catch(NoSuchElementException e) {
			throw new TestException(e.toString());
		}

		int listingCount = Integer.parseInt(listings);
		return listingCount;
	}
}