package edu.stevens.code.eager.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.Manager;

public class DesignUI extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4256491169699543466L;
	
	/** Game file */
	private String game = "PB01"; /* Default game */
	public String getGame() { return game; }
	public void setGame(String game_file) { this.game = game_file; }
	
	
	/** Origin */
	private final int xS0 = 1920/2 - 420;
	private final int yS0 = 1080/2 + 420;
	
	
	/** Current strategy */
	private int currentStrategy;
	public int getStrategy() {	return currentStrategy; }
	public void setStrategy(int strategy) { this.currentStrategy = strategy; }
	
	
	/** Variables xScreen and yScreen, setters, and getters */
	private int xScreen = 1920/2;
	public int getXscreen() { return xScreen; }
	public void setXscreen(int x_screen) { this.xScreen = x_screen; }
	
	private int yScreen = 1080/2;
	public int getYscreen() { return yScreen; }
	public void setYscreen(int y_screen) { this.yScreen = y_screen; }
	
	/** Variables xSi and xSi, setters, and getters
	 *  for players i (horizontal axis) and i (vertical axis)
	 **/
	private int[] xSi = {10,10};
	public int getXSi(int strategy) {
		if (strategy == 0 || strategy == 1) { return xSi[strategy]; }
		else { return 10; }
	}
	public void setXSi(int strategy, int x_Si) {
		if (strategy == 0 || strategy == 1) { this.xSi[strategy] = x_Si; }
	}
	
	private int[] xSj = {10,10};
	public int getXSj(int strategy) {
		if (strategy == 0 || strategy == 1) { return xSj[strategy]; }
		else { return 10; }
	}
	public void setXSj(int strategy, int x_Sj) {
		if (strategy == 0 || strategy == 1) { this.xSj[strategy] = x_Sj; }
	}
	
	
	public DesignUI(int strategy){
		/* JPanel preferred size */
		this.setStrategy(strategy);
		this.setPreferredSize(new Dimension(1920,1080));
		this.setBackground(Color.BLACK);
		this.setLayout(null);
		
	}
	
	/** MainUI constructor specifying game
	 * (.csv file in "resources" package)
	 */
	public DesignUI(String game_file, int strategy) {
		this(strategy);
		this.setGame(game_file);
	}
	
	
	/* Transform (X,Y) screen coordinates to (xi,xj) design space coordinates */
	private void screenToXij(int strategy, int x_screen, int y_screen){
		
		if (x_screen >= xS0 && x_screen < xS0 + 400 && y_screen <= yS0 && y_screen > yS0 - 400){
			setXSi( strategy, (int) Math.floor((x_screen - xS0)/40.) );
			setXSj( strategy, (int) Math.floor((yS0 - y_screen)/40.) );
		}
		
	}
	
	/* All the painting of the frame is done next. */
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		DesignPainting DP = null;
		try {
			DP = new DesignPainting( g2D,getGame() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		/* Draw Design Space */
		DP.drawDesign( getStrategy() );
		
//		screenToXij( getXscreen(), getYscreen() );
//		DP.drawRulersXi( getXSi(), getXSi() );		
//		DP.selectedCell( getXSi(), getXSj() );
		
	}
	
	@SuppressWarnings("unused")
	private Manager manager = null;
	
	public void observe(Manager manager, Designer[] designers) {
		this.manager = manager;
		for(Designer d : designers) {
			d.addObserver(new Observer() {
				@Override
				public void update(Observable arg0, Object arg1) {
					if(manager != null && self != null 
							&& d.getId() == manager.getDesignPartner(self.getId())) {
						
						setXSj( 0, d.getAgreedDesign(0) );
						setXSj( 1, d.getAgreedDesign(1) );
						
						repaint();
					}
				}
			});
		}
	}
	
	private Designer self = null;
	
	public void bindTo(Designer designer) {
		this.self = designer;
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				
				/* The next two lines take the (x,y) position of the MouseEvent "me"
				 * and assign it to xAi and xAj.
				 * I might use main_frame.setXAi(int) and main_frame.setXAj(int)
				 * instead of main_frame.xAi = int and main_frame.xAj = int,
				 * respectively; that could be a better practice. IDK!
				 */
				
				setXscreen(me.getX());
				setYscreen(me.getY());
				screenToXij( getStrategy(), getXscreen(), getYscreen() );
				
				if ( getXSi( getStrategy() ) != 10 ) {
					designer.setDesign( getStrategy(), getXSi( getStrategy() ) );
				}
				
				/* Here it is where I repaint the contents in the "main_frame". */
				repaint();
				
			}
        });
	}
	
}
