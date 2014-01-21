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

public class Buddy_array {
	public static int LOGIC_RANGE = 10;
	
	private ArrayList<Buddy> Buddylist;
	
	public Buddy_array() {
		Buddylist = new ArrayList<Buddy>();
	}
	
	public ArrayList<Buddy> getlist() {
		return Buddylist;
	}
	
	public int add_buddy(Buddy buddy) {
		Buddylist.add(buddy);
		return 0;
	}
	
	public int remove_buddy(Buddy buddy) {
		Buddylist.remove(buddy);
		return 0;
	}
	public Buddy nextotobuddy(int x, int y) {
		Buddy rightbguy = new Buddy(); // null buddy
		ListIterator<Buddy> bgiterator = Buddylist.listIterator();
		while (bgiterator.hasNext()) {
        	Buddy bguy=bgiterator.next();
        	if (bguy.overbuddy(x+1,y)==true) {
        		rightbguy=bguy;
        	}
        	if (bguy.overbuddy(x-1,y)==true) {
	    		rightbguy=bguy;
        	}
        	if (bguy.overbuddy(x,y+1)==true) {
	    		rightbguy=bguy;
        	}
        	if (bguy.overbuddy(x,y-1)==true) {
	    		rightbguy=bguy;
        	}
        }
        return rightbguy;
	}
	
	public Buddy overbuddy(int x, int y) {
		Buddy rightbguy = new Buddy(); // null enemy
		ListIterator<Buddy> bgiterator = Buddylist.listIterator();
        while (bgiterator.hasNext()) {
        	Buddy bguy=bgiterator.next();
        	if (bguy.overbuddy(x,y)==true) {
        		rightbguy=bguy;
        	}
        }
        return rightbguy;
	}
}
