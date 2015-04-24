/*
 * TestFlow Frontend request handler
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

package com.michaelpidde.testflow.client.frontend;

import com.michaelpidde.testflow.Cli;
import com.michaelpidde.testflow.engine.xml.TestDAO;
import com.michaelpidde.testflow.engine.util.TestResult;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
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
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException {
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		
		// Set up param defaults
		String action = request.getParameter("action");
		action = (action == null) ? "ListApps" : action;
		String selectedApp = request.getParameter("app");
		selectedApp = (selectedApp == null) ? "" : selectedApp;

		PrintWriter writer = response.getWriter();
		// Set up Freemarker template config
		Configuration config = new Configuration();
		config.setClassForTemplateLoading(this.getClass(), "/ftl-templates/frontend");
		config.setObjectWrapper(new DefaultObjectWrapper());
		config.setDefaultEncoding("utf-8");
		config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		
		// Set up data for template
		Map<String, Serializable> root = new HashMap<String, Serializable>();
		root.put("title", "TestFlow");
		Template template;
		TestDAO testDao = new TestDAO("./tests/Tests.xml");
		
		switch(action) {

			case "ListApps":
				ArrayList<String> apps = testDao.getApps();
				root.put("apps", apps);
				
				template = config.getTemplate("ListApps.ftl");
				try {
					template.process(root, writer);
				} catch(TemplateException e) {
					System.out.println(e.toString());
				}
			break;

			case "ListSuites":
				ArrayList<String> suites = testDao.getSuites(selectedApp);
				ArrayList<String> tests = testDao.getTests(selectedApp);
				root.put("app", selectedApp);
				root.put("suites", suites); 
				root.put("tests", tests);
				template = config.getTemplate("ListSuites.ftl");
				try {
					template.process(root, writer);
				} catch(TemplateException e) {
					System.out.println(e.toString());
				}
			break;

			case "run":
				String runSuite = request.getParameter("suite");
				if(runSuite != null) {
					// TODO Implement suite logic.
				} else {
					String selectedTests = request.getParameter("selectedTests");
					String selectedBrowser = request.getParameter("browser");
					//String selectedEnvironment = request.getParameter("environment");
					Boolean logResults = checkboxToBoolean(request.getParameter("logResults"));
					//Boolean sendMail =  checkboxToBoolean(request.getParameter("sendMail"));
					
					Cli cli = new Cli();

					ArrayList<String> args = new ArrayList<String>(Arrays.asList("-e", "-s", selectedApp, "-b", selectedBrowser, "-u", 
						testDao.getBaseUrl(selectedApp), "-t", selectedTests));
					if(logResults) {
						args.add("-l");
					}
					
					ArrayList<ArrayList<TestResult>> suiteResults = cli.runSuite(args.toArray(new String[args.size()]));
					// TODO Replace this with an HTML formatter.
					String body = "";
					for(ArrayList<TestResult> suite : suiteResults) {
						body += "<b>Suite Results:</b><br />";
						for(TestResult result : suite) {
							body += result.write().replaceAll("(\r\n|\n)", "<br />");
							body += "<hr />";
						}
						body += "<hr />";
					}
					root.put("body", body);
					template = config.getTemplate("ListResults.ftl");
					try {
						template.process(root, writer);
					} catch(TemplateException e) {
						System.out.println(e.toString());
					}
				}
			break;

			case "exit":
				System.exit(0);
			break;

		}
	}
	
	
	
	private Boolean checkboxToBoolean(String value) {
		return (value == null) ? false : true;
	}
	
}
