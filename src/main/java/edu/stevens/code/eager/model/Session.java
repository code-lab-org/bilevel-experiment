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
 * Session object model. A session contains the list of rounds to administer.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 * @author Ambrosio Valencia-Romero <avalenci@stevens.edu>
 */
public class Session {
	private String name;
	private Round[] rounds;
	
	/**
	 * Instantiates a new session.
	 */
	public Session() {
		this.rounds = new Round[0];
	}
	
	/**
	 * Gets the session name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the list of rounds.
	 *
	 * @return the rounds
	 */
	public Round[] getRounds() {
		return Arrays.copyOf(rounds, rounds.length);
	}
	
	/**
	 * Gets the round by index.
	 *
	 * @param index the index
	 * @return the round
	 */
	public Round getRound(int index) {
		if(index < 0 || index >= rounds.length) {
			throw new IllegalArgumentException("invalid round index");
		}
		return this.rounds[index];
	}
}
