package edu.stevens.code.ptg.gui;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.ManagerApp;

/**
 * The Class ManagerAppPanelImpl.
 */
public class ManagerAppPanelImpl extends ManagerAppPanel {
	private static final long serialVersionUID = -582914205975711140L;
	private ManagerPanel mPanel;
	private JPanel dPanels;
	
	/**
	 * Instantiates a new manager app panel impl.
	 */
	public ManagerAppPanelImpl() {
		setLayout(new FlowLayout());
		mPanel = new ManagerPanelImpl();
		add(mPanel);
		dPanels = new JPanel();
		dPanels.setLayout(new BoxLayout(dPanels, BoxLayout.Y_AXIS));
		add(dPanels);
	}

	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.gui.ManagerAppPanel#bindTo(edu.stevens.code.ptg.ManagerApp)
	 */
	@Override
	public void bindTo(ManagerApp app) {
		mPanel.observe(app.getManager());
		mPanel.bindTo(app);
		for(Designer designer : app.getDesigners()) {
			DesignerPanel dPanel = new DebugDesignerPanel();
			dPanel.observe(designer);
			dPanels.add(dPanel);
		}
	}
}
