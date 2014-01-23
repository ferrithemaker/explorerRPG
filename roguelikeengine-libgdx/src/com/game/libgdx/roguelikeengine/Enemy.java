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


public class Enemy implements TileOccupier {
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
	private boolean active; // if Enemy is chasing hero
	
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
		this.active=false;
	}
	
	public Enemy() {
		// void constructor
	}
	
	
	public String talk() {
		Random randomGenerator = new Random();
		int x = randomGenerator.nextInt(5);
		switch (x) {
		case 0:
			return getname() + ": Grrrrrrrrrrrrrrrrr";
		case 1:
			return getname() + ": You will die!";
		case 2:
			return getname() + ": Go away little bastard";
		case 3:
			return getname() + ": Run out of here!";
		case 4:
			return getname() + ": You wanna fight?";
		default:
			return getname() + ": Grrrrrrrrrrrr";
		}
		
	}
	
	// gets	
	public boolean isactive() {
		return active;
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
	public float percentlife() {
		return ((float)(float) this.life / (float)this.maxlife ) * 100;
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
	public void activate() {
		active=true;	
	}
	
	public void deactivate() {
		active=false;
	}
	
	public void setabsolutex(int value) {
		absolute_x=value;
	}
	
	public void setabsolutey(int value) {
		absolute_y=value;
	}
	
	public void updatehp(int value) {
		this.life=Math.max(0, this.life-value);
	}

	// control methods
	public boolean enemyonscreen(int xabsolutepos,int yabsolutepos,int layer) {
		return absolute_x >= xabsolutepos && absolute_x < xabsolutepos + WrapperEngine.ON_SCREEN_TILES_X &&
			   absolute_y >= yabsolutepos && absolute_y < yabsolutepos + WrapperEngine.ON_SCREEN_TILES_Y &&
			   layer==this.layer;
	}
	
	public boolean overenemy(int x,int y,int layer) {
		return (this.absolute_x==x && this.absolute_y==y && this.layer==layer);
	}

	@Override
	public String getdescription() {
		return "a " + getname();
	}

	@Override
	public int getabsolutecolumn(Map map) {
		return this.getabsolutex();
	}

	@Override
	public int getabsoluterow(Map map) {
		return this.getabsolutey();
	}
}
