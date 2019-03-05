package edu.stevens.code.ptg.gui;

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
import javax.swing.JTabbedPane;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;
import edu.stevens.code.ptg.Manager;

public class DesignerUI2 extends DesignerAppPanel {
	private static final long serialVersionUID = 1163389143406697128L;
	public static final String[] STRATEGY_LABELS = new String[] {
			"Red", 
			"Blue"
	};
	public static final Color[] STRATEGY_COLORS = new Color[] {
			Color.decode("#ffcccc"),
			Color.decode("#ccccff")
	};
	public static final Color[] VALUE_COLORS = new Color[] {

			/** Parula 21 */
//			Color.decode("#352a87"),
//			Color.decode("#353eaf"),
//			Color.decode("#1b55d7"),
//			Color.decode("#026ae1"),
//			Color.decode("#0f77db"),
//			Color.decode("#1484d4"),
//			Color.decode("#0d93d2"),
//			Color.decode("#06a0cd"),
//			Color.decode("#07aac1"),
//			Color.decode("#18b1b2"),
//			Color.decode("#33b8a1"),
//			Color.decode("#55bd8e"),
//			Color.decode("#7abf7c"),
//			Color.decode("#9bbf6f"),
//			Color.decode("#b8bd63"),
//			Color.decode("#d3bb58"),
//			Color.decode("#ecb94c"),
//			Color.decode("#ffc13a"),
//			Color.decode("#fad12b"),
//			Color.decode("#f5e31e"),
//			Color.decode("#f9fb0e")
			
			/** Cividis 21 */
//			Color.decode("#00204D"),
//			Color.decode("#002963"),
//			Color.decode("#00326F"),
//			Color.decode("#1B3B6D"),
//			Color.decode("#31446B"),
//			Color.decode("#414D6B"),
//			Color.decode("#4E566C"),
//			Color.decode("#5B5F6E"),
//			Color.decode("#666970"),
//			Color.decode("#717274"),
//			Color.decode("#7C7B78"),
//			Color.decode("#898579"),
//			Color.decode("#958F78"),
//			Color.decode("#A29976"),
//			Color.decode("#AFA473"),
//			Color.decode("#BDAF6F"),
//			Color.decode("#CBBA69"),
//			Color.decode("#D9C562"),
//			Color.decode("#E7D159"),
//			Color.decode("#F6DD4D"),
//			Color.decode("#FFEA46")
			
			/** Cividis 51 */
			Color.decode("#00204D"),
			Color.decode("#002455"),
			Color.decode("#00285E"),
			Color.decode("#002B67"),
			Color.decode("#002F6F"),
			Color.decode("#00326F"),
			Color.decode("#01356E"),
			Color.decode("#14396D"),
			Color.decode("#203D6D"),
			Color.decode("#29406C"),
			Color.decode("#31446B"),
			Color.decode("#38486B"),
			Color.decode("#3E4B6B"),
			Color.decode("#444F6B"),
			Color.decode("#49536B"),
			Color.decode("#4E566C"),
			Color.decode("#535A6C"),
			Color.decode("#585E6D"),
			Color.decode("#5D616E"),
			Color.decode("#62656F"),
			Color.decode("#666970"),
			Color.decode("#6B6C72"),
			Color.decode("#6F7073"),
			Color.decode("#747475"),
			Color.decode("#787877"),
			Color.decode("#7C7B78"),
			Color.decode("#817F79"),
			Color.decode("#868379"),
			Color.decode("#8B8779"),
			Color.decode("#908B79"),
			Color.decode("#958F78"),
			Color.decode("#9A9377"),
			Color.decode("#A09777"),
			Color.decode("#A59C76"),
			Color.decode("#AAA074"),
			Color.decode("#AFA473"),
			Color.decode("#B5A872"),
			Color.decode("#BAAD70"),
			Color.decode("#BFB16E"),
			Color.decode("#C5B56C"),
			Color.decode("#CBBA69"),
			Color.decode("#D0BE67"),
			Color.decode("#D6C364"),
			Color.decode("#DBC861"),
			Color.decode("#E1CC5D"),
			Color.decode("#E7D159"),
			Color.decode("#EDD655"),
			Color.decode("#F3DB50"),
			Color.decode("#F9E04A"),
			Color.decode("#FFE544"),
			Color.decode("#FFEA46")
	};

