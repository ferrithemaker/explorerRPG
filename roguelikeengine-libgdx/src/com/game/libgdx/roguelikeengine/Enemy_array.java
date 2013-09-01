package com.game.libgdx.roguelikeengine;

/*
    Copyright (C) 2013  Ferran F�bregas (ferri.fc@gmail.com)

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
import java.util.LinkedList;
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
	
	public Enemy overenemy(int x, int y) {
		Enemy rightbguy = new Enemy(); // null enemy
		ListIterator<Enemy> bgiterator = Enemylist.listIterator();
        while (bgiterator.hasNext()) {
        	Enemy bguy=bgiterator.next();
        	if (bguy.overenemy(x,y)==true) {
        		    		rightbguy=bguy;
        	}
        }
        return rightbguy;
	}
	
	public LinkedList<Enemy> onscreenonly(int x, int y) {
		LinkedList<Enemy> result = new LinkedList<Enemy>();
		
		for(Enemy enemy : this.Enemylist) {
			if(enemy.enemyonscreen(x, y)) result.add(enemy);
		}
		
		return result;
	}
	
	
	public LinkedList<Enemy> inlogicrangeonly(Hero hero) {
		int x = hero.getabsolutextile(), y = hero.getabsoluteytile();
		return inlogicrangeonly(x, y);
	}
	public LinkedList<Enemy> inlogicrangeonly(int x, int y) {
		LinkedList<Enemy> result = new LinkedList<Enemy>();
		
		int rangesq = LOGIC_RANGE * LOGIC_RANGE;
		
		
		int dist;
		for(Enemy enemy : this.Enemylist) {
			dist = enemy.getabsolutedistancesq(x, y);
			
			if(dist < rangesq) {
				result.add(enemy);
			}
		}
		
		
		return result;
	}

	public void update() {
		
	}
	
	/*public void onplayermove(Hero hero) {
		LinkedList<Enemy> inrange = inlogicrangeonly(hero);
		for(Enemy enemy : inrange) {
			enemy.onplayermove(hero);
		}
	}*/
	
}
