package edu.stevens.code.ptg;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

/**
 * The Class Manager.
 */
public class Manager extends Observable {
	public static final Object PROPERTY_ROUND = new Object();
	public static final Object PROPERTY_TIME = new Object();
	public static final Object PROPERTY_TASKS = new Object();
	public static final int MAX_TASK_TIME = 300;
	public static final int NUM_DESIGNERS = 2; /* AMVRO: It should be 4; check Main 77-78 in PTG package */
	public static final int NUM_TASKS = 2;
	
	private String roundName = "Initializing";
	private int timeRemaining = -1;
	private final Task[] tasks = new Task[NUM_TASKS];
	
	/**
	 * Instantiates a new manager.
	 */
	public Manager() {
		for(int i = 0; i < NUM_TASKS; i++) {
			this.tasks[i] = new Task();
			this.tasks[i].addObserver(new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					setChanged();
					notifyObservers(PROPERTY_TASKS);
				}
			});
		}
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
	
	public int getDesignPartner(int designerId) {
		if(designerId < 0 || designerId >= NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid designer id");
		}
		for(Task task : tasks) {
			// hard code only two designers per task
			if(Task.NUM_DESIGNERS != 2) {
				throw new RuntimeException("assumption violated!!!!!");
			}
			if(task.getDesignerId(0)==designerId) {
				return task.getDesignerId(1);
			}
			if(task.getDesignerId(1)==designerId) {
				return task.getDesignerId(0);
			}
		}
		return -1;
	}
	
	/**
	 * Sets the task.
	 *
	 * @param index the index
	 * @param task the task
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
	 */
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Manager";
	}
}
