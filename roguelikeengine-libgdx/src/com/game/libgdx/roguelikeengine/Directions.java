package com.game.libgdx.roguelikeengine;

public enum Directions {
	NORTH, SOUTH, EAST, WEST, NORTH_OR_SOUTH, EAST_OR_WEST;
	
	private Directions or;
	
	static {
		NORTH.or = NORTH_OR_SOUTH;
		SOUTH.or = NORTH_OR_SOUTH;
		
		EAST.or = EAST_OR_WEST;
		WEST.or = EAST_OR_WEST;
	}
	
	public Directions getOverallDirection() {
		return or;
	}
}
