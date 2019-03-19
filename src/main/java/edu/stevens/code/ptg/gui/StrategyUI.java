package edu.stevens.code.ptg.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import edu.stevens.code.ptg.Manager;

public class StrategyUI extends JPanel {
	private static final long serialVersionUID = -4318471579781451005L;
	
	private JPanel[][] valueContainers = new JPanel[Designer.NUM_STRATEGIES][Designer.NUM_STRATEGIES];
	private ValuePanel[][] valuePanels = new ValuePanel[Designer.NUM_STRATEGIES][Designer.NUM_STRATEGIES];
	private ValueLabel[][] valueLabels = new ValueLabel[Designer.NUM_STRATEGIES][Designer.NUM_STRATEGIES];
	private JRadioButton[] strategyRadios = new JRadioButton[Designer.NUM_STRATEGIES];
	
	private JToggleButton toggleButton;
	
	public StrategyUI() {		
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;

		c.gridwidth = 4;
		c.anchor = GridBagConstraints.EAST;
		toggleButton = new JToggleButton("Detail");
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
		// add(toggleButton, c);
		
		c.gridy++;
		c.gridx = Designer.NUM_STRATEGIES + 1;
		c.gridwidth = 1;
		c.gridheight = Designer.NUM_STRATEGIES;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		this.add(new ColorBarPanel(), c);
		
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			c.gridx = 0;
			c.weightx = 0;
			c.weighty = 0;
			JLabel designLabel = new JLabel(DesignerUI.STRATEGY_LABELS[i] + " Design", JLabel.CENTER);
			designLabel.setOpaque(true);
			designLabel.setBackground(DesignerUI.STRATEGY_COLORS[i]);
			this.add(designLabel, c);
			c.gridx++;
			c.weightx = 1;
			c.weighty = 1;
			
			for(int j = 0; j < Designer.NUM_STRATEGIES; j++) {
				valueContainers[i][j] = new JPanel(new BorderLayout());
				final int strategy = j;
				valueContainers[i][j].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						strategyRadios[strategy].doClick();
					}
				});
				if(j == 0) {
					valueContainers[i][j].setBorder(
							BorderFactory.createMatteBorder(10,10,10,10,Color.BLACK));
				} else {
					valueContainers[i][j].setBorder(
							BorderFactory.createEmptyBorder(10,10,10,10));
				}
				if(i == j) {
					valueContainers[i][j].setBackground(DesignerUI.STRATEGY_COLORS[j]);
				}
				valuePanels[i][j] = new ValuePanel();
				if(i == j) {
					valueLabels[i][j] = new ValueLabel();
				} else {
					valueLabels[i][j] = new ValueLabel();
				}
				valueContainers[i][j].add(valueLabels[i][j], BorderLayout.CENTER);
				this.add(valueContainers[i][j], c);
				c.gridx++;
			}
			c.gridy++;
		}
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 1;
		c.gridx = 0;
		c.weightx = 0;
		c.weighty = 0;
		this.add(new JLabel(""), c);
		c.gridx++;
		ButtonGroup radios = new ButtonGroup();
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			strategyRadios[i] = new JRadioButton(DesignerUI.STRATEGY_LABELS[i] + " Design");
			strategyRadios[i].setHorizontalAlignment(JLabel.CENTER);
			strategyRadios[i].setBackground(DesignerUI.STRATEGY_COLORS[i]);
			strategyRadios[i].addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
						if(strategyRadios[i].isSelected()) {
							for(int j = 0; j < Designer.NUM_STRATEGIES; j++) {
								valueContainers[j][i].setBorder(
										BorderFactory.createMatteBorder(10,10,10,10,Color.BLACK));
							}
						} else {
							for(int j = 0; j < Designer.NUM_STRATEGIES; j++) {
								valueContainers[j][i].setBorder(
										BorderFactory.createEmptyBorder(10,10,10,10));
							}
						}
					}
				}
			});
			radios.add(strategyRadios[i]);
			this.add(strategyRadios[i], c);
			c.gridx++;
		}
	}
	
	private void resetUI() {
		strategyRadios[0].doClick();
		if(toggleButton.isSelected()) {
			toggleButton.doClick();
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
				if(app.getManager().getTimeRemaining() == Manager.MAX_TASK_TIME) {
					resetUI();
				}
			}
		});
	}
}
