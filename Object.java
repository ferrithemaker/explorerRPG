/*
    Copyright (C) 2013  Ferran Fàbregas (ferri.fc@gmail.com)

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

import java.awt.image.BufferedImage;


public class Object {
	private String name;
	private int attack;
	private int defense;
	private int durability;
	private int absolute_x;
	private int absolute_y;
	private String position;
	private BufferedImage img;
	
	public Object(String name,String position,int attack, int defense, int durability,int x,int y,BufferedImage sprite) {
		this.name=name;
		this.attack=attack;
		this.defense=defense;
		this.durability=durability;
		this.absolute_x=x;
		this.absolute_y=y;
		this.position=position;
		this.img=sprite;
	}
	public Object() {
		// void constructor
	}
	public boolean overobject(int x,int y) {
		if (this.absolute_x==x && this.absolute_y==y) { return true; } else { return false; }
	}
	
	public int getabsolutex() {
		return this.absolute_x;
	}
	public String getname() {
		return this.name;
	}
	public String getposition() {
		return this.position;
	}
	public int getattack() {
		return this.attack;
	}
	public int getdefense() {
		return this.defense;
	}
	public int getdurability() {
		return this.durability;
	}
	public void reducedurability() {
		this.durability--;
	}
	public int getabsolutey() {
		return this.absolute_y;
	}
	public boolean objectonscreen(int xinitpos,int yinitpos) {
		for (int x=xinitpos;x<xinitpos+GameEngine.ON_SCREEN_TILES_X;x++) {
			for (int y=yinitpos;y<yinitpos+GameEngine.ON_SCREEN_TILES_Y;y++) {
				if (absolute_x==x && absolute_y==y) { return true; }
			}
		}
		return false;
	}
	public BufferedImage getsprite() {
		return this.img;
	}
}
