package edu.stevens.code.eager.model;

public class FakeValueMap {
	private final ValueMap realValueMap;
	
	public FakeValueMap(ValueMap realValueMap) {
		this.realValueMap = realValueMap;
	}
	
	public int[] getFakeValues(int strategy0, int strategy1, int[] designs0, int[] designs1) {
		// design_ij = player i's design under player j's strategy
		if(strategy0 == strategy1) {
			return realValueMap.getValues(strategy0, strategy1, designs0[strategy0], designs1[strategy1]);
		} else if (strategy0 == 0) {
			int[] values00 = realValueMap.getValues(strategy0, strategy0, designs0[strategy0], designs1[strategy0]);
			int[] values11 = realValueMap.getValues(strategy1, strategy1, designs0[strategy1], designs1[strategy1]);
			
			boolean is_inverse = realValueMap.getName().substring(realValueMap.getName().length() - 1).equals("i");
			
			double S = getSuckersPayoff(realValueMap.getName().substring(0, 2), is_inverse);
			double T = getTemptationToDefect(realValueMap.getName().substring(0, 2), is_inverse);
			
			int[] values = new int[2];

			values[0] = Math.min(Math.max((int) Math.round(S*values00[0] + (1-S)*values11[0]), 0), 100);
//			values[1] = Math.min(Math.max((int) Math.round(T*values00[1] + (1-T)*values11[1]), 0), 100);
			values[1] = Math.min(Math.max((int) Math.round(S*values00[1] + (1-S)*values11[1]), 0), 100);
			
			return values;
			
		} else if (strategy0 == 1) {
			int[] values00 = realValueMap.getValues(strategy1, strategy1, designs0[strategy1], designs1[strategy1]);
			int[] values11 = realValueMap.getValues(strategy0, strategy0, designs0[strategy0], designs1[strategy0]);
			
			boolean is_inverse = false;
			if (realValueMap.getName().length() > 0 && realValueMap.getName().substring(realValueMap.getName().length() - 1).equals("i") ){
				is_inverse = true;
			}

			double S = getSuckersPayoff(realValueMap.getName().substring(0, 2), is_inverse);
			double T = getTemptationToDefect(realValueMap.getName().substring(0, 2), is_inverse);
			
			int[] values = new int[2];
			
			values[0] = Math.min(Math.max((int) Math.round(T*values00[0] + (1-T)*values11[0]), 0), 100);
//			values[1] = Math.min(Math.max((int) Math.round(S*values00[1] + (1-S)*values11[1]), 0), 100);
			values[1] = Math.min(Math.max((int) Math.round(T*values00[1] + (1-T)*values11[1]), 0), 100);
			
			return values;
		}
		return new int[]{0,0};
	}
	
	private double getSuckersPayoff(String game_id, boolean is_inverse){
		if      (game_id.equals("CH") && is_inverse == false) { return  0.5; }// 1.0
		else if (game_id.equals("CH") && is_inverse == true)  { return -0.5; }//-1.0
		else if (game_id.equals("HA")) { return  1/3.; }// 1.0 ||  0.5
		else if (game_id.equals("PD")) { return -1.0 ; }//-1.0 || -0.5
		else if (game_id.equals("SH") && is_inverse == false) { return -0.5; }//-1.0
		else if (game_id.equals("SH") && is_inverse == true)  { return  0.5; }// 1.0
		else if (game_id.equals("T1")) { return  1/3.; }// 1.0 ||  0.5
		else if (game_id.equals("T2")) { return  0.5 ; }// 1.0 ||  0.5
		else if (game_id.equals("T3")) { return -0.5 ; }// 1.0 ||  0.5
		else if (game_id.equals("T4")) { return -1.0 ; }// 1.0 ||  0.5
		else    { return 0.0; }//-2/3.
	}
	
	private double getTemptationToDefect(String game_id, boolean is_inverse){
		if      (game_id.equals("CH") && is_inverse == false) { return 1.5; }// 2.0
		else if (game_id.equals("CH") && is_inverse == true)  { return 0.5; }// 0.0
		else if (game_id.equals("HA")) { return 2/3.; }// 0.0
		else if (game_id.equals("PD")) { return 2.0 ; }// 2.0 ||  1.5
		else if (game_id.equals("SH") && is_inverse == false) { return 0.5; }// 0.0
		else if (game_id.equals("SH") && is_inverse == true)  { return 1.5; }// 2.0
		else if (game_id.equals("T1")) { return  2/3.; }// 1.0 ||  0.5
		else if (game_id.equals("T2")) { return  1.5 ; }// 1.0 ||  0.5
		else if (game_id.equals("T3")) { return  0.5 ; }// 1.0 ||  0.5
		else if (game_id.equals("T4")) { return  2.0 ; }// 1.0 ||  0.5
		else    { return 1.0; }// 1/3.
	}
	
}
