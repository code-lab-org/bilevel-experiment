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
package edu.stevens.code.bilevel.model;

import java.util.Arrays;

/**
 * Task object model. Identifies the pair of assigned designers.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 * @author Ambrosio Valencia-Romero <avalenci@stevens.edu>
 */
public class Task {
	public static final int NUM_DESIGNERS = 2;
	
	private final String name;
	private final int[] designerIds;
	
	/**
	 * Instantiates a new task.
	 */
	public Task() {
		this.name = "";
		this.designerIds = new int[Task.NUM_DESIGNERS];
	}
	
	/**
	 * Instantiates a new task.
	 *
	 * @param name the name
	 * @param designerIds the designer ids
	 */
	public Task(String name, int[] designerIds) {
		this.name = name;
		this.designerIds = Arrays.copyOf(designerIds, designerIds.length);
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
	 * Gets the designer id.
	 *
	 * @param index the index
	 * @return the designer id
	 */
	public int getDesignerId(int index) {
		if(index < 0 || index >= NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid designer index");
		}
		return this.designerIds[index];
	}
	
	/**
	 * Gets the designer ids.
	 *
	 * @return the designer ids
	 */
	public int[] getDesignerIds() {
		return Arrays.copyOf(this.designerIds, NUM_DESIGNERS);
	}

	// cached value map
	private transient ValueMap valueMap = null;
	
	/**
	 * Gets the value map.
	 *
	 * @return the value map
	 */
	private ValueMap getValueMap() {
		// simple cache to avoid re-loading value map
		if(valueMap == null || !valueMap.getName().equals(getName())) {
			valueMap = new ValueMap(getName());
		}
		return valueMap;
	}
	
	/**
	 * Gets the payoff value for a designer as a function of the design/strategy decisions.
	 *
	 * @param designerId the designer id
	 * @param myStrategy the my strategy
	 * @param myDesigns the my designs
	 * @param partnerStrategy the partner strategy
	 * @param partnerDesigns the partner designs
	 * @return the value
	 */
	public int getValue(int designerId, int myStrategy, int[] myDesigns, int partnerStrategy, int[] partnerDesigns) {
		if(designerId < 0 || designerId >= Manager.NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid designer id");
		}
		int index = designerIds[0] == designerId ? 0 : 1;
		if(myStrategy == partnerStrategy) {
			return getValueMap().getValues(myStrategy, partnerStrategy, myDesigns[myStrategy], partnerDesigns[partnerStrategy])[index];
		} else {
			return new FakeValueMap(getValueMap()).getFakeValues(myStrategy, partnerStrategy, myDesigns, partnerDesigns)[index];
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
}
