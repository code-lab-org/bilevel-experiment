/******************************************************************************
 * Copyright 2020 Stevens Institute of Technology, Collective Design Lab
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.stevens.code.bilevel.gui.designer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import edu.stevens.code.bilevel.model.Designer;

/**
 * Panel that displays color bar information.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 * @author Ambrosio Valencia-Romero <avalenci@stevens.edu>
 */
public class ColorBarPanel extends JPanel {
	private static final long serialVersionUID = -1241763890892204288L;

	/**
	 * Instantiates a new color bar panel.
	 */
	public ColorBarPanel() {
		this.setMinimumSize(new Dimension(52,100));
		this.setPreferredSize(new Dimension(52,100));
		this.setBorder(BorderFactory.createEmptyBorder(50,4,48,10));
	}
	
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
			if (value % 10 == 0) {
				String text = new Integer(value).toString();
				int textX = (int) (x + width + textPadding);
				int textY = (int) (y+0.5*height - fm.getStringBounds(text, g).getCenterY());
				g.drawString(text, textX, textY);
			}
		}
	}
}
