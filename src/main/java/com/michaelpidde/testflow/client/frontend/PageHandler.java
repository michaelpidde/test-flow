/*
 * TestFlow Frontend request handler
 *
 * This file is part of TestFlow.
 *
 * Copyright (C) 2014-2016 Michael Pidde <michael.pidde@gmail.com>
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

package com.michaelpidde.testflow.client.frontend;

import com.michaelpidde.testflow.Cli;
import com.michaelpidde.testflow.engine.util.Config;
import com.michaelpidde.testflow.engine.util.Directory;
import com.michaelpidde.testflow.engine.util.TestResult;
import com.michaelpidde.testflow.engine.formatter.FormatterHTML;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class PageHandler extends AbstractHandler {

	public void handle(
		String target, 
		Request baseRequest, 
		HttpServletRequest request, 
		HttpServletResponse response
	) throws IOException, ServletException {

		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		
		// Set up param defaults
		String action = request.getParameter("action");
		action = (action == null) ? "ListApps" : action;

		PrintWriter writer = response.getWriter();
		
		switch(action) {
		case "ListApps":
			listAppsHandler(writer);
			break;
		case "ListSuites":
			listSuitesHandler(writer, request);
			break;
		case "run":
			runHandler(writer, request);
			break;
		case "exit":
			System.exit(0);
			break;
		}
	}


	private void listAppsHandler(final PrintWriter writer) {
		File directory = new File("./tests");
		Map<String, Serializable> root = getTemplateHashmap();

		ArrayList<String> apps = Directory.listDirectories(directory, ".git");
		root.put("apps", apps);

		renderTemplate("ListApps.ftl", root, writer);
	}


	private void listSuitesHandler(final PrintWriter writer, final HttpServletRequest request) {
		Map<String, Serializable> root = getTemplateHashmap();

		String selectedApp = getSelectedApp(request.getParameter("app"));
		Config testConfig = new Config("./tests/" + selectedApp + "/config.json");
		ArrayList<String> suites = testConfig.getSuites();
		File testDirectory = new File("./tests/" + selectedApp);
		ArrayList<String> tests = Directory.listDirectoryFiles(testDirectory, ".groovy");

		// Remove file extension.
		for(int i = 0; i < tests.size(); i++) {
			tests.set(i, tests.get(i).replace(".groovy", ""));
		}
		root.put("app", selectedApp);
		root.put("suites", suites); 
		root.put("tests", tests);

		renderTemplate("ListSuites.ftl", root, writer);
	}


	private void runHandler(final PrintWriter writer, final HttpServletRequest request) {
		Map<String, Serializable> root = getTemplateHashmap();

		String selectedApp = getSelectedApp(request.getParameter("app"));
		Config testConfig = new Config("./tests/" + selectedApp + "/config.json");

		String runSuite = request.getParameter("suite");
		if(runSuite != null) {
			// TODO Implement suite logic.
		} else {
			String selectedTests = request.getParameter("selectedTests");
			String selectedBrowser = request.getParameter("browser");
			//String selectedEnvironment = request.getParameter("environment");
			Boolean logDatabase = checkboxToBoolean(request.getParameter("logDatabase"));
			Boolean logResults = checkboxToBoolean(request.getParameter("logResults"));
			Boolean sendMail = checkboxToBoolean(request.getParameter("sendMail"));
			
			Cli cli = new Cli();

			ArrayList<String> args = new ArrayList<String>(Arrays.asList(
				"-a", selectedApp, 
				"-b", selectedBrowser, 
				"-u", testConfig.getBaseUrl(), 
				"-t", selectedTests
			));
			if(logResults) {
				args.add("-l");
			}
			if(sendMail) {
				args.add("-e");
			}
			if(logDatabase) {
				args.add("-d");
			}
			
			ArrayList<ArrayList<TestResult>> suiteResults = cli.runSuite(args.toArray(
				new String[args.size()])
			);
			FormatterHTML formatter = new FormatterHTML();
			String body = formatter.formatSuite(suiteResults);
			root.put("body", body);
			
			renderTemplate("ListResults.ftl", root, writer);
		}
	}


	private Map<String, Serializable> getTemplateHashmap() {
		Map<String, Serializable> root = new HashMap<String, Serializable>();
		root.put("title", "TestFlow");
		return root;
	}


	private Configuration getTemplateConfiguration() {
		Configuration config = new Configuration();
		config.setClassForTemplateLoading(this.getClass(), "/ftl-templates/frontend");
		config.setObjectWrapper(new DefaultObjectWrapper());
		config.setDefaultEncoding("utf-8");
		config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		return config;
	}


	private void renderTemplate(
		final String name, 
		final Map<String, Serializable> vars, 
		final PrintWriter writer
	) {

		Configuration config = getTemplateConfiguration();
		try {
			Template template = config.getTemplate(name);
			template.process(vars, writer);
		} catch(TemplateException e) {
			System.out.println(e.toString());
		} catch(java.io.IOException e) {
			System.out.println(e.toString());
		}
	}


	private String getSelectedApp(final String selectedApp) {
		return (selectedApp == null) ? "" : selectedApp;
	}
	
	
	private Boolean checkboxToBoolean(String value) {
		return (value == null) ? false : true;
	}

}
