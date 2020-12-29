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
package edu.stevens.code.eager.gui;

import javax.swing.JPanel;

import edu.stevens.code.eager.DesignerApp;
import edu.stevens.code.eager.model.Designer;

/**
 * An abstract panel to display designer state and receive user inputs.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
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
