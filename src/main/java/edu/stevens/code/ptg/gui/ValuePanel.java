package edu.stevens.code.ptg.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;
import edu.stevens.code.ptg.Manager;

public class ValuePanel extends JPanel {
	private static final long serialVersionUID = -125874855243548180L;
	
	private DesignerApp app;
	private int myStrategy, partnerStrategy;
	private int myDesign, partnerDesign;
	
	public ValuePanel() {
		this.setOpaque(false);
	}
	
	public void bindTo(DesignerApp app, int myStrategy, int partnerStrategy) {
		this.app = app;
		this.app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if(app.getManager().getTimeRemaining() == Manager.MAX_TASK_TIME) {
					partnerDesign = 0;
				}
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
	
	public int getDesign(int x) {
		Insets insets = this.getInsets();
		int width = (this.getWidth() - insets.left - insets.right)/Designer.NUM_DESIGNS;
		return x / width;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2D = (Graphics2D) g;
		
		Insets insets = this.getInsets();
		int width = (this.getWidth() - insets.left - insets.right)/Designer.NUM_DESIGNS;
		int height = (this.getHeight() - insets.top - insets.bottom)/Designer.NUM_DESIGNS;
		
		this.setFont(getFont().deriveFont(Math.max(Math.min(Math.min(width/2f,height/1.0f), 48), 12)));
		
		for(int i = 0; i < Designer.NUM_DESIGNS; i++) {
			for(int j = 0; j < Designer.NUM_DESIGNS; j++) {
				int value = app.getValue(myStrategy, i, partnerStrategy, j);
				if(app.getManager().isDesignEnabled() && value >= 0 && value <= 100) {
					g2D.setColor(DesignerUI.VALUE_COLORS[value/5]);
				} else {
					g2D.setColor(Color.BLACK);
				}
				g2D.fillRect(insets.left + i*width, insets.top + (Designer.NUM_DESIGNS-j-1)*height, width, height);
			}
		}
		if(app.getManager().isDesignEnabled()) {
			g2D.setColor(new Color(147, 93, 116));
			g2D.setStroke(new BasicStroke(1));
			g2D.drawRect(insets.left + myDesign*width, insets.top + 0, width, Designer.NUM_DESIGNS*height);
			g2D.setStroke(new BasicStroke(1));
			g2D.drawRect(insets.left + 0, insets.top + (Designer.NUM_DESIGNS-partnerDesign-1)*height, Designer.NUM_DESIGNS*width, height);
			g2D.setColor(Color.MAGENTA);
			int t = 2+(width*height/(100*100));
			g2D.setStroke(new BasicStroke(t));
			g2D.drawRect(insets.left + myDesign*width, insets.top + (Designer.NUM_DESIGNS-partnerDesign-1)*height + t/2, width, height);
			int value = app.getValue(myStrategy, myDesign, partnerStrategy, partnerDesign);
			if (value > 45) {
				g2D.setColor(Color.BLACK);
			} else {
				g2D.setColor(Color.WHITE);
			}
			String text = new Integer(value).toString();
			FontMetrics fm = getFontMetrics(getFont());
			int x = (int) (insets.left + (myDesign+0.5)*width - fm.getStringBounds(text, g2D).getCenterX());
			int y = (int) (insets.top + (Designer.NUM_DESIGNS-partnerDesign-0.5)*height - fm.getStringBounds(text, g2D).getCenterY());
			g2D.drawString(text, x, y);
		}
	}
}
