package edu.stevens.code.ptg;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.stevens.code.ptg.gui.DesignerPanel;
import edu.stevens.code.ptg.gui.ManagerPanel;
import edu.stevens.code.ptg.hla.Ambassador;
import hla.rti1516e.exceptions.RTIexception;

/**
 * The Class ManagerApp.
 */
public class ManagerApp implements App {
    private static final Logger logger = LogManager.getLogger(ManagerApp.class);
    
	private final Designer[] designers = new Designer[Manager.NUM_DESIGNERS];
	private Manager manager = new Manager();
	private Ambassador ambassador = null;
	private Session session = null;
	private int roundNumber = -1;
	
	/**
	 * Instantiates a new manager app.
	 *
	 * @param session the session
	 */
	public ManagerApp(Session session) {
		this.session = session;
		this.roundNumber = 0;
		manager.setRound(session.getRound(this.roundNumber));
		for(int i = 0; i < Manager.NUM_DESIGNERS; i++) {
			Designer d = new Designer();
			d.setId(i);
			designers[i] = d;
		}
	}
	
	/**
	 * Return to the previous round.
	 */
	public void previousRound() {
		this.roundNumber--;
		if(this.roundNumber >= 0) {
			manager.setRound(session.getRound(this.roundNumber));
		} else {
			this.roundNumber = 0;
		}
	}
	
	/**
	 * Advances to the next round.
	 */
	public void nextRound() {
		this.roundNumber++;
		if(this.roundNumber < session.getRounds().length) {
			manager.setRound(session.getRound(this.roundNumber));
		} else {
			this.roundNumber = session.getRounds().length;
			manager.setTimeRemaining(-1);
			manager.setRoundName("Complete");
		}
	}

	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.App#init(java.lang.String)
	 */
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
			ambassador.connectManager(this, federationName);
		} catch (RTIexception ex) {
			logger.error(ex);
		}
		
		manager.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				try {
					ambassador.updateManager(manager);
				} catch (RTIexception e) {
					logger.error(e);
				}
			}
		});
		
		ManagerApp self = this;

        SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame f = new JFrame();
				
				f.setIconImages(DesignerApp.ICONS); /* ADDED BY AMVRO */
				
				JPanel p = new JPanel();
				p.setLayout(new BorderLayout());
				ManagerPanel mPanel = new ManagerPanel();
				mPanel.observe(manager);
				mPanel.bindTo(self);
				p.add(mPanel, BorderLayout.WEST);
				JPanel dPanels = new JPanel();
				dPanels.setLayout(new BoxLayout(dPanels, BoxLayout.Y_AXIS));
				for(Designer designer : designers) {
					DesignerPanel dPanel = new DesignerPanel();
					dPanel.observe(designer);
					dPanels.add(dPanel);
				}
				p.add(dPanels, BorderLayout.EAST);
				f.setContentPane(p);
				f.setTitle(manager.toString());
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

	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.App#kill()
	 */
	@Override
	public void kill() {
		try {
			ambassador.disconnect();
		} catch (RTIexception ex) {
			logger.error(ex);
		}
		System.exit(0);
	}

	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.App#getSelf()
	 */
	@Override
	public Manager getSelf() {
		return manager;
	}

	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.App#getDesigner(int)
	 */
	@Override
	public Designer getDesigner(int index) {
		if(index < 0 || index >= Manager.NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid designer index");
		}
		return designers[index];
	}

	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.App#getDesigners()
	 */
	@Override
	public Designer[] getDesigners() {
		return Arrays.copyOf(designers, Manager.NUM_DESIGNERS);
	}

	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.App#getManager()
	 */
	@Override
	public Manager getManager() {
		return manager;
	}
}
