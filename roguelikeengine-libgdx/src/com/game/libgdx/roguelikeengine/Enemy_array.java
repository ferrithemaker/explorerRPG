package com.game.libgdx.roguelikeengine;

/*
    Copyright (C) 2013  Ferran Fabregas (ferri.fc@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.util.ArrayList;
import java.util.ListIterator;

public class Enemy_array {
	public static int LOGIC_RANGE = 10;
	
	private ArrayList<Enemy> Enemylist;
	
	public Enemy_array() {
		Enemylist = new ArrayList<Enemy>();
	}
	
	public ArrayList<Enemy> getlist() {
		return Enemylist;
	}
	
	public int add_enemy(Enemy enemy) {
		Enemylist.add(enemy);
		return 0;
	}
	
	public int remove_enemy(Enemy enemy) {
		Enemylist.remove(enemy);
		return 0;
	}
	
	public Enemy overenemy(int x, int y, int layer) {
		Enemy rightbguy = new Enemy(); // null enemy
		ListIterator<Enemy> bgiterator = Enemylist.listIterator();
        while (bgiterator.hasNext()) {
        	Enemy bguy=bgiterator.next();
        	if (bguy.overenemy(x,y,layer)==true) {
        		    		rightbguy=bguy;
        	}
        }
        return rightbguy;
	}
	public Enemy nextotoenemy(int x, int y,int layer) {
		Enemy rightbguy = new Enemy(); // null enemy
		ListIterator<Enemy> bgiterator = Enemylist.listIterator();
		while (bgiterator.hasNext()) {
        	Enemy bguy=bgiterator.next();
        	if (bguy.overenemy(x+1,y,layer)==true) {
        		rightbguy=bguy;
        	}
        	if (bguy.overenemy(x-1,y,layer)==true) {
	    		rightbguy=bguy;
        	}
        	if (bguy.overenemy(x,y+1,layer)==true) {
	    		rightbguy=bguy;
        	}
        	if (bguy.overenemy(x,y-1,layer)==true) {
	    		rightbguy=bguy;
        	}
        }
        return rightbguy;
	}
	
	/*public LinkedList<Enemy> onscreenonly(int x, int y) {
		LinkedList<Enemy> result = new LinkedList<Enemy>();
		
		for(Enemy enemy : this.Enemylist) {
			if(enemy.enemyonscreen(x, y)) result.add(enemy);
		}
		
		return result;
	}*/
}
