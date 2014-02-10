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


public class Consumable {
	private String name;
	private int powerup_agility;
	private int powerup_life;
	private int powerup_resist;
	private int powerup_force;
	private int absolute_x;
	private int absolute_y;
	private Sprite img;
	private int layer;
	
	public Consumable(int layer,String name, int agility, int hp,int force, int resist,int x,int y,String file) {
		this.layer=layer;
		this.name=name;
		this.powerup_agility=agility;
		this.powerup_life=hp;
		this.powerup_resist=resist;
		this.powerup_force=force;
		this.absolute_x=x;
		this.absolute_y=y;
		this.img = new Sprite(new Texture(Gdx.files.internal(file)));
	}
	public Consumable() {
		// void constructor 
	}
	
	// gets
	public Sprite getsprite() {
		return this.img;
	}
	public int getabsolutex() {
		return this.absolute_x;
	}
	public String getname() {
		return this.name;
	}
	public int getlayer() {
		return layer;
	}
	public int getpowerupagility() {
		return this.powerup_agility;
	}
	public int getpoweruplife() {
		return this.powerup_life;
	}
	public int getpowerupstrength() {
		return this.powerup_force;
	}
	public int getpowerupresist() {
		return this.powerup_resist;
	}
	public int getabsolutey() {
		return this.absolute_y;
	}
	// control methods
	public boolean consumableonscreen(int xinitpos,int yinitpos) {
		for (int x=xinitpos;x<xinitpos+WrapperEngine.ON_SCREEN_TILES_X;x++) {
			for (int y=yinitpos;y<yinitpos+WrapperEngine.ON_SCREEN_TILES_Y;y++) {
				if (absolute_x==x && absolute_y==y) { return true; }
			}
		}
		return false;
	}
	public boolean overconsumable(int x,int y) {
		return (this.absolute_x==x && this.absolute_y==y);
	}
	public void setlayer(int layer2) {
		this.layer = layer2;
	}
	public void setabsolutex(int column) {
		this.absolute_x = column;
	}
	public void setabsolutey(int row) {
		this.absolute_y = row;
	}
}
