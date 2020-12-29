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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Value Map object model. Provides a mapping between pairs of design/strategy 
 * decisions and the payoff value for each designer.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 * @author Ambrosio Valencia-Romero <avalenci@stevens.edu>
 */
public class ValueMap {
    private static final Logger logger = LogManager.getLogger(ValueMap.class);
    private final String name;
	private final int[][][][][] values = new int[Task.NUM_DESIGNERS][Designer.NUM_STRATEGIES][Designer.NUM_STRATEGIES][Designer.NUM_DESIGNS][Designer.NUM_DESIGNS];
	
	/**
	 * Instantiates a new value map.
	 *
	 * @param name the name
	 */
	public ValueMap(String name) {
		this.name = name;
		if(name == null || name.equals("")) {
			return;
		}
		try(BufferedReader br = new BufferedReader(new InputStreamReader(
				getClass().getClassLoader().getResourceAsStream("games/" + name + ".csv")))) {
			for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
				for(int j = 0; j < Designer.NUM_STRATEGIES; j++) {
					for(int k = 0; k < Designer.NUM_DESIGNS; k++) {
						// index u = complement to index k in 'modulo NUM_DESIGNS'
						int u = (Designer.MAX_DESIGN_VALUE - k) % Designer.NUM_DESIGNS;
						String[] row = br.readLine().split(",");
						for(int l = 0; l < Designer.NUM_DESIGNS; l++) {
							// index v = complement to index l in 'modulo NUM_DESIGNS'
							int v = (Designer.MAX_DESIGN_VALUE - l) % Designer.NUM_DESIGNS;
							// assume symmetric value map
							// flip player 0's value map U-D by replacing k with u in values
							this.values[0][i][j][l][k] = Integer.parseInt(row[l]);
							// flip player 1's value map L-R by replacing l with v in values
							this.values[1][j][i][u][v] = Integer.parseInt(row[l]);
						}
					}
				}
			}
		} catch (IOException e) {
			logger.warn(e);
		}
	}
	
	/**
	 * Gets the name of this value map.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the payoff values for the two designers as a function of the 
	 * strategy/design decisions.
	 *
	 * @param strategy0 the strategy0
	 * @param strategy1 the strategy1
	 * @param design0 the design0
	 * @param design1 the design1
	 * @return the values
	 */
	public int[] getValues(int strategy0, int strategy1, int design0, int design1) {
		// return value array, taking care to flip the design space for designer index 1
		return new int[] {
			values[0][strategy0][strategy1][design0][design1],
			values[1][strategy0][strategy1][design1][design0]
		};
	}
}
