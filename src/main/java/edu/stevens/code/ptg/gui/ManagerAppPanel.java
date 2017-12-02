package edu.stevens.code.ptg.gui;

import javax.swing.JPanel;

import edu.stevens.code.ptg.ManagerApp;

public abstract class ManagerAppPanel extends JPanel {
	private static final long serialVersionUID = 8811096233222838370L;
	
	public abstract void bindTo(ManagerApp app);
}
