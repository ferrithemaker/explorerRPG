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


public class Object_array {
	private ArrayList<Object> Objectlist;
	
	public  Object_array() {
		Objectlist = new ArrayList<Object>();
	}
	
	public void add_object(Object obj) {
		Objectlist.add(obj);
	}
	
	public void remove_object(Object obj) {
		Objectlist.remove(obj);
	}
	
	public ArrayList<Object> getlist() {
		return Objectlist;
	}
	
	public Object overobject(int x, int y) { // return the object on the x,y position. null if none.
		Object rightobject = new Object(); // null object
		ListIterator<Object> iterator = Objectlist.listIterator();
        while (iterator.hasNext()) {
        	Object object=iterator.next();
        	if (object.overobject(x,y)==true) {
        		    		rightobject=object;
        	}
        }
        return rightobject;
	}
}
