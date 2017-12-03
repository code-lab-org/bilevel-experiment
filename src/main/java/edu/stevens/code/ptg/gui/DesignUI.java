package edu.stevens.code.ptg.gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;

public class DesignUI extends JPanel {
	private static final long serialVersionUID = -4318471579781451005L;
	
	private int strategy;
	private JLabel valueLabel;
	private ValueSpacePanel valuePanel;
	private JSlider mySlider;
	private JSlider partnerSlider;
	
	public DesignUI(int strategy) {
		if(strategy < 0 || strategy >= Designer.NUM_STRATEGIES) {
			throw new IllegalArgumentException("invalid strategy index");
		}
		this.strategy = strategy;
		this.setBackground(DesignerUI.STRATEGY_COLORS[strategy]);
		
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 5;
		c.ipady = 5;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 2;
		JPanel scorePanel = new JPanel(new FlowLayout());
		scorePanel.add(new JLabel("Value:"));
		valueLabel = new JLabel("0", JLabel.CENTER);
		scorePanel.add(valueLabel);
		scorePanel.setOpaque(false);
		this.add(scorePanel, c);
		
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.VERTICAL;
		partnerSlider = new JSlider(Designer.MIN_DESIGN_VALUE, Designer.MAX_DESIGN_VALUE);
		partnerSlider.setOrientation(JSlider.VERTICAL);
		partnerSlider.setEnabled(false);
		partnerSlider.setOpaque(false);
		this.add(partnerSlider, c);
		
		c.gridx++;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		valuePanel = new ValueSpacePanel();
		valuePanel.setOpaque(false);
		this.add(valuePanel, c);
		
		c.gridy++;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		mySlider = new JSlider(Designer.MIN_DESIGN_VALUE, Designer.MAX_DESIGN_VALUE);
		mySlider.setEnabled(false);
		mySlider.setOpaque(false);
		this.add(mySlider, c);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		mySlider.setEnabled(enabled);
	}
	
	public void bindTo(DesignerApp app) {		
		mySlider.setValue(app.getController().getDesign(strategy));
		mySlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				app.getController().setDesign(strategy, mySlider.getValue());
			}
		});
		partnerSlider.setValue(0);
		valuePanel.bindTo(app, strategy, strategy);
		for(Designer designer : app.getDesigners()) {
			designer.addObserver(new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					if(designer == app.getDesignPartner() && designer.isReadyToShare()) {
						partnerSlider.setValue(designer.getDesign(strategy));
					}
					valueLabel.setText(new Integer(app.getValue(
						strategy, app.getController().getDesign(strategy), 
						strategy, partnerSlider.getValue()
					)).toString());
				}
			});
		}
		app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				setEnabled(app.getManager().isDesignEnabled());
			}
		});
	}
}
