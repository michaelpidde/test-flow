/*
 * TestFlow logging utility
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.ArrayList;
import java.rmi.server.UID;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;

public class Logger {
	private WebDriver driver;
	private Boolean logResults;
	private ArrayList<String> log = new ArrayList<String>();
	private Path logLocation;
	private Path screenshotLocation;



	public Logger(Boolean logResults) {
		this.logResults = logResults;
		if(logResults) {
			setLogLocation();
			setScreenshotLocation();
		}
	}
	
	
	
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}



	private void setLogLocation() {
		try {
			logLocation = Files.createTempDirectory(null);
		} catch(IOException e) {
			System.out.println(e);
		}
	}
	
	
	
	public Path getLogLocation() {
		return logLocation;
	}



	private void setScreenshotLocation() {
		try {
			screenshotLocation = Files.createDirectory(logLocation.resolve("images"));
		} catch(IOException e) {
			System.out.println(e);
		}
	}



	public String screenshot(int index, String prefix) {
		String filename = "";
		if(logResults) {
			File screenFile;
			try {
				screenFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				String newName = prefix + String.valueOf(index) + new UID().toString().replace(":", "") + ".jpg";
				Files.move(screenFile.toPath(), screenshotLocation.resolve(newName));
				filename = screenshotLocation.resolve(newName).toString();
			} catch(WebDriverException e) {
				System.out.println(e);
			} catch(IOException e) {
				System.out.println(e);
			}
		}
		return filename;
	}



	public void log(String message) {
		log.add(message);
	}



	public void dump() {
		for(String message : log) {
			System.out.println(message);
		}
	}
}