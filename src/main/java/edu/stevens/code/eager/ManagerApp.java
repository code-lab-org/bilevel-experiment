package edu.stevens.code.eager;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.stevens.code.eager.gui.ManagerAppMenuBar;
import edu.stevens.code.eager.gui.ManagerAppPanel;
import edu.stevens.code.eager.gui.ManagerUI;
import edu.stevens.code.eager.io.Ambassador;
import edu.stevens.code.eager.io.ZmqAmbassador;

/**
 * The Class ManagerApp.
 */
public class ManagerApp implements App {
	private final Designer[] designers = new Designer[Manager.NUM_DESIGNERS];
	private Manager manager = new Manager();
	private Ambassador ambassador = null;
	private Session session = null;
	private int roundIndex = -1;
	private int[][] scores = new int[Manager.NUM_DESIGNERS][0];
	private final ExperimentLogger expLogger;
	/**
	 * Instantiates a new manager app.
	 */
	public ManagerApp() {
		expLogger = new ExperimentLogger();
		manager.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if(Manager.PROPERTY_ROUND.equals(arg)) {
					expLogger.logRoundChange(manager);
				} else if(Manager.PROPERTY_TASKS.equals(arg)) {
					expLogger.logTaskChange(manager);
				} else if(Manager.PROPERTY_TIME.equals(arg) 
						&& manager.getTimeRemaining() == Manager.MAX_TASK_TIME - 1) {
					expLogger.logTaskStart(manager, getScores());
				} else if(Manager.PROPERTY_TIME.equals(arg) 
						&& manager.getTimeRemaining() == 0) {
					expLogger.logTaskEnd(manager, getScores());
				}
			}
		});
		for(int i = 0; i < Manager.NUM_DESIGNERS; i++) {
			Designer designer = new Designer();
			designer.setId(i);
			designers[i] = designer;
			designer.addObserver(new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					if(Designer.PROPERTY_DESIGNS.equals(arg)) {
						expLogger.logDesignerDesignChange(designer);
					} else if(Designer.PROPERTY_STRATEGY.equals(arg)) {
						expLogger.logDesignerStrategyChange(designer);
					}
				}
			});
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
	
	/**
	 * Gets the total score.
	 *
	 * @param designerId the designer id
	 * @return the total score
	 */
	public int getTotalScore(int designerId) {
		if(designerId < 0 || designerId >= Manager.NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid designer id");
		}
		int score = 0;
		for(int i = 0; i < session.getRounds().length; i++) {
			// do not include training tasks in total score
			if(!session.getRound(i).getName().contains("T")) { // .getName().toLowerCase().contains("training"))
				score += this.scores[designerId][i];
			}
		}
		return score;
	}
	
	/**
	 * Sets the session.
	 *
	 * @param session the new session
	 */
	public void setSession(Session session) {
		this.session = session;
		this.roundIndex = 0;
		for(int i = 0; i < Manager.NUM_DESIGNERS; i++) {
			this.scores[i] = new int[session.getRounds().length];
		}
		manager.setRound(session.getRound(this.roundIndex));
	}
	
	/**
	 * Reset time.
	 */
	public void resetTime() {
		manager.setTimeRemaining(Manager.MAX_TASK_TIME);
	}
	
	/**
	 * Reset scores.
	 */
	public void resetScores() {
		for(int i = 0; i < Manager.NUM_DESIGNERS; i++) {
			this.scores[i][this.roundIndex] = 0;
		}
	}
	
	/**
	 * Gets the scores.
	 *
	 * @return the scores
	 */
	private int[] getScores() {
		int[] scores = new int[Manager.NUM_DESIGNERS];
		for(int i = 0; i < Manager.NUM_DESIGNERS; i++) {
			int j = manager.getDesignPartner(i);
			Task task = manager.getTaskByDesignerId(i);
			scores[i] = task.getValue(i, designers[i].getStrategy(), designers[i].getDesigns(), designers[j].getStrategy(), designers[j].getDesigns());
		}
		return scores;
	}
	
	/**
	 * Record scores.
	 */
	public void recordScores() {
		int[] scores = getScores();
		for(int i = 0; i < Manager.NUM_DESIGNERS; i++) {
			this.scores[i][this.roundIndex] = scores[i];
		}
		manager.setTimeRemaining(0);
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
			ambassador = new ZmqAmbassador();
		}

		ambassador.connectManager(this, federationName);
		
		manager.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				ambassador.updateManager(manager, arg);
			}
		});
		
		ManagerApp self = this;

        SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame f = new JFrame();
				f.setIconImages(ICONS);
				f.setJMenuBar(new ManagerAppMenuBar(self));
				ManagerAppPanel panel = new ManagerUI();
				panel.bindTo(self);
				f.setContentPane(panel);
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
		ambassador.disconnect();
		System.exit(0);
	}

	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.App#getSelf()
	 */
	@Override
	public Manager getController() {
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
