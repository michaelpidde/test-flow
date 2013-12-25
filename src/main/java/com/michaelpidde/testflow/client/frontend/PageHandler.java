/*
 * TestFlow Frontend request handler
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
		
		switch(action) {

			case "ListApps":
				ArrayList<String> apps = TestDAO.getApps();
				root.put("apps", apps);
				
				template = config.getTemplate("ListApps.ftl");
				try {
					template.process(root, writer);
				} catch(TemplateException e) {
					System.out.println(e.toString());
				}
			break;

			case "ListSuites":
				ArrayList<String> suites = TestDAO.getSuites(selectedApp);
				ArrayList<String> tests = TestDAO.getTests(selectedApp);
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
						TestDAO.getBaseUrl(selectedApp), "-t", selectedTests));
					if(logResults) {
						args.add("-l");
					}
					
					ArrayList<ArrayList<TestResult>> suiteResults = cli.runSuite(args.toArray(new String[args.size()]));
					for(ArrayList<TestResult> suite : suiteResults) {
						for(TestResult result : suite) {
							writer.println(result.write());
							writer.println("<br />");
						}
						writer.println("<hr />");
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
