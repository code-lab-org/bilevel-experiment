/******************************************************************************
 * Copyright 2020 Stevens Institute of Technology, Collective Design Lab
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.stevens.code.eager.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.stevens.code.eager.DesignerApp;
import edu.stevens.code.eager.model.Designer;

/**
 * A designer panel that provides debug features.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 */
public class DebugDesignerPanel extends DesignerPanel {
	private static final long serialVersionUID = 2488259187981650893L;
	
	private JRadioButton[] strategyRadios = new JRadioButton[Designer.NUM_STRATEGIES];
	private JSlider[] designSliders = new JSlider[Designer.NUM_STRATEGIES];
	private JLabel[][] scoreLabels = new JLabel[Designer.NUM_STRATEGIES][Designer.NUM_STRATEGIES];
	private JToggleButton shareButton;
	
	private boolean alwaysShare = false;
	private int[] partnerDesigns = new int[Designer.NUM_STRATEGIES];
	
	/**
	 * Instantiates a new debug designer panel.
	 */
	public DebugDesignerPanel() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 1;
		ButtonGroup radios = new ButtonGroup();
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			c.fill = GridBagConstraints.NONE;
			c.gridx = 0;
			JRadioButton radio = new JRadioButton("Design " + i);
			radio.setEnabled(false);
			radios.add(radio);
			strategyRadios[i] = radio;
			this.add(radio, c);
			c.gridx++;
			c.weightx = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			JSlider slider = new JSlider();
			slider.setEnabled(false);
			slider.setMinimum(Designer.MIN_DESIGN_VALUE);
			slider.setMaximum(Designer.MAX_DESIGN_VALUE);
			designSliders[i] = slider;
			this.add(slider, c);
			c.gridx++;
			c.weightx = 0;
			JPanel scorePanel = new JPanel();
			scorePanel.setPreferredSize(new Dimension(50,25));
			scorePanel.setLayout(new GridLayout(1,Designer.NUM_STRATEGIES, 5, 5));
			for(int j = 0; j < Designer.NUM_STRATEGIES; j++) {
				scoreLabels[i][j] = new JLabel();
				if(i == j) {
					scoreLabels[i][j].setFont(getFont().deriveFont(Font.BOLD));
				} else {
					scoreLabels[i][j].setFont(getFont().deriveFont(Font.PLAIN));
				}
				scorePanel.add(scoreLabels[i][j]);
			}
			this.add(scorePanel, c);
			c.gridy++;
		}
		c.gridx++;
		c.gridheight = Designer.NUM_STRATEGIES;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		shareButton = new JToggleButton("Share");
		shareButton.setEnabled(false);
		this.add(shareButton, c);
	}
	
	/**
	 * Set to always share the design information.
	 *
	 * @param alwaysShare true, if design information should always be shared
	 */
	public void setAlwaysShare(boolean alwaysShare) {
		this.alwaysShare = alwaysShare;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			strategyRadios[i].setEnabled(enabled);
			designSliders[i].setEnabled(enabled);
		}
		shareButton.setEnabled(enabled);
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
				if(alwaysShare || designer.isReadyToShare()) {
					for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
						strategyRadios[i].setSelected(designer.getStrategy()==i);
						designSliders[i].setValue(designer.getDesign(i));
					}
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
			strategyRadios[i].setSelected(designer.getStrategy()==i);
			strategyRadios[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					designer.setStrategy(strategyIndex);
				}
			});
			designSliders[i].setValue(designer.getDesign(i));
			designSliders[i].addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					designer.setDesign(strategyIndex, designSliders[strategyIndex].getValue());
				}
			});
		}
		shareButton.setSelected(designer.isReadyToShare());
		shareButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				designer.setReadyToShare(shareButton.isSelected());
			}
		});
		this.setEnabled(true);
	}

	@Override
	public void bindTo(DesignerApp app) {
		for(Designer designer : app.getDesigners()) {
			// update the scores whenever a designer is updated
			designer.addObserver(new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					updateScores(app, designer);
				}
			});
			if(designer == app.getController()) {
				// bind to the app designer
				bindTo(app.getController());
			}
		}
		// observe the app manager to lock/unlock the interface
		app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				setEnabled(app.getManager().isDesignEnabled());
			}
		});
		// update the interface based on the current manager state
		setEnabled(app.getManager().isDesignEnabled());
	}
	
	/**
	 * Update scores.
	 *
	 * @param app the application
	 * @param designer the designer
	 */
	private void updateScores(DesignerApp app, Designer designer) {
		Designer controller = app.getController();
		Designer partner = app.getDesignPartner();
		// update partner designs if ready to share
		if(alwaysShare || (designer == partner && designer.isReadyToShare())) {
			partnerDesigns = designer.getDesigns();
		}
		// update if the designer is either controller or partner
		if(designer == controller || designer == partner) {
			for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
				for(int j = 0; j < Designer.NUM_STRATEGIES; j++) {
					scoreLabels[i][j].setText(new Integer(app.getValue(
							i, controller.getDesigns(), 
							j, partnerDesigns)).toString());
				}
			}
		}
	}
}
