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

/**
 * Fake Value Map object model. Provides a mapping between pairs of design/strategy 
 * decisions and the payoff value for each designer. Fake because it applies a
 * transformation of "diagonal" values rather than reading a stored value.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 * @author Ambrosio Valencia-Romero <avalenci@stevens.edu>
 */
public class FakeValueMap {
	private final ValueMap realValueMap;
	
	/**
	 * Instantiates a new fake value map.
	 *
	 * @param realValueMap the real value map
	 */
	public FakeValueMap(ValueMap realValueMap) {
		this.realValueMap = realValueMap;
	}
	
	/**
	 * Gets the fake payoff values for a set of strategy/design decisions for two designers.
	 *
	 * @param strategy0 the strategy 0
	 * @param strategy1 the strategy 1
	 * @param designs0 the designs 0
	 * @param designs1 the designs 1
	 * @return the fake values
	 */
	public int[] getFakeValues(int strategy0, int strategy1, int[] designs0, int[] designs1) {
		// design_ij = player i's design under player j's strategy
		if(strategy0 == strategy1) {
			// matching strategy decisions: use real value map
			return realValueMap.getValues(strategy0, strategy1, designs0[strategy0], designs1[strategy1]);
		} else if(strategy0 == 0) {
			int[] values00 = realValueMap.getValues(strategy0, strategy0, designs0[strategy0], designs1[strategy0]);
			int[] values11 = realValueMap.getValues(strategy1, strategy1, designs0[strategy1], designs1[strategy1]);
			boolean is_inverse = false;
			if (realValueMap.getName().length() > 0 
					&& realValueMap.getName().substring(realValueMap.getName().length() - 1).equals("i") ){
				is_inverse = true;
			}
			double S = getSuckersPayoff(realValueMap.getName().substring(0, 2), is_inverse);
			int[] values = new int[2];
			values[0] = Math.min(Math.max((int) Math.round(S*values00[0] + (1-S)*values11[0]), 0), 100);
			values[1] = Math.min(Math.max((int) Math.round(S*values00[1] + (1-S)*values11[1]), 0), 100);
			return values;
			
		} else if(strategy0 == 1) {
			int[] values00 = realValueMap.getValues(strategy1, strategy1, designs0[strategy1], designs1[strategy1]);
			int[] values11 = realValueMap.getValues(strategy0, strategy0, designs0[strategy0], designs1[strategy0]);
			boolean is_inverse = false;
			if (realValueMap.getName().length() > 0 
					&& realValueMap.getName().substring(realValueMap.getName().length() - 1).equals("i") ){
				is_inverse = true;
			}
			double T = getTemptationToDefect(realValueMap.getName().substring(0, 2), is_inverse);
			int[] values = new int[2];
			values[0] = Math.min(Math.max((int) Math.round(T*values00[0] + (1-T)*values11[0]), 0), 100);
			values[1] = Math.min(Math.max((int) Math.round(T*values00[1] + (1-T)*values11[1]), 0), 100);
			return values;
		}
		return new int[]{0,0};
	}
	
	/**
	 * Gets the suckers payoff.
	 *
	 * @param game_id the game id
	 * @param is_inverse the is inverse
	 * @return the suckers payoff
	 */
	private double getSuckersPayoff(String game_id, boolean is_inverse){
		if      (game_id.equals("CH") && is_inverse == false) { return  0.5; }
		else if (game_id.equals("CH") && is_inverse == true)  { return -0.5; }
		else if (game_id.equals("HA")) { return  1/3.; }
		else if (game_id.equals("PD")) { return -1.0 ; }
		else if (game_id.equals("SH") && is_inverse == false) { return -0.5; }
		else if (game_id.equals("SH") && is_inverse == true)  { return  0.5; }
		else if (game_id.equals("T1")) { return  1/3.; }
		else if (game_id.equals("T2")) { return  0.5 ; }
		else if (game_id.equals("T3")) { return -0.5 ; }
		else if (game_id.equals("T4")) { return -1.0 ; }
		else    { return 0.0; }
	}
	
	/**
	 * Gets the temptation to defect.
	 *
	 * @param game_id the game id
	 * @param is_inverse the is inverse
	 * @return the temptation to defect
	 */
	private double getTemptationToDefect(String game_id, boolean is_inverse){
		if      (game_id.equals("CH") && is_inverse == false) { return 1.5; }
		else if (game_id.equals("CH") && is_inverse == true)  { return 0.5; }
		else if (game_id.equals("HA")) { return 2/3.; }
		else if (game_id.equals("PD")) { return 2.0 ; }
		else if (game_id.equals("SH") && is_inverse == false) { return 0.5; }
		else if (game_id.equals("SH") && is_inverse == true)  { return 1.5; }
		else if (game_id.equals("T1")) { return  2/3.; }
		else if (game_id.equals("T2")) { return  1.5 ; }
		else if (game_id.equals("T3")) { return  0.5 ; }
		else if (game_id.equals("T4")) { return  2.0 ; }
		else    { return 1.0; }
	}
	
}
