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
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Buddy implements TileOccupier {
	private int absolute_x;
	private int absolute_y;
	private String name;
	private Sprite buddyimg;
	private int layer;
	private String speech;
	
	public Buddy(int layer,String name, int x,int y,String file,String speech) {
		// initial set-up
		this.layer=layer;
		this.name=name;
		this.absolute_x=x;
		this.absolute_y=y;
		this.speech=speech;
		Texture enemytexture = new Texture(Gdx.files.internal(file)); 
		enemytexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.buddyimg = new Sprite(enemytexture);
	}
	
	public Buddy() {
		// void constructor
	}
	
	public String talk() {
		return this.speech;
	}
	
	// gets	
	public Sprite getsprite() {
		return this.buddyimg;
	}
	public int getabsolutex() {
		return this.absolute_x;
	}
	public int getabsolutey() {
		return this.absolute_y;
	}
	
	public String getname() {
		return this.name;
	}
	
	public void healHero(Hero hero, int amount) {
		hero.updatehp(amount);
		
		GameplayScreen.instance.alert("You were healed for " + amount + " by a " + getname());
	}
	
	public int getlayer() {
		return layer;
	}

	// control methods
	public boolean buddyonscreen(int xinitpos,int yinitpos) {
		return absolute_x >= xinitpos && absolute_x < xinitpos + WrapperEngine.ON_SCREEN_TILES_X &&
			   absolute_y >= yinitpos && absolute_y < yinitpos + WrapperEngine.ON_SCREEN_TILES_Y;
	}
	
	public boolean overbuddy(int x,int y) {
		return (this.absolute_x==x && this.absolute_y==y);
	}

	@Override
	public String getdescription() {
		return "a friendly " + getname() + ", try talking to him.";
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
