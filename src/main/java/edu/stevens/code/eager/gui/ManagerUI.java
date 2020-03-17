package edu.stevens.code.eager.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import edu.stevens.code.eager.Designer;
import edu.stevens.code.eager.ManagerApp;

/**
 * The Class ManagerUI.
 */
public class ManagerUI extends ManagerAppPanel {
	private static final long serialVersionUID = -582914205975711140L;
	private ManagerPanel mPanel;
	private JPanel dPanels;
	
	/**
	 * Instantiates a new manager app panel impl.
	 */
	public ManagerUI() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		mPanel = new ManagerPanelImpl();
		add(mPanel, c);
		c.gridx++;
		dPanels = new JPanel();
		dPanels.setLayout(new GridBagLayout());
		add(dPanels, c);
	}

	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.gui.ManagerAppPanel#bindTo(edu.stevens.code.ptg.ManagerApp)
	 */
	@Override
	public void bindTo(ManagerApp app) {
		mPanel.observe(app.getManager());
		mPanel.bindTo(app);
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		for(Designer designer : app.getDesigners()) {
			DebugDesignerPanel dPanel = new DebugDesignerPanel();
			dPanel.setAlwaysShare(true);
			dPanel.observe(designer);
			dPanels.add(dPanel, c);
			c.gridy++;
		}
	}
}
