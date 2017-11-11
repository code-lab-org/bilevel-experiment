package edu.stevens.code.eager.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.stevens.code.eager.designer.DesignerUI;


public class DesignSpaceUI {
	
	private static final int X = DesignerUI.X_SHIFT;
	private static final int Y = DesignerUI.Y_SHIFT;
	
	public static final Font MONO1 = new Font("Consolas", Font.BOLD, 28);
	public static final Font MONO2 = new Font("Consolas", Font.BOLD, 48);
	public static final Font SANS1 = new Font("Arial", Font.BOLD, 32);
	
	/* Set of colors for the ruler marks */
	private static final Color R1 = new Color(255, 230, 230);
	private static final Color R2 = new Color(255, 127, 127);
	private static final Color B1 = new Color(230, 230, 255);
	private static final Color B2 = new Color(127, 127, 255);
	
	/* Graphics2D object */
	private Graphics2D g2D;	
	
	
	/* Payoff structure (2D int arrays), getters and setters */
	
	private int[][] AA;
	public int getAA(int i, int j) { return AA[i][j]; }
	public void setAA(int[][] aA) { AA = aA; }
	
	private int[][] AB;
	public int getAB(int i, int j) { return AB[i][j]; }
	public void setAB(int[][] aB) { AB = aB; }
	
	private int[][] BA;
	public int getBA(int i, int j) { return BA[i][j]; }
	public void setBA(int[][] bA) { BA = bA; }
	
