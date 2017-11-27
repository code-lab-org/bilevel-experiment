package edu.stevens.code.ptg;

import java.util.Arrays;

public class Session {
	private String name;
	private Round[] rounds;
	
	public Session() {
		this.rounds = new Round[0];
	}
	
	public String getName() {
		return this.name;
	}
	
	public Round[] getRounds() {
		return Arrays.copyOf(rounds, rounds.length);
	}
	
	public Round getRound(int index) {
		if(index < 0 || index >= rounds.length) {
			throw new IllegalArgumentException("invalid round index");
		}
		return this.rounds[index];
	}
}
