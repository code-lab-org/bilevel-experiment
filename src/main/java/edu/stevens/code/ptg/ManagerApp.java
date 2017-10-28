package edu.stevens.code.ptg;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.stevens.code.ptg.gui.ManagerPanel;
import edu.stevens.code.ptg.hla.Ambassador;
import hla.rti1516e.exceptions.RTIexception;

public class ManagerApp {
    private static final Logger logger = LogManager.getLogger(ManagerApp.class);
	
	private final Designer[] designers = new Designer[Manager.NUM_DESIGNERS];
	private Manager self = null;
	private Ambassador ambassador = null;
	
	public void init(String federationName) {
		self = new Manager();

		if(ambassador == null) {
			try {
				ambassador = new Ambassador();
			} catch (RTIexception ex) {
				logger.error(ex);
			}
		}
		
		try {
			ambassador.connectManager(self, federationName);
		} catch (RTIexception ex) {
			logger.error(ex);
		}
		
		self.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				try {
					ambassador.updateManager(self);
				} catch (RTIexception e) {
					logger.error(e);
				}
			}
		});

        SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame f = new JFrame();
				f.setContentPane(new ManagerPanel(self));
				f.setTitle(self.toString());
				f.setVisible(true);
		        f.pack();
		        f.setLocationRelativeTo(null);
		        f.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						kill();
					}
		        });
			}
        });
	}
	
	public void kill() {
		try {
			ambassador.disconnect();
		} catch (RTIexception ex) {
			logger.error(ex);
		}
		System.exit(0);
	}
}
