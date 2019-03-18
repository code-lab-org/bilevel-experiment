package edu.stevens.code.ptg.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Timer;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;
import edu.stevens.code.ptg.Manager;
import edu.stevens.code.ptg.Task;

public class DesignerUI extends DesignerAppPanel {
	private static final long serialVersionUID = 1163389143406697128L;
	public static final String[] STRATEGY_LABELS = new String[] {
			"the one on the left.", 
			"the one on the right."
	};
	public static final Color[] STRATEGY_COLORS = new Color[] {
			Color.decode("#fdd6c5"),
			Color.decode("#ffece3")
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
	public static final Color[] VALUE_COLORS_21 = new Color[] {
			
			/** Viridis 21 */
//			Color.decode("#440154"),
//			Color.decode("#481466"),
//			Color.decode("#482575"),
//			Color.decode("#463480"),
//			Color.decode("#414487"),
//			Color.decode("#3b528b"),
//			Color.decode("#345f8d"),
//			Color.decode("#2f6c8e"),
//			Color.decode("#2a788e"),
//			Color.decode("#25848e"),
//			Color.decode("#21908c"),
//			Color.decode("#1e9c89"),
//			Color.decode("#22a884"),
//			Color.decode("#2fb47c"),
//			Color.decode("#43bf71"),
//			Color.decode("#5dc963"),
//			Color.decode("#7ad151"),
//			Color.decode("#9ad93d"),
//			Color.decode("#bcdf27"),
//			Color.decode("#dde318"),
//			Color.decode("#fde725")
			
			/** Cividis 21 */
			Color.decode("#00204D"),
			Color.decode("#002963"),
			Color.decode("#00326F"),
			Color.decode("#1B3B6D"),
			Color.decode("#31446B"),
			Color.decode("#414D6B"),
			Color.decode("#4E566C"),
			Color.decode("#5B5F6E"),
			Color.decode("#666970"),
			Color.decode("#717274"),
			Color.decode("#7C7B78"),
			Color.decode("#898579"),
			Color.decode("#958F78"),
			Color.decode("#A29976"),
			Color.decode("#AFA473"),
			Color.decode("#BDAF6F"),
			Color.decode("#CBBA69"),
			Color.decode("#D9C562"),
			Color.decode("#E7D159"),
			Color.decode("#F6DD4D"),
			Color.decode("#FFEA46")
	};
	public static final Color[] VALUE_COLORS_51 = new Color[] {
			
			/** Viridis 51 */
//			Color.decode("#440154"),
//			Color.decode("#46085c"),
//			Color.decode("#471063"),
//			Color.decode("#48176a"),
//			Color.decode("#481e70"),
//			Color.decode("#482575"),
//			Color.decode("#472b7a"),
//			Color.decode("#46317e"),
//			Color.decode("#453882"),
//			Color.decode("#433e85"),
//			Color.decode("#414487"),
//			Color.decode("#3e4989"),
//			Color.decode("#3c4f8b"),
//			Color.decode("#39558c"),
//			Color.decode("#375a8c"),
//			Color.decode("#345f8d"),
//			Color.decode("#32648e"),
//			Color.decode("#30698e"),
//			Color.decode("#2e6e8e"),
//			Color.decode("#2c738e"),
//			Color.decode("#2a788e"),
//			Color.decode("#287d8e"),
//			Color.decode("#26828e"),
//			Color.decode("#24878e"),
//			Color.decode("#228b8d"),
//			Color.decode("#21908c"),
//			Color.decode("#1f958b"),
//			Color.decode("#1f9a8a"),
//			Color.decode("#1f9e88"),
//			Color.decode("#20a386"),
//			Color.decode("#22a884"),
//			Color.decode("#26ad81"),
//			Color.decode("#2cb17e"),
//			Color.decode("#33b67a"),
//			Color.decode("#3aba76"),
//			Color.decode("#43bf71"),
//			Color.decode("#4dc36c"),
//			Color.decode("#57c766"),
//			Color.decode("#62ca5f"),
//			Color.decode("#6ece58"),
//			Color.decode("#7ad151"),
//			Color.decode("#86d549"),
//			Color.decode("#93d741"),
//			Color.decode("#a1da38"),
//			Color.decode("#aedc30"),
//			Color.decode("#bcdf27"),
//			Color.decode("#c9e01f"),
//			Color.decode("#d7e21a"),
//			Color.decode("#e4e419"),
//			Color.decode("#f1e51d"),
//			Color.decode("#fde725")
			
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
	private JLabel timeLabel, infoLabel;
	private InstructionUI instructionUI;
	private DesignUI[] designUIs = new DesignUI[Designer.NUM_STRATEGIES];
	private StrategyUI strategyUI;
	private int[] partnerDesigns = new int[Designer.NUM_STRATEGIES];
	private int managerTime;
	
	public DesignerUI() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		JPanel infoPanel = new JPanel(new FlowLayout());
		infoLabel = new JLabel("Initializing", JLabel.LEFT);
		infoPanel.add(infoLabel);
		this.add(infoPanel, c);
		c.gridx++;
		c.anchor = GridBagConstraints.NORTHEAST;
		JPanel timePanel = new JPanel(new FlowLayout());
		timePanel.add(new JLabel("Time Remaining:", JLabel.RIGHT));
		timeLabel = new JLabel("0:00", JLabel.RIGHT);
		timeLabel.setFont(timeLabel.getFont().deriveFont(20f));
		timePanel.add(timeLabel);
		this.add(timePanel, c);
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy++;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		tabbedPane = new JTabbedPane();
		instructionUI = new InstructionUI();
		tabbedPane.addTab("Instructions", instructionUI);
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			designUIs[i] = new DesignUI(i);
			tabbedPane.addTab(STRATEGY_LABELS[i] + " Design", designUIs[i]);
		}
		strategyUI = new StrategyUI();
		tabbedPane.addTab("Decision", strategyUI);
		
		this.add(tabbedPane, c);
	}
	
	private void flashTab(int index, Color color) {
		tabbedPane.setBackgroundAt(index, color);
		Timer timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setBackgroundAt(index, tabbedPane.getBackground());
			}
		});
		timer.setRepeats(false);
		timer.start();
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
									if(tabbedPane.getSelectedIndex() != i + 1) {
										flashTab(i+1, Color.decode("#ffcc80"));
									}
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
				Task task = app.getManager().getTaskByDesignerId(app.getController().getId());
				if(task == null) {
					infoLabel.setText(app.getManager().getRoundName());
				} else {
					infoLabel.setText(app.getManager().getRoundName() + ": " + task.getName());
				}
				if(managerTime != app.getManager().getTimeRemaining()) {
					managerTime = app.getManager().getTimeRemaining();
					timeLabel.setText(String.format("%01d:%02d", managerTime/60, managerTime % 60));
					if(managerTime == Manager.MAX_TASK_TIME) {
						tabbedPane.setSelectedIndex(0);
					}
					if((managerTime < Manager.MAX_TASK_TIME && managerTime % 60 == 0)
							|| (managerTime <= 60 && managerTime % 15 == 0) 
							|| (managerTime <= 10) ) {
						timeLabel.setForeground(Color.RED);
					} else {
						timeLabel.setForeground(Color.BLACK);
					}
					if(tabbedPane.getSelectedIndex() != Designer.NUM_STRATEGIES + 1
							&& managerTime <= 10 && managerTime % 2 == 0) {
						flashTab(Designer.NUM_STRATEGIES+1, Color.RED);
					}
				}
				setEnabled(app.getManager().isDesignEnabled());
			}
		});
	}
}
