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
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;
import edu.stevens.code.ptg.Manager;

public class DesignSpaceUI extends JPanel implements ActionListener, KeyListener {
	
	/**
	 * This is the Main file for execution of the user interface
	 */
	private static final long serialVersionUID = -4137308466599318529L;
	
//	private JToggleButton shareButton;
	
	public static final int X = PaintingMethods.X_SHIFT;
	public static final int Y = PaintingMethods.Y_SHIFT;
	public static final int X_RANGE = 1624 + X;
	
	private final int xA0 = 1920/2 + X - 420;	
	private final int xB0 = 1920/2 + X +  20;
	
	private final int yA0 = 1080/2 - Y -  20;
	private final int yB0 = 1080/2 - Y + 420;
	
	/** Game file */
	private String game = "PB01"; /* Default game */
	public String getGame() { return game; }
	public void setGame(String game_file) { this.game = game_file; }
	
	/** Self is sharing */
	private boolean self_sharing = true;
	public boolean isSelfSharing() { return self_sharing; }
	public void setSelfStatus(boolean self_status) { self_sharing = self_status; }
	
	/** Partner is sharing */
	private boolean partner_sharing = true; /* false */
	public boolean isPartnerSharing() { return partner_sharing; }
	public void setPartnerStatus(boolean partner_status) { partner_sharing = partner_status; }
	
	/** Both are sharing */
	private boolean sharing = true;
	public boolean areBothSharing() { return sharing; }
	public void setSharingStatus(boolean self, boolean partner) { sharing = self && partner; }
	
	
	/** Variables xScreen and yScreen, setters, and getters */
	private int xScreen = 1920/2 + X;
	public int getXscreen() { return xScreen; }
	public void setXscreen(int x_screen) { this.xScreen = x_screen; }
	
	private int yScreen = 1080/2 - Y;
	public int getYscreen() { return yScreen; }
	public void setYscreen(int y_screen) { this.yScreen = y_screen; }
	
	/** Variables xAi and xBi, setters, and getters for player i (horizontal axis) */
	private int xSi = 10;
	public int getXSi() { return xSi; }
	public void setXSi(int x_Si) { this.xSi = x_Si; }
	
	private int xAi = 10;
	public int getXAi() { return xAi; }
	public void setXAi(int x_Ai) { this.xAi = x_Ai; }
	
	private int xBi = -1;
	public int getXBi() { return xBi; }
	public void setXBi(int x_Bi) { this.xBi = x_Bi; }

	/** Variables xAj and xBj, setters, and getters for player j (vertical axis) */
	private int xSj = 10;
	public int getXSj() { return xSj; }
	public void setXSj(int x_Sj) { this.xSj = x_Sj; }
	
	private int xAj = -1;
	public int getXAj() { return xAj; }
	public void setXAj(int x_Aj) { this.xAj = x_Aj; }

	private int xBj = 10;
	public int getXBj() { return xBj; }
	public void setXBj(int x_Bj) { this.xBj = x_Bj; }
	
//	/** Payoffs for player i */
//	private int pAA/*i's payoff @ AA*/ = 0;
//	public int getPAA() { return pAA; }
//	public void setPAA(int p_AA) { this.pAA = p_AA; }
//	
//	private int pAB/*i's payoff @ AB*/ = 0;
//	public int getPAB() { return pAB; }
//	public void setPAB(int p_AB) { this.pAB = p_AB; }
//	
//	private int pBA/*i's payoff @ BA*/ = 0;
//	public int getPBA() { return pBA; }
//	public void setPBA(int p_BA) { this.pBA = p_BA; }
//	
//	private int pBB/*i's payoff @ BB*/ = 0;
//	public int getPBB() { return pBB; }
//	public void setPBB(int p_BB) { this.pBB = p_BB; }
	
	
	/* Main */
	public static void main(String[] args) {
		
		/* Create frame that will contain the content defined
		 * through the MainUI object (which I have named "main_frame").
		 */
		JFrame frame = new JFrame("Collective Design Laboratory");
		
		/* Create the aforementioned "main_frame" object of class MainUI. */
		final DesignSpaceUI main_frame = new DesignSpaceUI();
		main_frame.setGame("NN0905_01");
		
		
		main_frame.setJFrameUI(frame);
		frame.setIconImages(DesignerApp.ICONS);
		
        frame.addKeyListener(main_frame);
        				
	}
			
