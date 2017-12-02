package edu.stevens.code.ptg.gui;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;

public class DesignerAppPanel extends JPanel {
	private static final long serialVersionUID = -6514239839168558527L;

	public DesignerAppPanel(DesignerApp app) {
		setLayout(new FlowLayout());
		JPanel dPanels = new JPanel();
		dPanels.setLayout(new BoxLayout(dPanels, BoxLayout.Y_AXIS));
		for(Designer designer : app.getDesigners()) {
			DesignerPanel dPanel = new DesignerPanelImpl();
			if(!designer.equals(app.getController())) {
				dPanel.observe(designer);
			} else {
				dPanel.bindTo(designer);
			}
			dPanels.add(dPanel);
		}
		add(dPanels);
		ManagerPanelImpl mPanel = new ManagerPanelImpl();
		mPanel.observe(app.getManager());
		add(mPanel);
	}
}
