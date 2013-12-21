package com.michaelpidde.automated;

import com.michaelpidde.automated.util.TestResult;
import com.michaelpidde.automated.util.TestSuite;
import com.michaelpidde.automated.util.Logger;
import com.michaelpidde.automated.util.Emailer;
import com.michaelpidde.automated.Frontend.Frontend;
import com.michaelpidde.automated.Formatter.*;
import com.michaelpidde.automated.XML.ConfigParser;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

import java.util.*;

public class TestCLI {
	/*
	 * Command line arguments
	 */
    @Option(name="-b", usage="Browser to test in.")
    private String browser;

    @Option(name="-t", usage="List of tests to run.")
    private String tests;

    @Option(name="-s", usage="Suite to look for tests in.")
    private String suite;

    @Option(name="-u", usage="Base URL of site to be tested.")
    private String baseUrl = "";

    @Option(name="-e", handler=BooleanOptionHandler.class, usage="Send email.")
    private Boolean sendMail = false;

    @Option(name="-l", handler=BooleanOptionHandler.class, usage="Log results to flat files.")
    private Boolean logResults = false;
    
    @Option(name="--frontend", handler=BooleanOptionHandler.class, usage="Run front end.")
    private Boolean frontend = false;


    /*
     * Instance variables
     */
    Logger logger;
    static ArrayList<String> emailRecipients;
    

    public static void main(String[] args) {
    	/*
    	 * Read in configuration.
    	 */
    	emailRecipients = ConfigParser.getEmailRecipients();
    	
        new TestCLI().runSuite(args);
    }



    public ArrayList<ArrayList<TestResult>> runSuite(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        ArrayList<ArrayList<TestResult>> suiteResults = new ArrayList<ArrayList<TestResult>>();
        
        try {
            parser.parseArgument(args);
        } catch(CmdLineException e) {
            System.out.println(e.getMessage());
            return suiteResults;
        }
        
        if(frontend) {
        	try {
        		new Frontend();
        	} catch(Exception e) {
        		System.out.println(e.toString());
        	}
        } else {
        	// Set up logger
            logger = new Logger(logResults);
            
        	// Process multiple baseURLs
        	HashSet<String> urls = new HashSet<String>(Arrays.asList(baseUrl.split(",")));
        	
        	Iterator<String> urlIterator = urls.iterator();
        	while(urlIterator.hasNext()) {
        		TestSuite testSuite = new TestSuite(logger, browser, (String)urlIterator.next(), suite, logResults);
            	testSuite.setup();
            	testSuite.runTests(new HashSet<String>(Arrays.asList(tests.split(","))));
            	testSuite.teardown();
            	suiteResults.add(testSuite.getSuiteResults());
        	}
        }
        
        
        /*
         * Output formatters
         */
        if(logResults) {
        	FormatterFileSystem formatterFileSystem = new FormatterFileSystem();
        	formatterFileSystem.setLogLocation(logger.getLogLocation());
        	formatterFileSystem.formatSuite(suiteResults);
        }
        
        // Console output
        FormatterText textFormatter = new FormatterText();
        System.out.println(textFormatter.formatSuite(suiteResults));
        
        if(sendMail) {
        	FormatterEmail formatterEmail = new FormatterEmail();
        	String messageBody = formatterEmail.formatSuite(suiteResults);
        	for(String email : emailRecipients) {
        		System.out.println("Sending email to: " + email);
        		//Emailer.send(email, messageBody);
        	}
        }
        
        
        return suiteResults;
    }
}
