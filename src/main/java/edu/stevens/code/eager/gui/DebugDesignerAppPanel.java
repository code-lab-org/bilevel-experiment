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

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import edu.stevens.code.eager.DesignerApp;
import edu.stevens.code.eager.model.Designer;

/**
 * A designer application panel that provides debug features.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 */
public class DebugDesignerAppPanel extends DesignerAppPanel {
	private static final long serialVersionUID = -3878809057023355653L;
	private JPanel dPanels;
	private ManagerPanel mPanel;
	
	/**
	 * Instantiates a new debug designer app panel.
	 */
	public DebugDesignerAppPanel() {
		setLayout(new FlowLayout());
		dPanels = new JPanel();
		dPanels.setLayout(new BoxLayout(dPanels, BoxLayout.Y_AXIS));
		add(dPanels);
		mPanel = new ManageUI();
		add(mPanel);
	}
	
	@Override
	public void bindTo(DesignerApp app) {
		for(Designer designer : app.getDesigners()) {
			DesignerPanel dPanel = new DebugDesignerPanel();
			if(!designer.equals(app.getController())) {
				dPanel.observe(designer);
			} else {
				dPanel.bindTo(app);
			}
			dPanels.add(dPanel);
		}
		mPanel.observe(app.getManager());
	}
}
