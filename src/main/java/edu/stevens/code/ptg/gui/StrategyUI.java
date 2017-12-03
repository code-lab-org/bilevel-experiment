package edu.stevens.code.ptg.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;

public class StrategyUI extends JPanel {
	private static final long serialVersionUID = -4318471579781451005L;
	
	private ValueSpacePanel[][] valuePanels = new ValueSpacePanel[Designer.NUM_STRATEGIES][Designer.NUM_STRATEGIES];
	private JRadioButton[] strategyRadios = new JRadioButton[Designer.NUM_STRATEGIES];
	
	public StrategyUI() {		
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 5;
		c.ipady = 5;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.CENTER;
		
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			c.fill = GridBagConstraints.VERTICAL;
			c.weightx = 0;
			c.weighty = 0;
			this.add(new JLabel("Design " + i, JLabel.CENTER), c);
			c.fill = GridBagConstraints.BOTH;
			c.gridx++;
			c.weightx = 1;
			c.weighty = 1;
			for(int j = 0; j < Designer.NUM_STRATEGIES; j++) {
				valuePanels[i][j] = new ValueSpacePanel();
				valuePanels[i][j].setOpaque(false);
				this.add(valuePanels[i][j], c);
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
