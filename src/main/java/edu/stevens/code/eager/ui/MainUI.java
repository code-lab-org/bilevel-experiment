package edu.stevens.code.eager.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainUI extends JPanel implements ActionListener, KeyListener{
	
	/**
	 * This is the Main file for execution of the user interface
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int X_SHIFT = -310;
	public static final int Y_SHIFT =   50;
	
	/* Game file */
	private String game;
	public String getGame() { return game; }
	public void setGame(String game_file) { this.game = game_file; }
	
	/* Variables xScreen and yScreen, setters, and getters */
	private int xScreen = 1920/2 + X_SHIFT;
	public int getXscreen() { return xScreen; }
	public void setXscreen(int x_screen) { this.xScreen = x_screen; }
	
	private int yScreen = 1080/2 - Y_SHIFT;
	public int getYscreen() { return yScreen; }
	public void setYscreen(int y_screen) { this.yScreen = y_screen; }	
	
	/* Main */
	public static void main(String[] args) {
		
		/* Create frame that will contain the content defined
		 * through the MainUI object (which I have named "main_frame").
		 */
		JFrame frame = new JFrame("Collective Design Laboratory");
		
		/* Create the aforementioned "main_frame" object of class MainUI. */
		final MainUI main_frame = new MainUI("PB01");
		
		/* Now, object "main_frome" becomes the content source
		 * of the JFrame object "frame" defined previously.
		 */
		frame.setContentPane(main_frame);
		
		/* Here I tell Java to remove the title bar */
		frame.setUndecorated(true);
		
		/* Because there is no title bar (see previous comment),
		 * the interface needs to be closed using Alt+F4.
		 */
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/* Initialize JFrame in maximized mode: */
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		/* This is the JFrame is packed */
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		System.out.println(screenSize);
		
		/* The next block makes it possible to click on every cell
		 * and update the position of the ruler.
		 */
		frame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				
				/* The next two lines take the (x,y) position of the MouseEvent "me"
				 * and assign it to xi and xj.
				 * I might use main_frame.setXi(int) and main_frame.setXj(int)
				 * instead of main_frame.xi = int and main_frame.xj = int,
				 * respectively; that could be a better practice. IDK!
				 */
				
				main_frame.setXscreen(me.getX());
				main_frame.setYscreen(me.getY());
				
				/* Here it is where I repaint the contents in the "main_frame". */
				main_frame.repaint();
				}
        });
		
        frame.addKeyListener(main_frame);
		
				
	}
			
	/* MainUI method */
	public MainUI(String game_file) {
		
		setGame(game_file);
		
		/* JPanel preferred size */
		this.setPreferredSize(new Dimension(1920,1080));
//		this.setPreferredSize(new Dimension(800,600));
//		this.setPreferredSize(getMaximumSize());
		this.setBackground(Color.BLACK);
		
	}
	
	/* All the painting of the frame is done next. */
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		DesignSpaceUI.drawColorbar(g2D);
		DesignSpaceUI.drawBorder(g2D);
		DesignSpaceUI.addAxis(g2D);
		
		DesignSpaceUI DS = null;
		try {
			DS = new DesignSpaceUI(g2D,getGame());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		/* Draw Design Space */
		DS.drawDesignSpace();
		DS.drawRuler( getXscreen(), getYscreen() );
		
	}
	
	/* Using the arrow keys */
	@Override
	public void keyPressed(KeyEvent e){
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_LEFT) {
			setXscreen(getXscreen()-40);
	    }
	    if (code == KeyEvent.VK_RIGHT) {
	        setXscreen(getXscreen()+40);
	    }
	    if (code == KeyEvent.VK_UP) {
	        setYscreen(getYscreen()-40);
	    }
	    if (code == KeyEvent.VK_DOWN) {
	        setYscreen(getYscreen()+40);
	    }
	    
	    if (getXscreen() > 1920/2 + X_SHIFT + 20 + 440) {
	    	setXscreen(1920/2 + X_SHIFT + 440);
	    } else if (getXscreen() < 1920/2 + X_SHIFT - 20 - 440) {
	    	setXscreen(1920/2 + X_SHIFT - 440);
	    }
	    
	    if (getYscreen() < 1080/2 - Y_SHIFT - 20 - 440) {
	    	setYscreen(1080/2 - Y_SHIFT - 440);
	    } else if (getYscreen() > 1080/2 - Y_SHIFT + 20 + 440) {
	    	setYscreen(1080/2 - Y_SHIFT + 440);
	    }
	    
	    repaint();
		
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
