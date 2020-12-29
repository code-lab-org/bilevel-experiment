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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import edu.stevens.code.bilevel.app.ManagerApp;
import edu.stevens.code.bilevel.gui.designer.DebugDesignerPanel;
import edu.stevens.code.bilevel.model.Designer;

/**
 * A user interface for the manager application.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 */
public class ManagerUI extends ManagerAppPanel {
	private static final long serialVersionUID = -582914205975711140L;
	private ManagerPanel mPanel;
	private JPanel dPanels;
	
	/**
	 * Instantiates a new manager user interface.
	 */
	public ManagerUI() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		mPanel = new ManageUI();
		add(mPanel, c);
		c.gridx++;
		dPanels = new JPanel();
		dPanels.setLayout(new GridBagLayout());
		add(dPanels, c);
	}

	@Override
	public void bindTo(ManagerApp app) {
		mPanel.observe(app.getManager());
		mPanel.bindTo(app);
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		// add debug panels for each designer
		for(Designer designer : app.getDesigners()) {
			DebugDesignerPanel dPanel = new DebugDesignerPanel();
			dPanel.setAlwaysShare(true);
			dPanel.observe(designer);
			dPanels.add(dPanel, c);
			c.gridy++;
		}
	}
}
