package com.game.libgdx.roguelikeengine.rooms;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.game.libgdx.roguelikeengine.GameplayScreen;
import com.game.libgdx.roguelikeengine.Map;
import com.game.libgdx.roguelikeengine.ObjectTileOccupier;
import com.game.libgdx.roguelikeengine.Tile;
import com.game.libgdx.roguelikeengine.TileOccupier;

public abstract class Room {
	public static int NO_CHANGE = -1;
	public static int WALL = Color.rgba8888(Color.BLUE);
	public static int WALL_TOP = Color.rgba8888(Color.BLACK);
	public static int FLOOR = Color.rgba8888(Color.WHITE);
	
	public static int CHEST_COLOR = Color.rgba8888(Color.YELLOW);
	
	private Sprite floor_img = null;
	private Sprite wall_img = null;
	private Sprite wall_top_img = null;
	
	protected String name = "Unnamed";
	protected boolean placed = false;
	protected int[][] data;
	
	protected HashMap<String, Decoration> decor;
	
	public Room(String name, int[][] data, String floor, String wall, String wallTop, Decoration...decorations) {
		this.name = name;
		this.data = data;
		
		if(floor_img == null || wall_img == null || wall_top_img == null) {
			floor_img = new Sprite(new Texture(Gdx.files.internal(floor)));
			wall_top_img = new Sprite(new Texture(Gdx.files.internal(wallTop)));
			wall_img = new Sprite(new Texture(Gdx.files.internal(wall)));
		}
		
		this.decor = new HashMap<String, Decoration>();
		for(Decoration decoration : decorations) {
			this.decor.put(decoration.value+"", decoration);
		}
	}
	
	public boolean tryPlace(Map map, int column, int row) {
		if(canFit(map, column, row)) {
			int value = NO_CHANGE;
			for(int i = column, c = 0; i < column + data.length; ++i, ++c) {
				for(int j = row + data[c].length, r = 0; j > row; --j, ++ r) {
					Tile tile = map.gettileat(i, j);
					value = data[c][r];
					placeValueOnTile(map, tile, value, i, j);
				}
			}
			
			onPlaced(map, column, row);
			return true;
		}
		
		return false;
	}
	
	public boolean canFit(Map map, int column, int row) {
		for(int i = column; i < column + data.length; ++i) {
			for(int j = row; j < row + data[0].length; ++j) {
				Tile tile = map.gettileat(i, j);
				
				if(tile == null || tile.isbloqued()) {
					if(tile == null) {
						System.out.println(name + " room will not fit on map");
					} else {
						System.out.println(name + " room is blocked");
					}
					return false;
				}
			}
		}
		
		return true;
	}
	
	protected void placeValueOnTile(Map map, Tile tile, int value, int column, int row) {
		TileOccupier to = null;
		Decoration decoration = null;
		
		if(decor.containsKey(value + "")) {
			decoration = decor.get(value + "");
		}
		
		if(value == WALL || (decoration != null && decoration.isWall)) {
			to = new ObjectTileOccupier(map.getlayer(), name + " wall", "a " + name, column, row);
			tile.settileimage(wall_img);
		} else if(value == WALL_TOP) {
			to = new ObjectTileOccupier(map.getlayer(), name + " wall", "a " + name, column, row);
			tile.settileimage(wall_top_img);
		} else if(value == FLOOR || (decoration != null && decoration.isFloor)) {
			tile.settileimage(floor_img);
		} else if(value == CHEST_COLOR) {
			tile.settileimage(floor_img);
			if(Math.random() < 0.4) {
				GameplayScreen.instance.createchest(column, row);
			}
		}
		
		if(to != null) {
			tile.block(to);
		}
		
		if(decoration != null) {
			if(decoration.passCheck()) {
				tile.settiledecoration(decoration.sprite);
			}
		}
		
		tile.setPCG(false);	// make sure no content is added inside room
	}
	
	public abstract void onPlaced(Map map, int column, int row);
}
