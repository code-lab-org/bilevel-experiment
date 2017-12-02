package edu.stevens.code.ptg.gui;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;

/**
 * The Class DesignerAppPanelImpl.
 */
public class DesignerAppPanelImpl extends DesignerAppPanel {
	private static final long serialVersionUID = -3878809057023355653L;
	private JPanel dPanels;
	private ManagerPanel mPanel;
	
	/**
	 * Instantiates a new designer app panel impl.
	 */
	public DesignerAppPanelImpl() {
		setLayout(new FlowLayout());
		dPanels = new JPanel();
		dPanels.setLayout(new BoxLayout(dPanels, BoxLayout.Y_AXIS));
		add(dPanels);
		mPanel = new ManagerPanelImpl();
		add(mPanel);
	}
	
	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.gui.DesignerAppPanel#bindTo(edu.stevens.code.ptg.DesignerApp)
	 */
	@Override
	public void bindTo(DesignerApp app) {
		for(Designer designer : app.getDesigners()) {
			DesignerPanel dPanel = new DesignerPanelImpl();
			if(!designer.equals(app.getController())) {
				dPanel.observe(designer);
			} else {
				dPanel.bindTo(designer);
			}
			dPanels.add(dPanel);
		}
		mPanel.observe(app.getManager());
	}
}
