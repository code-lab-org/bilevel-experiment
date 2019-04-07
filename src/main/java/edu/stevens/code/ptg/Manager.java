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
	public static final int STRATEGY_TIME = 15; /* 0.25 minutes (previously 1/3 minutes) */
	public static final int MAX_TASK_TIME = 45 + STRATEGY_TIME; /* 1.75 minutes (previously 2.0) + STRATEGY_TIME */
	public static final int NUM_DESIGNERS = 4;
	public static final int NUM_TASKS = 2;
	
	private String roundName = "";
	private int timeRemaining = -1;
	private Task[] tasks = new Task[NUM_TASKS];
	
	/**
	 * Instantiates a new manager.
	 */
	public Manager() {
		for(int i = 0; i < NUM_TASKS; i++) {
			this.tasks[i] = new Task();
		}
	}
	
	/**
	 * Sets the round.
	 *
	 * @param round the new round
	 */
	public void setRound(Round round) {
		if(round.getTasks().length != NUM_TASKS) {
			throw new IllegalArgumentException("invalid number of tasks");
		}
		for(int i = 0; i < NUM_TASKS; i++) {
			for(int j = 0; j < round.getTask(i).getDesignerIds().length; j++) {
				if(round.getTask(i).getDesignerId(j) < 0 
						|| round.getTask(i).getDesignerId(j) >= NUM_DESIGNERS) {
					throw new IllegalArgumentException("invalid designer id");
				}
			}
		}
		synchronized(this) {
			this.setRoundName(round.getName());
			this.setTimeRemaining(MAX_TASK_TIME);
			Task[] tasks = new Task[NUM_TASKS];
			for(int i = 0; i < NUM_TASKS; i++) {
				tasks[i] = new Task(round.getTask(i).getName(), round.getTask(i).getDesignerIds());
			}
			this.setTasks(tasks);
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
	
	/**
	 * Gets the design partner.
	 *
	 * @param designerId the designer id
	 * @return the design partner
	 */
	public synchronized int getDesignPartner(int designerId) {
		if(designerId < 0 || designerId >= NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid designer id");
		}
		for(Task task : tasks) {
			// hard code only two designers per task
			if(task.getDesignerId(0) == designerId) {
				return task.getDesignerId(1);
			}
			if(task.getDesignerId(1) == designerId) {
				return task.getDesignerId(0);
			}
		}
		return -1;
	}
	
	/**
	 * Gets the task by designer id.
	 *
	 * @param designerId the designer id
	 * @return the task
	 */
	public synchronized Task getTaskByDesignerId(int designerId) {
		if(designerId < 0 || designerId >= NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid designer id");
		}
		for(Task task : tasks) {
			for(int id : task.getDesignerIds()) {
				if(id == designerId) {
					return task;
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets the value.
	 *
	 * @param designerId the designer id
	 * @param strategy the strategy
	 * @param design the design
	 * @param partnerStrategy the partner strategy
	 * @param partnerDesign the partner design
	 * @return the value
	 */
	public synchronized int getValue(int designerId, int strategy, 
			int[] design, int partnerStrategy, int[] partnerDesign) {
		return getTaskByDesignerId(designerId).getValue(designerId, strategy, design, partnerStrategy, partnerDesign);
	}
	
	/**
	 * Checks if is design enabled.
	 *
	 * @return true, if is design enabled
	 */
	public boolean isDesignEnabled() {
		return getTimeRemaining() > 0 && getTimeRemaining() < Manager.MAX_TASK_TIME;
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
