package edu.stevens.code.eager;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;

/**
 * The Interface App.
 */
public interface App {
	public final static ArrayList<Image> ICONS = new ArrayList<Image>(Arrays.asList(
			new ImageIcon(App.class.getResource("/icons/icon__16.png")).getImage(),
			new ImageIcon(App.class.getResource("/icons/icon__32.png")).getImage(),
			new ImageIcon(App.class.getResource("/icons/icon__48.png")).getImage(),
			new ImageIcon(App.class.getResource("/icons/icon__64.png")).getImage(),
			new ImageIcon(App.class.getResource("/icons/icon_128.png")).getImage(),
			new ImageIcon(App.class.getResource("/icons/icon_256.png")).getImage()));
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
	 * Gets the controller.
	 *
	 * @return the controller
	 */
	public Object getController();
	
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
