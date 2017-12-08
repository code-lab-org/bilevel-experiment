package edu.stevens.code.ptg.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;

public class ValuePanel extends JPanel {
	private static final long serialVersionUID = -125874855243548180L;
	
	private DesignerApp app;
	private int myStrategy, partnerStrategy;
	private int myDesign, partnerDesign;
	
	public ValuePanel() {
		this.setPreferredSize(new Dimension(200,200));
		this.setOpaque(false);
	}
	
	public void bindTo(DesignerApp app, int myStrategy, int partnerStrategy) {
		this.app = app;
		this.app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				repaint();
			}
		});
		for(Designer designer : app.getDesigners()) {
			designer.addObserver(new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					if(designer == app.getDesignPartner() && designer.isReadyToShare()) {
						if(partnerDesign != designer.getDesign(partnerStrategy)) {
							partnerDesign = designer.getDesign(partnerStrategy);
							repaint();
						}
					} else if(designer == app.getController()) {
						if(myDesign != app.getController().getDesign(myStrategy)) {
							myDesign = app.getController().getDesign(myStrategy);
							repaint();
						}
					}
				}
			});
		}
		this.myStrategy = myStrategy;
		this.partnerStrategy = partnerStrategy;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		Insets insets = this.getInsets();
		int width = (this.getWidth() - insets.left - insets.right)/Designer.NUM_DESIGNS;
		int height = (this.getHeight() - insets.top - insets.bottom)/Designer.NUM_DESIGNS;
		
		this.setFont(getFont().deriveFont(Math.max(Math.min(Math.min(width/2f,height/1.0f), 48), 12)));
		
		for(int i = 0; i < Designer.NUM_DESIGNS; i++) {
			for(int j = 0; j < Designer.NUM_DESIGNS; j++) {
				int value = app.getValue(myStrategy, i, partnerStrategy, j);
				if(app.getManager().isDesignEnabled() && value >= 0 && value <= 100) {
					g.setColor(DesignerUI.VALUE_COLORS[value/5]);
				} else {
					g.setColor(Color.BLACK);
				}
				g.fillRect(insets.left + i*width, insets.top + (Designer.NUM_DESIGNS-j-1)*height, width, height);
			}
		}
		if(app.getManager().isDesignEnabled()) {
			g.setColor(Color.RED);
			g.drawRect(insets.left + myDesign*width, insets.top + (Designer.NUM_DESIGNS-partnerDesign-1)*height, width, height);
			int value = app.getValue(myStrategy, myDesign, partnerStrategy, partnerDesign);
			if (value > 45) {
				g.setColor(Color.BLACK);
			} else {
				g.setColor(Color.WHITE);
			}
			String text = new Integer(value).toString();
			FontMetrics fm = getFontMetrics(getFont());
			int x = (int) (insets.left + (myDesign+0.5)*width - fm.getStringBounds(text, g).getWidth()/2);
			int y = (int) (insets.top + (Designer.NUM_DESIGNS-partnerDesign)*height - (height-fm.getStringBounds(text, g).getHeight()));
			g.drawString(text, x, y);
		}
	}
}
