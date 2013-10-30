package com.michaelpidde.automated.PageObject.mlsfinder;

import com.michaelpidde.automated.util.TestException;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;

public class General {
	@FindBy(how = How.XPATH, using = "//*[contains(., 'Error Occurred While Processing Request')]")
	private WebElement cferror;
	
	public boolean isCFError() throws TestException {
		try {
			return cferror.isDisplayed();
		} catch(NoSuchElementException e) {
			throw new TestException(e.toString());
		}
	}
}