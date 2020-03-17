package edu.stevens.code.eager.gui;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import edu.stevens.code.eager.Designer;
import edu.stevens.code.eager.DesignerApp;

/**
 * The Class DebugDesignerAppPanel.
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
		mPanel = new ManagerPanelImpl();
		add(mPanel);
	}
	
	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.gui.DesignerAppPanel#bindTo(edu.stevens.code.ptg.DesignerApp)
	 */
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
