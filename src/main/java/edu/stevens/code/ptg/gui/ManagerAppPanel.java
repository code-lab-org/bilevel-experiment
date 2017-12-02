package edu.stevens.code.ptg.gui;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.ManagerApp;

public class ManagerAppPanel extends JPanel {
	private static final long serialVersionUID = 8811096233222838370L;

	public ManagerAppPanel(ManagerApp app) {
		setLayout(new FlowLayout());
		ManagerPanel mPanel = new ManagerPanelImpl();
		mPanel.observe(app.getManager());
		mPanel.bindTo(app);
		add(mPanel);
		JPanel dPanels = new JPanel();
		dPanels.setLayout(new BoxLayout(dPanels, BoxLayout.Y_AXIS));
		for(Designer designer : app.getDesigners()) {
			DesignerPanel dPanel = new DesignerPanelImpl();
			dPanel.observe(designer);
			dPanels.add(dPanel);
		}
		add(dPanels);
	}
}
