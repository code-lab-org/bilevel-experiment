package edu.stevens.code.eager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import edu.stevens.code.eager.designer.DesignerUI;

public class Main extends JFrame implements KeyListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	DesignerUI panel;
	
	public static void main(String[] args) {
		
		new Main("PB01");
//		new Main("PB01");
		

	}
	
	public Main(String game){
		
		panel = new DesignerUI(game);
		
		setContentPane(panel);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DesignerUI.loadIcons(this);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
//		panel.useMouse(this);
		
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
