package edu.stevens.code.ptg.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;
import edu.stevens.code.ptg.Manager;

public class ValueLabel extends JLabel {
	private static final long serialVersionUID = -125874855243548180L;
	
	protected DesignerApp app;
	protected int myStrategy, partnerStrategy;
	protected int myDesign, partnerDesign;
	
	public ValueLabel() {
		this.setPreferredSize(new Dimension(200,200));
		this.setOpaque(true);
		this.setVerticalAlignment(JLabel.CENTER);
		this.setHorizontalAlignment(JLabel.CENTER);
	}
	
	public void bindTo(DesignerApp app, int myStrategy, int partnerStrategy) {
		this.app = app;
		this.app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if(app.getManager().getTimeRemaining() == Manager.MAX_TASK_TIME) {
					partnerDesign = 0;
				}
				updateLabel();
			}
		});
		for(Designer designer : app.getDesigners()) {
			designer.addObserver(new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					if(designer == app.getDesignPartner() && designer.isReadyToShare()) {
						if(partnerDesign != designer.getDesign(partnerStrategy)) {
							partnerDesign = designer.getDesign(partnerStrategy);
							updateLabel();
						}
					} else if(designer == app.getController()) {
						if(myDesign != app.getController().getDesign(myStrategy)) {
							myDesign = app.getController().getDesign(myStrategy);
							updateLabel();
						}
					}
				}
			});
		}
		this.myStrategy = myStrategy;
		this.partnerStrategy = partnerStrategy;
	}
	
	protected int getValue() {
		return app.getValue(myStrategy, myDesign, partnerStrategy, partnerDesign);
	}
	
	protected void updateLabel() {
		int value = getValue();
		this.setFont(getFont().deriveFont(Math.max(Math.min(Math.min(getWidth()/4f,getHeight()/2f), 48), 12)));
		if(app.getManager().isDesignEnabled() && value >= 0 && value <= 100) {
			this.setBackground(DesignerUI.VALUE_COLORS[value/5]);
			this.setText(new Integer(value).toString());
			if (value > 45) {
				this.setForeground(Color.BLACK);
			} else {
				this.setForeground(Color.WHITE);
			}
		} else {
			this.setBackground(Color.BLACK);
			this.setText("");
		}
	}
}
