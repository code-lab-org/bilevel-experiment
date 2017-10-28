package edu.stevens.code.ptg;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.stevens.code.ptg.gui.DesignerPanel;
import edu.stevens.code.ptg.hla.Ambassador;
import hla.rti1516e.exceptions.RTIexception;

public class DesignerApp {
    private static final Logger logger = LogManager.getLogger(DesignerApp.class);
	
	private final Designer[] designers = new Designer[Manager.NUM_DESIGNERS];
	private Manager manager = null;
	private Designer self = null;
	private Ambassador ambassador = null;
	
	public void init(int id, String federationName) {
		if(id >= Manager.NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid designer id");
		}
		
		self = new Designer();
		self.setId(id);
		designers[id] = self;

		if(ambassador == null) {
			try {
				ambassador = new Ambassador();
			} catch (RTIexception ex) {
				logger.error(ex);
			}
		}
		
		try {
			ambassador.connectDesigner(self, federationName);
		} catch (RTIexception ex) {
			logger.error(ex);
		}
		
		self.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				try {
					ambassador.updateDesigner(self);
				} catch (RTIexception e) {
					logger.error(e);
				}
			}
		});

        SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame f = new JFrame();
				DesignerPanel designer = new DesignerPanel();
				designer.bindTo(self);
				f.setContentPane(designer);
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
