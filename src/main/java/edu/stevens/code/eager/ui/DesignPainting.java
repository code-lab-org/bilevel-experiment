package edu.stevens.code.eager.ui;

import java.awt.Graphics2D;
import java.io.IOException;

public class DesignPainting extends PaintingMethods {
	
	/** Graphics2D object */
	private Graphics2D g2D;
	
	
	public DesignPainting(Graphics2D g2D_object, String game_file) throws IOException {
		
		this.g2D = g2D_object;
				
		setDesignSpace(game_file);
		
		drawColorbar(g2D_object);
		
	}
	
	/** Draws each cell on each of the quadrants of the design space */
	protected void drawCell(int xi, int xj, int payoff){
		
		int dx;
		int dy;
		
		dx = 560 + X; dy = 860 - Y;
		
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
	
	

}