	/** Default MainUI constructor */
	public DesignSpaceUI() {
		/* JPanel preferred size */
		this.setPreferredSize(new Dimension(1920,1080));
		this.setBackground(Color.BLACK);
		this.setLayout(null);
		
	}
	
	/** MainUI constructor specifying game
	 * (.csv file in "resources" package)
	 */
	public DesignSpaceUI(String game_file) {
		this();
		this.setGame(game_file);
	}

	
	/* Transform (X,Y) screen coordinates to (xi,xj) design space coordinates */
	private void screenToXij(int x_screen, int y_screen){
		
		if (x_screen >= xA0 && x_screen < xB0 + 400 && y_screen <= yB0 && y_screen > yA0 - 400){
			setXSi( (int) Math.floor((x_screen - xA0)/40.) );
			setXSj( (int) Math.floor((yB0 - y_screen)/40.) );
		}
				
//		if (x_screen >= xA0 && x_screen < xA0 + 400){
//			setXAi( (int) Math.floor((x_screen - xA0)/40.) );
//		}
//		
//		if (x_screen >= xB0 && x_screen < xB0 + 400){
//			setXBi( (int) Math.floor((x_screen - xB0)/40.) );
//		}
		
//		if (y_screen <= yA0 && y_screen > yA0 - 400){
//			setXAj( (int) Math.floor((yA0 - y_screen)/40.) );
//		}
//		
//		if (y_screen <= yB0 && y_screen > yB0 - 400){
//			setXBj( (int) Math.floor((yB0 - y_screen)/40.) );
//		}
		
	}
	
	/* Transform (xi,xj) design space coordinates to (X,Y) screen coordinates */
	private void xijToScreen(int xi, int xj){
				
		if (xi >= 0 && xi <= 10){
			setXscreen( 40*getXBi() + xB0 + 20 );
		} else if (xi >= 11 && xi <= 20){
			setXscreen( 40*getXAi() + xA0 + 20 );
		}
		
		if (xj >= 0 && xj <= 10){
			setYscreen( yB0 - 40*getXBj() - 20 );
		} else if (xj >= 11 && xj <= 20){
			setYscreen( yA0 - 40*getXAj() - 20 );
		}		
	}
	
