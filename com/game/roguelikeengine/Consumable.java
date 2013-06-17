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


public class Consumable {
	private String name;
	private int powerup_agility;
	private int powerup_life;
	private int absolute_x;
	private int absolute_y;
	private BufferedImage img;
	
	public Consumable(String name, int p_agility, int p_life,int x,int y,String file) {
		this.name=name;
		this.powerup_agility=p_agility;
		this.powerup_life=p_life;
		this.absolute_x=x;
		this.absolute_y=y;
		try {
			this.img=ImageIO.read(new File(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Consumable() {
		// void constructor
	}
	
	// gets
	public BufferedImage getsprite() {
		return this.img;
	}
	public int getabsolutex() {
		return this.absolute_x;
	}
	public String getname() {
		return this.name;
	}
	public int getpowerupagility() {
		return this.powerup_agility;
	}
	public int getpoweruplife() {
		return this.powerup_life;
	}
	public int getabsolutey() {
		return this.absolute_y;
	}
	// control methods
	public boolean consumableonscreen(int xinitpos,int yinitpos) {
		for (int x=xinitpos;x<xinitpos+GameEngine.ON_SCREEN_TILES_X;x++) {
			for (int y=yinitpos;y<yinitpos+GameEngine.ON_SCREEN_TILES_Y;y++) {
				if (absolute_x==x && absolute_y==y) { return true; }
			}
		}
		return false;
	}
	public boolean overconsumable(int x,int y) {
		if (this.absolute_x==x && this.absolute_y==y) { return true; } else { return false; }
	}
}
