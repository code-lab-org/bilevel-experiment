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
package edu.stevens.code.bilevel.gui.designer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.stevens.code.bilevel.app.DesignerApp;
import edu.stevens.code.bilevel.gui.manager.ManageUI;
import edu.stevens.code.bilevel.model.Designer;
import edu.stevens.code.bilevel.model.Manager;

/**
 * A user interface for the designer application.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 * @author Ambrosio Valencia-Romero <avalenci@stevens.edu>
 */
public class DesignerUI extends DesignerAppPanel {
	private static final long serialVersionUID = 1163389143406697128L;
	public static final String[] STRATEGY_LABELS = new String[] {
			"&#10112;", 
			"&#10113;"
	};
	public static final Color[] STRATEGY_COLORS = new Color[] {
			Color.decode("#fdd6c5"),
			Color.decode("#ffece3")
	};
	// cvidis 101
	public static final Color[] VALUE_COLORS = new Color[] {
			Color.decode("#00204d"),
			Color.decode("#002251"),Color.decode("#002455"),Color.decode("#00265a"),Color.decode("#00285e"),
			Color.decode("#002963"),Color.decode("#002b67"),Color.decode("#002d6c"),Color.decode("#002f6f"),
			Color.decode("#00306f"),Color.decode("#00326f"),Color.decode("#00336f"),Color.decode("#01356e"),
			Color.decode("#0b376e"),Color.decode("#14396d"),Color.decode("#1b3b6d"),Color.decode("#203d6d"),
			Color.decode("#253f6c"),Color.decode("#29406c"),Color.decode("#2d426c"),Color.decode("#31446b"),
			Color.decode("#34466b"),Color.decode("#38486b"),Color.decode("#3b496b"),Color.decode("#3e4b6b"),
			Color.decode("#414d6b"),Color.decode("#444f6b"),Color.decode("#46516b"),Color.decode("#49536b"),
			Color.decode("#4c546c"),Color.decode("#4e566c"),Color.decode("#51586c"),Color.decode("#535a6c"),
			Color.decode("#565c6d"),Color.decode("#585e6d"),Color.decode("#5b5f6e"),Color.decode("#5d616e"),
			Color.decode("#5f636f"),Color.decode("#62656f"),Color.decode("#646770"),Color.decode("#666970"),
			Color.decode("#696a71"),Color.decode("#6b6c72"),Color.decode("#6d6e72"),Color.decode("#6f7073"),
			Color.decode("#717274"),Color.decode("#747475"),Color.decode("#767676"),Color.decode("#787877"),
			Color.decode("#7a7a78"),Color.decode("#7c7b78"),Color.decode("#7f7d78"),Color.decode("#817f79"),
			Color.decode("#848179"),Color.decode("#868379"),Color.decode("#898579"),Color.decode("#8b8779"),
			Color.decode("#8e8979"),Color.decode("#908b79"),Color.decode("#938d78"),Color.decode("#958f78"),
			Color.decode("#989178"),Color.decode("#9a9377"),Color.decode("#9d9577"),Color.decode("#a09777"),
			Color.decode("#a29976"),Color.decode("#a59c76"),Color.decode("#a79e75"),Color.decode("#aaa074"),
			Color.decode("#ada274"),Color.decode("#afa473"),Color.decode("#b2a672"),Color.decode("#b5a872"),
			Color.decode("#b7aa71"),Color.decode("#baad70"),Color.decode("#bdaf6f"),Color.decode("#bfb16e"),
			Color.decode("#c2b36d"),Color.decode("#c5b56c"),Color.decode("#c8b86b"),Color.decode("#cbba69"),
			Color.decode("#cdbc68"),Color.decode("#d0be67"),Color.decode("#d3c165"),Color.decode("#d6c364"),
			Color.decode("#d9c562"),Color.decode("#dbc861"),Color.decode("#deca5f"),Color.decode("#e1cc5d"),
			Color.decode("#e4cf5b"),Color.decode("#e7d159"),Color.decode("#ead457"),Color.decode("#edd655"),
			Color.decode("#f0d852"),Color.decode("#f3db50"),Color.decode("#f6dd4d"),Color.decode("#f9e04a"),
			Color.decode("#fce247"),Color.decode("#ffe544"),Color.decode("#ffe743"),Color.decode("#ffea46")
	};

	private JLabel timeLabelDesign, timeLabelStrategy, taskLabel;
	private DesignUI[] designUIs = new DesignUI[Designer.NUM_STRATEGIES];
	private int[] partnerDesigns = new int[Designer.NUM_STRATEGIES];
	private int managerTime;
	
