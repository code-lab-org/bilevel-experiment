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
package edu.stevens.code.bilevel.io;

import edu.stevens.code.bilevel.app.DesignerApp;
import edu.stevens.code.bilevel.app.ManagerApp;
import edu.stevens.code.bilevel.model.Designer;
import edu.stevens.code.bilevel.model.Manager;

/**
 * The common interface to a middleware ambassador.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 */
public interface Ambassador {
	
	/**
	 * Connect a manager application to the middleware.
	 *
	 * @param managerApp the manager application
	 * @param federationName the federation name
	 */
	public void connectManager(ManagerApp managerApp, String federationName);
	
	/**
	 * Connect a designer application to the middleware.
	 *
	 * @param designerApp the designer application
	 * @param federationName the federation name
	 */
	public void connectDesigner(DesignerApp designerApp, String federationName);
	
	/**
	 * Callback function to process an update for a manager.
	 *
	 * @param manager the manager
	 * @param properties the properties
	 */
	public void updateManager(Manager manager, Object... properties);
	
	/**
	 * Callback function to process an update for a designer.
	 *
	 * @param designer the designer
	 * @param properties the properties
	 */
	public void updateDesigner(Designer designer, Object... properties);
	
	/**
	 * Disconnects from the middleware.
	 */
	public void disconnect();
}
