package edu.stevens.code.ptg;

import java.util.Arrays;
import java.util.Observable;

/**
 * The Class Task.
 */
public class Task extends Observable {
	public static final Object PROPERTY_NAME = new Object();
	public static final Object PROPERTY_DESIGNER_IDS = new Object();
	public static final int NUM_DESIGNERS = 2;
	
	private String name = "";
	private int[] designerIds = new int[Manager.NUM_DESIGNERS];
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public synchronized String getName() {
		return this.name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		synchronized(this) {
			if(this.name != name) {
				this.name = name;
				this.setChanged();
			}
		}
		this.notifyObservers(PROPERTY_NAME);
	}
	
	/**
	 * Gets the designer id.
	 *
	 * @param index the index
	 * @return the designer id
	 */
	public synchronized int getDesignerId(int index) {
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
	public synchronized int[] getDesignerIds() {
		return Arrays.copyOf(this.designerIds, NUM_DESIGNERS);
	}
	
	/**
	 * Sets the designer id.
	 *
	 * @param index the index
	 * @param id the id
	 */
	public void setDesignerId(int index, int id) {
		if(index < 0 || index > NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid designer index");
		}
		if(id < 0 || id >= Manager.NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid designer id");
		}
		synchronized(this) {
			if(this.designerIds[index] != id) {
				this.designerIds[index] = id;
				this.setChanged();
			}
		}
		this.notifyObservers(PROPERTY_DESIGNER_IDS);
	}
	
	/**
	 * Sets the designer ids.
	 *
	 * @param ids the new designer ids
	 */
	public void setDesignerIds(int[] ids) {
		if(ids.length != NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid number of designers");
		}
		for(int id : ids) {
			if(id < 0 || id >= Manager.NUM_DESIGNERS) {
				throw new IllegalArgumentException("invalid designer id");
			}
		}
		synchronized(this) {
			if(!Arrays.equals(this.designerIds, ids)) {
				this.designerIds = Arrays.copyOf(ids, NUM_DESIGNERS);
				this.setChanged();
			}
		}
		this.notifyObservers(PROPERTY_DESIGNER_IDS);
	}

	// cached value map
	private transient ValueMap valueMap = null;
	
	/**
	 * Gets the value map.
	 *
	 * @return the value map
	 */
	public ValueMap getValueMap() {
		// simple cache to avoid re-loading value map
		if(valueMap == null || !valueMap.getName().equals(getName())) {
			valueMap = new ValueMap(getName());
		}
		return valueMap;
	}
	
	/**
	 * Gets the value.
	 *
	 * @param id0 the designer id 0
	 * @param strategy0 the strategy0
	 * @param design0 the design0
	 * @param id1 the designer id 1
	 * @param strategy1 the strategy1
	 * @param design1 the design1
	 * @return the value
	 */
	public int getValue(int id0, int strategy0, int design0, int id1, int strategy1, int design1) {
		if(id0 < 0 || id0 >= Manager.NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid designer id");
		}
		if(id1 < 0 || id1 >= Manager.NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid designer id");
		}
		if(id0 == id1) {
			throw new IllegalArgumentException("invalid designer ids for task");
		}
		for(int id : designerIds) {
			if(id != id0 && id != id1) {
				throw new IllegalArgumentException("invalid designer ids for task");
			}
		}
		if(designerIds[0] == id0) {
			return getValueMap().getValues(strategy0, strategy1, design0, design1)[0];
		} else {
			return getValueMap().getValues(strategy1, strategy0, design1, design0)[1];
		}
	}
}
