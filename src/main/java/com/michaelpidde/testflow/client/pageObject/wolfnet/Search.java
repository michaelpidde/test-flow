package com.michaelpidde.testflow.client.pageObject.wolfnet;

import com.michaelpidde.testflow.engine.util.TestException;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

public class Search {
	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Sign in")
	public WebElement loginLink;

	@FindBy(how = How.ID_OR_NAME, using = "username")
	public WebElement usernameInput;

	@FindBy(how = How.ID_OR_NAME, using = "password")
	public WebElement passwordInput;

	//@FindBy(how = How.XPATH, using = "//span[@class='user-menu active']/a")
	@FindBy(how = How.XPATH, using = "//span[contains(@id, 'user-menu-')]/a")
	public WebElement userMenuButton;

	@FindBy(how = How.XPATH, using = "//button[text()=\"Sign in\"]")
	public WebElement loginButton;

	@FindBy(how = How.CLASS_NAME, using = "criteria")
	public WebElement searchOptionsBar;

	@FindBy(how = How.XPATH, using = "//span[@class='result-count-label']/preceding-sibling::span")
	public WebElement resultCount;

	private WebDriver driver;


	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}


	public void login() {
		loginLink.click();
		usernameInput.sendKeys("michael.pidde@gmail.com");
		passwordInput.sendKeys("password");
		loginButton.click();
	}


	public void openSearchOptionMenu(String menuLabel) {
		WebElement menu = searchOptionsBar.findElement(
			By.xpath("//fieldset/legend/span[contains(text(),'" + menuLabel + "')]")
		);
		menu.click();
	}


	public void selectPropertyType(String label) {
		WebElement checkbox = driver.findElement(
			By.xpath("//label[contains(., '" + label + "')]/input")
		);
		checkbox.click();
	}


	public int getResultCount() {
		return Integer.parseInt(resultCount.getText());
	}
}