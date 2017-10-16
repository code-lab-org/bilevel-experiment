package edu.stevens.code.eager.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainUI extends JPanel{
	
	/**
	 * This is the Main file for execution of the user interface
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	/* Main */
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Collective Design Laboratory");
		final MainUI main_frame = new MainUI();
		
		frame.setContentPane(main_frame);
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/* Initialize JFrame in maximized mode: */
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		main_frame.setXi(10);
		main_frame.setXj(10);
		
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		System.out.println(screenSize);
		
		frame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				main_frame.xi = (int) (Math.floor((20*Math.floor((me.getX()+1)/20.) + 20)/40) - 14);
				main_frame.xj = (int) (23 - Math.floor((20*Math.floor((me.getY()+1)/20.) + 20)/40));
				
//				System.out.println(String.valueOf(xi)+","+String.valueOf(xj));
				main_frame.repaint();
				}
        }); 
		
		
	}
	
		
	/* MainUI method */
	public MainUI() {

		/* JPanel preferred size */
		this.setPreferredSize(new Dimension(1920,1080));
//		this.setPreferredSize(new Dimension(800,600));
//		this.setPreferredSize(getMaximumSize());
		this.setBackground(Color.WHITE);
		
	}
	
	
	private int xi;
	private int xj;
	
	public int getXi() { return xi; }
	public void setXi(int xi) { this.xi = xi; }

	public int getXj() { return xj; }
	public void setXj(int xj) { this.xj = xj; }	
	
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		DesignSpaceUI DS = null;
		try {
			DS = new DesignSpaceUI(g2D,"PB01");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DesignSpaceUI.drawColorbar(g2D);
		DesignSpaceUI.addAxis(g2D);
		
		/* Draw Cells */		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				DS.drawCell("AA",i,j, DS.getAA(i,j) );
				DS.drawCell("AB",i,j, DS.getAB(i,j) );
				DS.drawCell("BA",i,j, DS.getBA(i,j) );
				DS.drawCell("BB",i,j, DS.getBB(i,j) );
			}
		}
		
		DS.drawBorder();
		DS.drawRuler( getXi(), getXj() );
		
	}


}
