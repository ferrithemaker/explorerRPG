package com.game.libgdx.roguelikeengine.pathing;

import com.game.libgdx.roguelikeengine.Map;
import com.game.libgdx.roguelikeengine.MovingTileOccupier;
import com.game.libgdx.roguelikeengine.Tile;

public class Pathing<T extends MovingTileOccupier> {
	protected boolean walking = false;
	protected Tile lastTile = null;
	
	private Map map;
	
	protected Tile walkTarget;
	
	public Pathing(Map map) {
		this.setMap(map);
	}
	
	public Path<T> getPath(Tile start, Tile end) {
		walkTarget=start;
		return new AStar<T>(getMap(), start, end);
	}
	
	public void walk(Path<T> path, T walker) {
		if(path.isCompletePath()) {
			boolean onTarget = (walker.getabsolutecolumn(map) == walkTarget.getcolumn() && walker.getabsoluterow(map) == walkTarget.getrow());
			
			if(walkTarget == path.getEnd() && onTarget) {
				return;
			} else if(onTarget) {
				walkTarget = path.getNext();
			}
			
			if(walkTarget.getcolumn() < walker.getabsolutecolumn(getMap())) {
				walker.goLeft(getMap());
			} else if(walkTarget.getcolumn() > walker.getabsolutecolumn(getMap())) {
				walker.goRight(getMap());
			}
			
			if(walkTarget.getrow() < walker.getabsoluterow(getMap())) {
				walker.goUp(getMap());
			} else if(walkTarget.getrow() > walker.getabsoluterow(getMap())){
				walker.goDown(getMap());
			}
		}
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
}
