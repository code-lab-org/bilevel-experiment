package edu.stevens.code.ptg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Class ValueMap.
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
		try(BufferedReader br = new BufferedReader(new FileReader("src/test/java/games/"+name+".csv"))) {
			for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
				for(int j = 0; j < Designer.NUM_STRATEGIES; j++) {
					for(int k = 0; k < Designer.NUM_DESIGNS; k++) {
						String[] row = br.readLine().split(",");
						for(int l = 0; l < Designer.NUM_DESIGNS; l++) {
							this.values[0][i][j][l][k] = Integer.parseInt(row[l]);
							// assume symmetric value map
							this.values[1][j][i][k][l] = Integer.parseInt(row[l]);
						}
					}
				}
			}
		} catch (IOException e) {
			logger.warn(e);
		}
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the values.
	 *
	 * @param strategy0 the strategy0
	 * @param strategy1 the strategy1
	 * @param design0 the design0
	 * @param design1 the design1
	 * @return the values
	 */
	public int[] getValues(int strategy0, int strategy1, int design0, int design1) {
		return new int[] {
			values[0][strategy0][strategy1][design0][design1],
			values[1][strategy0][strategy1][design0][design1]
		};
	}
}
