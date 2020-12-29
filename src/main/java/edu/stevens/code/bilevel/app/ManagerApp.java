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
package edu.stevens.code.bilevel.app;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.stevens.code.bilevel.gui.manager.ManagerAppMenuBar;
import edu.stevens.code.bilevel.gui.manager.ManagerAppPanel;
import edu.stevens.code.bilevel.gui.manager.ManagerUI;
import edu.stevens.code.bilevel.io.Ambassador;
import edu.stevens.code.bilevel.io.ExperimentLogger;
import edu.stevens.code.bilevel.io.ZmqAmbassador;
import edu.stevens.code.bilevel.model.Designer;
import edu.stevens.code.bilevel.model.Manager;
import edu.stevens.code.bilevel.model.Round;
import edu.stevens.code.bilevel.model.Session;
import edu.stevens.code.bilevel.model.Task;

/**
 * The software application used by managers.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 * @author Ambrosio Valencia-Romero <avalenci@stevens.edu>
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
	 * Instantiates a new manager application.
	 */
	public ManagerApp() {
		// initialize a logger
		expLogger = new ExperimentLogger();
		// add an observer to the manager to trigger log messages
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
		// initialize the designer objects
		for(int i = 0; i < Manager.NUM_DESIGNERS; i++) {
			Designer designer = new Designer();
			designer.setId(i);
			designers[i] = designer;
			// add an observer to the designer trigger log messages
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
	 * Instantiates a new manager application.
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
			// training tasks are identified by the character 'T' in the round name
			if(!session.getRound(i).getName().contains("T")) {
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

	@Override
	public void init(String hostname) {
		// initialize the ambassador
		if(ambassador == null) {
			ambassador = new ZmqAmbassador();
		}
		// connect the ambassador
		ambassador.connectManager(this, hostname);
		// add an observer to the manager to process ambassador updates
		manager.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				ambassador.updateManager(manager, arg);
			}
		});
		// launch the graphical user interface
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

	@Override
	public void kill() {
		ambassador.disconnect();
		System.exit(0);
	}

	@Override
	public Manager getController() {
		return manager;
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
