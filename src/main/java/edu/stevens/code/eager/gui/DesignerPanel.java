package edu.stevens.code.eager.gui;

import javax.swing.JPanel;

import edu.stevens.code.eager.Designer;
import edu.stevens.code.eager.DesignerApp;

/**
 * The Class DesignerPanel.
 */
public abstract class DesignerPanel extends JPanel {
	private static final long serialVersionUID = 6467998374058444275L;

	/**
	 * Observe a designer.
	 *
	 * @param designer the designer
	 */
	public abstract void observe(Designer designer);
	
	/**
	 * Bind to a designer.
	 *
	 * @param designer the designer
	 */
	public abstract void bindTo(Designer designer);
	
	/**
	 * Bind to a designer application.
	 *
	 * @param app the application
	 */
	public abstract void bindTo(DesignerApp app);
}
