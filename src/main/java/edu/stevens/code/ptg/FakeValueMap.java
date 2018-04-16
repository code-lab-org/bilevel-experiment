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
		} else if (strategy0 == 0) {
			int[] values00 = realValueMap.getValues(strategy0, strategy0, design00, design10);
			int[] values11 = realValueMap.getValues(strategy1, strategy1, design01, design11);
			
			boolean is_inverse = false;
			if (realValueMap.getName().substring(realValueMap.getName().length() - 1).equals("i") ){
				is_inverse = true;
			}
			
			double S = Sucker(realValueMap.getName().substring(0, 2), is_inverse);
			double T = Temptation(realValueMap.getName().substring(0, 2), is_inverse);
			
			int[] values = new int[2];

			values[0] = (int) Math.round(S*values00[0] + (1-S)*values11[0]);
			values[1] = (int) Math.round(T*values00[1] + (1-T)*values11[1]);
			
			if (values[0] < 0){values[0] = 0;} else if (values[0] > 100){values[0] = 100;};
			if (values[1] < 0){values[1] = 0;} else if (values[1] > 100){values[1] = 100;};
			
			return values;
			
		} else if (strategy0 == 1) {
			int[] values00 = realValueMap.getValues(strategy1, strategy1, design01, design11);
			int[] values11 = realValueMap.getValues(strategy0, strategy0, design00, design10);
			
			boolean is_inverse = false;
			if (realValueMap.getName().length() > 0 && realValueMap.getName().substring(realValueMap.getName().length() - 1).equals("i") ){
				is_inverse = true;
			}

			double S = Sucker(realValueMap.getName().substring(0, 2), is_inverse);
			double T = Temptation(realValueMap.getName().substring(0, 2), is_inverse);
			
			int[] values = new int[2];
			
			values[0] = (int) Math.round(T*values00[0] + (1-T)*values11[0]);
			values[1] = (int) Math.round(S*values00[1] + (1-S)*values11[1]);
			
			if (values[0] < 0){values[0] = 0;} else if (values[0] > 100){values[0] = 100;};
			if (values[1] < 0){values[1] = 0;} else if (values[1] > 100){values[1] = 100;};
			
			return values;
		}
		return new int[]{0,0};
	}
	
	public double Sucker(String game_id, boolean is_inverse){
		
		if      (game_id.equals("CH") && is_inverse == false) { return  1.0; }
		else if (game_id.equals("CH") && is_inverse == true)  { return -1.0; }
		else if (game_id.equals("HA")) { return  1.0; }
		else if (game_id.equals("PD")) { return -1.0; }
		else if (game_id.equals("SH") && is_inverse == false) { return -1.0; }
		else if (game_id.equals("SH") && is_inverse == true)  { return  1.0; }
		else    { return -2/3.; }
	}
	
	public double Temptation(String game_id, boolean is_inverse){
		
		if      (game_id.equals("CH") && is_inverse == false) { return 2.0; }
		else if (game_id.equals("CH") && is_inverse == true)  { return 0.0; }
		else if (game_id.equals("HA")) { return 0.0; }
		else if (game_id.equals("PD")) { return 2.0; }
		else if (game_id.equals("SH") && is_inverse == false) { return 0.0; }
		else if (game_id.equals("SH") && is_inverse == true)  { return 2.0; }
		else    { return 1/3.; }
	}
	
}
