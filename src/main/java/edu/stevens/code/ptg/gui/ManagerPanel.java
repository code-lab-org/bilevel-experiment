package edu.stevens.code.ptg.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

import edu.stevens.code.ptg.Manager;

public class ManagerPanel extends JPanel {
	private static final long serialVersionUID = -4935834958965987709L;
	
	private Manager manager;
	
	public ManagerPanel(Manager manager) {
		this.manager = manager;
		
		this.setPreferredSize(new Dimension(400,300));
	}
}
