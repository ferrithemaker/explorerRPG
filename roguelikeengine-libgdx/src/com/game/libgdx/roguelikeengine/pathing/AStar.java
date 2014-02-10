package com.game.libgdx.roguelikeengine.pathing;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import com.game.libgdx.roguelikeengine.Chest;
import com.game.libgdx.roguelikeengine.Map;
import com.game.libgdx.roguelikeengine.MovingTileOccupier;
import com.game.libgdx.roguelikeengine.ObjectTileOccupier;
import com.game.libgdx.roguelikeengine.Tile;
import com.game.libgdx.roguelikeengine.WrapperEngine;

public class AStar<T extends MovingTileOccupier> implements Path<T> {
	protected static final LinkedList<Long> times = new LinkedList<Long>();
	
	protected LinkedHashSet<Tile> opened = new LinkedHashSet<Tile>();
	protected LinkedHashSet<Tile> closed = new LinkedHashSet<Tile>();
	
	protected HashMap<Tile, Tile> parentRecord = new HashMap<Tile, Tile>();
	
	protected LinkedList<Tile> result = new LinkedList<Tile>();
	protected int currentIndex = 0;
	protected Tile current;
	
	protected Map map;
	protected Tile start;
	protected Tile end;
	
	protected boolean pathFound = false;
	protected long timeSearchStarted = 0l;
	
	public AStar(Map map, Tile start, Tile end) {
		this.map = map;
		this.start = start;
		this.end = end;
	}
	
	public void search() {
		parentRecord.clear();
		
		result.clear();
		opened.clear();
		closed.clear();
		
		open(start);
		Tile current = null;
		
		timeSearchStarted = System.currentTimeMillis();
		long tooLong = Math.max(100L, AStar.getAverageSearchTime() * 2L);
		
		List<Tile> neighbors = null;
		while(opened.size() > 0) {
			current = getLowestCost();
			
			if(current == end) {
				close(current);
				break;
			}
			
			neighbors = map.getmooreneighbors(current);
			for(Tile tile : neighbors) {
				if((tile.isbloqued() && tile != end) || tile == current) continue;
				if(tile != end && map.isAccessPoint(tile.getcolumn(), tile.getrow())) continue;
				
				if(closed.contains(tile)) {
					
				} else if(opened.contains(tile)) {
					
				} else {
					this.setParent(tile, current);
					open(tile);
				}
			}
			
			close(current);
			
			if(System.currentTimeMillis() - timeSearchStarted > tooLong) {
				return;
			}
		}
		
		if(current == end) {
			while(current != null) {
				result.add(current);
				
				current = getParent(current);
			}
			
			Collections.reverse(result);
		}
		
		if(result.size() > 0) {
			this.times.add(System.currentTimeMillis() - timeSearchStarted);
			this.pathFound = true;
			currentIndex = 0;
		}
	}
	
	public void open(Tile tile) {
		closed.remove(tile);
		opened.add(tile);
	}
	
	public void close(Tile tile) {
		closed.add(tile);
		opened.remove(tile);
	}
	
	public Tile getParent(Tile tile) {
		return parentRecord.get(tile);
	}
	
	public void setParent(Tile child, Tile parent) {
		parentRecord.put(child, parent);
	}
	
	public Tile getLowestCost() {
		Tile result = null;
		
		Iterator<Tile> iter = opened.iterator();
		Tile current = null;
		
		float lowest = Float.MAX_VALUE;
		while(iter.hasNext() && (current = iter.next()) != null) {
			if(result == null || getCost(current) < lowest) {
				lowest = getCost(current);
				result = current;
			}
		}
		
		return result;
	}
	
	public float getCost(Tile tile) {	// basic manhatten
		float xDiff = tile.getcolumn() - end.getcolumn();
		float yDiff = tile.getrow() - end.getrow();
		float cost = 0.5f * (xDiff * xDiff + yDiff * yDiff); 
		
		return cost;
	}
	
	@Override
	public Tile getNext() {
		Tile current = result.get(currentIndex++);
		Tile next = null;
		
		if(currentIndex < result.size()) {
			next = result.get(currentIndex);
		} else {
			currentIndex--;
		}
		
		return next != null ?  next : current;
	}

	@Override
	public Tile getCurrent() {
		return current;
	}

	@Override
	public Tile getStart() {
		return start;
	}

	@Override
	public Tile getEnd() {
		return end;
	}

	@Override
	public boolean isCompletePath() {
		return pathFound;
	}

	public static long getAverageSearchTime() {
		long accum = 0L;
		for(Long value : times) {
			accum += value;
		}
		
		return times.size() > 0 ? accum / times.size() : 0L;
	}
}
