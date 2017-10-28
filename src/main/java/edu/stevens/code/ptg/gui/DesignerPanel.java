package edu.stevens.code.ptg.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.stevens.code.ptg.Designer;

public class DesignerPanel extends JPanel {
	private static final long serialVersionUID = 2488259187981650893L;
	
	private JRadioButton[] strategies = new JRadioButton[Designer.NUM_STRATEGIES];
	private JSlider[] designs = new JSlider[Designer.NUM_STRATEGIES];
	private JToggleButton share = new JToggleButton("Share");
	
	public DesignerPanel() {
		this.setPreferredSize(new Dimension(400,300));
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		ButtonGroup radios = new ButtonGroup();
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			JRadioButton radio = new JRadioButton("Design " + i);
			radios.add(radio);
			strategies[i] = radio;
			this.add(radio, c);
			c.gridx++;
			c.fill = GridBagConstraints.HORIZONTAL;
			JSlider slider = new JSlider();
			slider.setMinimum(Designer.MIN_DESIGN_VALUE);
			slider.setMaximum(Designer.MAX_DESIGN_VALUE);
			designs[i] = slider;
			this.add(slider, c);
			c.fill = GridBagConstraints.NONE;
			c.gridy++;
			c.gridx = 0;
		}
		this.add(share, c);
	}
	
	public void observe(Designer designer) {
		designer.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
					strategies[i].setSelected(designer.getStrategy()==i);
					designs[i].setValue(designer.getDesign(i));
				}
				share.setSelected(designer.isReadyToShare());
			}
		});
	}
	
	public void bindTo(Designer designer) {		
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			final int strategyIndex = i;
			strategies[i].setSelected(designer.getStrategy()==i);
			strategies[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					designer.setStrategy(strategyIndex);
				}
			});
			designs[i].setValue(designer.getDesign(i));
			designs[i].addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					designer.setDesign(strategyIndex, designs[strategyIndex].getValue());
				}
			});
		}
		share.setSelected(designer.isReadyToShare());
		share.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				designer.setReadyToShare(share.isSelected());
			}
		});
	}
}
