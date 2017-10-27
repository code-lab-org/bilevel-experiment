package edu.stevens.code.ptg;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.stevens.code.ptg.gui.DesignerPanel;
import edu.stevens.code.ptg.gui.ManagerPanel;
import edu.stevens.code.ptg.hla.Ambassador;
import hla.rti1516e.exceptions.RTIexception;

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
			
			if(cmd.hasOption("h")) {
				// print the help menu and quit
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("CoDE", options);
				System.exit(0);
			}
			if(cmd.hasOption("d")) {
				int id = Integer.parseInt(cmd.getOptionValue("d"));
				
				Designer d = new Designer();
				d.setId(id);
				
				Ambassador a = new Ambassador();
				a.connectDesigner(federationName, d);
				
				d.addObserver(new Observer() {
					@Override
					public void update(Observable o, Object arg) {
						try {
							a.updateDesigner(d);
						} catch (RTIexception e) {
							logger.error(e);
						}
					}
				});

		        SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						JFrame f = new JFrame();
						f.setContentPane(new DesignerPanel(d));
						f.setTitle(d.toString());
						f.setVisible(true);
				        f.pack();
				        f.setLocationRelativeTo(null);
				        f.addWindowListener(new WindowAdapter() {
							@Override
							public void windowClosing(WindowEvent e) {
								try {
									a.disconnect();
								} catch (RTIexception ex) {
									logger.error(ex);
								}
								System.exit(0);
							}
				        });
					}
		        });
			}
			if(cmd.hasOption("m")) {
				Manager m = new Manager();

		        SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						JFrame f = new JFrame();
						f.setContentPane(new ManagerPanel(m));
						f.setTitle(m.toString());
						f.setVisible(true);
				        f.pack();
				        f.setLocationRelativeTo(null);
				        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					}
		        });
			}
		} catch (ParseException | RTIexception e) {
			logger.error("Unexpected exception: " + e.getMessage());
			System.exit(1);
		};
	}
}
