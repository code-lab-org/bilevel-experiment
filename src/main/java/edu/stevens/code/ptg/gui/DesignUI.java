package edu.stevens.code.ptg.gui;

import java.awt.BorderLayout;
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
	private JLabel value;
	private ValueSpacePanel valuePanel;
	private JSlider mySlider;
	private JSlider partnerSlider;
	
	public DesignUI(int strategy) {
		this.strategy = strategy;
		
		this.setLayout(new BorderLayout());
		value = new JLabel("0", JLabel.CENTER);
		this.add(value, BorderLayout.NORTH);
		valuePanel = new ValueSpacePanel();
		this.add(valuePanel, BorderLayout.CENTER);
		partnerSlider = new JSlider(Designer.MIN_DESIGN_VALUE, Designer.MAX_DESIGN_VALUE);
		partnerSlider.setOrientation(JSlider.VERTICAL);
		partnerSlider.setEnabled(false);
		this.add(partnerSlider, BorderLayout.WEST);
		mySlider = new JSlider(Designer.MIN_DESIGN_VALUE, Designer.MAX_DESIGN_VALUE);
		mySlider.setEnabled(false);
		this.add(mySlider, BorderLayout.SOUTH);
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
					value.setText(new Integer(app.getValue(
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
