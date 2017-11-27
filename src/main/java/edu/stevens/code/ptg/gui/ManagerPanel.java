package edu.stevens.code.ptg.gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.Timer;

import edu.stevens.code.ptg.Manager;
import edu.stevens.code.ptg.ManagerApp;

public class ManagerPanel extends JPanel {
	private static final long serialVersionUID = -4935834958965987709L;
	
	private JTextField roundText;
	private JFormattedTextField timeText;
	private TaskPanel[] taskPanels = new TaskPanel[Manager.NUM_TASKS];
	private JToggleButton advanceTime;
	private JButton resetTime;
	private JButton prevRound;
	private JButton nextRound;
	
	private static ImageIcon playIcon = new ImageIcon(
			ManagerPanel.class.getResource("/icons/control_play.png"));
	private static ImageIcon pauseIcon = new ImageIcon(
			ManagerPanel.class.getResource("/icons/control_pause.png"));
	private static ImageIcon resetIcon = new ImageIcon(
			ManagerPanel.class.getResource("/icons/control_repeat.png"));
	private static ImageIcon rightArrowIcon = new ImageIcon(
			ManagerPanel.class.getResource("/icons/arrow_right.png"));
	private static ImageIcon leftArrowIcon = new ImageIcon(
			ManagerPanel.class.getResource("/icons/arrow_left.png"));
	
	public ManagerPanel() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		this.add(new JLabel("Round: "), c);
		c.gridx++;
		c.fill = GridBagConstraints.HORIZONTAL;
		roundText = new JTextField(20);
		roundText.setEnabled(false);
		this.add(roundText, c);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
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
		nextRound = new JButton(rightArrowIcon);
		nextRound.setToolTipText("Advance to next round");
		nextRound.setEnabled(false);
		buttonPanel.add(nextRound);
		this.add(buttonPanel, c);
	}
	
	public void observe(Manager manager) {
		this.setBorder(BorderFactory.createTitledBorder(manager.toString()));
		roundText.setText(manager.getRoundName());
		timeText.setValue(manager.getTimeRemaining());
		manager.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				roundText.setText(manager.getRoundName());
				timeText.setValue(manager.getTimeRemaining());
			}
		});
		for(int i = 0; i < Manager.NUM_TASKS; i++) {
			taskPanels[i].observe(manager.getTask(i));
		}
	}
	
	public void bindTo(ManagerApp app) {
		Timer timer = new Timer(1000, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(app.getManager().getTimeRemaining() > 0) {
					app.getManager().setTimeRemaining(
							app.getManager().getTimeRemaining() - 1);
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
			}
		});
		advanceTime.setEnabled(true);
		advanceTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(advanceTime.isSelected()) {
					if(app.getManager().getTimeRemaining() <= 0) {
						app.getManager().setTimeRemaining(Manager.MAX_TASK_TIME);
					}
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
				app.getManager().setTimeRemaining(Manager.MAX_TASK_TIME);
				if(advanceTime.isSelected()) {
					timer.restart();
				}
			}
		});
	}
	
	public void bindTo(Manager manager) {
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
				}
			}
		});
		advanceTime.setEnabled(true);
		advanceTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(advanceTime.isSelected()) {
					if(manager.getTimeRemaining() <= 0) {
						manager.setTimeRemaining(Manager.MAX_TASK_TIME);
					}
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
	}
}
