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
package edu.stevens.code.bilevel.gui.manager;

import javax.swing.JPanel;

import edu.stevens.code.bilevel.app.ManagerApp;
import edu.stevens.code.bilevel.model.Manager;

/**
 * An abstract panel to display manager state and receive user inputs.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 */
public abstract class ManagerPanel extends JPanel {
	private static final long serialVersionUID = 5680166362732993135L;
	
	/**
	 * Observe a manager to reflect changes.
	 *
	 * @param manager the manager
	 */
	public abstract void observe(Manager manager);
	
	/**
	 * Bind to a manager to relay user input.
	 *
	 * @param manager the manager
	 */
	public abstract void bindTo(Manager manager);
	
	/**
	 * Bind to a manager application to replay user input.
	 *
	 * @param app the application
	 */
	public abstract void bindTo(ManagerApp app);
}