	/* All the painting of the frame is done next. */
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		DesignSpacePainting DSP = null;
		try {
			DSP = new DesignSpacePainting( g2D,getGame() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		/* Draw Design Space */
		DSP.drawDesignSpace();
		
		screenToXij( getXscreen(), getYscreen() );
		DSP.drawRulersXi( getXAi(), getXBi() );		
		DSP.drawRulersXj( getXAj(), getXBj(), areBothSharing() );
		
		DSP.updateNormalForm(getXAi(), getXAj(), getXBi(), getXBj(), areBothSharing() );
//		DSP.displayNormalForm( areBothSharing() );
		DSP.selectedCell( getXSi(), getXSj() );
		
		int[] arr = {getXSi(), getXSj()};
		System.out.println( Arrays.toString(arr) );
				
	}
	
	/* Using the mouse */
	public void useMouse(JFrame j_frame){
		
		j_frame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				
				/* The next two lines take the (x,y) position of the MouseEvent "me"
				 * and assign it to xAi and xAj.
				 * I might use main_frame.setXAi(int) and main_frame.setXAj(int)
				 * instead of main_frame.xAi = int and main_frame.xAj = int,
				 * respectively; that could be a better practice. IDK!
				 */
				
				setXscreen(me.getX());
				setYscreen(me.getY());
				
//				int[] arr = {getXscreen(),getYscreen()};
//				
//				System.out.println( Arrays.toString( arr ) );
				
				/* Here it is where I repaint the contents in the "main_frame". */
				repaint();
				}
        });
		
	}
	
	/* Get JFrame ready */
	public void setJFrameUI(JFrame j_frame) {
		/* Now, object "main_frame" becomes the content source
		 * of the JFrame object "frame" defined previously.
		 */
		j_frame.setContentPane(this);
		
		/* Here I tell Java to remove the title bar */
		j_frame.setUndecorated(true);
		
		/* Because there is no title bar (see previous comment),
		 * the interface needs to be closed using Alt+F4.
		 */
		j_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/* Initialize JFrame in maximized mode: */
		j_frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		/* This is the JFrame is packed */
		j_frame.pack();
		j_frame.setLocationRelativeTo(null);
		j_frame.setVisible(true);
		
		this.useMouse(j_frame);
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
	    if (code == KeyEvent.VK_SPACE) {
	    	xijToScreen(getXSi(), getXSj());
	    }
	    
	    if (getXscreen() > xB0 + 400) {
	    	setXscreen(xB0 + 400 - 20);
	    } else if (getXscreen() < xA0) {
	    	setXscreen(xA0 + 20);
	    }
	    
	    if (getYscreen() < yA0 - 400) {
	    	setYscreen(yA0 - 400 + 20);
	    } else if (getYscreen() > yB0) {
	    	setYscreen(yB0 - 20);
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
	
	@SuppressWarnings("unused")
	private Manager manager = null;
	
	public void observe(Manager manager, Designer[] designers) {
		this.manager = manager;
		for(Designer d : designers) {
			d.addObserver(new Observer() {
				@Override
				public void update(Observable arg0, Object arg1) {
					if(manager != null && self != null && getXAi() != 10 && getXBi() != -1
							&& d.getId() == manager.getDesignPartner(self.getId())) {
						
						setXAj( d.getDesign(0) );
						setXBj( d.getDesign(1) );
						
						setPartnerStatus( d.isReadyToShare() );
						setSharingStatus( isSelfSharing(), isPartnerSharing() );
						
//						repaint();
					}
				}
			});
		}
		
		repaint();
		
	}
	
	private Designer self = null;
	
	public void bindTo(Designer designer) {
		this.self = designer;
		
//		setXAi(designer.getDesign(0));
//		setXBi(designer.getDesign(1));
//		
//		repaint();
		
//		addMouseListener(new MouseAdapter() {
//			public void mousePressed(MouseEvent me) {
//				
//				/* The next two lines take the (x,y) position of the MouseEvent "me"
//				 * and assign it to xAi and xAj.
//				 * I might use main_frame.setXAi(int) and main_frame.setXAj(int)
//				 * instead of main_frame.xAi = int and main_frame.xAj = int,
//				 * respectively; that could be a better practice. IDK!
//				 */
//				
////				setXscreen(me.getX());
////				setYscreen(me.getY());
////				screenToXij( getXscreen(), getYscreen() );
////				
////				if (getXAi() != 10 && getXBi() != 10) {
////					designer.setDesign(0, getXAi());
////					designer.setDesign(1, getXBi());
////				}
//				
//				setXAi(designer.getDesign(0));
//				setXBi(designer.getDesign(1));
//				
//				/* Here it is where I repaint the contents in the panel. */
//				repaint();
//				
//			}
//        });
		
//		shareButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				designer.setReadyToShare(shareButton.isSelected());
//				setSelfStatus(shareButton.isSelected());
//				setSharingStatus( isSelfSharing(), isPartnerSharing() );
//			}
//		});
		
		/** AMVRO: Added temporarily before removing sharing definitely */
		designer.setReadyToShare( true );
//		setSelfStatus( true );
//		setSharingStatus( true, true );
		
	}
}
