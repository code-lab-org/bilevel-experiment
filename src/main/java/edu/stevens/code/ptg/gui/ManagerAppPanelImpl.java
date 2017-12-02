package edu.stevens.code.ptg.gui;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.ManagerApp;

public class ManagerAppPanelImpl extends ManagerAppPanel {
	private static final long serialVersionUID = -582914205975711140L;
	private ManagerPanel mPanel;
	private JPanel dPanels;
	
	public ManagerAppPanelImpl() {
		setLayout(new FlowLayout());
		mPanel = new ManagerPanelImpl();
		add(mPanel);
		dPanels = new JPanel();
		dPanels.setLayout(new BoxLayout(dPanels, BoxLayout.Y_AXIS));
		add(dPanels);
	}

	@Override
	public void bindTo(ManagerApp app) {
		mPanel.observe(app.getManager());
		mPanel.bindTo(app);
		for(Designer designer : app.getDesigners()) {
			DesignerPanel dPanel = new DesignerPanelImpl();
			dPanel.observe(designer);
			dPanels.add(dPanel);
		}
	}
}
