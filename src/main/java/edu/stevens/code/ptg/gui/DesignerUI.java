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
			"Red", 
			"Blue"
	};
	public static final Color[] STRATEGY_COLORS = new Color[] {
			Color.decode("#ffcccc"),
			Color.decode("#ccccff")
	};
	public static final Color[] VALUE_COLORS_21 = new Color[] {
			
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
			
			/** Parula 51 */
			
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
