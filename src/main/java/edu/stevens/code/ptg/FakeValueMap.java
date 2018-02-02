package edu.stevens.code.ptg;

public class FakeValueMap {
	private final ValueMap realValueMap;
	private double S;
	private double T;
	
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
			
			String SS = realValueMap.getName().substring(0, 2);
			
			if      (SS == "CH") {S =  0.5; T = 1.5; }
			else if (SS == "HA") {S =  0.5; T = 0.5; }
			else if (SS == "PD") {S = -0.5; T = 1.5; }
			else if (SS == "SH") {S = -0.5; T = 0.5; }
			
			int v01 = (int) Math.round( S*values00[0] + (1-S)*values11[0] );
			int v10 = (int) Math.round( T*values00[0] + (1-T)*values11[0] );
			
			
			return new int[]{v01, v10};
		}
	}
}
