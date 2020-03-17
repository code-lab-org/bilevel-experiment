package edu.stevens.code.eager;

import java.util.Arrays;

/**
 * The Class Round.
 */
public class Round {
	private String name;
	private Task[] tasks;
	
	/**
	 * Instantiates a new round.
	 */
	public Round() {
		this.tasks = new Task[0];
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the tasks.
	 *
	 * @return the tasks
	 */
	public Task[] getTasks() {
		return Arrays.copyOf(tasks, tasks.length);
	}
	
	/**
	 * Gets the task.
	 *
	 * @param index the index
	 * @return the task
	 */
	public Task getTask(int index) {
		if(index < 0 || index >= tasks.length) {
			throw new IllegalArgumentException("invalid task index");
		}
		return this.tasks[index];
	}
}
