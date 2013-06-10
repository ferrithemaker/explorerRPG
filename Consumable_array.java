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


public class Consumable_array {
		private ArrayList<Consumable> Consumablelist;
		
		public  Consumable_array() {
			Consumablelist = new ArrayList<Consumable>();
		}
		
		public int add_consumable(Consumable consumable) {
			Consumablelist.add(consumable);
			return 0;
		}
		
		public int remove_consumable(Consumable consumable) {
			Consumablelist.remove(consumable);
			return 0;
		}
		public ArrayList<Consumable> getlist() {
			return Consumablelist;
		}
		public Consumable overconsumable(int x, int y) {
			Consumable rightconsumable = new Consumable(); // null enemy
			ListIterator<Consumable> iterator = Consumablelist.listIterator();
	        while (iterator.hasNext()) {
	        	Consumable consumable=iterator.next();
	        	if (consumable.overconsumable(x,y)==true) {
	        		rightconsumable=consumable;
	        	}
	        }
	        return rightconsumable;
		}

}
