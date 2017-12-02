package edu.stevens.code.ptg.gui;

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

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;
import edu.stevens.code.ptg.Task;

/**
 * The Class DebugDesignerPanel.
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
			c.gridx++;
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
			c.fill = GridBagConstraints.NONE;
			c.gridy++;
			c.gridx = 0;
		}
		shareButton = new JToggleButton("Share");
		shareButton.setEnabled(false);
		this.add(shareButton, c);
	}
	
	/**
	 * Sets the always share.
	 *
	 * @param alwaysShare the new always share
	 */
	public void setAlwaysShare(boolean alwaysShare) {
		this.alwaysShare = alwaysShare;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			strategyRadios[i].setEnabled(enabled);
			designSliders[i].setEnabled(enabled);
		}
		shareButton.setEnabled(enabled);
	}
	
	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.gui.DesignerPanel#observe(edu.stevens.code.ptg.Designer)
	 */
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

	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.gui.DesignerPanel#bindTo(edu.stevens.code.ptg.Designer)
	 */
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

	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.gui.DesignerPanel#bindTo(edu.stevens.code.ptg.DesignerApp)
	 */
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
	
	private void updateScores(DesignerApp app, Designer designer) {
		Designer controller = app.getController();
		Designer partner = app.getDesignPartner();
		
		// update partner designs if ready to share
		if(alwaysShare || (designer == partner && designer.isReadyToShare())) {
			partnerDesigns = designer.getDesigns();
		}
		
		// update if the designer is either controller or partner
		if(designer == controller || designer == partner) {
			Task task = app.getManager().getTaskByDesignerId(controller.getId());
			for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
				for(int j = 0; j < Designer.NUM_STRATEGIES; j++) {
					if(task.getDesignerId(0) == controller.getId()) {
						scoreLabels[i][j].setText(new Integer(
								task.getValueMap().getValues(
										i, 
										j, 
										controller.getDesign(i), 
										partnerDesigns[j]
								)[0]).toString());
					} else {
						scoreLabels[i][j].setText(new Integer(
								task.getValueMap().getValues(
										j, 
										i, 
										partnerDesigns[j], 
										controller.getDesign(i)
								)[1]).toString());
					}
				}
			}
		}
	}
}
