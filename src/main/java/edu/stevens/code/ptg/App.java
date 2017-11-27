package edu.stevens.code.ptg;

/**
 * The Interface App.
 */
public interface App {
	
	/**
	 * Gets the designers.
	 *
	 * @return the designers
	 */
	public Designer[] getDesigners();
	
	/**
	 * Gets the designer.
	 *
	 * @param index the index
	 * @return the designer
	 */
	public Designer getDesigner(int index);
	
	/**
	 * Gets the manager.
	 *
	 * @return the manager
	 */
	public Manager getManager();
	
	/**
	 * Gets the self.
	 *
	 * @return the self
	 */
	public Object getSelf();
	
	/**
	 * Inits the application.
	 *
	 * @param federationName the federation name
	 */
	public void init(String federationName);
	
	/**
	 * Kill.
	 */
	public void kill();
}
