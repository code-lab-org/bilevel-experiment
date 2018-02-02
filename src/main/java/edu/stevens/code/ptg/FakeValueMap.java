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
			
			// values00[0]: value for designer 0 under shared strategy 0
			// values00[1]: value for designer 1 under shared strategy 0
			// values11[0]: value for designer 0 under shared strategy 1
			// values11[1]: value for designer 1 under shared strategy 1
			
			String SS = realValueMap.getName().substring(0, 2);

			double S = 0;
			double T = 0;
			
			if      (SS.equals("CH")) {S =  0.5; T = 1.5; }
			else if (SS.equals("HA")) {S =  0.5; T = 0.5; }
			else if (SS.equals("PD")) {S = -0.5; T = 1.5; }
			else if (SS.equals("SH")) {S = -0.5; T = 0.5; }
			
			int[] values = new int[2];

			// value[0]: value for designer 0 under mixed strategy (strategy0, strategy1)
			// value[1]: value for designer 1 under mixed strategy (strategy0, strategy1)
			
			values[0] = (int) Math.round(strategy0==0 ? S*values00[0] + (1-S)*values11[0] : T*values00[0] + (1-T)*values11[0]);
			values[1] = (int) Math.round(strategy0==0 ? S*values00[1] + (1-S)*values11[1] : T*values00[1] + (1-T)*values11[1]);
			
			if(strategy0 == 0) {
				values[0] = 10;
				values[1] = 20;
			} else {
				values[0] = 30;
				values[1] = 40;
			}
			
			return values;
		}
	}
}
