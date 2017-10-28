package edu.stevens.code.ptg.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.Timer;

import edu.stevens.code.ptg.Manager;

public class ManagerPanel extends JPanel {
	private static final long serialVersionUID = -4935834958965987709L;
	
	private JTextField roundText;
	private JFormattedTextField timeText;
	private JToggleButton advanceTime;
	private TaskPanel[] taskPanels = new TaskPanel[Manager.NUM_TASKS];
	
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
		c.gridx++;
		advanceTime = new JToggleButton("Advance");
		advanceTime.setEnabled(false);
		this.add(advanceTime, c);
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
					timer.start();
				} else {
					timer.stop();
				}
			}
		});
		for(int i = 0; i < Manager.NUM_TASKS; i++) {
			taskPanels[i].bindTo(manager.getTask(i));
		}
	}
}
