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
package edu.stevens.code.bilevel.gui.designer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.stevens.code.bilevel.app.DesignerApp;
import edu.stevens.code.bilevel.model.Designer;
import edu.stevens.code.bilevel.model.Manager;

/**
 * A panel for a user interface to design decisions.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 * @author Ambrosio Valencia-Romero <avalenci@stevens.edu>
 */
public class DesignUI extends JPanel {
	private static final long serialVersionUID = -4318471579781451005L;
	
	private int strategy;
	private JLabel[] valueLabels = new JLabel[Designer.NUM_STRATEGIES];
	private JToggleButton strategyToggle;
	private JPanel valueContainer;
	protected ValuePanel[] valuePanels = new ValuePanel[Designer.NUM_STRATEGIES];
	private JSlider mySlider;
	private JSlider partnerSlider;
	
	/**
	 * Instantiates a new design UI.
	 *
	 * @param strategy the strategy
	 */
	public DesignUI(int strategy) {
		if(strategy < 0 || strategy >= Designer.NUM_STRATEGIES) {
			throw new IllegalArgumentException("invalid strategy index");
		}
		this.strategy = strategy;
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createCompoundBorder(
				       BorderFactory.createMatteBorder(1,1,1,1, Color.BLACK),
				       BorderFactory.createEmptyBorder(12, 12, 12, 12)));
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 5;
		c.ipady = 5;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 3;
		// add a panel to display the value (currently disabled)
		JPanel scorePanel = new JPanel(new FlowLayout());
		scorePanel.add(new JLabel("Value:"));
		{
			valueLabels[strategy] = new JLabel("-", JLabel.CENTER);
			scorePanel.add(valueLabels[strategy]);
			valueLabels[strategy].setFont(getFont().deriveFont(Font.BOLD));
			JLabel label = new JLabel("(Agree)");
			label.setFont(getFont().deriveFont(Font.BOLD));
			scorePanel.add(label);
		}
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			if(i != strategy) {
				valueLabels[i] = new JLabel("-", JLabel.CENTER);
				scorePanel.add(valueLabels[i]);
				valueLabels[i].setFont(getFont().deriveFont(Font.PLAIN));
				JLabel label = new JLabel("(Disagree)");
				label.setFont(getFont().deriveFont(Font.PLAIN));
				scorePanel.add(label);
			}
		}
		scorePanel.setOpaque(false);
		// this.add(scorePanel, c);
		// add a panel to toggle partner strategies (currently disabled)
		JLabel strategyID = new JLabel("<html>&nbsp;&nbsp;&emsp;"+DesignerUI.STRATEGY_LABELS[strategy]+"</html>");
		strategyID.setFont(new Font("Arial", Font.BOLD, 72));
		this.add(strategyID, c);
		c.gridx = 2;
		c.gridwidth = 1;		
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		strategyToggle = new JToggleButton("Agree", false);
		strategyToggle.setFocusable(false);
		strategyToggle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateValues();
			}
		});
		JPanel comboPanel = new JPanel(new FlowLayout());
		comboPanel.setOpaque(false);
		comboPanel.add(new JLabel("Partner:"));
		comboPanel.add(strategyToggle);
		// add(comboPanel, c);
		// add the value panels
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy++;
		c.weighty = 0;
		c.anchor = GridBagConstraints.SOUTH;
		c.fill = GridBagConstraints.VERTICAL;
		partnerSlider = new JSlider(Designer.MIN_DESIGN_VALUE, Designer.MAX_DESIGN_VALUE, Designer.NUM_DESIGNS/2);
		partnerSlider.setOrientation(JSlider.VERTICAL);
		partnerSlider.setEnabled(false);
		partnerSlider.setOpaque(false);
		this.add(partnerSlider, c);
		c.gridx++;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.SOUTH;
		c.fill = GridBagConstraints.BOTH;
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			valuePanels[i] = new ValuePanel();
			valuePanels[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int design = ((ValuePanel)e.getSource()).getDesign(e.getX());
					if (design >= 0 && mySlider.isEnabled()){
						mySlider.requestFocus();
						mySlider.setValue(design);
					} else {
						// allow the design ui to process the click
						processMouseEvent(e);
					}
				}
			});
		}
		String key = strategy == 1 ? "N" : "V";
		this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed " + key), "shiftStates");
		this.getActionMap().put("shiftStates", new AbstractAction() {
			private static final long serialVersionUID = -6000894619328234043L;
			@Override
			public void actionPerformed(ActionEvent e) {
				strategyToggle.setSelected(true);
				updateValues();
			}
		});
		this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released " + key), "unshiftStates");
		this.getActionMap().put("unshiftStates", new AbstractAction() {
			private static final long serialVersionUID = -65459306537253835L;
			@Override
			public void actionPerformed(ActionEvent e) {
				strategyToggle.setSelected(false);
				updateValues();
			}
		});
		valueContainer = new JPanel(new GridBagLayout()); // use layout to align sliders
		valueContainer.setOpaque(false);
		valueContainer.add(valuePanels[strategy]);
		this.add(valueContainer, c);
		// add a color bar panel
		c.gridx+=2;
		c.weightx = 0.26;
		c.weighty = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.VERTICAL;
		this.add(new ColorBarPanel(), c);
		// add a slider for my design decisions
		c.gridx = 1;
		c.gridy++;
		c.weightx = 0;
		c.weighty = 0.7;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		mySlider = new JSlider(Designer.MIN_DESIGN_VALUE, Designer.MAX_DESIGN_VALUE, Designer.NUM_DESIGNS/2);
		mySlider.setOpaque(false);
		this.add(mySlider, c);
		// set up listener to always keep value panels in correct aspect ratio
		valueContainer.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Insets insets = valueContainer.getInsets();
    			int size = Math.min(valueContainer.getWidth() - insets.left - insets.right, valueContainer.getHeight() - insets.top - insets.bottom);
				for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
        			valuePanels[i].setPreferredSize(new Dimension(size - 5, size - 5)); // subtract 5 pixels due to flickering effects
				}
				partnerSlider.setBorder(BorderFactory.createEmptyBorder(
						(valueContainer.getHeight() - size)/2 + size/(2*(Designer.NUM_DESIGNS+1)), 0, 
						(valueContainer.getHeight() - size)/2 + 3*size/(2*(Designer.NUM_DESIGNS+1)), 0));
				mySlider.setBorder(BorderFactory.createEmptyBorder(
						0, (valueContainer.getWidth() - size)/2 + 3*size/(2*(Designer.NUM_DESIGNS+1)), 
						0, (valueContainer.getWidth() - size)/2 + size/(2*(Designer.NUM_DESIGNS+1))));
				revalidate();
			}
		});
	}
	
	/**
	 * Update values.
	 */
	private void updateValues() {
		valuePanels[strategy].shiftStates(strategyToggle.isSelected());
		if(strategyToggle.isSelected()) {
			strategyToggle.setText("Disagree");
		} else {
			strategyToggle.setText("Agree");
		}
	}
	
	/**
	 * Reset UI.
	 *
	 * @param app the designer application
	 */
	private void resetUI(DesignerApp app) {
		mySlider.setValue(Designer.NUM_DESIGNS/2);
		partnerSlider.setValue(Designer.NUM_DESIGNS/2);
		strategyToggle.setText("Agree");
		strategyToggle.setSelected(false);
		valueContainer.removeAll();
		valueContainer.add(valuePanels[strategy]);
		valueContainer.validate();
		valueContainer.repaint();
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			int value = app.getValue(strategy, new int[2], i, new int[2]);
			valueLabels[i].setText(new Integer(value).toString());
		}
	}
	
	/**
	 * Bind to a designer application.
	 *
	 * @param app the designer application
	 */
	public void bindTo(DesignerApp app) {		
		mySlider.setValue(app.getController().getDesign(strategy));
		mySlider.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				app.getController().setStrategy(strategy);
			}
		});
		app.getController().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if(app.getManager().getTimeRemaining() < Manager.MAX_TASK_TIME 
						&& app.getController().getStrategy() == strategy) {
					Border empty_b = BorderFactory.createMatteBorder(1,1,1,1, Color.BLACK);
					empty_b = BorderFactory.createCompoundBorder(empty_b,BorderFactory.createEmptyBorder(4,4,4,4));
					Border black_b = BorderFactory.createMatteBorder(3,3,3,3, Color.BLACK);
					Border white_b = BorderFactory.createMatteBorder(2,2,2,2, Color.WHITE);
					Border bandw_b = BorderFactory.createCompoundBorder(black_b,white_b);
					empty_b = BorderFactory.createCompoundBorder(empty_b,bandw_b);
					setBorder(BorderFactory.createCompoundBorder(empty_b,black_b));
					setBackground(Color.decode("#ffcca2"));
				} else {
					setBorder(BorderFactory.createCompoundBorder(
							  BorderFactory.createMatteBorder(1,1,1,1, Color.BLACK),
							  BorderFactory.createEmptyBorder(12, 12, 12, 12)));
					setBackground(Color.WHITE);
				}
			}
		});
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(app.getManager().getTimeRemaining() < Manager.MAX_TASK_TIME 
						&& app.getManager().getTimeRemaining() > 0) {
					app.getController().setStrategy(strategy);
					mySlider.requestFocus();
				}
			}
		});
		mySlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(app.getManager().getTimeRemaining() > Manager.STRATEGY_TIME 
						&& app.getManager().isDesignEnabled()) {
					if(app.getController().getStrategy() != strategy) {
						app.getController().setStrategy(strategy);
					}
					app.getController().setDesign(strategy, mySlider.getValue());
				} else {
					mySlider.setValue(app.getController().getDesign(strategy));
				}
			}
		});
		partnerSlider.setValue(Designer.NUM_DESIGNS/2);
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			valuePanels[i].bindTo(app, strategy, i);
			int value = app.getValue(strategy, new int[2], i, new int[2]);
			valueLabels[i].setText(new Integer(value).toString());
		}
		for(Designer designer : app.getDesigners()) {
			designer.addObserver(new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					if(designer.getDesign(strategy) >= 0 && designer == app.getDesignPartner() && designer.isReadyToShare()) {
						partnerSlider.setValue(designer.getDesign(strategy));
						for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
							int value = app.getValue(strategy, app.getController().getDesigns(), i, designer.getDesigns());
							valueLabels[i].setText(new Integer(value).toString());
						}
					}
				}
			});
		}
		app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {				
				if(app.getManager().getTimeRemaining() == Manager.MAX_TASK_TIME) {
					resetUI(app);
				}
			}
		});
	}
}
