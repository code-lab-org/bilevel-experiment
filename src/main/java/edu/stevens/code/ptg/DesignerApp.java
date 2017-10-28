package edu.stevens.code.ptg;

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.stevens.code.ptg.gui.DesignerPanel;
import edu.stevens.code.ptg.gui.ManagerPanel;
import edu.stevens.code.ptg.hla.Ambassador;
import hla.rti1516e.exceptions.RTIexception;

public class DesignerApp implements App {
    private static final Logger logger = LogManager.getLogger(DesignerApp.class);
	
	private final Designer[] designers = new Designer[Manager.NUM_DESIGNERS];
	private Designer self = null;
	private Manager manager = new Manager();
	private Ambassador ambassador = null;
	
	public DesignerApp(int id) {
		if(id < 0 || id >= Manager.NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid designer id");
		}
		for(int i = 0; i < Manager.NUM_DESIGNERS; i++) {
			Designer d = new Designer();
			d.setId(i);
			designers[i] = d;
		}
		self = designers[id];
	}

	@Override
	public void init(String federationName) {
		if(ambassador == null) {
			try {
				ambassador = new Ambassador();
			} catch (RTIexception ex) {
				logger.error(ex);
			}
		}
		
		try {
			ambassador.connectDesigner(this, federationName);
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
				JPanel p = new JPanel();
				p.setLayout(new FlowLayout());
				for(Designer designer : designers) {
					DesignerPanel dPanel = new DesignerPanel();
					if(!designer.equals(self)) {
						dPanel.observe(designer);
					} else {
						dPanel.bindTo(self);
					}
					p.add(dPanel);
				}
				ManagerPanel mPanel = new ManagerPanel();
				mPanel.observe(getManager());
				p.add(mPanel);
				f.setContentPane(p);
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

	@Override
	public void kill() {
		try {
			ambassador.disconnect();
		} catch (RTIexception ex) {
			logger.error(ex);
		}
		System.exit(0);
	}

	@Override
	public Designer getSelf() {
		return self;
	}

	@Override
	public Designer getDesigner(int index) {
		if(index < 0 || index >= Manager.NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid designer index");
		}
		return designers[index];
	}

	@Override
	public Designer[] getDesigners() {
		return Arrays.copyOf(designers, Manager.NUM_DESIGNERS);
	}

	@Override
	public Manager getManager() {
		return manager;
	}
}
