package edu.stevens.code.ptg.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Observable;
import java.util.Observer;

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
			Color.decode("#352a87"),
			Color.decode("#353eaf"),
			Color.decode("#1b55d7"),
			Color.decode("#026ae1"),
			Color.decode("#0f77db"),
			Color.decode("#1484d4"),
			Color.decode("#0d93d2"),
			Color.decode("#06a0cd"),
			Color.decode("#07aac1"),
			Color.decode("#18b1b2"),
			Color.decode("#33b8a1"),
			Color.decode("#55bd8e"),
			Color.decode("#7abf7c"),
			Color.decode("#9bbf6f"),
			Color.decode("#b8bd63"),
			Color.decode("#d3bb58"),
			Color.decode("#ecb94c"),
			Color.decode("#ffc13a"),
			Color.decode("#fad12b"),
			Color.decode("#f5e31e"),
			Color.decode("#f9fb0e")
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
						timeLabelDesign.setText(String.format("%01d:%02d", 0, 0));
						timeLabelStrategy.setText(String.format("%01d:%02d", managerTime/60, managerTime % 60));
					} else {
						timeLabelDesign.setText(String.format("%01d:%02d", (managerTime - Manager.STRATEGY_TIME)/60, (managerTime - Manager.STRATEGY_TIME) % 60));
						timeLabelStrategy.setText(String.format("%01d:%02d", Manager.STRATEGY_TIME/60, Manager.STRATEGY_TIME % 60));
					}
					
					if(managerTime == Manager.MAX_TASK_TIME) {
						tabbedPane.setSelectedIndex(0);
					}
					if((managerTime < Manager.MAX_TASK_TIME && managerTime % 60 == 0)
							|| (managerTime <= 60 && managerTime % 15 == 0) 
							|| (managerTime <= 10) ) {
						timeLabelDesign.setForeground(Color.RED);
						timeLabelStrategy.setForeground(Color.RED);
					} else {
						timeLabelDesign.setForeground(Color.BLACK);
						timeLabelStrategy.setForeground(Color.BLACK);
					}
				}
				setEnabled(app.getManager().isDesignEnabled());
			}
		});
	}
}
