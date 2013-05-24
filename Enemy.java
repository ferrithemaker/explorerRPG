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


public class Enemy {
	private int agility;
	private int force;
	private int resist;
	private int life;
	private int absolute_x;
	private int absolute_y;
	private String name;
	private BufferedImage enemyimg;
	
	public Enemy(String name,int ag,int str, int res, int lf, int x,int y,BufferedImage sprite) {
		// initial set-up
		this.agility=ag;
		this.force=str;
		this.life=lf;
		this.name=name;
		this.resist=res;
		this.absolute_x=x;
		this.absolute_y=y;
		this.enemyimg=sprite;
		
	}
	
	public Enemy() {
		// void constructor
	}
	
	public boolean enemyonscreen(int xinitpos,int yinitpos) {
		for (int x=xinitpos;x<xinitpos+GameEngine.ON_SCREEN_TILES_X;x++) {
			for (int y=yinitpos;y<yinitpos+GameEngine.ON_SCREEN_TILES_Y;y++) {
				if (absolute_x==x && absolute_y==y) { return true; }
			}
		}
		return false;
	}
	
	public boolean overenemy(int x,int y) {
		if (this.absolute_x==x && this.absolute_y==y) { return true; } else { return false; }
	}
	public int getabsolutex() {
		return this.absolute_x;
	}
	public int getabsolutey() {
		return this.absolute_y;
	}
	public int getforce() {
		return this.force;
	}
	public int getresist() {
		return this.resist;
	}
	public int getlp() {
		return this.life;
	}
	public void updatelp(int value) {
		this.life=this.life+value;
	}
	public String getname() {
		return this.name;
	}
	public int getagility() {
		return this.agility;
	}
	public BufferedImage getsprite() {
		return this.enemyimg;
	}
}
