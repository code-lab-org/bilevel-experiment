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
	public final static ArrayList<String> COLORS = new ArrayList<String>(
			Arrays.asList("#352a87", "#353eaf", "#1b55d7", "#026ae1", "#0f77db",
						  "#1484d4", "#0d93d2", "#06a0cd", "#07aac1", "#18b1b2",
					      "#33b8a1",
					      "#55bd8e", "#7abf7c", "#9bbf6f", "#b8bd63", "#d3bb58",
					      "#ecb94c", "#ffc13a", "#fad12b", "#f5e31e", "#f9fb0e"));
	
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
	
	/* Public method that returns the color that corresponds to a payoff */
	public static Color payoffColor(int payoff) {
		
		Color payoff_color = null;
		
		if (payoff > -1 && payoff < 101){
			payoff_color = Color.decode( COLORS.get(payoff/5) );
		} else {
			payoff_color = Color.BLACK;
		}
		
		return payoff_color;
		
	}	
	
	/* Protected method that returns the font color depending on the payoff */
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
