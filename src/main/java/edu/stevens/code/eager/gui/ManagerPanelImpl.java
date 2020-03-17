package edu.stevens.code.eager.gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import edu.stevens.code.eager.Manager;
import edu.stevens.code.eager.ManagerApp;

/**
 * The Class ManagerPanelImpl.
 */
public class ManagerPanelImpl extends ManagerPanel {
	private static final long serialVersionUID = -4935834958965987709L;
	
	private JTextField roundText;
	private JFormattedTextField timeText;
	private TaskPanel[] taskPanels = new TaskPanel[Manager.NUM_TASKS];
	private JTable scoreTable;
	private JToggleButton advanceTime;
	private JButton resetTime;
	private JButton recordScore;
	private JButton prevRound;
	private JButton nextRound;
	
	private static ImageIcon playIcon = new ImageIcon(
			ManagerPanelImpl.class.getResource("/icons/silk/control_play.png"));
	private static ImageIcon pauseIcon = new ImageIcon(
			ManagerPanelImpl.class.getResource("/icons/silk/control_pause.png"));
	private static ImageIcon resetIcon = new ImageIcon(
			ManagerPanelImpl.class.getResource("/icons/silk/control_repeat.png"));
	private static ImageIcon scoreIcon = new ImageIcon(
			ManagerPanelImpl.class.getResource("/icons/silk/wand.png"));
	private static ImageIcon rightArrowIcon = new ImageIcon(
			ManagerPanelImpl.class.getResource("/icons/silk/arrow_right.png"));
	private static ImageIcon leftArrowIcon = new ImageIcon(
			ManagerPanelImpl.class.getResource("/icons/silk/arrow_left.png"));
	
