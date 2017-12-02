package edu.stevens.code.ptg.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;

public class DesignerPanelImpl extends DesignerPanel {
	private static final long serialVersionUID = 2488259187981650893L;
	
	private JRadioButton[] strategyRadios = new JRadioButton[Designer.NUM_STRATEGIES];
	private JSlider[] designSliders = new JSlider[Designer.NUM_STRATEGIES];
	private JToggleButton shareButton;
	
	public DesignerPanelImpl() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		ButtonGroup radios = new ButtonGroup();
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			JRadioButton radio = new JRadioButton("Design " + i);
			radio.setEnabled(false);
			radios.add(radio);
			strategyRadios[i] = radio;
			this.add(radio, c);
			c.gridx++;
			c.fill = GridBagConstraints.HORIZONTAL;
			JSlider slider = new JSlider();
			slider.setEnabled(false);
			slider.setMinimum(Designer.MIN_DESIGN_VALUE);
			slider.setMaximum(Designer.MAX_DESIGN_VALUE);
			designSliders[i] = slider;
			this.add(slider, c);
			c.fill = GridBagConstraints.NONE;
			c.gridy++;
			c.gridx = 0;
		}
		shareButton = new JToggleButton("Share");
		shareButton.setEnabled(false);
		this.add(shareButton, c);
	}
	
	@Override
	public void observe(Designer designer) {
		this.setBorder(BorderFactory.createTitledBorder(designer.toString()));
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			strategyRadios[i].setSelected(designer.getStrategy()==i);
			designSliders[i].setValue(designer.getDesign(i));
		}
		shareButton.setSelected(designer.isReadyToShare());
		designer.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
					strategyRadios[i].setSelected(designer.getStrategy()==i);
					designSliders[i].setValue(designer.getDesign(i));
				}
				shareButton.setSelected(designer.isReadyToShare());
			}
		});
	}

	@Override
	public void bindTo(Designer designer) {
		this.setBorder(BorderFactory.createTitledBorder(designer.toString()));
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			final int strategyIndex = i;
			strategyRadios[i].setEnabled(true);
			strategyRadios[i].setSelected(designer.getStrategy()==i);
			strategyRadios[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					designer.setStrategy(strategyIndex);
				}
			});
			designSliders[i].setEnabled(true);
			designSliders[i].setValue(designer.getDesign(i));
			designSliders[i].addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					designer.setDesign(strategyIndex, designSliders[strategyIndex].getValue());
				}
			});
		}
		shareButton.setEnabled(true);
		shareButton.setSelected(designer.isReadyToShare());
		shareButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				designer.setReadyToShare(shareButton.isSelected());
			}
		});
	}

	@Override
	public void bindTo(DesignerApp app) { }
}
