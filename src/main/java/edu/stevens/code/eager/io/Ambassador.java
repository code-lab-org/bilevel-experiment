package edu.stevens.code.eager.io;

import edu.stevens.code.eager.DesignerApp;
import edu.stevens.code.eager.ManagerApp;
import edu.stevens.code.eager.model.Designer;
import edu.stevens.code.eager.model.Manager;

public interface Ambassador {
	public void connectManager(ManagerApp managerApp, String federationName);
	public void connectDesigner(DesignerApp designerApp, String federationName);
	public void updateManager(Manager manager, Object... properties);
	public void updateDesigner(Designer designer, Object... properties);
	public void disconnect();
}
