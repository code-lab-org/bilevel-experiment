package edu.stevens.code.ptg.io;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;
import edu.stevens.code.ptg.Manager;
import edu.stevens.code.ptg.ManagerApp;

public interface Ambassador {
	public void connectManager(ManagerApp managerApp, String federationName);
	public void connectDesigner(DesignerApp designerApp, String federationName);
	public void updateManager(Manager manager, Object... properties);
	public void updateDesigner(Designer designer, Object... properties);
	public void disconnect();
}
