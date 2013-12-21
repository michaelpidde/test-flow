package com.michaelpidde.automated.Formatter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.rmi.server.UID;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import com.michaelpidde.automated.util.TestResult;

public class FormatterFileSystem implements IResultFormatter {
	private Path logLocation;
	private Configuration config;
	
	
	
	public void setLogLocation(Path logLocation) {
		this.logLocation = logLocation;
		this.config = setConfig();
	}
	
	
	
	private Configuration setConfig() {
		// Set up Freemarker template config
		Configuration config = new Configuration();
		config.setClassForTemplateLoading(this.getClass(), "/ftl-templates/logging");
		config.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
		config.setDefaultEncoding("utf-8");
		config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		return config;
	}
	
	
	
	@Override
	public String formatResult(TestResult result) {
		// Set up data for template
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("title", "Wolfnet Regression Testing Results: " + result.testName);
		root.put("steps", result.steps);
		root.put("error", result.error);
		Template template;
		
		FileWriter writer;
		String filename = logLocation.toString() + "/" + result.testName + new UID().toString().replace(":", "") + ".htm";
		try {
			writer = new FileWriter(filename);
			template = config.getTemplate("TestResult.ftl");
			try {
				template.process(root, writer);
			} catch(TemplateException e) {
				System.out.println(e.toString());
			}
			writer.close();
		} catch(IOException e) {
			System.out.println(e.toString());
		}
		
		return filename;
	}

	
	
	@Override
	public String formatSuite(ArrayList<ArrayList<TestResult>> suite) {
		// Set up data for template
		Map<String, Serializable> root = new HashMap<String, Serializable>();
		root.put("title", "Wolfnet Regression Testing Results");
		root.put("logLocation", logLocation.toString());
		Template template;
		
		String file = logLocation.toString() + "/index.htm";
		
		// Write the index page
		try {
			FileWriter index = new FileWriter(file);
			template = config.getTemplate("Frameset.ftl");
			
			try {
				template.process(root, index);
			} catch(TemplateException e) {
				System.out.println(e.toString());
			}
			
			index.close();
		} catch(IOException e) {
			System.out.println(e.toString());
		}

		// Write the test pages
		try {
			FileWriter main = new FileWriter(logLocation.toString() + "/main.htm");
			template = config.getTemplate("Main.ftl");
			root.put("suite", suite);
			ArrayList<String> resultLinks = new ArrayList<String>();
			
			for(ArrayList<TestResult> run : suite) {
				for(TestResult result : run) {
					resultLinks.add(formatResult(result));
				}
			}
			
			root.put("resultLinks", resultLinks);
			
			try {
				template.process(root, main);
			} catch(TemplateException e) {
				System.out.println(e.toString());
			}
			
			main.close();
		} catch(IOException e) {
			System.out.println(e.toString());
		}
		
		return null;
	}

}