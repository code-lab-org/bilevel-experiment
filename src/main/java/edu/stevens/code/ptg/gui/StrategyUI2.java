package edu.stevens.code.ptg.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	
	private JPanel[][] valueContainers = new JPanel[Designer.NUM_STRATEGIES][Designer.NUM_STRATEGIES];
	private ValueLabel[][] valueLabels = new ValueLabel[Designer.NUM_STRATEGIES][Designer.NUM_STRATEGIES];
	private JToggleButton[] strategyButtons = new JToggleButton[Designer.NUM_STRATEGIES];
	
	public StrategyUI2() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.5;
		c.weighty = 1;
		c.insets = new Insets(5,5,5,5);
		c.fill = GridBagConstraints.BOTH;

		ButtonGroup radios = new ButtonGroup();
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			c.gridy = 0;
			JLabel agreeLabel = new JLabel("Agree on " + DesignerUI.STRATEGY_LABELS[i], JLabel.CENTER);
			agreeLabel.setFont(agreeLabel.getFont().deriveFont(24f));
			agreeLabel.setOpaque(true);
			agreeLabel.setBackground(DesignerUI.STRATEGY_COLORS[i]);
			this.add(agreeLabel, c);
			c.gridy++;
			valueContainers[i][i] = new JPanel(new BorderLayout());
			valueContainers[i][i].setBorder(
					BorderFactory.createEmptyBorder(10,10,10,10));
			valueContainers[i][i].setBackground(DesignerUI.STRATEGY_COLORS[i]);
			valueLabels[i][i] = new ValueLabel();
			valueLabels[i][i].setPreferredSize(new Dimension(100,75));
			valueContainers[i][i].add(valueLabels[i][i], BorderLayout.CENTER);
			this.add(valueContainers[i][i], c);
			
			c.gridy++;
			c.gridwidth = 2;
			c.fill = GridBagConstraints.NONE;
			strategyButtons[i] = new JToggleButton("Choose " + DesignerUI.STRATEGY_LABELS[i]);
			strategyButtons[i].setFont(strategyButtons[i].getFont().deriveFont(24f));
			strategyButtons[i].setHorizontalAlignment(JLabel.CENTER);
			radios.add(strategyButtons[i]);
			this.add(strategyButtons[i], c);

			c.gridx++;
			c.gridwidth = 1;
			c.gridy = 0;
			c.fill = GridBagConstraints.BOTH;
			JLabel disagreeLabel = new JLabel("Disagree (" + DesignerUI.STRATEGY_LABELS[i] + "/" + DesignerUI.STRATEGY_LABELS[1-i] + ")", JLabel.CENTER);
			disagreeLabel.setFont(disagreeLabel.getFont().deriveFont(24f));
			this.add(disagreeLabel, c);
			c.gridy++;				
			valueContainers[i][1-i] = new JPanel(new BorderLayout());
			valueContainers[i][1-i].setBorder(
					BorderFactory.createEmptyBorder(10,10,10,10));
			valueLabels[i][1-i] = new FakeValueLabel();
			valueLabels[i][1-i].setPreferredSize(new Dimension(100,75));
			valueContainers[i][1-i].add(valueLabels[i][1-i], BorderLayout.CENTER);
			this.add(valueContainers[i][1-i], c);
			
			c.gridx++;
		}
	}
	
	private void resetUI() {
		strategyButtons[0].doClick();
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
