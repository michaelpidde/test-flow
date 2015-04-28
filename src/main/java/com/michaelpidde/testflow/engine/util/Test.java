/*
 * TestFlow test container
 *
 * This file is part of TestFlow.
 *
 * Copyright (C) 2014-2015 Michael Pidde <michael.pidde@gmail.com>
 *
 * TestFlow is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * TestFlow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TestFlow; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.michaelpidde.testflow.engine.util;

import com.michaelpidde.testflow.engine.util.Logger;
import com.michaelpidde.testflow.engine.util.TestStep;
import com.michaelpidde.testflow.engine.util.PageObjectCompiler;

import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.util.ArrayList;
import java.lang.Thread;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import groovy.lang.Script;

public abstract class Test extends Script {
	protected WebDriver driver;
	protected String baseUrl;
	protected Logger logger;
	private int screenIndex = 1;
	private String name = this.getClass().getSimpleName();
	private ArrayList<TestStep> steps = new ArrayList<TestStep>();


	public void setup(WebDriver driver, String baseUrl, Logger logger) {
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


	public WebElement findElementByClass(String name) {
		WebElement element = driver.findElement(By.className(name));
		return element;
	}


	public Object loadPageObject(Class cls) {
		return PageFactory.initElements(driver, cls);
	}


	public void wait(int seconds) {
		try {
			// Wait for login redirect.
			Thread.sleep(seconds * 1000);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
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