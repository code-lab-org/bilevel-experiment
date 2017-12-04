package edu.stevens.code.ptg.gui;

import javax.swing.JPanel;

import edu.stevens.code.ptg.ManagerApp;

/**
 * The Class ManagerAppPanel.
 */
public abstract class ManagerAppPanel extends JPanel {
	private static final long serialVersionUID = 8811096233222838370L;
	
	/**
	 * Bind to a manager application.
	 *
	 * @param app the application
	 */
	public abstract void bindTo(ManagerApp app);
}
