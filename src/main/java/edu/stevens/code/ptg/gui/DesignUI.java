package edu.stevens.code.ptg.gui;

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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;
import edu.stevens.code.ptg.Manager;

public class DesignUI extends JPanel {
	private static final long serialVersionUID = -4318471579781451005L;
	
	private int strategy;
	private JLabel[] valueLabels = new JLabel[Designer.NUM_STRATEGIES];
	private JToggleButton strategyToggle;
	private JPanel valueContainer;
	protected ValuePanel[] valuePanels = new ValuePanel[Designer.NUM_STRATEGIES];
	private JSlider mySlider;
	private JSlider partnerSlider;
	
	public DesignUI(int strategy) {
		if(strategy < 0 || strategy >= Designer.NUM_STRATEGIES) {
			throw new IllegalArgumentException("invalid strategy index");
		}
		this.strategy = strategy;
		this.setBackground(DesignerUI.STRATEGY_COLORS[strategy]);
		this.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
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
//		this.add(scorePanel, c);
		
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
//		add(comboPanel, c);
		
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
			@Override
			public void actionPerformed(ActionEvent e) {
				strategyToggle.setSelected(true);
				updateValues();
			}
		});
		this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released " + key), "unshiftStates");
		this.getActionMap().put("unshiftStates", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				strategyToggle.setSelected(false);
				updateValues();
			}
		});
		valueContainer = new JPanel(new GridBagLayout()); // GridBag needed to align sliders
		valueContainer.setOpaque(false);
		valueContainer.add(valuePanels[strategy]);
		this.add(valueContainer, c);
		c.gridx+=2;
		c.weightx = 0.26;
		c.weighty = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.VERTICAL;
		this.add(new ColorBarPanel(), c);
		
		c.gridx = 1;
		c.gridy++;
		c.weightx = 0;
		c.weighty = 0.7;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		mySlider = new JSlider(Designer.MIN_DESIGN_VALUE, Designer.MAX_DESIGN_VALUE, Designer.NUM_DESIGNS/2);
		mySlider.setEnabled(false);
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
	
	private void updateValues() {
		//valueContainer.removeAll();
		valuePanels[strategy].shiftStates(strategyToggle.isSelected());
		if(strategyToggle.isSelected()) {
			strategyToggle.setText("Disagree");
			//valueContainer.add(valuePanels[1-strategy], BorderLayout.CENTER);
		} else {
			strategyToggle.setText("Agree");
			//valueContainer.add(valuePanels[strategy], BorderLayout.CENTER);
		}
		//valueContainer.validate();
		//valueContainer.repaint();
	}
	
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
	
	private void setSliderState(int timeRemaining, boolean isDesignEnabled) {
		if(timeRemaining <= Manager.STRATEGY_TIME) {
			mySlider.setEnabled(false);
		} else if(timeRemaining > 0) {
			mySlider.setEnabled(isDesignEnabled);
		}
	}
	
	public void bindTo(DesignerApp app) {		
		mySlider.setValue(app.getController().getDesign(strategy));
		app.getController().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if(app.getManager().getTimeRemaining() < Manager.MAX_TASK_TIME 
						&& app.getController().getStrategy() == strategy) {
					setBorder(BorderFactory.createMatteBorder(8, 8, 8, 8, Color.BLACK));
				} else {
					setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
				}
			}
		});
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(app.getManager().getTimeRemaining() < Manager.MAX_TASK_TIME 
						&& app.getManager().getTimeRemaining() > 0) {
					app.getController().setStrategy(strategy);
				}
			}
		});
		mySlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(app.getController().getStrategy() != strategy) {
					app.getController().setStrategy(strategy);
				}
				app.getController().setDesign(strategy, mySlider.getValue());
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
		setSliderState(app.getManager().getTimeRemaining(), app.getManager().isDesignEnabled());
		app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				setSliderState(app.getManager().getTimeRemaining(), app.getManager().isDesignEnabled());
				
				if(app.getManager().getTimeRemaining() == Manager.MAX_TASK_TIME) {
					resetUI(app);
				}
			}
		});
	}
}
