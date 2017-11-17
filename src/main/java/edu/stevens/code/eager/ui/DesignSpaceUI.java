package edu.stevens.code.eager.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.IOException;

import edu.stevens.code.eager.designer.DesignerUI;


public class DesignSpaceUI extends UI {
	
	/* Graphics2D object */
	private Graphics2D g2D;
		
	/* Variables xi and xj, setters, and getters */
	private int xi;
	public int getXi() { return xi; }
	public void setXi(int xi) { this.xi = xi; }

	private int xj;
	public int getXj() { return xj; }
	public void setXj(int xj) { this.xj = xj; }
		
	/* Payoff */
	private int payoff;
	public int getPayoff() { return payoff; }
	public void setPayoff(int new_payoff) { payoff = new_payoff; }
	
	/* Payoff at A */
	private int[] pA = new int[2];
	public int[] getPA() { return pA; }
	public void setPA(int[] new_pA) { pA = new_pA; }
		
	/* Payoff at B*/
	private int[] pB = new int[2];
	public int[] getPB() { return pB; }
	public void setPB(int[] new_pB) { pB = new_pB; }
		
	/* Strategy */
	private String strategy = "NN";
	public String getStrategy() { return strategy; }
	public void setStrategy(String new_strategy) { strategy = new_strategy; }
	
	
	/* Default constructor. */
	public DesignSpaceUI(Graphics2D g2D_object, String game_file) throws IOException {
		
		/** Remark:
		 * ------------------------------------------------------------------------------
		 * Add matrix/array input to default constructor
		 * The matrix/array should of size 10x10
		 * Each element of this matrix is a payoff pk = 0:5:100 (21 data points)
		 * Use each value to query a color from a list or 1-dimensional array of Strings
		 */
				
		this.g2D = g2D_object;
		
		g2D_object.setColor(Color.WHITE);
		
		g2D_object.fillRect(DesignerUI.X_RANGE, 0, 1920-DesignerUI.X_RANGE/* width = 610 */, 1080);
		
		setDesignSpace(game_file);
		
		drawColorbar(g2D_object);
		drawBorder(g2D_object);
		addAxis(g2D_object);
		drawNormalForm(g2D_object);
        
	}
	
	/* Public method to draw the borders of the design spaces */ 
	public static void drawBorder(Graphics2D g2D_object){
		
		int t = 6;
		g2D_object.setStroke(new BasicStroke(t));
		
		/* Border for Mia's strategy boxes */
		g2D_object.setColor(R2);
		g2D_object.fillRect( 500 + X, 120-Y,  40, 400);
		g2D_object.fillRect(1380 + X, 120-Y, 40, 400);
		g2D_object.setColor(B2);
		g2D_object.fillRect( 500 + X, 560-Y,  40, 400);
		g2D_object.fillRect(1380 + X, 560-Y, 40, 400);
		
		/* Border for current player's strategy boxes */
		g2D_object.setColor(Color.RED);
		g2D_object.fillRect(540 + X,  80-Y, 400, 40);
		g2D_object.fillRect(540 + X, 960-Y, 400, 40);
		g2D_object.drawRect(540+t/2 + X,  80-Y+t/2, 400, 440);
		g2D_object.drawRect(540+t/2 + X, 560-Y-t/2, 400, 440);
		g2D_object.setColor(Color.BLUE);
		g2D_object.fillRect(980 + X,  80-Y, 400, 40);
		g2D_object.fillRect(980 + X, 960-Y, 400, 40);
		g2D_object.drawRect(980-t/2 + X,  80-Y+t/2, 400, 440);
		g2D_object.drawRect(980-t/2 + X, 560-Y-t/2, 400, 440);
		
	}
	
