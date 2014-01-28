package com.game.libgdx.roguelikeengine;

public interface MovingTileOccupier extends TileOccupier {
	public boolean goLeft(Map map);
	public boolean goRight(Map map);
	public boolean goUp(Map map);
	public boolean goDown(Map map);
}
