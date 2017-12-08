package edu.stevens.code.ptg;

import java.util.Arrays;

/**
 * The Class Task.
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
		this.designerIds = new int[Manager.NUM_DESIGNERS];
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
}
