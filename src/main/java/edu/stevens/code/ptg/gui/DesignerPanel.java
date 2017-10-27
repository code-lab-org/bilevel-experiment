package edu.stevens.code.ptg.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

import edu.stevens.code.ptg.Designer;

public class DesignerPanel extends JPanel {
	private static final long serialVersionUID = 2488259187981650893L;	
	
	private final Designer designer;
	
	public DesignerPanel(Designer designer) {
		this.designer = designer;
		
		this.setPreferredSize(new Dimension(400,300));
	}
}
