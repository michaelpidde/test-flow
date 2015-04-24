/*
 * TestFlow Command Line Interface
 *
 * Copyright (C) 2014 Michael Pidde <michael.pidde@gmail.com>
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

package com.michaelpidde.testflow;

import com.michaelpidde.testflow.engine.util.TestResult;
import com.michaelpidde.testflow.engine.util.TestSuite;
import com.michaelpidde.testflow.engine.util.Logger;
import com.michaelpidde.testflow.engine.util.Emailer;
import com.michaelpidde.testflow.engine.formatter.*;
import com.michaelpidde.testflow.engine.xml.ConfigParser;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

import java.util.*;

public class Cli {
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

    @Option(name="-h", usage="Show help.", forbids={"-b", "-t", "-s", "-u", "-e", "-l"})
    private Boolean showHelp = false;


    /*
     * Instance variables
     */
    Logger logger;
    ArrayList<String> emailRecipients;
    

    public static void main(String[] args) {
        new Cli().runSuite(args);
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

        /*
         * Read in configuration.
         */
        ConfigParser config = new ConfigParser("./tests/" + suite + "/Config.xml");
        emailRecipients = config.getEmailRecipients();

        if(showHelp) {
            parser.printUsage(System.out);
            System.exit(0);
        }
        
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
