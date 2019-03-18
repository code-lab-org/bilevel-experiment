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
			
			/** Viridis 101 */
//			Color.decode("#440154"),
//			Color.decode("#450458"),
//			Color.decode("#46085c"),
//			Color.decode("#470c5f"),
//			Color.decode("#471063"),
//			Color.decode("#481466"),
//			Color.decode("#48176a"),
//			Color.decode("#481b6d"),
//			Color.decode("#481e70"),
//			Color.decode("#482173"),
//			Color.decode("#482575"),
//			Color.decode("#482878"),
//			Color.decode("#472b7a"),
//			Color.decode("#472e7c"),
//			Color.decode("#46317e"),
//			Color.decode("#463480"),
//			Color.decode("#453882"),
//			Color.decode("#443b83"),
//			Color.decode("#433e85"),
//			Color.decode("#424186"),
//			Color.decode("#414487"),
//			Color.decode("#404788"),
//			Color.decode("#3e4989"),
//			Color.decode("#3d4c8a"),
//			Color.decode("#3c4f8b"),
//			Color.decode("#3b528b"),
//			Color.decode("#39558c"),
//			Color.decode("#38578c"),
//			Color.decode("#375a8c"),
//			Color.decode("#365d8d"),
//			Color.decode("#345f8d"),
//			Color.decode("#33628d"),
//			Color.decode("#32648e"),
//			Color.decode("#31678e"),
//			Color.decode("#30698e"),
//			Color.decode("#2f6c8e"),
//			Color.decode("#2e6e8e"),
//			Color.decode("#2d718e"),
//			Color.decode("#2c738e"),
//			Color.decode("#2b768e"),
//			Color.decode("#2a788e"),
//			Color.decode("#297b8e"),
//			Color.decode("#287d8e"),
//			Color.decode("#277f8e"),
//			Color.decode("#26828e"),
//			Color.decode("#25848e"),
//			Color.decode("#24878e"),
//			Color.decode("#23898e"),
//			Color.decode("#228b8d"),
//			Color.decode("#218e8d"),
//			Color.decode("#21908c"),
//			Color.decode("#20928c"),
//			Color.decode("#1f958b"),
//			Color.decode("#1f978b"),
//			Color.decode("#1f9a8a"),
//			Color.decode("#1e9c89"),
//			Color.decode("#1f9e88"),
//			Color.decode("#1fa188"),
//			Color.decode("#20a386"),
//			Color.decode("#21a685"),
//			Color.decode("#22a884"),
//			Color.decode("#24aa83"),
//			Color.decode("#26ad81"),
//			Color.decode("#29af80"),
//			Color.decode("#2cb17e"),
//			Color.decode("#2fb47c"),
//			Color.decode("#33b67a"),
//			Color.decode("#36b878"),
//			Color.decode("#3aba76"),
//			Color.decode("#3fbc73"),
//			Color.decode("#43bf71"),
//			Color.decode("#48c16e"),
//			Color.decode("#4dc36c"),
//			Color.decode("#52c569"),
//			Color.decode("#57c766"),
//			Color.decode("#5dc963"),
//			Color.decode("#62ca5f"),
//			Color.decode("#68cc5c"),
//			Color.decode("#6ece58"),
//			Color.decode("#74d055"),
//			Color.decode("#7ad151"),
//			Color.decode("#80d34d"),
//			Color.decode("#86d549"),
//			Color.decode("#8dd645"),
//			Color.decode("#93d741"),
//			Color.decode("#9ad93d"),
//			Color.decode("#a1da38"),
//			Color.decode("#a7db34"),
//			Color.decode("#aedc30"),
//			Color.decode("#b5de2b"),
//			Color.decode("#bcdf27"),
//			Color.decode("#c3e023"),
//			Color.decode("#c9e01f"),
//			Color.decode("#d0e11c"),
//			Color.decode("#d7e21a"),
//			Color.decode("#dde318"),
//			Color.decode("#e4e419"),
//			Color.decode("#ebe51a"),
//			Color.decode("#f1e51d"),
//			Color.decode("#f7e620"),
//			Color.decode("#fde725")
			
			/** Cividis 101 */
			Color.decode("#00204d"),
			Color.decode("#002251"),
			Color.decode("#002455"),
			Color.decode("#00265a"),
			Color.decode("#00285e"),
			Color.decode("#002963"),
			Color.decode("#002b67"),
			Color.decode("#002d6c"),
			Color.decode("#002f6f"),
			Color.decode("#00306f"),
			Color.decode("#00326f"),
			Color.decode("#00336f"),
			Color.decode("#01356e"),
			Color.decode("#0b376e"),
			Color.decode("#14396d"),
			Color.decode("#1b3b6d"),
			Color.decode("#203d6d"),
			Color.decode("#253f6c"),
			Color.decode("#29406c"),
			Color.decode("#2d426c"),
			Color.decode("#31446b"),
			Color.decode("#34466b"),
			Color.decode("#38486b"),
			Color.decode("#3b496b"),
			Color.decode("#3e4b6b"),
			Color.decode("#414d6b"),
			Color.decode("#444f6b"),
			Color.decode("#46516b"),
			Color.decode("#49536b"),
			Color.decode("#4c546c"),
			Color.decode("#4e566c"),
			Color.decode("#51586c"),
			Color.decode("#535a6c"),
			Color.decode("#565c6d"),
			Color.decode("#585e6d"),
			Color.decode("#5b5f6e"),
			Color.decode("#5d616e"),
			Color.decode("#5f636f"),
			Color.decode("#62656f"),
			Color.decode("#646770"),
			Color.decode("#666970"),
			Color.decode("#696a71"),
			Color.decode("#6b6c72"),
			Color.decode("#6d6e72"),
			Color.decode("#6f7073"),
			Color.decode("#717274"),
			Color.decode("#747475"),
			Color.decode("#767676"),
			Color.decode("#787877"),
			Color.decode("#7a7a78"),
			Color.decode("#7c7b78"),
			Color.decode("#7f7d78"),
			Color.decode("#817f79"),
			Color.decode("#848179"),
			Color.decode("#868379"),
			Color.decode("#898579"),
			Color.decode("#8b8779"),
			Color.decode("#8e8979"),
			Color.decode("#908b79"),
			Color.decode("#938d78"),
			Color.decode("#958f78"),
			Color.decode("#989178"),
			Color.decode("#9a9377"),
			Color.decode("#9d9577"),
			Color.decode("#a09777"),
			Color.decode("#a29976"),
			Color.decode("#a59c76"),
			Color.decode("#a79e75"),
			Color.decode("#aaa074"),
			Color.decode("#ada274"),
			Color.decode("#afa473"),
			Color.decode("#b2a672"),
			Color.decode("#b5a872"),
			Color.decode("#b7aa71"),
			Color.decode("#baad70"),
			Color.decode("#bdaf6f"),
			Color.decode("#bfb16e"),
			Color.decode("#c2b36d"),
			Color.decode("#c5b56c"),
			Color.decode("#c8b86b"),
			Color.decode("#cbba69"),
			Color.decode("#cdbc68"),
			Color.decode("#d0be67"),
			Color.decode("#d3c165"),
			Color.decode("#d6c364"),
			Color.decode("#d9c562"),
			Color.decode("#dbc861"),
			Color.decode("#deca5f"),
			Color.decode("#e1cc5d"),
			Color.decode("#e4cf5b"),
			Color.decode("#e7d159"),
			Color.decode("#ead457"),
			Color.decode("#edd655"),
			Color.decode("#f0d852"),
			Color.decode("#f3db50"),
			Color.decode("#f6dd4d"),
			Color.decode("#f9e04a"),
			Color.decode("#fce247"),
			Color.decode("#ffe544"),
			Color.decode("#ffe743"),
			Color.decode("#ffea46")
	};

	private JTabbedPane tabbedPane;
	private JLabel timeLabelDesign, timeLabelStrategy, taskLabel;
//	private InstructionUI instructionUI;
	private DesignUI[] designUIs = new DesignUI[Designer.NUM_STRATEGIES];
	private StrategyUI2 strategyUI;
	private int[] partnerDesigns = new int[Designer.NUM_STRATEGIES];
	private int managerTime;
	
	public DesignerUI2() {
		this.setLayout(new BorderLayout());
		tabbedPane = new JTabbedPane();
		tabbedPane.setFocusable(false);
		this.add(tabbedPane, BorderLayout.CENTER);
//		instructionUI = new InstructionUI();
//		tabbedPane.addTab("Instructions", instructionUI);
		
		JPanel designPanel = new JPanel(new GridBagLayout());
		designPanel.setBackground(Color.WHITE);
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
//		instructionUI.observe(app.getManager());
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
				String task = app.getManager().getRoundName();
				taskLabel.setText("<html><center>"+
			                       task.substring(task.length()-5, task.length())+
			                       "</center></html>");
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
