package edu.stevens.code.eager.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Arrays;

public class DesignPainting extends PaintingMethods {
	
	/** Graphics2D object */
	private Graphics2D g2D;
	
	
	public DesignPainting(Graphics2D g2D_object, String game_file, int strategy) throws IOException {
		
		this.g2D = g2D_object;
				
		setDesignSpace(game_file);
		
		drawColorbar(g2D_object);
		drawBorder(g2D_object, strategy);
		addAxis(g2D_object);
		
	}
		
	/** The (xi,xj) axis, 0 <= xi,xj <= 9 */
	public static void addAxis(Graphics2D g2D_object){
		
//		drawBorder(g2D_object);
		
		g2D_object.setFont(MONO3);
		int fs = MONO3.getSize();
		
		g2D_object.setColor(Color.WHITE);
		
		FontMetrics fm = g2D_object.getFontMetrics(g2D_object.getFont());
		int fh = 40 + (5*fs/2)/7;
		int fw = fm.stringWidth("0");
		
		for (int x = 0; x < 10; x++){
			String xi = String.valueOf(x);
			
			g2D_object.drawString( xi,  600-fw/2+80*x + X_SHIFT, fh +  70-Y_SHIFT);			
			g2D_object.drawString( xi,  600-fw/2+80*x + X_SHIFT, fh + 930-Y_SHIFT);
			
			g2D_object.drawString( xi,  530-fw/2 + X_SHIFT - 6, fh + 860-Y_SHIFT-80*x);
			g2D_object.drawString( xi, 1390-fw/2 + X_SHIFT + 6, fh + 860-Y_SHIFT-80*x);
			
		}
	}
	
	/** Public method to draw the borders of the design spaces */ 
	public static void drawBorder(Graphics2D g2D_object, int strategy){
		
		int t = 12;		
		
		/** Border for Mia's strategy boxes */
		if (strategy == 0) { g2D_object.setColor(R2); }
		else if (strategy == 1) { g2D_object.setColor(B2); }		
		g2D_object.fillRect( 500 + X_SHIFT, 140-Y_SHIFT, 920, 800);
		
		
		/** Border for current player's strategy boxes */
		if (strategy == 0) { g2D_object.setColor(Color.RED); }
		else if (strategy == 1) { g2D_object.setColor(Color.BLUE); }
		g2D_object.fillRect(560-t + X_SHIFT, 80-Y_SHIFT, 800+2*t, 920);
		
	}
	
	/** Draws each cell on each of the quadrants of the design space */
	protected void drawCell(int xi, int xj, int payoff){
		
		int dx;
		int dy;
		
		dx = 560 + X_SHIFT; dy = 860 - Y_SHIFT;
		
		g2D.setColor(payoffColor(payoff));
		g2D.fillRect((80*xi)+dx, dy-(80*xj), 80, 80);
		
	}
	
	/** Draws design space */
	public void drawDesign(int strategy){
		
		/* Draw Cells */		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				
				if (strategy == 0) {
					drawCell(i,j, getAA(i,j) );
				} else if (strategy == 1) {
					drawCell(i,j, getBB(i,j) );
				}
				
			}
		}
		
	}
	
	/** Rulers */
	public void drawRulers(int x_Si, int x_Sj){
		
		int[] arr = {x_Si,x_Si};
		System.out.println(Arrays.toString(arr));
		
		/* Draw ruler, thickness = t */
		int t = 12;
		g2D.setStroke(new BasicStroke(t));
		g2D.setColor(new Color(147, 93, 116));
		
		/* Rulers moving along horizontal axis (vertical box) */
		g2D.drawRect(560+X_SHIFT+(80*x_Si)-t/2,  80-Y_SHIFT-3*t/2,            80+t, 920+3*t); /* i */
		g2D.drawRect(540+X_SHIFT-40-3*t/2,      860-Y_SHIFT-(80*x_Sj)-t/2, 920+3*t,    80+t); /* j */
		
//		/* Draw horizontal triangular ruler marks, strategy A */
//		int[] xAiPoints = {540+X_SHIFT+(40*x_Ai)-28+20, 540+X_SHIFT+(40*x_Ai)+20-t/2, 540+X_SHIFT+(40*x_Ai)+20-t/2};
//		int[] yAiPoints = {1048-Y_SHIFT+4*t, 1048-Y_SHIFT+4*t, 1000-Y_SHIFT+4*t};
//		g2D.setColor(Color.RED); g2D.fillPolygon(xAiPoints, yAiPoints, 3);
//		g2D.setColor(R2); g2D.drawPolygon(xAiPoints, yAiPoints, 3);
//		
//		/* Draw horizontal triangular ruler marks, strategy B */
//		int[] xBiPoints = {540+X_SHIFT+(40*x_Bi)+20+t/2, 540+X_SHIFT+(40*x_Bi)+28+20, 540+X_SHIFT+(40*x_Bi)+20+t/2};
//		int[] yBiPoints = {1048-Y_SHIFT+4*t, 1048-Y_SHIFT+4*t, 1000-Y_SHIFT+4*t};
//		g2D.setColor(Color.BLUE); g2D.fillPolygon(xBiPoints, yBiPoints, 3);
//		g2D.setColor(B2); g2D.drawPolygon(xBiPoints, yBiPoints, 3);
		
	}
	
	

}
