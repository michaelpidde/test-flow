/*
 * TestFlow Command Line Interface
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

package com.michaelpidde.testflow;

import com.michaelpidde.testflow.engine.util.TestResult;
import com.michaelpidde.testflow.engine.util.TestSuite;
import com.michaelpidde.testflow.engine.util.Logger;
import com.michaelpidde.testflow.engine.util.Emailer;
import com.michaelpidde.testflow.engine.formatter.*;
import com.michaelpidde.testflow.engine.util.Config;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

import java.util.*;
import java.nio.file.Path;

public class Cli {
	/*
	 * Command line arguments
	 */
    @Option(name="-b", usage="Browser to test in. [chrome|ff|ie]")
    private String browser;

    @Option(name="-t", usage="Comma separated list of tests to run.")
    private String tests;

    @Option(name="-a", usage="Application to get tests in.")
    private String app;

    @Option(name="-u", usage="Comma separated list of URLs of sites to be tested.")
    private String baseUrl = "";

    @Option(name="-e", handler=BooleanOptionHandler.class, usage="Send email.")
    private Boolean sendMail = false;

    @Option(name="-l", handler=BooleanOptionHandler.class, usage="Log results to flat files.")
    private Boolean logFilesystem = false;

    @Option(name="-d", handler=BooleanOptionHandler.class, usage="Log results to database.")
    private Boolean logDatabase = false;

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

        if(showHelp) {
            parser.printUsage(System.out);
            System.exit(0);
        }

        // Read in config
        Config config = new Config("./tests/" + app + "/config.json");
        this.emailRecipients = config.getEmailRecipients();
        
    	// Set up logger
        this.logger = new Logger(logFilesystem);
        
    	// Process multiple baseURLs
    	HashSet<String> urls = new HashSet<String>(Arrays.asList(baseUrl.split(",")));
    	
    	Iterator<String> urlIterator = urls.iterator();
    	while(urlIterator.hasNext()) {
    		TestSuite testSuite = new TestSuite(logger, browser, (String)urlIterator.next(), app, logFilesystem);
        	testSuite.setup();
        	testSuite.runTests(new HashSet<String>(Arrays.asList(tests.split(","))));
        	testSuite.teardown();
        	suiteResults.add(testSuite.getSuiteResults());
    	}
        
        doLogging(suiteResults);
        
        return suiteResults;
    }


    private void doLogging(final ArrayList<ArrayList<TestResult>> suiteResults) {
        if(logFilesystem) {
            logToFilesystem(suiteResults, logger.getLogLocation());
        }

        if(logDatabase) {
            logToDatabase(suiteResults);
        }
 
        if(sendMail) {
            logToEmail(suiteResults);
        }

        logToConsole(suiteResults);
    }


    private void logToConsole(final ArrayList<ArrayList<TestResult>> suiteResults) {
        FormatterText textFormatter = new FormatterText();
        System.out.println(textFormatter.formatSuite(suiteResults));
    }


    private void logToEmail(final ArrayList<ArrayList<TestResult>> suiteResults) {
        FormatterEmail formatterEmail = new FormatterEmail();
        String messageBody = formatterEmail.formatSuite(suiteResults);
        for(String email : emailRecipients) {
            System.out.println("Sending email to: " + email);
            //Emailer.send(email, messageBody);
        }
    }


    private void logToDatabase(final ArrayList<ArrayList<TestResult>> suiteResults) {
        System.out.println("Log to database.");
    }


    private void logToFilesystem(final ArrayList<ArrayList<TestResult>> suiteResults, final Path logLocation) {
        FormatterFileSystem formatterFileSystem = new FormatterFileSystem();
        formatterFileSystem.setLogLocation(logLocation);
        formatterFileSystem.formatSuite(suiteResults);
    }
}
