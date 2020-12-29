/******************************************************************************
 * Copyright 2020 Stevens Institute of Technology, Collective Design Lab
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.stevens.code.eager.model;

import java.util.Arrays;
import java.util.Observable;

/**
 * Manager object class. A manager maintains the list of current tasks and can
 * compute the payoff value for a designer based on the set of design/strategy
 * decisions.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 * @author Ambrosio Valencia-Romero <avalenci@stevens.edu>
 */
public class Manager extends Observable {
	public static final Object PROPERTY_ROUND = new Object();
	public static final Object PROPERTY_TIME = new Object();
	public static final Object PROPERTY_TASKS = new Object();
	public static final int STRATEGY_TIME = 15; // seconds
	public static final int MAX_TASK_TIME = 106 + STRATEGY_TIME; // seconds
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
	 * Sets the current round.
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
	 * Gets the time remaining (seconds).
	 *
	 * @return the time remaining
	 */
	public synchronized int getTimeRemaining() {
		return this.timeRemaining;
	}
	
	/**
	 * Sets the time remaining (seconds).
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
	 * Gets the task given by an index.
	 *
	 * @param index the task index
	 * @return the task
	 */
	public synchronized Task getTask(int index) {
		if(index < 0 || index >= NUM_TASKS) {
			throw new IllegalArgumentException("invalid task index");
		}
		return this.tasks[index];
	}
	
	/**
	 * Gets the list of current tasks.
	 *
	 * @return the tasks
	 */
	public synchronized Task[] getTasks() {
		return Arrays.copyOf(this.tasks, NUM_TASKS);
	}
	
	/**
	 * Gets the partner assigned to a designer (by designer id).
	 *
	 * @param designerId the designer id
	 * @return the design partner id
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
	 * Gets the task assigned to a designer (by designer id).
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
	 * Gets the payoff value for a designer (by designer id) for a set of design/strategy decisions.
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
		Task task = getTaskByDesignerId(designerId);
		if(task == null || task.getName().contentEquals("")) {
			return 0;
		} else {
			return task.getValue(designerId, strategy, design, partnerStrategy, partnerDesign);
		}
	}
	
	/**
	 * Checks if design is enabled.
	 *
	 * @return true, if is design enabled
	 */
	public boolean isDesignEnabled() {
		return getTimeRemaining() > 0 && getTimeRemaining() < Manager.MAX_TASK_TIME;
	}
	
	/**
	 * Sets the current task by index.
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
	 * Sets the current list of tasks.
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
	
	@Override
	public String toString() {
		return "Manager";
	}
}