	/* Draw strategy space (normal form space)  */
	public static void drawNormalForm(Graphics2D g2D_object){
		
		FontMetrics fm = g2D_object.getFontMetrics(MONO3);
		int fw = fm.stringWidth("000");
		int w = 140;
//		int h =  60;
		
		g2D_object.setColor(Color.RED);
		g2D_object.fillRect(1560-w/2+fw/2,     302+w, w, w);
		g2D_object.setColor(R2);
		g2D_object.fillRect(1560-w/2+fw/2 - w, 302-w, w, w);
		g2D_object.setColor(Color.BLUE);
		g2D_object.fillRect(1560-w/2+fw/2 + w, 302+w, w, w);
		g2D_object.setColor(B2);
		g2D_object.fillRect(1560-w/2+fw/2 - w, 302,   w, w);
		
	}
	
	
	/* The (xi,xj) axis, 0 <= xi,xj <= 9 */
	public static void addAxis(Graphics2D g2D_object){
		
		g2D_object.setFont(MONO2);
		int fs = MONO2.getSize();
		
		g2D_object.setColor(Color.WHITE);
		
		FontMetrics fm = g2D_object.getFontMetrics(g2D_object.getFont());
		int fh = 20 + (5*fs/2)/7;
		int fw = fm.stringWidth("0");
		
		for (int x = 0; x < 10; x++){
			String xi = String.valueOf(x);
			
			g2D_object.drawString( xi,  560-fw/2+40*x + X, fh +  80-Y);
			g2D_object.drawString( xi, 1000-fw/2+40*x + X, fh +  80-Y);
			
			g2D_object.drawString( xi,  560-fw/2+40*x + X, fh + 960-Y);
			g2D_object.drawString( xi, 1000-fw/2+40*x + X, fh + 960-Y);
			
			g2D_object.drawString( xi,  520-fw/2 + X, fh + 480-Y-40*x);
			g2D_object.drawString( xi,  520-fw/2 + X, fh + 920-Y-40*x);
			
			g2D_object.drawString( xi, 1400-fw/2 + X, fh + 480-Y-40*x);
			g2D_object.drawString( xi, 1400-fw/2 + X, fh + 920-Y-40*x);
			
		}
		
		
	}	
	
	/* Draws each cell on each of the quadrants of the design space */
	protected void drawCell(String strategy, int xi, int xj, int payoff){
		
		int dx;
		int dy;
		
		switch (strategy){
			case "AA": dx = 540 + X; dy = 480 - Y; break;
			case "AB": dx = 540 + X; dy = 920 - Y; break;
			case "BA": dx = 980 + X; dy = 480 - Y; break;
			case "BB": dx = 980 + X; dy = 920 - Y; break;
			
			default:
				throw new IllegalArgumentException("Invalid strategy: " + strategy);
		}
		
		g2D.setColor(payoffColor(payoff));
		g2D.fillRect((40*xi)+dx, dy-(40*xj), 40, 40);
		
	}
	
