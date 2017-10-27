package edu.stevens.code.ptg;

import java.util.Observable;

public class Manager extends Observable {
	private static final int NUM_DESIGNERS = 4;
	
	private String taskName;
	private int[] payoffs;
	
	public Manager() {
		this.taskName = "Initializing";
		this.payoffs = new int[NUM_DESIGNERS];
	}
	
	public synchronized String getTaskName() {
		return this.taskName;
	}
	
	public void setTaskName(String name) {
		synchronized(this) {
			if(this.taskName != name) {
				this.taskName = name;
			}
			this.setChanged();
		}
		this.notifyObservers();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Manager";
	}
}