	private int[][] BB;
	public int getBB(int i, int j) { return BB[i][j]; }
	public void setBB(int[][] bB) { BB = bB; }

	
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

//        System.out.println(Arrays.deepToString(gameBA));
        
		
	}
	
	/* The 21 colors from the Parula colormap */
	protected static ArrayList<String> colors = new ArrayList<String>(
			Arrays.asList("#352a87", "#353eaf", "#1b55d7", "#026ae1", "#0f77db",
						  "#1484d4", "#0d93d2", "#06a0cd", "#07aac1", "#18b1b2",
					      "#33b8a1",
					      "#55bd8e", "#7abf7c", "#9bbf6f", "#b8bd63", "#d3bb58",
					      "#ecb94c", "#ffc13a", "#fad12b", "#f5e31e", "#f9fb0e"));
	
	
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
	
	/* Public method to draw the Parula color bar */
	public static void drawColorbar(Graphics2D g2D_object){
		
		/* A cell for each color */
		for (int j = 0; j < 21; j++) {
			g2D_object.setColor(Color.decode( colors.get( 20-j ) ));
			g2D_object.setStroke(new BasicStroke(0));
			g2D_object.fillRect(1460 + X, 120-Y+40*j, 80, 40);
		}
		
		/* The color scale */
		g2D_object.setFont(MONO1);
		int fs = MONO1.getSize();
		FontMetrics fm = g2D_object.getFontMetrics(g2D_object.getFont());
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
	
	
	/* The (xi,xj) axis, 0 <= xi,xj <= 9 */
	public static void addAxis(Graphics2D g2D_object){
		
		g2D_object.setFont(MONO1);
		int fs = MONO1.getSize();
		
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
	
	/* Set design space */
	protected void setDesignSpace(String game_file) throws NumberFormatException, IOException{
		/** This is how I call the "game_file" stored in package "games";
		 * To switch between games more easily, a drop-down list containing
		 * the names of all the game (.csv) files in "games" should be added,
		 * and picking a new game from that list should automatically
		 * update the design space of the interface.
		 */
		BufferedReader br = new BufferedReader(new FileReader(new File("src/test/java/games/"+game_file+".csv")));
        
		/** The next String "line" will get every line from the the game (.csv) file.
		 * Them, every value between commas in this line will be added to
		 * the Integer list "values".
		 */
        String line;
        List<Integer> values = new ArrayList<Integer> ();
        
        
        while((line = br.readLine())!= null){
        	/* Create a String array "r" from every line
        	 * by splitting it after every comma */     
        	String[] r = line.split(",");
        	
        	/* Now, take every element in "r"  and add it
        	 * as an Integer to the "values" list.
        	 */
        	for(int i = 0; i < r.length; i++){
        		int val = Integer.parseInt(r[i]);
        		values.add((int) val);
        	}
        }
        /* Close the game (.csv) file with safely */
        br.close();
		
        /* Now, create the 2D int arrays that will store the
         * values for every quadrant in the design space.
         */
		int[][] gameAA = new int[10][10];
		int[][] gameAB = new int[10][10];
		int[][] gameBA = new int[10][10];
		int[][] gameBB = new int[10][10];
		
		/* Take the entries in the "values" list and assign them to
		 * the appropriate 2D int array in the design space
		 */
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
            	gameAA[j][i] = values.get(    10*i+j);
            	gameAB[j][i] = values.get(100+10*i+j);
            	gameBA[j][i] = values.get(200+10*i+j);
            	gameBB[j][i] = values.get(300+10*i+j);
            }
        }
        
        /* Now, update the private 2D int arrays */
        setAA(gameAA);
        setAB(gameAB);
        setBA(gameBA);
        setBB(gameBB);
	}
	
	
	
	/* Draws each cell on each of the quadrants of the design space */
	protected void drawCell(String SS, int xi, int xj, int payoff){
		
		int dx;
		int dy;
		
		switch (SS){
			case "AA": dx = 540 + X; dy = 480 - Y; break;
			case "AB": dx = 540 + X; dy = 920 - Y; break;
			case "BA": dx = 980 + X; dy = 480 - Y; break;
			case "BB": dx = 980 + X; dy = 920 - Y; break;
			
			default:
				throw new IllegalArgumentException("Invalid strategy: " + SS);
		}
		
		g2D.setColor(Color.decode( colors.get(payoff/5) ));
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
	
	
	/* Compute payoff of selected cell */
	public void computePayoff(String SS, int xi, int xj){
		
		int[] p_A = new int[2];
		int[] p_B = new int[2];
		
		switch (SS){
			case "AA": p_A[0] = getAA(xi,xj); setPayoff(p_A[0]); break;
			case "AB": p_A[1] = getAB(xi,xj); setPayoff(p_A[1]); break;
			case "BA": p_B[1] = getBA(xi,xj); setPayoff(p_B[1]); break;
			case "BB": p_B[0] = getBB(xi,xj); setPayoff(p_B[0]); break;
			
			default:
				SS = "NN"; setPayoff(-5); break;
		}
		
		setPA(p_A); setPB(p_B);
				
	}
	
	/* Ruler Ai */
	public void drawRulerAi(int x_Ai){
		
		/* Draw ruler, thickness = t */
		int t = 6;
		g2D.setStroke(new BasicStroke(t));
		g2D.setColor(new Color(147, 93, 116));
		
		/* Ruler moving along horizontal axis (vertical box) */
		g2D.drawRect(540+X+(40*x_Ai)-t/2, 80-Y-3*t/2, 40+t, 920+3*t);
		
		/* Draw horizontal triangular ruler marks */
		int[] xiPoints = {540+X+(40*x_Ai)-28+20, 540+X+(40*x_Ai)+20-t/2, 540+X+(40*x_Ai)+20-t/2};
		int[] yiPoints = {1048-Y+4*t, 1048-Y+4*t, 1000-Y+4*t};
		g2D.setColor(Color.RED); g2D.fillPolygon(xiPoints, yiPoints, 3);
		g2D.setColor(R2); g2D.drawPolygon(xiPoints, yiPoints, 3);
	}
	
	/* Ruler Bi */
	public void drawRulerBi(int x_Bi){
		
		x_Bi = x_Bi + 11;
		
		/* Draw ruler, thickness = t */
		int t = 6;
		g2D.setStroke(new BasicStroke(t));
		g2D.setColor(new Color(147, 93, 116));
		
		/* Ruler moving along horizontal axis (vertical box) */
		g2D.drawRect(540+X+(40*x_Bi)-t/2, 80-Y-3*t/2, 40+t, 920+3*t);
		
		/* Draw horizontal triangular ruler marks */
		int[] xiPoints = {540+X+(40*x_Bi)+20+t/2, 540+X+(40*x_Bi)+28+20, 540+X+(40*x_Bi)+20+t/2};
		int[] yiPoints = {1048-Y+4*t, 1048-Y+4*t, 1000-Y+4*t};
		g2D.setColor(Color.BLUE); g2D.fillPolygon(xiPoints, yiPoints, 3);
		g2D.setColor(B2); g2D.drawPolygon(xiPoints, yiPoints, 3);
	}
	
	/* Rulers Xj */
	public void drawRulersXj(int x_Aj, int x_Bj, boolean is_sharing){
		
		if (is_sharing == true) {
		
			x_Aj = x_Aj + 11;
			
			/* Draw ruler, thickness = t */
			int t = 6;
			g2D.setStroke(new BasicStroke(t));
			g2D.setColor(new Color(147, 93, 116));
			
			/* Ruler moving along horizontal axis (vertical box) */
			g2D.drawRect(540+X-40-3*t/2,   920-Y-(40*x_Aj)-t/2, 920+3*t,    40+t);
			g2D.drawRect(540+X-40-3*t/2,   920-Y-(40*x_Bj)-t/2, 920+3*t,    40+t);
			
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
	public void selectCell(int x_i, int x_j){
		
		String SS = new String("");
		int t = 8;
		
		g2D.setStroke(new BasicStroke(t));
		g2D.setColor(Color.MAGENTA);
		g2D.drawRect(540+X+(40*x_i)-t/2,920-Y-(40*x_j)-t/2, 40+t, 40+t);
		
		/* Which strategy? Compute payoff */
		setXi(x_i); setXj(x_j);
		
		if      (x_i > -1 && x_i < 10) { SS = SS+"A";}
		else if (x_i > 10 && x_i < 21) { SS = SS+"B"; setXi(x_i - 11);}
		else { SS = SS+"N"; setXi(10); }
				
		if      (x_j > -1 && x_j < 10) { SS = SS+"B";}
		else if (x_j > 10 && x_j < 21) { SS = SS+"A"; setXj(x_j - 11);}
		else { SS = SS+"N"; setXj(10); }
		
		computePayoff(SS, getXi(), getXj());
		
		/* Test string on screen */
		g2D.setColor(Color.BLACK);
		String output = SS+"  ("+String.format("%2d",getXi())+","+
                String.format("%2d",getXj())+") "+
                String.format("%3d",getPayoff());
		g2D.drawString( output,  1680, 40);
				
		/* Draw triangular colorbar mark */
		t = 6;
		int ci = getPayoff()/5;
		int[] xcPoints = {1540+t/2 + X,1588+t/2 + X,1588+t/2 + X};
		int[] ycPoints = {920-Y-(40*ci)+20,920-Y-(40*ci)-28+20,920-Y-(40*ci)+28+20};
		
		if (ci > -1) {
			g2D.setColor(Color.decode(colors.get(ci)));
		} else {
			g2D.setColor(Color.BLACK);
		}
		
		g2D.fillPolygon(xcPoints, ycPoints, 3);
		g2D.setStroke(new BasicStroke(t));
		g2D.setColor(Color.MAGENTA);
		g2D.drawPolygon(xcPoints, ycPoints, 3);
				
	}
	
	
	public void drawRuler(int x_i, int x_j){
		
		setXi(x_i); setXj(x_j);
		
		/* Internal variables */
		String SS = new String("");
		Color[] fill = new Color[2];
		Color[] edge = new Color[2];
				
		
		/* Check the given x values */
		if      (x_i > -1 && x_i < 10) { SS = SS+"A";}
		else if (x_i > 10 && x_i < 21) { SS = SS+"B"; setXi(x_i - 11);}
		else { SS = SS+"N"; setXi(10); }
				
		if      (x_j > -1 && x_j < 10) { SS = SS+"B";}
		else if (x_j > 10 && x_j < 21) { SS = SS+"A"; setXj(x_j - 11);}
		else { SS = SS+"N"; setXj(10); }
		
		/* Change the drawing parameters according to the strategy selected */
		switch (SS){
			case "AA":
			   fill[0] = R2; 		 fill[1] = R1;
			   edge[0] = Color.RED; edge[1] = R2;
			   break;
			case "AB":
			   fill[0] = R2; 		 fill[1] = B1;
			   edge[0] = Color.RED; edge[1] = B2;
			   break;
			case "BA":
			   fill[0] = B2; 		  fill[1] = R1;
			   edge[0] = Color.BLUE; edge[1] = R2;
			   break;
			case "BB":
			   fill[0] = B2; 		  fill[1] = B1;
			   edge[0] = Color.BLUE; edge[1] = B2;
			   break;
			
			default:
				fill[0] = Color.WHITE; fill[0] = Color.WHITE;
				edge[0] = Color.WHITE; fill[0] = Color.WHITE;
				break;
//				throw new IllegalArgumentException("Invalid strategy: " + SS);
		}
		
		setStrategy(SS);
		
		g2D.setFont(SANS1);
		
		/* Draw ruler, thickness = t */
		int t = 6;
		g2D.setStroke(new BasicStroke(t));
		g2D.setColor(new Color(147, 93, 116));
		
		if (x_i < 0){ x_i = -1;} else if (x_i > 20){ x_i = 21;}
		if (x_j < 0){ x_j = -1;} else if (x_j > 20){ x_j = 21;}
		
		/* Ruler moving along horizontal axis (vertical box) */
		g2D.drawRect(540+X+(40*x_i)-t/2,       80-Y-3*t/2,    40+t, 920+3*t);
		/* Ruler moving along vertical axis (horizontal box) */
		g2D.drawRect(540+X-40-3*t/2,   920-Y-(40*x_j)-t/2, 920+3*t,    40+t);
		
		/* Draw border of selected cell */
		t = 8;
		g2D.setColor(Color.MAGENTA);
		g2D.drawRect(540+X+(40*x_i)-t/2,920-Y-(40*x_j)-t/2, 40+t, 40+t);
		
		/* Draw horizontal triangular ruler marks */
		int[] xiPoints = {540+X+(40*x_i)-28+20, 540+X+(40*x_i)+28+20, 540+X+(40*x_i)+20};
		int[] yiPoints = {1048-Y+2*t, 1048-Y+2*t, 1000-Y+2*t};
		g2D.setColor(fill[0]); g2D.fillPolygon(xiPoints, yiPoints, 3);
		g2D.setColor(edge[0]); g2D.drawPolygon(xiPoints, yiPoints, 3);
		
		g2D.setColor(fill[0]);
		g2D.drawString("Your pick", xiPoints[2]-72, yiPoints[0]+40);
				
		/* Draw vertical triangular ruler mark */
		int[] xjPoints = {452-2*t + X, 452-2*t + X,500-2*t + X};
		int[] yjPoints = {920-Y-(40*x_j)-28+20,920-Y-(40*x_j)+28+20,920-Y-(40*x_j)+20};
		g2D.setColor(fill[1]); g2D.fillPolygon(xjPoints, yjPoints, 3);
		g2D.setColor(edge[1]); g2D.drawPolygon(xjPoints, yjPoints, 3);
		
		g2D.setColor(edge[1]);
		g2D.drawString("Mia's", xjPoints[0]-95, yjPoints[2]+12);
		
		/* Print strategy, x values, and payoff on UI */
		computePayoff(SS, getXi(), getXj());
		g2D.setColor(Color.BLACK);
		String output = SS+"  ("+String.format("%2d",getXi())+","+
		                          String.format("%2d",getXj())+") "+
				                  String.format("%3d",getPayoff());
//		System.out.println(SS+","+String.valueOf(getXi())+","+String.valueOf(getXj())+","+String.valueOf(getPayoff()));
		g2D.drawString( output,  1680, 40);
		
		/* Draw triangular colorbar mark */
//		drawColorbarMark();		
		int ci = getPayoff()/5;
		int[] xcPoints = {1540+t/2 + X,1588+t/2 + X,1588+t/2 + X};
		int[] ycPoints = {920-Y-(40*ci)+20,920-Y-(40*ci)-28+20,920-Y-(40*ci)+28+20};
		
		if (ci > -1) {
			g2D.setColor(Color.decode(colors.get(ci)));
		} else {
			g2D.setColor(Color.WHITE);
		}
		
		g2D.fillPolygon(xcPoints, ycPoints, 3);
		g2D.setColor(edge[0]);
		g2D.drawPolygon(xcPoints, ycPoints, 3);
		
	}
	
	/* Draw normal form of the game */
	public void drawNormalForm(int pAA, int pAB, int pBA, int pBB){
				
		g2D.setFont(DesignSpaceUI.MONO2);
		g2D.setColor(Color.BLACK);
		
		g2D.drawString( String.format("%3d",pAA), 1560, 290);
		g2D.drawString( String.format("%3d",pAB), 1560, 350);

		g2D.drawString( String.format("%3d",pBA), 1700, 290);
		g2D.drawString( String.format("%3d",pBB), 1700, 350);
		
	}
	
	

}