	private JTabbedPane tabbedPane;
	private JLabel timeLabelDesign, timeLabelStrategy;
	private InstructionUI instructionUI;
	private DesignUI[] designUIs = new DesignUI[Designer.NUM_STRATEGIES];
	private StrategyUI2 strategyUI;
	private int[] partnerDesigns = new int[Designer.NUM_STRATEGIES];
	private int managerTime;
	
	public DesignerUI2() {
		this.setLayout(new BorderLayout());
		tabbedPane = new JTabbedPane();
		tabbedPane.setFocusable(false);
		this.add(tabbedPane, BorderLayout.CENTER);
		instructionUI = new InstructionUI();
		tabbedPane.addTab("Instructions", instructionUI);
		
		JPanel designPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 0;
		c2.gridy = 0;
		c2.weightx = 1;
		c2.weighty = .8;
		c2.fill = GridBagConstraints.BOTH;
		c2.anchor = GridBagConstraints.CENTER;
		
		designUIs[0] = new DesignUI(0);
		designPanel.add(designUIs[0], c2);
		c2.gridx++;

		c2.weightx = 0;
		c2.fill = GridBagConstraints.HORIZONTAL;
		JPanel timePanel = new JPanel();
		timePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
		timePanel.add(new JLabel("<html><center>Design Time<br />Remaining:</center></html>", JLabel.CENTER));
		timeLabelDesign = new JLabel("0:00", JLabel.CENTER);
		timeLabelDesign.setFont(timeLabelDesign.getFont().deriveFont(32f));
		timePanel.add(timeLabelDesign);
		timePanel.add(Box.createRigidArea(new Dimension(1,50)));
		timePanel.add(new JLabel("<html><center>Decision Time<br />Remaining:</center></html>", JLabel.CENTER));
		timeLabelStrategy = new JLabel("0:00", JLabel.CENTER);
		timeLabelStrategy.setFont(timeLabelStrategy.getFont().deriveFont(32f));
		timePanel.add(timeLabelStrategy);
		designPanel.add(timePanel, c2);
		c2.weightx = 1;
		c2.fill = GridBagConstraints.BOTH;
		c2.gridx++;
		
		designUIs[1] = new DesignUI(1);
		designPanel.add(designUIs[1], c2);
		c2.gridx = 0;
		c2.gridy++;
		c2.gridwidth = 3;
		c2.weighty = .2;
		strategyUI = new StrategyUI2();
		designPanel.add(strategyUI, c2);
		tabbedPane.addTab("Design", designPanel);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		tabbedPane.setEnabled(enabled);
	}
	
	@Override
	public void bindTo(DesignerApp app) {
		app.getController().setReadyToShare(true);
		instructionUI.observe(app.getManager());
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
		strategyUI.bindTo(app);
		setEnabled(app.getManager().isDesignEnabled());
		app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
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
						tabbedPane.setSelectedIndex(0);
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
				setEnabled(app.getManager().isDesignEnabled());
			
				if((app.getManager().getTimeRemaining() - Manager.STRATEGY_TIME) == 10
				 || app.getManager().getTimeRemaining() == 10) {
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

				if( ((app.getManager().getTimeRemaining() - Manager.STRATEGY_TIME) % 30) == 0
				  && (app.getManager().getTimeRemaining() - Manager.STRATEGY_TIME) <= 60) {
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

				if(app.getManager().getTimeRemaining() == 0 && arg == Manager.PROPERTY_TIME) {
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
	}
}
