package edu.stevens.code.ptg;

import java.util.Arrays;
import java.util.Observable;

/**
 * The Class Manager.
 */
public class Manager extends Observable {
	public static final Object PROPERTY_ROUND = new Object();
	public static final Object PROPERTY_TIME = new Object();
	public static final Object PROPERTY_TASKS = new Object();
	public static final int MAX_TASK_TIME = 300;
	public static final int NUM_DESIGNERS = 4;
	public static final int NUM_TASKS = 2;
	
	private String roundName;
	private int timeRemaining;
	private Task[] tasks;
	
	/**
	 * Instantiates a new manager.
	 */
	public Manager() {
		this.roundName = "Initializing";
		this.timeRemaining = -1;
		this.tasks = new Task[NUM_TASKS];
	}
	
	/**
	 * Gets the round name.
	 *
	 * @return the round name
	 */
	public synchronized String getRoundName() {
		return this.roundName;
	}
	
	/**
	 * Sets the round name.
	 *
	 * @param name the new round name
	 */
	public void setRoundName(String name) {
		synchronized(this) {
			if(this.roundName != name) {
				this.roundName = name;
				this.setChanged();
			}
		}
		this.notifyObservers(PROPERTY_ROUND);
	}
	
	/**
	 * Gets the time remaining.
	 *
	 * @return the time remaining
	 */
	public synchronized int getTimeRemaining() {
		return this.timeRemaining;
	}
	
	/**
	 * Sets the time remaining.
	 *
	 * @param timeRemaining the new time remaining
	 */
	public void setTimeRemaining(int timeRemaining) {
		synchronized(this) {
			if(this.timeRemaining != timeRemaining) {
				this.timeRemaining = timeRemaining;
				this.setChanged();
			}
		}
		this.notifyObservers(PROPERTY_TIME);
	}
	
	/**
	 * Gets the task.
	 *
	 * @param index the index
	 * @return the task
	 */
	public synchronized Task getTask(int index) {
		if(index < 0 || index >= NUM_TASKS) {
			throw new IllegalArgumentException("invalid task index");
		}
		return this.tasks[index];
	}
	
	/**
	 * Gets the tasks.
	 *
	 * @return the tasks
	 */
	public synchronized Task[] getTasks() {
		return Arrays.copyOf(this.tasks, NUM_TASKS);
	}
	
	/**
	 * Sets the task.
	 *
	 * @param index the index
	 * @param task the task
	 */
	public void setTask(int index, Task task) {
		if(index < 0 || index >= NUM_TASKS) {
			throw new IllegalArgumentException("invalid task index");
		}
		synchronized(this) {
			if(this.tasks[index] != task) {
				this.tasks[index] = task;
				this.setChanged();
			}
		}
		this.notifyObservers(PROPERTY_TASKS);
	}
	
	/**
	 * Sets the tasks.
	 *
	 * @param tasks the new tasks
	 */
	public void setTasks(Task[] tasks) {
		if(tasks.length != NUM_TASKS) {
			throw new IllegalArgumentException("invalid number of tasks");
		}
		synchronized(this) {
			if(!Arrays.deepEquals(this.tasks, tasks)) {
				this.tasks = Arrays.copyOf(tasks, NUM_TASKS);
				this.setChanged();
			}
		}
		this.notifyObservers(PROPERTY_TASKS);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Manager";
	}
}
