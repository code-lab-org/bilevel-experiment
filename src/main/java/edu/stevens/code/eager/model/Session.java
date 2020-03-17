package edu.stevens.code.eager.model;

import java.util.Arrays;

/**
 * The Class Session.
 */
public class Session {
	private String name;
	private Round[] rounds;
	
	/**
	 * Instantiates a new session.
	 */
	public Session() {
		this.rounds = new Round[0];
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
	 * Gets the rounds.
	 *
	 * @return the rounds
	 */
	public Round[] getRounds() {
		return Arrays.copyOf(rounds, rounds.length);
	}
	
	/**
	 * Gets the round.
	 *
	 * @param index the index
	 * @return the round
	 */
	public Round getRound(int index) {
		if(index < 0 || index >= rounds.length) {
			throw new IllegalArgumentException("invalid round index");
		}
		return this.rounds[index];
	}
}
