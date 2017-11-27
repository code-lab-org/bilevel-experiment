package edu.stevens.code.ptg;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    			Option.builder("f")
	    			.longOpt("federation")
    				.hasArg()
    				.argName("name")
	    			.desc("federation name (default: code)")
	    			.build()
    			);

		try {
			CommandLine cmd = parser.parse(options, args);
			
			String federationName = cmd.getOptionValue("f", "code");
			if(cmd.hasOption("d")) {
				for(String value : cmd.getOptionValues("d")) {
					int id = Integer.parseInt(value);
					new DesignerApp(id).init(federationName);
				}
			}
			if(cmd.hasOption("m")) {
				/* AMVRO: Added game file "SH01" */
				new ManagerApp("SH01").init(federationName);
			} else {
			}
			if(!(cmd.hasOption("d") || cmd.hasOption("m"))) {
				// print the help menu and quit
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("CoDE", options);
				System.exit(0);
			}
		} catch (ParseException e) {
			logger.error("Unexpected exception: " + e.getMessage());
			System.exit(1);
		};
	}
}
