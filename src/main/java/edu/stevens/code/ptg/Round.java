package edu.stevens.code.ptg;

import java.util.Arrays;

public class Round {
	private String name;
	private Task[] tasks;
	
	public Round() {
		this.tasks = new Task[0];
	}
	
	public String getName() {
		return this.name;
	}
	
	public Task[] getTasks() {
		return Arrays.copyOf(tasks, tasks.length);
	}
	
	public Task getTask(int index) {
		if(index < 0 || index >= tasks.length) {
			throw new IllegalArgumentException("invalid task index");
		}
		return this.tasks[index];
	}
}
