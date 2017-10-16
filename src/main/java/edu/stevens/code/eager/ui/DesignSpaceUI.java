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


public class DesignSpaceUI {
	
	/* Default constructor */
	public DesignSpaceUI(Graphics2D g2D_object, String game_file) throws IOException {
		
		// Remark:
		// ------------------------------------------------------------------------------
		// Add matrix/array input to default constructor
		// The matrix/array should of size 10x10
		// Each element of this matrix is a payoff pk = 0:5:100 (21 data points)
		// Use each value to query a color from a list or 1-dimensional array of Strings
				
		this.g2D = g2D_object;
		
		BufferedReader br = new BufferedReader(new FileReader(new File("src/test/java/games/"+game_file+".csv")));
        
        List<Integer> values = new ArrayList<Integer> ();
        String line;
        
        while((line = br.readLine())!= null){
        //	split the line you read after the     
        	String[] r = line.split(",");
        
        	for(int i = 0; i < r.length; i++){
        		int val = Integer.parseInt(r[i]);
        		values.add((int) val);
        	}
        }
        br.close();
				
		int[][] gameAA = new int[10][10];
		int[][] gameAB = new int[10][10];
		int[][] gameBA = new int[10][10];
		int[][] gameBB = new int[10][10];
		
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
            	gameAA[j][i] = values.get(    10*i+j);
            	gameAB[j][i] = values.get(100+10*i+j);
            	gameBA[j][i] = values.get(200+10*i+j);
            	gameBB[j][i] = values.get(300+10*i+j);
            }
        }

