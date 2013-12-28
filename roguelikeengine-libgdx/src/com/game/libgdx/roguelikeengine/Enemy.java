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



import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Enemy {
	private int agility;
	private int force;
	private int resist;
	private int maxlife;
	private int life;
	private int absolute_x;
	private int absolute_y;
	private String name;
	private Sprite enemyimg;
	private int layer;
	
	public Enemy(int layer,String name,int baseagility,int basestrength, int baseresist, int basehp, int x,int y,String file) {
		// initial set-up
		this.layer=layer;
		this.agility=baseagility;
		this.force=basestrength;
		this.life=this.maxlife=basehp;
		this.name=name;
		this.resist=baseresist;
		this.absolute_x=x;
		this.absolute_y=y;
		Texture enemytexture = new Texture(Gdx.files.internal(file)); 
		enemytexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.enemyimg = new Sprite(enemytexture);
	}
	
	public Enemy() {
		// void constructor
	}
	
	// gets	
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
	public int gethp() {
		return this.life;
	}
	public Sprite getsprite() {
		return this.enemyimg;
	}
	public String getname() {
		return this.name;
	}
	public int getagility() {
		return this.agility;
	}
	public int getlayer() {
		return layer;
	}
	public int percentlife() {
		return (this.maxlife / this.life) * 100;
	}
	public int chancetowin(Hero hero) {
		int chance = 0;
		
		// if weakened, just return 0
		if(percentlife() < 20) return chance;
		
		if(hero.getagility() < this.agility) {
			chance += 10;
		}
		
		if(hero.getresist() < this.resist) {
			chance += 10;
		}
		
		if(hero.getforce() < this.force) {
			chance += 10;
		}
		
		if(hero.gethp() < this.life) {
			chance += 10;
		}
		
		// a bit of bravery
		chance += new Random().nextInt(10);
		
		return chance;
	}
	
	// sets / updates
	public void updatehp(int value) {
		this.life=Math.max(0, this.life-value);
	}

	// control methods
	public boolean enemyonscreen(int xinitpos,int yinitpos) {
		return absolute_x >= xinitpos && absolute_x < xinitpos + WrapperEngine.ON_SCREEN_TILES_X &&
			   absolute_y >= yinitpos && absolute_y < yinitpos + WrapperEngine.ON_SCREEN_TILES_Y;
	}
	
	public boolean overenemy(int x,int y) {
		return (this.absolute_x==x && this.absolute_y==y);
	}
}
