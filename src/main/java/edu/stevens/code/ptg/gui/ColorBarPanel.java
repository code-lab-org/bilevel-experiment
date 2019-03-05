package edu.stevens.code.ptg.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import edu.stevens.code.ptg.Designer;

public class ColorBarPanel extends JPanel {
	private static final long serialVersionUID = -1241763890892204288L;

	/**
	 * Instantiates a new color bar panel.
	 */
	public ColorBarPanel() {
		this.setMinimumSize(new Dimension(50,100));
		this.setPreferredSize(new Dimension(50,100));
		this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		Insets insets = this.getInsets();
		
		int textPadding = 5;
		FontMetrics fm = getFontMetrics(getFont());
		int maxTextWidth = (int) fm.getStringBounds("100", g).getWidth();
		int width = this.getWidth() - insets.left - insets.right - textPadding - maxTextWidth;
		int height = (this.getHeight() - insets.top - insets.bottom )/(100/Designer.VALUE_DELTA + 1);
		
		for(int value = 0; value <= 100; value+=Designer.VALUE_DELTA) {
			if(value >= 0 && value <= 100) {
				g.setColor(DesignerUI.VALUE_COLORS[value/Designer.VALUE_DELTA]);
			} else {
				g.setColor(Color.BLACK);
			}
			int x = insets.left;
			int y = insets.top + ((100-value)/Designer.VALUE_DELTA)*height;
			g.fillRect(x, y, width, height);

			g.setColor(Color.BLACK);
			String text = new Integer(value).toString();
			int textX = (int) (x + width + textPadding);
			int textY = (int) (y+0.5*height - fm.getStringBounds(text, g).getCenterY());
			g.drawString(text, textX, textY);
		}
	}
}
