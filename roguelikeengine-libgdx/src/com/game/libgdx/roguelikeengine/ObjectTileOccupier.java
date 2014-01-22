package com.game.libgdx.roguelikeengine;

public class ObjectTileOccupier implements TileOccupier {
	protected String name;
	protected String description;
	
	protected int layer;
	protected int column;
	protected int row;
	
	public ObjectTileOccupier(int layer, String name, String description, int column, int row) {
		this.name= name;
		this.description = description;
		this.column = column;
		this.row = row;
	}
	
	@Override
	public String getname() {
		return name;
	}

	@Override
	public String getdescription() {
		return description;
	}

	@Override
	public int getabsolutecolumn(Map map) {
		return column;
	}

	@Override
	public int getabsoluterow(Map map) {
		return row;
	}

	@Override
	public int getlayer() {
		return layer;
	}

}
