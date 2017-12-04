package edu.stevens.code.ptg.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
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

public class DesignerUI extends DesignerAppPanel {
	private static final long serialVersionUID = 1163389143406697128L;
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
	private JLabel timeLabel, infoLabel;
	private DesignUI[] designUIs = new DesignUI[Designer.NUM_STRATEGIES];
	private StrategyUI strategyUI;
	private int[] partnerDesigns = new int[Designer.NUM_STRATEGIES];
	private int managerTime;
	
	public DesignerUI() {
		this.setLayout(new BorderLayout());
		JPanel infoPanel = new JPanel(new FlowLayout());
		infoLabel = new JLabel("Initializing", JLabel.LEFT);
		infoPanel.add(infoLabel);
		infoPanel.add(new JLabel("Time Remaining:"));
		timeLabel = new JLabel("0", JLabel.RIGHT);
		infoPanel.add(timeLabel);
		this.add(infoPanel, BorderLayout.NORTH);
		tabbedPane = new JTabbedPane();
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			designUIs[i] = new DesignUI(i);
			tabbedPane.addTab("Design " + i, designUIs[i]);
		}
		strategyUI = new StrategyUI();
		tabbedPane.addTab("Composite", strategyUI);
		
		this.add(tabbedPane, BorderLayout.CENTER);
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
									if(tabbedPane.getSelectedIndex() != i) {
										flashTab(i, Color.decode("#ffcc80"));
									}
								}
							}
						}
					}
				});
			}
		}
		strategyUI.bindTo(app);
		app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				infoLabel.setText(app.getManager().getRoundName());
				if(managerTime != app.getManager().getTimeRemaining()) {
					managerTime = app.getManager().getTimeRemaining();
					timeLabel.setText(new Integer(managerTime).toString());
					if(managerTime == Manager.MAX_TASK_TIME) {
						tabbedPane.setSelectedIndex(0);
					}
					if((managerTime % 60 == 0)
							|| (managerTime <= 60 && managerTime % 15 == 0) 
							|| (managerTime <= 10) ) {
						timeLabel.setForeground(Color.RED);
					} else {
						timeLabel.setForeground(Color.BLACK);
					}
					if(tabbedPane.getSelectedIndex() != Designer.NUM_STRATEGIES
							&& managerTime <= 10 && managerTime % 2 == 0) {
						flashTab(Designer.NUM_STRATEGIES, Color.RED);
					}
				}
			}
		});
	}
}
