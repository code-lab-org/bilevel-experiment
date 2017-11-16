package edu.stevens.code.eager.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;

import edu.stevens.code.eager.designer.DesignerUI;

public class UI {
	
	protected static final int X = DesignerUI.X_SHIFT;
	protected static final int Y = DesignerUI.Y_SHIFT;
	
	public static final Font MONO1 = new Font("Consolas", Font.BOLD, 20);
	public static final Font MONO2 = new Font("Consolas", Font.BOLD, 28);
	public static final Font MONO3 = new Font("Consolas", Font.BOLD, 60);
	public static final Font SANS1 = new Font("Arial", Font.BOLD, 32);
	
	/* Set of colors for the ruler marks */
	protected static final Color R1 = new Color(255, 230, 230);
	protected static final Color R2 = new Color(255, 127, 127);
	protected static final Color B1 = new Color(230, 230, 255);
	protected static final Color B2 = new Color(127, 127, 255);
	
		
	/* The 21 colors from the Parula colormap */
	protected static ArrayList<String> colors = new ArrayList<String>(
			Arrays.asList("#352a87", "#353eaf", "#1b55d7", "#026ae1", "#0f77db",
						  "#1484d4", "#0d93d2", "#06a0cd", "#07aac1", "#18b1b2",
					      "#33b8a1",
					      "#55bd8e", "#7abf7c", "#9bbf6f", "#b8bd63", "#d3bb58",
					      "#ecb94c", "#ffc13a", "#fad12b", "#f5e31e", "#f9fb0e"));
	
		
	public static Color payoffColor(int payoff) {
		
		Color payoff_color = null;
		
		if (payoff > -1 && payoff < 101){
			payoff_color = Color.decode( colors.get(payoff/5) );
		} else {
			payoff_color = Color.BLACK;
		}
		
		return payoff_color;
		
	}	
	
		protected Color payoffFontColor(int payoff) {
		
		Color font_color = null;
		
		if (payoff > 45){
			font_color = Color.BLACK;
		} else if (payoff > -1){
			font_color = Color.WHITE;
		}		
		return font_color;
	}
		
		/* Public method to draw the Parula color bar */
		public static void drawColorbar(Graphics2D g2D_object){
			
			/* A cell for each color */
			for (int j = 0; j < 21; j++) {
				
				g2D_object.setColor(payoffColor( 5*(20-j) ));
				g2D_object.setStroke(new BasicStroke(0));
				g2D_object.fillRect(1460 + X, 120-Y+40*j, 80, 40);
			}
			
			/* The color scale */
			g2D_object.setFont(MONO2);
			int fs = MONO2.getSize();
			FontMetrics fm = g2D_object.getFontMetrics(MONO2);
			int fh = 20 + (5*fs/2)/7;
			
			for (int v = 0; v < 3; v++){
				g2D_object.setColor(Color.BLACK);
				String val = String.valueOf(100-v*20);
				int fw = fm.stringWidth(val);
				g2D_object.drawString( val, 1500-fw/2 + X, fh+120-Y+160*v);
			}
			
			for (int v = 3; v < 6; v++){
				g2D_object.setColor(Color.WHITE);
				String val = String.valueOf(100-v*20);
				int fw = fm.stringWidth(val);
				g2D_object.drawString( val, 1500-fw/2 + X, fh+120-Y+160*v);
			}
			
		}
	
}
