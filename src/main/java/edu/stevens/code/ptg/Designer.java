package edu.stevens.code.ptg;

import java.util.Arrays;
import java.util.Observable;

/**
 * The Class Designer.
 */
public class Designer extends Observable {
	public static final Object PROPERTY_ID = new Object();
	public static final Object PROPERTY_DESIGNS = new Object();
	public static final Object PROPERTY_STRATEGY = new Object();
	public static final Object PROPERTY_SHARE = new Object();
	
	public static final int NUM_STRATEGIES = 2;
	public static final int MIN_DESIGN_VALUE = 0;
	public static final int MAX_DESIGN_VALUE = 6; /* 7 */
	public static final int NUM_DESIGNS = MAX_DESIGN_VALUE - MIN_DESIGN_VALUE + 1;
	
	private int id = -1;
	private int[] designs = new int[NUM_STRATEGIES];
	private int[] agreedDesigns = new int[NUM_STRATEGIES];
	private int strategy = 0;
	private boolean readyToShare = false;
	
	public Designer(){
		for(int i = 0; i < NUM_STRATEGIES; i++)
			designs[i] = NUM_DESIGNS/2;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public synchronized int getId() {
		return this.id;
	}
	
	/**
	 * Sets the id.
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
	 * Resets this designer.
	 */
	public void reset() {
		for(int index = 0; index < NUM_STRATEGIES; index++) {
			this.setDesign(index, 0);
		}
		this.setStrategy(0);
	}
	
	/**
	 * Gets the designs.
	 *
	 * @return the designs
	 */
	public synchronized int[] getDesigns() {
		return Arrays.copyOf(designs, designs.length);
	}
	
	/**
	 * Gets the agreed designs.
	 *
	 * @return the agreed designs
	 */
	public synchronized int[] getAgreedDesigns() {
		return Arrays.copyOf(agreedDesigns, agreedDesigns.length);
	}
	
	/**
	 * Gets the design.
	 *
	 * @param index the index
	 * @return the design
	 */
	public synchronized int getDesign(int index) {
		if(index < 0 || index >= NUM_STRATEGIES) {
			throw new IllegalArgumentException("invalid design index");
		}
		return this.designs[index];
	}
	
	/**
	 * Gets the agreed design.
	 *
	 * @param index the index
	 * @return the agreed design
	 */
	public synchronized int getAgreedDesign(int index) {
		if(index < 0 || index >= NUM_STRATEGIES) {
			throw new IllegalArgumentException("invalid agreed design index");
		}
		return this.agreedDesigns[index];
	}
	
	/**
	 * Sets the design.
	 *
	 * @param index the index
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
	 * Sets the agreed design.
	 *
	 * @param index the index
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
	 * Sets the designs.
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
	 * Sets the agreed designs.
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
	 * Gets the strategy.
	 *
	 * @return the strategy
	 */
	public synchronized int getStrategy() {
		return this.strategy;
	}
	
	/**
	 * Sets the strategy.
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Designer " + id;
	}
}
