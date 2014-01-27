package com.game.libgdx.roguelikeengine.pathing;

import com.game.libgdx.roguelikeengine.MovingTileOccupier;
import com.game.libgdx.roguelikeengine.Tile;


public interface Path<T extends MovingTileOccupier> {
	public Tile getNext();
	public Tile getCurrent();
	
	public Tile getStart();
	public Tile getEnd();
	
	public void search();
	
	public boolean isCompletePath();
}
