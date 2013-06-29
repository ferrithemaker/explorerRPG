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

public class Consumable_inventory {

	private Consumable inventory[];
	
	public Consumable_inventory() {
		inventory= new Consumable[GameEngine.INVENTORY_SIZE];
		for (int i=0;i<GameEngine.INVENTORY_SIZE;i++) {
			inventory[i]=null;
		}
	}
	
	public void set_consumable(int pos, Consumable obj) {
		inventory[pos]=obj;
	}
	
	public Consumable get_consumable(int pos) {
		return inventory[pos];
	}

	public void delete_consumable(int pos) {
		inventory[pos]=null;
	}
	public int getfreeslot() { // if firstfreeposition=-1 theres no free slots
		int firstfreeposition=-1;
		for (int i=GameEngine.INVENTORY_SIZE-1;i>=0;i--) {
			if (inventory[i]==null) {
				firstfreeposition=i;
			}
		}
		return firstfreeposition;
	}
}
