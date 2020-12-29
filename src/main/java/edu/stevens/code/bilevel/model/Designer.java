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
import java.util.Observable;

/**
 * Designer object class. A designer makes lower-level design decisions and 
 * upper-level strategy decisions. There is a unique lower-level decision for 
 * each strategy alternative.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 * @author Ambrosio Valencia-Romero <avalenci@stevens.edu>
 */
public class Designer extends Observable {
	public static final Object PROPERTY_ID = new Object();
	public static final Object PROPERTY_DESIGNS = new Object();
	public static final Object PROPERTY_STRATEGY = new Object();
	public static final Object PROPERTY_SHARE = new Object();
	
	public static final int NUM_STRATEGIES = 2;
	public static final int MIN_DESIGN_VALUE = 0;
	public static final int MAX_DESIGN_VALUE = 8;
	public static final int NUM_DESIGNS = MAX_DESIGN_VALUE - MIN_DESIGN_VALUE + 1;
	public static final int VALUE_DELTA = 1;
	
	private int id = -1;
	private int[] designs = new int[NUM_STRATEGIES];
	private int[] agreedDesigns = new int[NUM_STRATEGIES];
	private int strategy = 0;
	private boolean readyToShare = false;
	
	/**
	 * Instantiates a new designer.
	 */
	public Designer(){
		for(int i = 0; i < NUM_STRATEGIES; i++)
			designs[i] = NUM_DESIGNS/2;
	}
	
	/**
	 * Gets the designer id.
	 *
	 * @return the id
	 */
	public synchronized int getId() {
		return this.id;
	}
	
	/**
	 * Sets the designer id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		if(id < 0) {
			throw new IllegalArgumentException("invalid designer id");
		}
		synchronized(this) {
			if(this.id != id) {
				this.id = id;
				this.setChanged();
			}
		}
		this.notifyObservers(PROPERTY_ID);
	}
	
	/**
	 * Resets the design and strategy decisions.
	 */
	public void reset() {
		for(int index = 0; index < NUM_STRATEGIES; index++) {
			this.setDesign(index, 0);
		}
		this.setStrategy(0);
	}
	
	/**
	 * Gets the design decisions (one per strategy alternative).
	 *
	 * @return the designs
	 */
	public synchronized int[] getDesigns() {
		return Arrays.copyOf(designs, designs.length);
	}
	
	/**
	 * Gets the agreed design decisions (one per strategy alternative).
	 *
	 * @return the agreed designs
	 */
	public synchronized int[] getAgreedDesigns() {
		return Arrays.copyOf(agreedDesigns, agreedDesigns.length);
	}
	
	/**
	 * Gets the design decision for a given strategy index.
	 *
	 * @param index the strategy index
	 * @return the design
	 */
	public synchronized int getDesign(int index) {
		if(index < 0 || index >= NUM_STRATEGIES) {
			throw new IllegalArgumentException("invalid design index");
		}
		return this.designs[index];
	}
	
	/**
	 * Gets the agreed design decision for a given strategy index.
	 *
	 * @param index the strategy index
	 * @return the agreed design
	 */
	public synchronized int getAgreedDesign(int index) {
		if(index < 0 || index >= NUM_STRATEGIES) {
			throw new IllegalArgumentException("invalid agreed design index");
		}
		return this.agreedDesigns[index];
	}
	
	/**
	 * Sets the design decision for a given strategy index.
	 *
	 * @param index the strategy index
	 * @param value the value
	 */
	public void setDesign(int index, int value) {
		if(index < 0 || index >= NUM_STRATEGIES) {
			throw new IllegalArgumentException("invalid design index");
		}
		if(value < MIN_DESIGN_VALUE || value > MAX_DESIGN_VALUE) {
			throw new IllegalArgumentException("invalid design value");
		}
		synchronized(this) {
			if(this.designs[index] != value) {
				this.designs[index] = value;
				this.setChanged();
			}
		}
		this.notifyObservers(PROPERTY_DESIGNS);
	}
	
	/**
	 * Sets the agreed design decision for a given strategy index.
	 *
	 * @param index the strategy index
	 * @param value the value
	 */
	public void setAgreedDesign(int index, int value) {
		if(index < 0 || index >= NUM_STRATEGIES) {
			throw new IllegalArgumentException("invalid design index");
		}
		if(value < MIN_DESIGN_VALUE || value > MAX_DESIGN_VALUE) {
			throw new IllegalArgumentException("invalid design value");
		}
		synchronized(this) {
			if(this.agreedDesigns[index] != value) {
				this.agreedDesigns[index] = value;
				this.setChanged();
			}
		}
		this.notifyObservers(PROPERTY_DESIGNS);
	}
	
	/**
	 * Sets the design decisions (one per strategy alternative).
	 *
	 * @param value the values
	 */
	public void setDesigns(int[] values) {
		if(values.length != NUM_STRATEGIES) {
			throw new IllegalArgumentException("invalid number of designs");
		}
		for(int value : values) {
			if(value < MIN_DESIGN_VALUE || value > MAX_DESIGN_VALUE) {
				throw new IllegalArgumentException("invalid design value");
			}
		}
		synchronized(this) {
			if(!Arrays.equals(this.designs, values)) {
				this.designs = Arrays.copyOf(values, NUM_STRATEGIES);
				this.setChanged();
			}
		}
		this.notifyObservers(PROPERTY_DESIGNS);
	}
	
	/**
	 * Sets the agreed design decisions (one per strategy alternative).
	 *
	 * @param value the values
	 */
	public void setAgreedDesigns(int[] values) {
		if(values.length != NUM_STRATEGIES) {
			throw new IllegalArgumentException("invalid number of agreed designs");
		}
		for(int value : values) {
			if(value < MIN_DESIGN_VALUE || value > MAX_DESIGN_VALUE) {
				throw new IllegalArgumentException("invalid design value");
			}
		}
		synchronized(this) {
			if(!Arrays.equals(this.agreedDesigns, values)) {
				this.agreedDesigns = Arrays.copyOf(values, NUM_STRATEGIES);
				this.setChanged();
			}
		}
		this.notifyObservers(PROPERTY_DESIGNS);
	}
	
	/**
	 * Gets the strategy decision.
	 *
	 * @return the strategy
	 */
	public synchronized int getStrategy() {
		return this.strategy;
	}
	
	/**
	 * Sets the strategy decision.
	 *
	 * @param value the new strategy
	 */
	public void setStrategy(int value) {
		if(value < 0 || value >= NUM_STRATEGIES) {
			throw new IllegalArgumentException("invalid strategy value");
		}
		synchronized(this) {
			if(this.strategy != value) {
				this.strategy = value;
				this.setChanged();
			}
		}
		this.notifyObservers(PROPERTY_STRATEGY);
	}
	
	/**
	 * Checks if is ready to share.
	 *
	 * @return true, if is ready to share
	 */
	public synchronized boolean isReadyToShare() {
		return readyToShare;
	}
	
	/**
	 * Sets the ready to share.
	 *
	 * @param isReady the new ready to share
	 */
	public void setReadyToShare(boolean isReady) {
		synchronized(this) {
			if(this.readyToShare != isReady) {
				this.readyToShare = isReady;
				this.setChanged();
			}
		}
		this.notifyObservers(PROPERTY_SHARE);
	}
	
	@Override
	public String toString() {
		return "Designer " + id;
	}
}