	/**
	 * Instantiates a new manager panel impl.
	 */
	public ManagerPanelImpl() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 1;
		this.add(new JLabel("Round: "), c);
		c.gridx++;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		roundText = new JTextField(20);
		roundText.setEnabled(false);
		this.add(roundText, c);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.weightx = 0;
		c.gridy++;
		this.add(new JLabel("Time: "), c);
		c.gridx++;
		c.fill = GridBagConstraints.HORIZONTAL;
		timeText = new JFormattedTextField(5);
		timeText.setEnabled(false);
		this.add(timeText, c);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		for(int i = 0; i < Manager.NUM_TASKS; i++) {
			TaskPanel panel = new TaskPanel();
			taskPanels[i] = panel;
			this.add(panel, c);
			c.gridy++;
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(new JLabel("Scores"), c);
		c.gridy++;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 2;
		scoreTable = new JTable();
		scoreTable.setFocusable(false);
		scoreTable.setEnabled(false);
		JScrollPane scoreTableScroll = new JScrollPane(scoreTable);
		this.add(scoreTableScroll, c);
		c.gridy++;
		c.weighty = 1;
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		prevRound = new JButton(leftArrowIcon);
		prevRound.setToolTipText("Return to previous round");
		prevRound.setEnabled(false);
		buttonPanel.add(prevRound);
		advanceTime = new JToggleButton(playIcon);
		advanceTime.setToolTipText("Advance time (toggle)");
		advanceTime.setEnabled(false);
		buttonPanel.add(advanceTime);
		resetTime = new JButton(resetIcon);
		resetTime.setToolTipText("Reset time");
		resetTime.setEnabled(false);
		buttonPanel.add(resetTime);
		recordScore = new JButton(scoreIcon);
		recordScore.setToolTipText("Record scores");
		recordScore.setEnabled(false);
		buttonPanel.add(recordScore);
		nextRound = new JButton(rightArrowIcon);
		nextRound.setToolTipText("Advance to next round");
		nextRound.setEnabled(false);
		buttonPanel.add(nextRound);
		this.add(buttonPanel, c);
	}
	
	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.gui.ManagerPanel#observe(edu.stevens.code.ptg.Manager)
	 */
	@Override
	public void observe(Manager manager) {
		this.setBorder(BorderFactory.createTitledBorder(manager.toString()));
		roundText.setText(manager.getRoundName());
		timeText.setValue(manager.getTimeRemaining());
		manager.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				roundText.setText(manager.getRoundName());
				timeText.setValue(manager.getTimeRemaining());

				if((manager.getTimeRemaining() - Manager.STRATEGY_TIME) == 10
				 || manager.getTimeRemaining() == 10) {
					try {
						InputStream audioStream = ManagerPanelImpl.class.getResourceAsStream("/10s_countdown.wav");
						final AudioInputStream inputStream = AudioSystem.getAudioInputStream(audioStream);
						Clip clip = AudioSystem.getClip();
						clip.addLineListener(new LineListener() {
							@Override
							public void update(LineEvent e) {
								if(e.getType() == LineEvent.Type.STOP) {
									clip.close();
								}
							}
						});
						clip.open(inputStream);
						clip.start();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (UnsupportedAudioFileException e) {
						e.printStackTrace();
					} catch (LineUnavailableException e) {
						e.printStackTrace();
					}
				}

				if( ((manager.getTimeRemaining() - Manager.STRATEGY_TIME) % 30) == 0
				  && (manager.getTimeRemaining() - Manager.STRATEGY_TIME) <= 90) {
					try {
						InputStream audioStream = ManagerPanelImpl.class.getResourceAsStream("/metronome.wav");
						final AudioInputStream inputStream = AudioSystem.getAudioInputStream(audioStream);
						Clip clip = AudioSystem.getClip();
						clip.addLineListener(new LineListener() {
							@Override
							public void update(LineEvent e) {
								if(e.getType() == LineEvent.Type.STOP) {
									clip.close();
								}
							}
						});
						clip.open(inputStream);
						clip.start();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (UnsupportedAudioFileException e) {
						e.printStackTrace();
					} catch (LineUnavailableException e) {
						e.printStackTrace();
					}
				}

				if(manager.getTimeRemaining() == 0 && arg == Manager.PROPERTY_TIME) {
					try {
						InputStream audioStream = ManagerPanelImpl.class.getResourceAsStream("/success-1.wav");
						final AudioInputStream inputStream = AudioSystem.getAudioInputStream(audioStream);
						Clip clip = AudioSystem.getClip();
						clip.addLineListener(new LineListener() {
							@Override
							public void update(LineEvent e) {
								if(e.getType() == LineEvent.Type.STOP) {
									clip.close();
								}
							}
						});
						clip.open(inputStream);
						clip.start();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (UnsupportedAudioFileException e) {
						e.printStackTrace();
					} catch (LineUnavailableException e) {
						e.printStackTrace();
					}
				}
			}
		});
		for(int i = 0; i < Manager.NUM_TASKS; i++) {
			taskPanels[i].observe(manager, i);
		}
	}

	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.gui.ManagerPanel#bindTo(edu.stevens.code.ptg.ManagerApp)
	 */
	@Override
	public void bindTo(ManagerApp app) {
		Timer timer = new Timer(1000, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int time = app.getManager().getTimeRemaining();
				if(time > 0) {
					app.getManager().setTimeRemaining(time - 1);
				} else {
					recordScore.doClick();
					advanceTime.doClick();
					nextRound.doClick();
				}
			}
		});
		prevRound.setEnabled(true);
		prevRound.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(advanceTime.isSelected()) {
					advanceTime.doClick();
				}
				app.previousRound();
				scoreTable.getSelectionModel().setSelectionInterval(
						app.getRoundIndex(), app.getRoundIndex());
			}
		});
		nextRound.setEnabled(true);
		nextRound.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(advanceTime.isSelected()) {
					advanceTime.doClick();
				}
				app.nextRound();
				scoreTable.getSelectionModel().setSelectionInterval(
						app.getRoundIndex(), app.getRoundIndex());
			}
		});
		advanceTime.setEnabled(true);
		advanceTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(advanceTime.isSelected() && app.getManager().getTimeRemaining() > 0) {
					advanceTime.setIcon(pauseIcon);
					timer.start();
					prevRound.setEnabled(false);
					nextRound.setEnabled(false);
				} else {
					advanceTime.setIcon(playIcon);
					timer.stop();
					prevRound.setEnabled(true);
					nextRound.setEnabled(true);
				}
			}
		});
		resetTime.setEnabled(true);
		resetTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				app.resetTime();
				app.resetScores();
				((ScoreTableModel)scoreTable.getModel()).fireTableRowsUpdated(
						app.getRoundIndex(), app.getRoundIndex());
				((ScoreTableModel)scoreTable.getModel()).fireTableRowsUpdated(
						app.getSession().getRounds().length, 
						app.getSession().getRounds().length);
				if(advanceTime.isSelected()) {
					timer.restart();
				}
			}
		});
		scoreTable.setModel(new ScoreTableModel(app));
		DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
		renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
		for(int i = 1; i <= app.getDesigners().length; i++) {
			scoreTable.getColumnModel().getColumn(i).setCellRenderer(renderRight);
		}
		scoreTable.getSelectionModel().setSelectionInterval(
				app.getRoundIndex(), app.getRoundIndex());
		recordScore.setEnabled(true);
		recordScore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				app.recordScores();
				((ScoreTableModel)scoreTable.getModel()).fireTableRowsUpdated(
						app.getRoundIndex(), app.getRoundIndex());
				((ScoreTableModel)scoreTable.getModel()).fireTableRowsUpdated(
						app.getSession().getRounds().length, 
						app.getSession().getRounds().length);
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.gui.ManagerPanel#bindTo(edu.stevens.code.ptg.Manager)
	 */
	@Override
	public void bindTo(Manager manager) {
		/* unused
		this.setBorder(BorderFactory.createTitledBorder(manager.toString()));
		roundText.setText(manager.getRoundName());
		roundText.setEnabled(true);
		roundText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				manager.setRoundName(roundText.getText());
			}
		});
		
		timeText.setValue(manager.getTimeRemaining());
		manager.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				timeText.setValue(manager.getTimeRemaining());
			}
		});
		Timer timer = new Timer(1000, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(manager.getTimeRemaining() > 0) {
					manager.setTimeRemaining(manager.getTimeRemaining() - 1);
				} else {
					advanceTime.doClick();
					recordScore.doClick();
				}
			}
		});
		advanceTime.setEnabled(true);
		advanceTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(advanceTime.isSelected()) {
					advanceTime.setIcon(pauseIcon);
					timer.start();
				} else {
					advanceTime.setIcon(playIcon);
					timer.stop();
				}
			}
		});
		for(int i = 0; i < Manager.NUM_TASKS; i++) {
			taskPanels[i].bindTo(manager.getTask(i));
		}
		*/
	}
	
	/**
	 * The Class ScoreTableModel.
	 */
	class ScoreTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 2952712852662467213L;
		
		private ManagerApp app;
		
		/**
		 * Instantiates a new score table model.
		 *
		 * @param app the app
		 */
		public ScoreTableModel(ManagerApp app) {
			this.app = app;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
		 */
		@Override
		public String getColumnName(int columnIndex) {
			if(columnIndex == 0) {
				return "";
			} else {
				return app.getDesigner(columnIndex-1).toString();
			}
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		@Override
		public int getRowCount() {
			return app.getSession().getRounds().length+1;
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		@Override
		public int getColumnCount() {
			return app.getDesigners().length+1;
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(columnIndex == 0) {
				if(rowIndex < app.getSession().getRounds().length) {
					return app.getSession().getRound(rowIndex).getName();
				} else {
					return "<html><b>Total</b></html>";
				}
			}
			if(rowIndex < app.getSession().getRounds().length) {
				return app.getScore(columnIndex-1, rowIndex);
			} else {
				return "<html><b>"+app.getTotalScore(columnIndex-1)+"</b></html>";
			}
		}
	}
}
