package edu.stevens.code.ptg;

public interface App {
	public Designer[] getDesigners();
	public Designer getDesigner(int index);
	public Manager getManager();
	public Object getSelf();
	public void init(String federationName);
	public void kill();
}
