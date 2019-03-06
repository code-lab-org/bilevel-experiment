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
	private ValueMap getValueMap() {
		// simple cache to avoid re-loading value map
		if(valueMap == null || !valueMap.getName().equals(getName())) {
			valueMap = new ValueMap(getName());
		}
		return valueMap;
	}
	
	/**
	 * Gets the task value.
	 * 
	 * @param designerId
	 * @param myStrategy
	 * @param myDesigns
	 * @param partnerStrategy
	 * @param partnerDesigns
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
}
