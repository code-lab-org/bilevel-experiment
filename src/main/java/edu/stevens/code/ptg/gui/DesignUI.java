package edu.stevens.code.ptg.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.ListCellRenderer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;
import edu.stevens.code.ptg.Manager;

public class DesignUI extends JPanel {
	private static final long serialVersionUID = -4318471579781451005L;
	
	private int strategy;
	private JLabel[] valueLabels = new JLabel[Designer.NUM_STRATEGIES];
	private JComboBox<String> strategyCombo;
	private JPanel valueContainer;
	private ValuePanel[] valuePanels = new ValuePanel[Designer.NUM_STRATEGIES];
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
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
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
		this.add(scorePanel, c);
		
		c.gridx = 2;
		c.gridwidth = 1;		
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		String[] labels = new String[Designer.NUM_STRATEGIES];
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			if(i == strategy) {
				labels[i] = "Agree";
			} else {

				labels[i] = "Disagree";
			}
		}
		strategyCombo = new JComboBox<String>(labels);
		strategyCombo.setRenderer(new ListCellRenderer<String>() {
			public Component getListCellRendererComponent(
					JList<? extends String> list, String value, int index, 
					boolean isSelected, boolean cellHasFocus) {
				JLabel label = new JLabel(value);
				label.setOpaque(true);
				if(index >= 0 && index < Designer.NUM_STRATEGIES) {
					label.setBackground(DesignerUI.STRATEGY_COLORS[index]);
				}
				return label;
			}
		});
		strategyCombo.setOpaque(true);
		strategyCombo.setBackground(DesignerUI.STRATEGY_COLORS[strategy]);
		strategyCombo.setSelectedIndex(strategy);
		strategyCombo.setFocusable(false);
		strategyCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(strategyCombo.getSelectedIndex() >= 0 
						&& strategyCombo.getSelectedIndex() < Designer.NUM_STRATEGIES) {
					strategyCombo.setBackground(DesignerUI.STRATEGY_COLORS[strategyCombo.getSelectedIndex()]);
					valueContainer.removeAll();
					valueContainer.add(valuePanels[strategyCombo.getSelectedIndex()], BorderLayout.CENTER);
					valueContainer.validate();
					valueContainer.repaint();
				}
			}
		});
		JPanel comboPanel = new JPanel(new FlowLayout());
		comboPanel.setOpaque(false);
		comboPanel.add(new JLabel("Partner:"));
		comboPanel.add(strategyCombo);
		add(comboPanel, c);
		
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy++;
		c.fill = GridBagConstraints.VERTICAL;
		partnerSlider = new JSlider(Designer.MIN_DESIGN_VALUE, Designer.MAX_DESIGN_VALUE);
		partnerSlider.setOrientation(JSlider.VERTICAL);
		partnerSlider.setEnabled(false);
		partnerSlider.setOpaque(false);
		this.add(partnerSlider, c);
		
		c.gridx++;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			valuePanels[i] = new ValuePanel();
		}
		valueContainer = new JPanel(new BorderLayout());
		valueContainer.setOpaque(false);
		valueContainer.add(valuePanels[strategy], BorderLayout.CENTER);
		this.add(valueContainer, c);
		
		c.gridy++;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		mySlider = new JSlider(Designer.MIN_DESIGN_VALUE, Designer.MAX_DESIGN_VALUE);
		mySlider.setEnabled(false);
		mySlider.setOpaque(false);
		this.add(mySlider, c);
		
		this.addComponentListener(new ResizeListener());
	}
	
	class ResizeListener extends ComponentAdapter {
		public void componentResized(ComponentEvent e) {
			partnerSlider.setBorder(BorderFactory.createEmptyBorder(
					valuePanels[strategy].getHeight()/20 - 6, 0, 
					valuePanels[strategy].getHeight()/20 - 6, 0));
			mySlider.setBorder(BorderFactory.createEmptyBorder(
					0, valuePanels[strategy].getWidth()/20 - 8, 
					0, valuePanels[strategy].getWidth()/20 - 0));
		}
	}
	
	private void resetUI(DesignerApp app) {
		mySlider.setValue(0);
		strategyCombo.setSelectedIndex(strategy);
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			int value = app.getValue(strategy, 0, strategy, 0);
			valueLabels[i].setText(new Integer(value).toString());
		}
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		strategyCombo.setEnabled(enabled);
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
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			valuePanels[i].bindTo(app, strategy, i);
			int value = app.getValue(strategy, 0, strategy, 0);
			valueLabels[i].setText(new Integer(value).toString());
		}
		for(Designer designer : app.getDesigners()) {
			designer.addObserver(new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					if(designer == app.getDesignPartner() && designer.isReadyToShare()) {
						partnerSlider.setValue(designer.getDesign(strategy));
					}
					for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
						int value = app.getValue(
							strategy, app.getController().getDesign(strategy), 
							i, partnerSlider.getValue()
						);
						valueLabels[i].setText(new Integer(value).toString());
					}
				}
			});
		}
		app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				setEnabled(app.getManager().isDesignEnabled());
				if(app.getManager().getTimeRemaining() == Manager.MAX_TASK_TIME) {
					resetUI(app);
				}
			}
		});
	}
}
