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

/**
 * Round object model. The round contains all of the tasks to assign to designers.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 * @author Ambrosio Valencia-Romero <avalenci@stevens.edu>
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
	 * Gets the round name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the list of tasks.
	 *
	 * @return the tasks
	 */
	public Task[] getTasks() {
		return Arrays.copyOf(tasks, tasks.length);
	}
	
	/**
	 * Gets the task by index.
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
