package com.michaelpidde.automated.Frontend;

import com.michaelpidde.automated.TestCLI;
import com.michaelpidde.automated.Frontend.TestDAO;
import com.michaelpidde.automated.util.TestResult;

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
		
		String action = request.getParameter("action");
		action = (action == null) ? "ListApps" : action;
		
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
		
		if(action.equals("ListApps")) {
			ArrayList<String> apps = TestDAO.getApps();
			root.put("apps", apps);
			
			template = config.getTemplate("ListApps.ftl");
			try {
				template.process(root, writer);
			} catch(TemplateException e) {
				System.out.println(e.toString());
			}
		} else if(action.equals("ListSuites")) {
			String selectedApp = request.getParameter("app");
			/* This used to be handled in a biz logic DAO. */
			ArrayList<String> markets = new ArrayList<String>(Arrays.asList("mn_rmls"));
			ArrayList<String> suites = TestDAO.getSuites(selectedApp);
			ArrayList<String> tests = TestDAO.getTests(selectedApp);
			root.put("suites", suites);
			root.put("markets", markets);
			root.put("tests", tests);
			template = config.getTemplate("ListSuites.ftl");
			try {
				template.process(root, writer);
			} catch(TemplateException e) {
				System.out.println(e.toString());
			}
		} else if(action.equals("run")) {
			String runSuite = request.getParameter("suite");
			if(runSuite != null) {
				/*HashMap<String, ArrayList<String>> staff = MlsfinderDAO.getStaffMarkets();
				Iterator<Entry<String, ArrayList<String>>> iterator = staff.entrySet().iterator();
				while(iterator.hasNext()) {
					Map.Entry<String, ArrayList<String>> element = (Map.Entry<String, ArrayList<String>>)iterator.next();
					System.out.println("Staff Member: " + element.getKey());
					for(String url : element.getValue()) {
						System.out.println(url);
					}
				}*/
				
				// TODO Implement suite logic.
			} else {
				String selectedMarkets = request.getParameter("selectedMarkets");
				String selectedTests = request.getParameter("selectedTests");
				//String selectedBrowser = request.getParameter("browser");
				//String selectedEnvironment = request.getParameter("environment");
				Boolean logResults = checkboxToBoolean(request.getParameter("logResults"));
				//Boolean sendMail =  checkboxToBoolean(request.getParameter("sendMail"));
				
				TestCLI cli = new TestCLI();
				
				ArrayList<String> args = new ArrayList<String>(Arrays.asList("-e", "-s", "mlsfinder", "-b", "ff", "-u", 
					generateMlsfinderUrlString(new ArrayList<String>(Arrays.asList(selectedMarkets.split(","))), "live"), 
					"-t", selectedTests
				));
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
		} else if(action.equals("exit")) {
			System.exit(0);
		}
	}
	
	
	
	private Boolean checkboxToBoolean(String value) {
		return (value == null) ? false : true;
	}
	
	
	
	private String generateMlsfinderUrlString(ArrayList<String> markets, String environment) {
		String output = "";
		String subdomain = "";

		/* Removed some conditions from this */
		if(environment == "live") {
			subdomain += "www";
		}
		
		for(String market : markets) {
			output += String.format("http://%s.mlsfinder.com/%s/wordpressplugincobrandsite/index.cfm?resetform=y,", subdomain, market);
		}
		output = output.substring(0, output.length()-1);
		
		return output;
	}
	
}