	/* Draws design space */
	public void drawDesignSpace(){
		
		/* Draw Cells */		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				drawCell("AA",i,j, getAA(i,j) );
				drawCell("AB",i,j, getAB(i,j) );
				drawCell("BA",i,j, getBA(i,j) );
				drawCell("BB",i,j, getBB(i,j) );
			}
		}
		
	}
	
	/* Query payoff of selected cell */
	public void queryPayoff(int x_Si, int x_Sj){
		
		String SS = "";
		
		if      (x_Si > -1 && x_Si < 10) { SS = SS+"A";}
		else if (x_Si > 10 && x_Si < 21) { SS = SS+"B"; x_Si = x_Si - 11;}
		else { SS = SS+"N"; x_Si = 10; }
				
		if      (x_Sj > -1 && x_Sj < 10) { SS = SS+"B";}
		else if (x_Sj > 10 && x_Sj < 21) { SS = SS+"A"; x_Sj = x_Sj - 11;}
		else { SS = SS+"N"; x_Sj = 10; }
		
		switch (SS){
			case "AA": setPayoff( getAA(x_Si,x_Sj) ); break;
			case "AB": setPayoff( getAB(x_Si,x_Sj) ); break;
			case "BA": setPayoff( getBA(x_Si,x_Sj) ); break;
			case "BB": setPayoff( getBB(x_Si,x_Sj) ); break;
			
			default:
				SS = "NN"; setPayoff(-5); break;
		}
		
		setXi(x_Si); setXj(x_Sj);
		setStrategy(SS);
		
	}
	
	/* Compute normal form (payoff structure) */
	public void updateNormalForm(int x_Ai, int x_Aj, int x_Bi, int x_Bj, boolean is_sharing){
		
		int[] p_A = new int[2];
		int[] p_B = new int[2];
		
		if (x_Ai >= 0 && x_Ai < 10 && x_Bi >= 0 && x_Bi < 10 && 
			x_Aj >= 0 && x_Aj < 10 && x_Bj >= 0 && x_Bj < 10 && is_sharing == true) {
		
			p_A[0] = getAA(x_Ai,x_Aj);
			p_A[1] = getAB(x_Ai,x_Bj);
			p_B[1] = getBA(x_Bi,x_Aj);
			p_B[0] = getBB(x_Bi,x_Bj);
		
			displayCellValue("AA",x_Ai,x_Aj);
			displayCellValue("AB",x_Ai,x_Bj);
			displayCellValue("BA",x_Bi,x_Aj);
			displayCellValue("BB",x_Bi,x_Bj);
		
		}
		
		setPA(p_A); setPB(p_B);
		
	}
	
	/* Ruler Ai */
	public void drawRulersXi(int x_Ai, int x_Bi){
		
		x_Bi = x_Bi + 11;
		
		/* Draw ruler, thickness = t */
		int t = 6;
		g2D.setStroke(new BasicStroke(t));
		g2D.setColor(new Color(147, 93, 116));
		
		/* Rulers moving along horizontal axis (vertical box) */
		g2D.drawRect(540+X+(40*x_Ai)-t/2, 80-Y-3*t/2, 40+t, 920+3*t); /* A */
		g2D.drawRect(540+X+(40*x_Bi)-t/2, 80-Y-3*t/2, 40+t, 920+3*t); /* B */
		
		/* Draw horizontal triangular ruler marks, strategy A */
		int[] xAiPoints = {540+X+(40*x_Ai)-28+20, 540+X+(40*x_Ai)+20-t/2, 540+X+(40*x_Ai)+20-t/2};
		int[] yAiPoints = {1048-Y+4*t, 1048-Y+4*t, 1000-Y+4*t};
		g2D.setColor(Color.RED); g2D.fillPolygon(xAiPoints, yAiPoints, 3);
		g2D.setColor(R2); g2D.drawPolygon(xAiPoints, yAiPoints, 3);
		
		/* Draw horizontal triangular ruler marks, strategy B */
		int[] xBiPoints = {540+X+(40*x_Bi)+20+t/2, 540+X+(40*x_Bi)+28+20, 540+X+(40*x_Bi)+20+t/2};
		int[] yBiPoints = {1048-Y+4*t, 1048-Y+4*t, 1000-Y+4*t};
		g2D.setColor(Color.BLUE); g2D.fillPolygon(xBiPoints, yBiPoints, 3);
		g2D.setColor(B2); g2D.drawPolygon(xBiPoints, yBiPoints, 3);
		
	}
	
	/* Rulers Xj */
	public void drawRulersXj(int x_Aj, int x_Bj, boolean is_sharing){
		
		if (is_sharing == true) {
		
			x_Aj = x_Aj + 11;
			
			/* Draw ruler, thickness = t */
			int t = 6;
			g2D.setStroke(new BasicStroke(t));
			g2D.setColor(new Color(147, 93, 116));
			
			/* Rulers moving along horizontal axis (vertical box) */
			g2D.drawRect(540+X-40-3*t/2,   920-Y-(40*x_Aj)-t/2, 920+3*t,    40+t); /* A */
			g2D.drawRect(540+X-40-3*t/2,   920-Y-(40*x_Bj)-t/2, 920+3*t,    40+t); /* B */
			
			/* Draw vertical triangular ruler mark at A */
			int[] xAjPoints = {452-4*t + X, 452-4*t + X,500-4*t + X};
			int[] yAjPoints = {920-Y-(40*x_Aj)-28+20,920-Y-(40*x_Aj)+20-t/2,920-Y-(40*x_Aj)+20-t/2};
			g2D.setColor(R1); g2D.fillPolygon(xAjPoints, yAjPoints, 3);
			g2D.setColor(R2); g2D.drawPolygon(xAjPoints, yAjPoints, 3);
			
			/* Draw vertical triangular ruler marks at B*/
			int[] xBjPoints = {452-4*t + X, 452-4*t + X,500-4*t + X};
			int[] yBjPoints = {920-Y-(40*x_Bj)+28+20,920-Y-(40*x_Bj)+20+t/2,920-Y-(40*x_Bj)+20+t/2};
			g2D.setColor(B1); g2D.fillPolygon(xBjPoints, yBjPoints, 3);
			g2D.setColor(B2); g2D.drawPolygon(xBjPoints, yBjPoints, 3);
		
		}
	}	
	
	/* Selected cell */
	public void selectedCell(int x_Si, int x_Sj){
		
		int t = 8;
		
		g2D.setFont(DesignSpaceUI.MONO2);
		g2D.setStroke(new BasicStroke(t));
		g2D.setColor(Color.MAGENTA);
		g2D.drawRect(540+X+(40*x_Si)-t/2,920-Y-(40*x_Sj)-t/2, 40+t, 40+t);
		
		/* Which strategy? Query payoff and update current strategy. */
		queryPayoff(x_Si, x_Sj);
		
		/* Test string on screen */
		g2D.setColor(Color.BLACK);
		String output = getStrategy()+"  ("+String.format("%2d",getXi())+","+
                		 					  String.format("%2d",getXj())+") "+
                		 					  String.format("%3d",getPayoff());
		
		g2D.drawString( output, 1580, 40);
				
		/* Draw triangular colorbar mark */
		t = 6;
		int ci = getPayoff()/5;
		int[] xcPoints = {1540+t + X,1588+t + X,1588+t + X};
		int[] ycPoints = {920-Y-(40*ci)+20,920-Y-(40*ci)-28+20,920-Y-(40*ci)+28+20};
		
		if (ci > -1) {
			g2D.setColor(payoffColor(getPayoff()));
		} else {
			g2D.setColor(Color.BLACK);
		}
		
		g2D.fillPolygon(xcPoints, ycPoints, 3);
		g2D.setStroke(new BasicStroke(t));
		g2D.setColor(Color.MAGENTA);
		g2D.drawPolygon(xcPoints, ycPoints, 3);
		
		displayCellValue(getStrategy(),getXi(),getXj());
				
		/* Display the current cell value on the colorbar */
		g2D.setFont(MONO2);
		int fs = MONO2.getSize();
		FontMetrics fm = g2D.getFontMetrics(MONO2);
		int fh = 20 + (5*fs/2)/7;
		String val = String.valueOf(getPayoff());
		int fw = fm.stringWidth(val);
		
		if (getPayoff() > 45){
			g2D.setColor(Color.BLACK);
			g2D.drawString( val, 1500-fw/2 + X, fh+920-Y-8*getPayoff());
		} else if (getPayoff() > -1){
			g2D.setColor(Color.WHITE);
			g2D.drawString( val, 1500-fw/2 + X, fh+920-Y-8*getPayoff());
		}
		
	}
	
	/* Display the current cell value on the design space */
	public void displayCellValue(String strategy, int x_i, int x_j) {
		
		int dx = 0;
		int dy = 0;
		int p = 0;
		
		switch (strategy){
			case "AA": dx = 540 + X; dy = 480 - Y; p = getAA(x_i,x_j); break;
			case "AB": dx = 540 + X; dy = 920 - Y; p = getAB(x_i,x_j); break;
			case "BA": dx = 980 + X; dy = 480 - Y; p = getBA(x_i,x_j); break;
			case "BB": dx = 980 + X; dy = 920 - Y; p = getBB(x_i,x_j); break;
		}
		
		g2D.setFont(MONO1);
		int fs = MONO1.getSize();
		FontMetrics fm = g2D.getFontMetrics(MONO1);
		int fh = 20 + (5*fs/2)/7;
		String val = String.valueOf(p);
		int fw = fm.stringWidth(val);
		
		g2D.setColor(payoffFontColor(p));
		g2D.drawString( val, dx+(40*x_i)-fw/2+20, dy+fh-(40*x_j));		
		
	}
	
	
	/* Display normal form of the game */
	public void displayNormalForm(boolean is_sharing){
		
		g2D.setFont(DesignSpaceUI.MONO3);
		
		FontMetrics fm = g2D.getFontMetrics(MONO3);
		int fw = fm.stringWidth("000");
		int w = 140;
//		int h =  60;
		
		if (is_sharing == true) {
			
			g2D.setColor(payoffColor(getPA()[0]));
			g2D.fillRect(1560-w/2+fw/2,     302-w, w, w);
			g2D.setColor(payoffFontColor(getPA()[0]));
			g2D.drawString( String.format("%3d",getPA()[0]), 1560, 290); /* AA */
			
			g2D.setColor(payoffColor(getPA()[1]));
			g2D.fillRect(1560-w/2+fw/2,     302,   w, w);
			g2D.setColor(payoffFontColor(getPA()[1]));
			g2D.drawString( String.format("%3d",getPA()[1]), 1560, 350); /* AB */
	
			g2D.setColor(payoffColor(getPB()[1]));
			g2D.fillRect(1560-w/2+fw/2 + w, 302-w, w, w);
			g2D.setColor(payoffFontColor(getPB()[1]));
			g2D.drawString( String.format("%3d",getPB()[1]), 1700, 290); /* BA */
			
			g2D.setColor(payoffColor(getPB()[0]));
			g2D.fillRect(1560-w/2+fw/2 + w, 302,   w, w);
			g2D.setColor(payoffFontColor(getPB()[0]));
			g2D.drawString( String.format("%3d",getPB()[0]), 1700, 350); /* BB */
		
		}
		
	}

}
