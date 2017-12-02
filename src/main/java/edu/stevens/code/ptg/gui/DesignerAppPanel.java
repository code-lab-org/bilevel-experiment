package edu.stevens.code.ptg.gui;

import javax.swing.JPanel;

import edu.stevens.code.ptg.DesignerApp;

public abstract class DesignerAppPanel extends JPanel {
	private static final long serialVersionUID = -6514239839168558527L;

	/**
	 * Bind to a designer application.
	 *
	 * @param app the application
	 */
	public abstract void bindTo(DesignerApp app);
}
