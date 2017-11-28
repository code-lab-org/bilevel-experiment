package edu.stevens.code.ptg;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

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
	private int roundIndex = -1;
	private int[][] scores = new int[Manager.NUM_DESIGNERS][0];
	
	/**
	 * Instantiates a new manager app.
	 */
	public ManagerApp() {
		for(int i = 0; i < Manager.NUM_DESIGNERS; i++) {
			Designer d = new Designer();
			d.setId(i);
			designers[i] = d;
		}
	}
	
	/**
	 * Instantiates a new manager app.
	 *
	 * @param session the session
	 */
	public ManagerApp(Session session) {
		this();
		this.setSession(session);
	}
	
	/**
	 * Gets the session.
	 *
	 * @return the session
	 */
	public Session getSession() {
		return this.session;
	}
	
	/**
	 * Gets the round.
	 *
	 * @return the round
	 */
	public Round getRound() {
		return this.session.getRound(this.roundIndex);
	}
	
	/**
	 * Gets the round index.
	 *
	 * @return the round index
	 */
	public int getRoundIndex() {
		return this.roundIndex;
	}
	
	/**
	 * Gets the score.
	 *
	 * @param designerId the designer id
	 * @param roundNumber the round number
	 * @return the score
	 */
	public int getScore(int designerId, int roundNumber) {
		if(designerId < 0 || designerId >= Manager.NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid designer id");
		}
		if(roundNumber < 0 || roundNumber >= session.getRounds().length) {
			throw new IllegalArgumentException("invalid round number");
		}
		return this.scores[designerId][roundNumber];
	}
	
	public int getTotalScore(int designerId) {
		if(designerId < 0 || designerId >= Manager.NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid designer id");
		}
		int score = 0;
		for(int i = 0; i < session.getRounds().length; i++) {
			score += this.scores[designerId][i];
		}
		return score;
	}
	
	/**
	 * Sets the session.
	 *
	 * @param session the new session
	 */
	private void setSession(Session session) {
		this.session = session;
		this.roundIndex = 0;
		for(int i = 0; i < Manager.NUM_DESIGNERS; i++) {
			this.scores[i] = new int[session.getRounds().length];
		}
		manager.setRound(session.getRound(this.roundIndex));
	}
	
	public void resetTime() {
		manager.setTimeRemaining(Manager.MAX_TASK_TIME);
	}
	
	public void resetScores() {
		for(int i = 0; i < Manager.NUM_DESIGNERS; i++) {
			this.scores[i][this.roundIndex] = 0;
		}
	}
	
	public void recordScores() {
		for(int i = 0; i < Manager.NUM_DESIGNERS; i++) {
			this.scores[i][this.roundNumber] = 10;
		}
	}
	
	/**
	 * Return to the previous round.
	 */
	public void previousRound() {
		this.roundIndex--;
		if(this.roundIndex >= 0) {
			manager.setRound(session.getRound(this.roundIndex));
		} else {
			this.roundIndex = 0;
		}
	}
	
	/**
	 * Advances to the next round.
	 */
	public void nextRound() {
		this.roundIndex++;
		if(this.roundIndex < session.getRounds().length) {
			manager.setRound(session.getRound(this.roundIndex));
		} else {
			this.roundIndex = session.getRounds().length - 1;
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
				f.setIconImages(ICONS);

				Gson gson = new Gson();
				JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
				fileChooser.setDialogTitle("Open Session");
				fileChooser.setFileFilter(new FileNameExtensionFilter("Session JSON files", "json"));
				
				JMenuBar menuBar = new JMenuBar();
				JMenu fileMenu = new JMenu("File");
				fileMenu.setMnemonic(KeyEvent.VK_F);
				JMenuItem openItem = new JMenuItem("Open");
				openItem.setMnemonic(KeyEvent.VK_O);
				openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
				openItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(fileChooser.showOpenDialog(f) == JFileChooser.APPROVE_OPTION) {
							File file = fileChooser.getSelectedFile();
							try {
								BufferedReader reader = new BufferedReader(new FileReader(file));
								Session session = gson.fromJson(reader, Session.class);
								setSession(session);
							} catch(FileNotFoundException ex) {
								logger.error(e);
							}
						}
					}
				});
				fileMenu.add(openItem);
				menuBar.add(fileMenu);
				f.setJMenuBar(menuBar);
				
				JPanel p = new JPanel();
				p.setLayout(new FlowLayout());
				ManagerPanel mPanel = new ManagerPanel();
				mPanel.observe(manager);
				mPanel.bindTo(self);
				p.add(mPanel);
				JPanel dPanels = new JPanel();
				dPanels.setLayout(new BoxLayout(dPanels, BoxLayout.Y_AXIS));
				for(Designer designer : designers) {
					DesignerPanel dPanel = new DesignerPanel();
					dPanel.observe(designer);
					dPanels.add(dPanel);
				}
				p.add(dPanels);
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
