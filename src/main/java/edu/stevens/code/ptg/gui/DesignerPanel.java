package edu.stevens.code.ptg.gui;

import javax.swing.JPanel;

import edu.stevens.code.ptg.Designer;

/**
 * The Class DesignerPanel.
 */
public abstract class DesignerPanel extends JPanel {
	private static final long serialVersionUID = 6467998374058444275L;

	/**
	 * Observe.
	 *
	 * @param designer the designer
	 */
	public abstract void observe(Designer designer);
	
	/**
	 * Bind to.
	 *
	 * @param designer the designer
	 */
	public abstract void bindTo(Designer designer);
}
