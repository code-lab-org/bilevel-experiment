/******************************************************************************
 * Copyright 2020 Stevens Institute of Technology, Collective Design Lab
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.stevens.code.bilevel.app;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;

import edu.stevens.code.bilevel.model.Designer;
import edu.stevens.code.bilevel.model.Manager;

/**
 * An interface to the application. Provides common functions for both the designer and manager applications.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 * @author Ambrosio Valencia-Romero <avalenci@stevens.edu>
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
	 * Gets the array of designers in this application.
	 *
	 * @return the designers
	 */
	public Designer[] getDesigners();
	
	/**
	 * Gets the designer specified by an index.
	 *
	 * @param index the index
	 * @return the designer
	 */
	public Designer getDesigner(int index);
	
	/**
	 * Gets the manager for this application.
	 *
	 * @return the manager
	 */
	public Manager getManager();
	
	/**
	 * Gets the controller of this application (either a designer or a manager).
	 *
	 * @return the controller
	 */
	public Object getController();
	
	/**
	 * Initializes the application.
	 *
	 * @param federationName the federation name
	 */
	public void init(String federationName);
	
	/**
	 * Kills the application.
	 */
	public void kill();
}
