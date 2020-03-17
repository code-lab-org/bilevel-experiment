package edu.stevens.code.eager.gui;

import javax.swing.JPanel;

import edu.stevens.code.eager.ManagerApp;
import edu.stevens.code.eager.model.Manager;

/**
 * The Class ManagerPanel.
 */
public abstract class ManagerPanel extends JPanel {
	private static final long serialVersionUID = 5680166362732993135L;
	
	/**
	 * Observe a manager.
	 *
	 * @param manager the manager
	 */
	public abstract void observe(Manager manager);
	
	/**
	 * Bind to a manager.
	 *
	 * @param manager the manager
	 */
	public abstract void bindTo(Manager manager);
	
	/**
	 * Bind to a manager application.
	 *
	 * @param app the application
	 */
	public abstract void bindTo(ManagerApp app);
}
