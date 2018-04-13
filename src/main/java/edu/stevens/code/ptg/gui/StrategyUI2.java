package edu.stevens.code.ptg.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;
import edu.stevens.code.ptg.Manager;

public class StrategyUI2 extends JPanel {
	private static final long serialVersionUID = -4318471579781451005L;
	
	private JPanel[] strategyPanels = new JPanel[Designer.NUM_STRATEGIES];
	private JPanel[][] valueContainers = new JPanel[Designer.NUM_STRATEGIES][Designer.NUM_STRATEGIES];
	private ValueLabel[][] valueLabels = new ValueLabel[Designer.NUM_STRATEGIES][Designer.NUM_STRATEGIES];
	private JToggleButton[] strategyButtons = new JToggleButton[Designer.NUM_STRATEGIES];
	
	public StrategyUI2() {
		this.setLayout(new GridLayout(1,0,5,5));
		

		ButtonGroup radios = new ButtonGroup();
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			strategyPanels[i] = new JPanel();
			strategyPanels[i].setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			strategyPanels[i].setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 0.5;
			c.weighty = 1;
			
			for(int j : new int[]{i, 1-i}) {
				c.gridwidth = 1;
				c.gridy = 0;
				c.fill = GridBagConstraints.BOTH;
				
				JLabel label;
				if(i == j) {
					label = new JLabel("     Agree on " + DesignerUI.STRATEGY_LABELS[i] + "     ", JLabel.CENTER);
					label.setBackground(DesignerUI.STRATEGY_COLORS[i]);
				} else {
					label = new JLabel("Disagree (" + DesignerUI.STRATEGY_LABELS[i] + "/" + DesignerUI.STRATEGY_LABELS[1-i] + ")", JLabel.CENTER);
				}
				
				label.setFont(label.getFont().deriveFont(24f));
				label.setOpaque(true);
				strategyPanels[i].add(label, c);
				c.gridy++;
				valueContainers[i][j] = new JPanel(new BorderLayout());
				valueContainers[i][j].setBorder(
						BorderFactory.createEmptyBorder(10,20,10,20));
				if(i == j) {
					valueContainers[i][j].setBackground(DesignerUI.STRATEGY_COLORS[i]);
				}
				valueLabels[i][j] = new ValueLabel();
				valueLabels[i][j].setPreferredSize(new Dimension(100,75));
				valueContainers[i][j].add(valueLabels[i][j], BorderLayout.CENTER);
				strategyPanels[i].add(valueContainers[i][j], c);

				c.gridx++;
			}

			c.gridy++;
			c.gridx = 0;
			c.gridwidth = Designer.NUM_STRATEGIES;
			c.fill = GridBagConstraints.NONE;
			strategyButtons[i] = new JToggleButton("Choose " + DesignerUI.STRATEGY_LABELS[i]);
			strategyButtons[i].setFont(strategyButtons[i].getFont().deriveFont(24f));
			strategyButtons[i].setHorizontalAlignment(JLabel.CENTER);
			radios.add(strategyButtons[i]);
			strategyPanels[i].add(strategyButtons[i], c);

			final int strategy = i;
			strategyPanels[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					strategyButtons[strategy].doClick();
				}
			});
			this.add(strategyPanels[i]);
		}
	}
	
	private void resetUI() {
		strategyButtons[0].doClick();
		strategyPanels[0].setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			strategyButtons[i].setEnabled(enabled);
		}
	}
	
	public void bindTo(DesignerApp app) {		
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			final int strategy = i;
			strategyButtons[i].setSelected(app.getController().getStrategy()==i);
			strategyButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					app.getController().setStrategy(strategy);
					for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
						if(i == strategy) {
							strategyPanels[i].setBorder(
									BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
						} else {
							strategyPanels[i].setBorder(
									BorderFactory.createEmptyBorder(5, 5, 5, 5));
						}
					}
				}
			});
			for(int j = 0; j < Designer.NUM_STRATEGIES; j++) {
				valueLabels[j][i].bindTo(app, i, j);
			}
		}
		app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				setEnabled(app.getManager().isDesignEnabled());
				if(app.getManager().getTimeRemaining() == Manager.MAX_TASK_TIME) {
					resetUI();
				}
			}
		});
	}
}
