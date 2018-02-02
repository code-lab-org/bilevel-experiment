package edu.stevens.code.ptg;

public class FakeValueMap {
	private final ValueMap realValueMap;
	
	public FakeValueMap(ValueMap realValueMap) {
		this.realValueMap = realValueMap;
	}
	
	public int[] getFakeValues(int strategy0, int strategy1, int design00, int design11, int design01, int design10) {
		// design_ij = player i's design under player j's strategy
		if(strategy0 == strategy1) {
			return realValueMap.getValues(strategy0, strategy1, design00, design11);
		} else {
			int[] values00 = realValueMap.getValues(strategy0, strategy0, design00, design10);
			int[] values11 = realValueMap.getValues(strategy1, strategy1, design01, design11);
			// fill in your function here
			return new int[]{(int) ((values00[0]+values11[0])/2d), (int) ((values00[1]+values11[1])/2d)};
		}
	}
}
