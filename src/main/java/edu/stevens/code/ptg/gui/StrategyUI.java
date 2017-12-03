package edu.stevens.code.ptg.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;

public class StrategyUI extends JPanel {
	private static final long serialVersionUID = -4318471579781451005L;
	
	private JPanel[][] valueContainers = new JPanel[Designer.NUM_STRATEGIES][Designer.NUM_STRATEGIES];
	private ValueSpacePanel[][] valuePanels = new ValueSpacePanel[Designer.NUM_STRATEGIES][Designer.NUM_STRATEGIES];
	private ValueLabel[][] valueLabels = new ValueLabel[Designer.NUM_STRATEGIES][Designer.NUM_STRATEGIES];
	private JRadioButton[] strategyRadios = new JRadioButton[Designer.NUM_STRATEGIES];
	
	public StrategyUI() {		
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.CENTER;

		c.gridwidth = 3;
		c.anchor = GridBagConstraints.EAST;
		JToggleButton toggleButton = new JToggleButton("Detail");
		toggleButton.setHorizontalAlignment(JToggleButton.RIGHT);
		toggleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
					for(int j = 0; j < Designer.NUM_STRATEGIES; j++) {
						if(toggleButton.isSelected()) {
							valueContainers[i][j].remove(valueLabels[i][j]);
							valueContainers[i][j].add(valuePanels[i][j], BorderLayout.CENTER);
							valueContainers[i][j].validate();
							valueContainers[i][j].repaint();
						} else {
							valueContainers[i][j].remove(valuePanels[i][j]);
							valueContainers[i][j].add(valueLabels[i][j], BorderLayout.CENTER);
							valueContainers[i][j].validate();
							valueContainers[i][j].repaint();
						}
					}
				}
			}
		});
		add(toggleButton, c);
		c.gridwidth = 1;
		c.gridy++;
		
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			c.fill = GridBagConstraints.VERTICAL;
			c.weightx = 0;
			c.weighty = 0;
			JLabel designLabel = new JLabel("Design " + i, JLabel.CENTER);
			designLabel.setOpaque(true);
			designLabel.setBackground(DesignerUI.STRATEGY_COLORS[i]);
			this.add(designLabel, c);
			c.fill = GridBagConstraints.BOTH;
			c.gridx++;
			c.weightx = 1;
			c.weighty = 1;
			for(int j = 0; j < Designer.NUM_STRATEGIES; j++) {
				valueContainers[i][j] = new JPanel(new BorderLayout());
				valueContainers[i][j].setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
				if(i == j) {
					valueContainers[i][j].setBackground(DesignerUI.STRATEGY_COLORS[j]);
				}
				valuePanels[i][j] = new ValueSpacePanel();
				valueLabels[i][j] = new ValueLabel();
				valueContainers[i][j].add(valueLabels[i][j], BorderLayout.CENTER);
				this.add(valueContainers[i][j], c);
				c.gridx++;
			}
			c.gridx = 0;
			c.gridy++;
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.weighty = 0;
		this.add(new JLabel(""), c);
		c.gridx++;
		ButtonGroup radios = new ButtonGroup();
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			strategyRadios[i] = new JRadioButton("Design " + i);
			strategyRadios[i].setHorizontalAlignment(JLabel.CENTER);
			strategyRadios[i].setBackground(DesignerUI.STRATEGY_COLORS[i]);
			radios.add(strategyRadios[i]);
			this.add(strategyRadios[i], c);
			c.gridx++;
		}
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			strategyRadios[i].setEnabled(enabled);
		}
	}
	
	public void bindTo(DesignerApp app) {		
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			final int strategy = i;
			strategyRadios[i].setSelected(app.getController().getStrategy()==i);
			strategyRadios[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					app.getController().setStrategy(strategy);
				}
			});
			for(int j = 0; j < Designer.NUM_STRATEGIES; j++) {
				valueLabels[j][i].bindTo(app, i, j);
				valuePanels[j][i].bindTo(app, i, j);
			}
		}
		app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				setEnabled(app.getManager().isDesignEnabled());
			}
		});
	}
}
