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

public class Object_inventory {
	private Object inventory[];
	
	public Object_inventory() {
		inventory= new Object[WrapperEngine.INVENTORY_SIZE];
		for (int i=0;i<WrapperEngine.INVENTORY_SIZE;i++) {
			inventory[i]=null;
		}
	}
	
	public void set_object(int pos, Object obj) {
		inventory[pos]=obj;
	}
	
	public Object get_object(int pos) {
		return inventory[pos];
	}
	
	public void delete_object(int pos) {
		inventory[pos]=null;
	}
	
	public void delete_object(Object obj) {
		int found = -1;
		for(int i = 0; i < inventory.length; ++i) {
			if(obj.equals(inventory[i])) {
				found = i;
				break;
			}
		}
		
		if(found >= 0) {
			delete_object(found);
		}
	}
	
	public int getfreeslot() { // return first avialable slot on inventory, if return firstfreeposition=-1 there is no free slots.
		int firstfreeposition=-1;
		for (int i=WrapperEngine.INVENTORY_SIZE-1;i>=0;i--) {
			if (inventory[i]==null) {
				firstfreeposition=i;
			}
		}
		return firstfreeposition;
	}
	
	public Object[] getinventory() { return inventory; }
}
