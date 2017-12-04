package edu.stevens.code.ptg.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
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
		
		int width = this.getWidth()/Designer.NUM_DESIGNS;
		int height = this.getHeight()/Designer.NUM_DESIGNS;
		
		for(int i = 0; i < Designer.NUM_DESIGNS; i++) {
			for(int j = 0; j < Designer.NUM_DESIGNS; j++) {
				int value = app.getValue(myStrategy, i, partnerStrategy, j);
				if(app.getManager().isDesignEnabled() && value >= 0 && value <= 100) {
					g.setColor(DesignerUI.VALUE_COLORS[value/5]);
				} else {
					g.setColor(Color.BLACK);
				}
				g.fillRect(i*width, (Designer.NUM_DESIGNS-j-1)*height, width, height);
			}
		}
		if(app.getManager().isDesignEnabled()) {
			g.setColor(Color.RED);
			g.drawRect(myDesign*width, (Designer.NUM_DESIGNS-partnerDesign-1)*height, width, height);
			int value = app.getValue(myStrategy, myDesign, partnerStrategy, partnerDesign);
			if (value > 45) {
				g.setColor(Color.BLACK);
			} else {
				g.setColor(Color.WHITE);
			}
			String text = new Integer(value).toString();
			FontMetrics fm = getFontMetrics(getFont());
			int x = (int) ((myDesign+0.5)*width - fm.getStringBounds(text, g).getWidth()/2);
			int y = (int) ((Designer.NUM_DESIGNS-partnerDesign-0.5)*height + fm.getStringBounds(text, g).getHeight()/2);
			g.drawString(new Integer(value).toString(), x, y);
		}
	}
}
