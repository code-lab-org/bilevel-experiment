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
    				.hasArg()
    				.argName("index")
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
    			Option.builder("t")
	    			.longOpt("test")
    				.hasArg()
    				.argName("experiment")
	    			.desc("launch a test interface with an experiment")
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
				int id = Integer.parseInt(cmd.getOptionValue("d"));
				
				new DesignerApp(id).init(federationName);
			} else if(cmd.hasOption("m")) {
				new ManagerApp().init(federationName);
			} else if(cmd.hasOption("t")) {
				new ManagerApp().init(federationName);
				new DesignerApp(0).init(federationName);
				new DesignerApp(1).init(federationName);
				new DesignerApp(2).init(federationName);
				new DesignerApp(3).init(federationName);
			} else {
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
