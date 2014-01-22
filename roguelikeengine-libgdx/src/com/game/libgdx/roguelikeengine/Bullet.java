package com.game.libgdx.roguelikeengine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Bullet {
	protected Vector2 position = new Vector2();
	protected boolean finished = false;
	protected boolean firedByHero = false;
	protected Directions direction;
	
	protected TileOccupier hit;
	
	protected float speed = 15f;
	protected float tilesTraveled = 0f;
	protected float tilesBeforeFinished = 0f;
	
	protected int layer = 0;
	
	protected float rot = 0f;
	
	protected Map map;
	protected TileOccupier source;
	
	protected Tile start;
	protected Tile last;
	
	public Bullet(Map map, TileOccupier source, Directions direction) {
		this.map = map;
		this.source = source;
		this.direction = direction;
		
		switch(this.direction.getOverallDirection()) {
		case NORTH_OR_SOUTH:
			tilesBeforeFinished = WrapperEngine.ON_SCREEN_TILES_Y;
			break;
		case EAST_OR_WEST:
		default:
			tilesBeforeFinished = WrapperEngine.ON_SCREEN_TILES_X;
			break;
		}
		
		this.position = new Vector2(source.getabsolutecolumn(map) * WrapperEngine.TILE_X_SIZE, source.getabsoluterow(map) * WrapperEngine.TILE_Y_SIZE);
		this.start = this.getTileOver(map);
		if(this.start == null) {
			System.out.println("Bullet fired outside map!");
			finished = true;
		}
	}
	
	public void update() {
		if(finished) return;
		
		if(tilesTraveled >= tilesBeforeFinished) {
			finished = true;
		}
		
		float xSpeed = 0f;
		float ySpeed = 0f;
		
		switch(this.direction) {
		case NORTH:
			ySpeed = -speed;
			break;
		case SOUTH:
			ySpeed = speed;
			break;
		case EAST:
			xSpeed = speed;
			break;
		case WEST:
		default:
			xSpeed = -speed;
		}
		
		position.x += xSpeed;
		position.y += ySpeed;
		
		TileOccupier hit = null;
		if((hit = this.getTileOccupierOver(map)) != null) {
			if(hit != source) {
				if(this.layer == hit.getlayer()) {
					this.onHit(source, hit);
				}
			}
		}
		
		Tile now = this.getTileOver(map);
		if(now == null) {
			this.finished = true;
		} else if(now != last) {	// new tile
			if(now.getcolumn() < map.getfirstxtile() || now.getcolumn() > map.getfirstxtile() + WrapperEngine.ON_SCREEN_TILES_X ||
			   now.getrow() < map.getfirstytile() || now.getrow() > map.getfirstytile() + WrapperEngine.ON_SCREEN_TILES_Y) {
				this.finished = true;
			}
			
			float xDist = now.getcolumn() - start.getcolumn();
			float yDist = now.getrow() - start.getrow();
			
			tilesTraveled = (float) Math.sqrt(xDist * xDist + yDist * yDist);
		}
		
		last = now;
		this.onUpdate();
	}
	
	public void render(SpriteBatch batch, int currentLayer) {
		if(finished || layer != currentLayer) return;
		
		this.onRender(batch);
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public TileOccupier getTargetHit() {
		return hit;
	}
	
	public boolean firedByHero() {
		return firedByHero;
	}
	
	public int getLayer() { return layer; }
	public float getRotation() { return rot; }
	
	public abstract void onUpdate();
	public abstract void onRender(SpriteBatch batch);
	public abstract void onHit(TileOccupier source, TileOccupier hit);
	
	public Tile getTileOver(Map map) {
		int column = (int) (position.x / WrapperEngine.TILE_X_SIZE);
		int row = (int) (position.y / WrapperEngine.TILE_Y_SIZE);

		return map.gettileat(column, row);
	}
	
	public TileOccupier getTileOccupierOver(Map map) {
		Tile tile = this.getTileOver(map);
		return tile == null ? null : tile.getblocker();
	}
}