//        System.out.println(Arrays.deepToString(gameBA));
        
        setAA(gameAA);
        setAB(gameAB);
        setBA(gameBA);
        setBB(gameBB);
		
	}

	/* Graphics2D object */
	private Graphics2D g2D;
	
	
	/* Payoff structure */
	private int[][] AA;
	private int[][] AB;
	private int[][] BA;
	private int[][] BB;

	public int getAA(int i, int j) { return AA[i][j]; }
	public void setAA(int[][] aA) { AA = aA; }
	
	public int getAB(int i, int j) { return AB[i][j]; }
	public void setAB(int[][] aB) { AB = aB; }
	
	public int getBA(int i, int j) { return BA[i][j]; }
	public void setBA(int[][] bA) { BA = bA; }
	
	public int getBB(int i, int j) { return BB[i][j]; }
	public void setBB(int[][] bB) { BB = bB; }
	
	private int payoff;
	public int getPayoff() { return payoff; }
	public void setPayoff(int new_payoff) { payoff = new_payoff; }
	
	/* The 21 colors */
	protected static ArrayList<String> colors = new ArrayList<String>(
			Arrays.asList("#352a87", "#353eaf", "#1b55d7", "#026ae1", "#0f77db",
						  "#1484d4", "#0d93d2", "#06a0cd", "#07aac1", "#18b1b2",
					      "#33b8a1",
					      "#55bd8e", "#7abf7c", "#9bbf6f", "#b8bd63", "#d3bb58",
					      "#ecb94c", "#ffc13a", "#fad12b", "#f5e31e", "#f9fb0e"));
	
	
	public void drawBorder(){
		
		int t = 6;
		g2D.setStroke(new BasicStroke(t));
		
		g2D.setColor(Color.RED);
		g2D.drawRect(540-t/2, 100-t/2, 400+t, 400+t);
		g2D.drawRect(540-t/2, 540-t/2, 400+t, 400+t);
		
		g2D.setColor(Color.BLUE);
		g2D.drawRect(980-t/2, 100-t/2, 400+t, 400+t);
		g2D.drawRect(980-t/2, 540-t/2, 400+t, 400+t);
		
	}
	
	
	public static void drawColorbar(Graphics2D g2D_object){
		
		for (int j = 0; j < 21; j++) {
			g2D_object.setColor(Color.decode( colors.get( 20-j ) ));
			g2D_object.setStroke(new BasicStroke(0));
			g2D_object.fillRect(1460, 100+40*j, 80, 40);
		}
		
		int fs = 28;
		g2D_object.setFont(new Font("Consolas", Font.PLAIN, fs));
		FontMetrics fm = g2D_object.getFontMetrics(g2D_object.getFont());
		int fh = 20 + (5*fs/2)/7;
		
		for (int v = 0; v < 3; v++){
			g2D_object.setColor(Color.BLACK);
			String val = String.valueOf(100-v*20);
			int fw = fm.stringWidth(val);
			g2D_object.drawString( val, 1500-fw/2, fh+100+160*v);
		}
		
		for (int v = 3; v < 6; v++){
			g2D_object.setColor(Color.WHITE);
			String val = String.valueOf(100-v*20);
			int fw = fm.stringWidth(val);
			g2D_object.drawString( val, 1500-fw/2, fh+100+160*v);
		}
		
	}
	
	
	public static void addAxis(Graphics2D g2D_object){
		
		int fs = 28;
		g2D_object.setFont(new Font("Consolas", Font.PLAIN, fs));
		g2D_object.setColor(Color.BLACK);
		
		FontMetrics fm = g2D_object.getFontMetrics(g2D_object.getFont());
		int fh = 20 + (5*fs/2)/7;
		int fw = fm.stringWidth("0");
		
		for (int x = 0; x < 10; x++){
			String xi = String.valueOf(x);
			
			g2D_object.drawString( xi,  560-fw/2+40*x, fh + 60);
			g2D_object.drawString( xi, 1000-fw/2+40*x, fh + 60);
			
			g2D_object.drawString( xi,  560-fw/2+40*x, fh +940);
			g2D_object.drawString( xi, 1000-fw/2+40*x, fh +940);
			
			g2D_object.drawString( xi,  520-fw/2, fh + 460-40*x);
			g2D_object.drawString( xi,  520-fw/2, fh + 900-40*x);
			
			g2D_object.drawString( xi, 1400-fw/2, fh + 460-40*x);
			g2D_object.drawString( xi, 1400-fw/2, fh + 900-40*x);
			
		}
		
		
	}
	
	
	public void drawCell(String strategy, int xi, int xj, int payoff){
		
		int dx;
		int dy;
		
		switch (strategy){
			case "AA": dx = 540; dy = 460; break;
			case "AB": dx = 540; dy = 900; break;
			case "BA": dx = 980; dy = 460; break;
			case "BB": dx = 980; dy = 900; break;
			
			default:
				throw new IllegalArgumentException("Invalid strategy: " + strategy);
		}
		
		g2D.setColor(Color.decode( colors.get(payoff/5) ));
		g2D.setStroke(new BasicStroke(0));
		g2D.fillRect((40*xi)+dx, dy-(40*xj), 40, 40);
		
	}
	
	public void computePayoff(String strategy, int xi, int xj){
		
		switch (strategy){
			case "AA": setPayoff(getAA(xi,xj)); break;
			case "AB": setPayoff(getAB(xi,xj)); break;
			case "BA": setPayoff(getBA(xi,xj)); break;
			case "BB": setPayoff(getBB(xi,xj)); break;
			
			default:
				strategy = "NN"; setPayoff(-1); break;
		}
				
	}
	
	
	public void drawRuler(int xi, int xj){
		
		/* Internal variables */
		int dx;
		int dy;
		String strategy = new String("");
		Color[] fill = new Color[2];
		Color[] edge = new Color[2];
		
		/* Set of colors for the ruler marks */
		Color R1 = new Color(255, 230, 230);
		Color R2 = new Color(255, 127, 127);
		Color B1 = new Color(230, 230, 255);
		Color B2 = new Color(127, 127, 255);
		
		/* Check the given x values */
		if      (xi > -1 && xi < 10) { strategy = strategy+"A";}
		else if (xi > 10 && xi < 21) { strategy = strategy+"B"; xi = xi - 11;}
				
		if      (xj > -1 && xj < 10) { strategy = strategy+"B";}
		else if (xj > 10 && xj < 21) { strategy = strategy+"A"; xj = xj - 11;}
		
		/* Change the drawing parameters according to the strategy selected */
		switch (strategy){
			case "AA": dx = 540; dy = 460;
			   fill[0] = Color.RED; fill[1] = R1;
			   edge[0] = R2; 		 edge[1] = R2;
			   break;
			case "AB": dx = 540; dy = 900;
			   fill[0] = Color.RED; fill[1] = B1;
			   edge[0] = R2; 		 edge[1] = B2;
			   break;
			case "BA": dx = 980; dy = 460;
			   fill[0] = Color.BLUE; fill[1] = R1;
			   edge[0] = B2; 		  edge[1] = R2;
			   break;
			case "BB": dx = 980; dy = 900;
			   fill[0] = Color.BLUE; fill[1] = B1;
			   edge[0] = B2; 		  edge[1] = B2;
			   break;
			
			default:
				strategy = "NN";
				xi = 10; xj = 10;
				dx = 540; dy = 900;
				fill[0] = Color.WHITE; fill[0] = Color.WHITE;
				edge[0] = Color.WHITE; fill[0] = Color.WHITE;
				break;
//				throw new IllegalArgumentException("Invalid strategy: " + strategy);
		}
		
		
		/* Draw ruler, thickness = t */
		int t = 6;
		g2D.setStroke(new BasicStroke(t));
		g2D.setColor(new Color(147, 93, 116));
		g2D.drawRect(dx+(40*xi)-t/2,   20+40, 40+t, 920);
		g2D.drawRect(540-40,  dy-(40*xj)-t/2, 920, 40+t);
		
		/* Draw both triangular ruler marks */
		int[] xiPoints = {dx+(40*xi)-28+20,dx+(40*xi)+28+20,dx+(40*xi)+20};
		int[] yiPoints = {1028+3*t/2,1028+3*t/2,980+3*t/2};
		g2D.setColor(fill[0]); g2D.fillPolygon(xiPoints, yiPoints, 3);
		g2D.setColor(edge[0]); g2D.drawPolygon(xiPoints, yiPoints, 3);

		int[] xjPoints = {452-3*t/2,452-3*t/2,500-3*t/2};
		int[] yjPoints = {dy-(40*xj)-28+20,dy-(40*xj)+28+20,dy-(40*xj)+20};
		g2D.setColor(fill[1]); g2D.fillPolygon(xjPoints, yjPoints, 3);
		g2D.setColor(edge[1]); g2D.drawPolygon(xjPoints, yjPoints, 3);
				
		/* Draw border of selected cell */
		t = 8;
		g2D.setColor(Color.MAGENTA);
		g2D.drawRect(dx+(40*xi)-t/2,dy-(40*xj)-t/2, 40+t, 40+t);
		
		/* Print strategy, x values, and payoff on UI */
		computePayoff(strategy, xi, xj);
		g2D.setColor(Color.BLACK);
		String output = strategy+"  ("+String.format("%2d",xi)+","+
		                                String.format("%2d",xj)+") "+
				                        String.format("%3d",getPayoff());
		System.out.println(strategy+","+String.valueOf(xi)+","+String.valueOf(xj)+","+String.valueOf(getPayoff()));
		g2D.drawString( output,  100, 100);
		
		/* Draw triangular colorbar mark */
		int ci = getPayoff()/5;
		int[] xcPoints = {1540+t/2,1588+t/2,1588+t/2};
		int[] ycPoints = {900-(40*ci)+20,900-(40*ci)-28+20,900-(40*ci)+28+20};
		
		g2D.setColor(Color.decode(colors.get(ci)));
		g2D.fillPolygon(xcPoints, ycPoints, 3);
		g2D.setColor(Color.MAGENTA);
		g2D.drawPolygon(xcPoints, ycPoints, 3);
			
	}



}
