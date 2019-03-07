package edu.stevens.code.ptg.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;
import edu.stevens.code.ptg.Manager;

public class ValueLabel extends JLabel {
	private static final long serialVersionUID = -125874855243548180L;
	
	private DesignerApp app;
	private int myStrategy, partnerStrategy;
	private int[] myDesigns = new int[] {Designer.NUM_DESIGNS/2, Designer.NUM_DESIGNS/2};
	private int[] partnerDesigns = new int[] {Designer.NUM_DESIGNS/2, Designer.NUM_DESIGNS/2};
	
	public ValueLabel() {
		this.setPreferredSize(new Dimension(200,200));
		this.setOpaque(true);
		this.setVerticalAlignment(JLabel.CENTER);
		this.setHorizontalAlignment(JLabel.CENTER);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				setFont(getFont().deriveFont(Math.max(Math.min(Math.min(getWidth()/4f,getHeight()/2f), 48), 12)));
			}
		});
	}
	
	public void bindTo(DesignerApp app, int myStrategy, int partnerStrategy) {
		this.app = app;
		this.app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if(app.getManager().getTimeRemaining() == Manager.MAX_TASK_TIME) {
					partnerDesigns[0] = Designer.NUM_DESIGNS/2;
					partnerDesigns[1] = Designer.NUM_DESIGNS/2;
				}
				updateLabel();
			}
		});
		for(Designer designer : app.getDesigners()) {
			designer.addObserver(new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					if(designer == app.getDesignPartner() && designer.isReadyToShare()) {
						if(!Arrays.equals(partnerDesigns, designer.getDesigns())) {
							partnerDesigns[0] = designer.getDesign(0);
							partnerDesigns[1] = designer.getDesign(1);
							updateLabel();
						}
					} else if(designer == app.getController()) {
						if(!Arrays.equals(myDesigns, app.getController().getDesigns())) {
							myDesigns[0] = app.getController().getDesign(0);
							myDesigns[1] = app.getController().getDesign(1);
							updateLabel();
						}
					}
				}
			});
		}
		this.myStrategy = myStrategy;
		this.partnerStrategy = partnerStrategy;
	}
	
	protected void updateLabel() {
		int value = app.getValue(myStrategy, myDesigns, partnerStrategy, partnerDesigns);
		if(app.getManager().isDesignEnabled() && value >= 0 && value <= 100) {
			this.setBackground(DesignerUI.VALUE_COLORS[ (int) Math.round(value) ]);
//			if(Designer.VALUE_DELTA == 5) {
//				this.setBackground(DesignerUI.VALUE_COLORS_21[ (int) Math.round(value/Designer.VALUE_DELTA) ]);
//			} else if(Designer.VALUE_DELTA == 2) {
//				this.setBackground(DesignerUI.VALUE_COLORS_51[ (int) Math.round(value/Designer.VALUE_DELTA) ]);
//			}
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
