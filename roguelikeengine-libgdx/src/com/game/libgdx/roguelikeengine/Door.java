package com.game.libgdx.roguelikeengine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Door extends Chest {
	public Door(int layer, int column, int row) {
		super(layer, column, row);
		
		Texture opentexture = new Texture(Gdx.files.internal("doors/closed_door.png")); 
		opentexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		openedSprite = new Sprite(opentexture);
		
		this.buddyimg = new Sprite(opentexture);
		this.locked = true;
	}
	
	public void giveItemToPlayer(Hero hero) {
		GameplayScreen.instance.alert("You used a key, and opened the door!");
	}
	
	@Override
	public String getname() {
		return opened ? "opened door" : "door";
	}
	
	@Override
	public String getdescription() {
		Chest.interacting = this;
		
		return opened ? "An opened door. You already opened this, remember?" : "A locked_door!";
	}
}
