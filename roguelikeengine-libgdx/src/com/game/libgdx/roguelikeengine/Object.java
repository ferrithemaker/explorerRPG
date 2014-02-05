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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;



public class Object {
	private String name;
	private int attack;
	private int defense;
	private int durability;
	private int absolute_x;
	private int absolute_y;
	private int layer;
	private String bodyLocation;
	private Sprite img;
	
	public Object(int layer,String name,String position,int baseattack, int basedefense, int basedurability,int x,int y,String file) {
		this.layer=layer;
		this.name=name;
		this.attack=baseattack;
		this.defense=basedefense;
		this.durability=basedurability;
		this.absolute_x=x;
		this.absolute_y=y;
		this.bodyLocation=position;
		this.img = new Sprite(new Texture(Gdx.files.internal(file)));
	}
	public Object() {
		// void constructor
	}
	
	// gets
	public int getabsolutex() {
		return this.absolute_x;
	}
	public void setabsolutex(int x) {
		this.absolute_x = x;
	}
	public String getname() {
		return this.name;
	}
	public int getlayer() {
		return layer;
	}
	public String getposition() {
		return this.bodyLocation;
	}
	public int getattack() {
		if (this.durability>0) {
			return this.attack;
		} else {
			return 0;
		}
	}
	public int getdefense() {
		if (this.durability>0) {
			return this.defense;
		} else {
			return 0;
		}
	}
	public int getdurability() {
		return this.durability;
	}
	public int getabsolutey() {
		return this.absolute_y;
	}
	public void setabsolutey(int y) {
		this.absolute_y = y;
	}
	public Sprite getsprite() {
		return this.img;
	}
	// sets / updates
	public void reducedurability(int value) {
		if (this.durability>0) {
			this.durability=this.durability-value;
		}
	}
	// control methods
	public boolean objectonscreen(int xinitpos,int yinitpos) { // return true of object is on current player screen
		for (int x=xinitpos;x<xinitpos+WrapperEngine.ON_SCREEN_TILES_X;x++) {
			for (int y=yinitpos;y<yinitpos+WrapperEngine.ON_SCREEN_TILES_Y;y++) {
				if (absolute_x==x && absolute_y==y) { return true; }
			}
		}
		return false;
	}
	public boolean overobject(int x,int y) {
		if (this.absolute_x==x && this.absolute_y==y) { return true; } else { return false; }
	}
	public void setlayer(int layer2) {
		this.layer = layer2;
	}
}
