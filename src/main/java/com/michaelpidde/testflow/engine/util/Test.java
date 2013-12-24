/*
 * TestFlow test container
 *
 * Copyright (C) 2013 Michael Pidde <michael.pidde@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.michaelpidde.testflow.engine.util;

import com.michaelpidde.testflow.engine.util.Logger;
import com.michaelpidde.testflow.engine.util.TestStep;

import java.util.ArrayList;
import org.openqa.selenium.WebDriver;

public abstract class Test {
	protected WebDriver driver;
	protected String baseUrl;
	protected Logger logger;
	private int screenIndex = 1;
	private String name = this.getClass().getSimpleName();
	private ArrayList<TestStep> steps = new ArrayList<TestStep>();



	public Test(WebDriver driver, String baseUrl, Logger logger) {
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