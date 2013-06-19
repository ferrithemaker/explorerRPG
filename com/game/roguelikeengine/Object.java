package com.game.roguelikeengine;

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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



public class Object {
	private String name;
	private int attack;
	private int defense;
	private int durability;
	private int absolute_x;
	private int absolute_y;
	private String position;
	private BufferedImage img;
	
	public Object(String name,String position,int baseattack, int basedefense, int basedurability,int x,int y,String file) {
		this.name=name;
		this.attack=baseattack;
		this.defense=basedefense;
		this.durability=basedurability;
		this.absolute_x=x;
		this.absolute_y=y;
		this.position=position;
		try {
			this.img=ImageIO.read(new File(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Object() {
		// void constructor
	}
	
	// gets
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
	public BufferedImage getsprite() {
		return this.img;
	}
	// sets / updates
	public void updatedurability(int value) {
		this.durability=this.durability-value;
	}
	// control methods
	public boolean objectonscreen(int xinitpos,int yinitpos) { // return true of object is on current player screen
		for (int x=xinitpos;x<xinitpos+GameEngine.ON_SCREEN_TILES_X;x++) {
			for (int y=yinitpos;y<yinitpos+GameEngine.ON_SCREEN_TILES_Y;y++) {
				if (absolute_x==x && absolute_y==y) { return true; }
			}
		}
		return false;
	}
	public boolean overobject(int x,int y) {
		if (this.absolute_x==x && this.absolute_y==y) { return true; } else { return false; }
	}
}
