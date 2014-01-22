package com.game.libgdx.roguelikeengine;

public interface TileOccupier {
	public String getname();
	public String getdescription();
	
	public int getlayer();
	public int getabsolutecolumn(Map map);
	public int getabsoluterow(Map map);
}
