/******************************************************************************
 * Copyright 2020 Stevens Institute of Technology, Collective Design Lab
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.stevens.code.bilevel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import edu.stevens.code.bilevel.app.DesignerApp;
import edu.stevens.code.bilevel.app.ManagerApp;
import edu.stevens.code.bilevel.model.Session;

/**
 * The main application launcher. Reads options from a command line interface (CLI).
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 * @author Ambrosio Valencia-Romero <avalenci@stevens.edu>
 */
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    
	public static void main(String[] args) {
    	// create the command line parser
    	CommandLineParser parser = new DefaultParser();
    	
    	// create the command line options
    	Options options = new Options();
    	options.addOption(
    			Option.builder("h")
    				.longOpt("help")
    				.desc("prints the help menu")
    				.build()
    			);
    	options.addOption(
    			Option.builder("d")
    				.longOpt("designer")
    				.hasArgs()
    				.argName("index (indices)")
    				.desc("launch a designer interface")
    				.build()
				);
    	options.addOption(
    			Option.builder("m")
	    			.longOpt("manager")
    				.hasArg()
    				.argName("experiment")
	    			.desc("launch a manager interface with an experiment")
	    			.build()
    			);
    	options.addOption(
    			Option.builder("h")
	    			.longOpt("hostname")
    				.hasArg()
    				.argName("name")
	    			.desc("Manager IP address/hostname (default: localhost)")
	    			.build()
    			);
    	// try to parse the command line arguments
		try {
			CommandLine cmd = parser.parse(options, args);
			String hostname = cmd.getOptionValue("h", "localhost");
			if(cmd.hasOption("d")) {
				for(String value : cmd.getOptionValues("d")) {
					try {
						int id = Integer.parseInt(value);
						new DesignerApp(id).init(hostname);
					} catch(NumberFormatException e) {
						logger.error(e);
					}
				}
			}
			if(cmd.hasOption("m")) {
				Gson gson = new Gson();
				try {
					String path = cmd.getOptionValue("m");
					BufferedReader reader = new BufferedReader(new FileReader(path));
					Session session = gson.fromJson(reader, Session.class);
					new ManagerApp(session).init(hostname);
				} catch(FileNotFoundException e) {
					logger.error(e);
				}
			}
			if(!(cmd.hasOption("d") || cmd.hasOption("m"))) {
				// print the help menu and quit
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("CoDe", options);
				System.exit(0);
			}
		} catch (ParseException e) {
			logger.fatal("Unexpected exception: " + e.getMessage());
			System.exit(1);
		};
	}
}