	public DesignerUI() {
		this.setLayout(new BorderLayout());
		JPanel designPanel = new JPanel(new GridBagLayout());
		designPanel.setBackground(Color.WHITE);
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 0;
		c2.gridy = 0;
		c2.weightx = 1;
		c2.weighty = .8;
		c2.fill = GridBagConstraints.BOTH;
		c2.anchor = GridBagConstraints.CENTER;
		// add a design UI for strategy 0
		designUIs[0] = new DesignUI(0);
		designPanel.add(designUIs[0], c2);
		c2.gridx++;
		c2.weightx = 0;
		c2.fill = GridBagConstraints.HORIZONTAL;
		// add a panel to display time information
		JPanel timePanel = new JPanel();
		timePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		timePanel.setBackground(Color.WHITE);
		timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
		timePanel.add(new JLabel("<html><center>Task:</center></html>", JLabel.CENTER));
		taskLabel = new JLabel("<html><center>k/K</center></html>", JLabel.CENTER);
		taskLabel.setFont(taskLabel.getFont().deriveFont(32f));
		timePanel.add(taskLabel);
		timePanel.add(Box.createRigidArea(new Dimension(1,100)));
		timePanel.add(new JLabel("<html><center>Design Time<br />Remaining:</center></html>", JLabel.CENTER));
		timeLabelDesign = new JLabel(String.format(" %01d:%02d", 0, 0), JLabel.CENTER);
		timeLabelDesign.setFont(timeLabelDesign.getFont().deriveFont(32f));
		timePanel.add(timeLabelDesign);
		timePanel.add(Box.createRigidArea(new Dimension(1,50)));
		timePanel.add(new JLabel("<html><center>Decision Time<br />Remaining:</center></html>", JLabel.CENTER));
		timeLabelStrategy = new JLabel(String.format(" %01d:%02d", 0, 0), JLabel.CENTER);
		timeLabelStrategy.setFont(timeLabelStrategy.getFont().deriveFont(32f));
		timePanel.add(timeLabelStrategy);
		designPanel.add(timePanel, c2);
		c2.weightx = 1;
		c2.fill = GridBagConstraints.BOTH;
		c2.gridx++;
		// add a design UI for strategy 1
		designUIs[1] = new DesignUI(1);
		designPanel.add(designUIs[1], c2);
		this.add(designPanel, BorderLayout.CENTER);
	}
	
	@Override
	public void bindTo(DesignerApp app) {
		app.getController().setReadyToShare(true);
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			designUIs[i].bindTo(app);
		}
		for(Designer designer : app.getDesigners()) {
			if(designer != app.getController()) {
				designer.addObserver(new Observer() {
					@Override
					public void update(Observable o, Object arg) {
						for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
							if(designer == app.getDesignPartner() 
									&& designer.isReadyToShare()) {
								if(partnerDesigns[i] != designer.getDesign(i)) {
									partnerDesigns[i] = designer.getDesign(i);
								}
							}
						}
					}
				});
			}
		}
		app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				String task = app.getManager().getRoundName();
				if(task.length() > 5) {
					taskLabel.setText("<html><center>" + task.substring(task.length()-5, task.length()) + "</center></html>");
				} else {
					taskLabel.setText("<html><center>" + task + "</center></html>");
				}
				if(managerTime != app.getManager().getTimeRemaining()) {
					managerTime = app.getManager().getTimeRemaining();
					if(managerTime < Manager.STRATEGY_TIME) {
						timeLabelDesign.setText(String.format(" %01d:%02d", 0, 0));
						timeLabelStrategy.setText(String.format(" %01d:%02d", managerTime/60, managerTime % 60));
					} else {
						timeLabelDesign.setText(String.format(" %01d:%02d", (managerTime - Manager.STRATEGY_TIME)/60, (managerTime - Manager.STRATEGY_TIME) % 60));
						timeLabelStrategy.setText(String.format(" %01d:%02d", Manager.STRATEGY_TIME/60, Manager.STRATEGY_TIME % 60));
					}
					if(managerTime == Manager.MAX_TASK_TIME) {
						app.getController().setStrategy(0);
					}
					if((managerTime < Manager.MAX_TASK_TIME && (managerTime - Manager.STRATEGY_TIME) % 60 == 0)
							|| ((managerTime - Manager.STRATEGY_TIME) <= 60 && (managerTime - Manager.STRATEGY_TIME) % 15 == 0) 
							|| ((managerTime - Manager.STRATEGY_TIME) <= 15) ) {
						timeLabelDesign.setForeground(Color.RED);
						timeLabelStrategy.setForeground(Color.RED);
					} else {
						timeLabelDesign.setForeground(Color.BLACK);
						timeLabelStrategy.setForeground(Color.BLACK);
					}
				}
				// play the countdown timer when 10 second remain
				if((app.getManager().getTimeRemaining() - Manager.STRATEGY_TIME) == 10
						|| app.getManager().getTimeRemaining() == 10) {
					try {
						InputStream audioStream = ManageUI.class.getResourceAsStream("/10s_countdown.wav");
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
				// play the metronome sound every 30 seconds when under 60 seconds remaining
				if(((app.getManager().getTimeRemaining() - Manager.STRATEGY_TIME) % 30) == 0 
						&& app.getManager().getTimeRemaining() - Manager.STRATEGY_TIME <= 60) {
					try {
						InputStream audioStream = ManageUI.class.getResourceAsStream("/metronome.wav");
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
				// play the success sound when the task ends
				if(app.getManager().getTimeRemaining() == 0 && arg == Manager.PROPERTY_TIME) {
					try {
						InputStream audioStream = ManageUI.class.getResourceAsStream("/success-1.wav");
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
	}
}
