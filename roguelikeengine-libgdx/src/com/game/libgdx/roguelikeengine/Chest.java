package com.game.libgdx.roguelikeengine;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Chest extends Buddy {
	protected static Chest interacting = null;	// the chest the player is interacting with
	
	protected Sprite openedSprite;
	
	protected boolean opened = false;
	protected int destroyDelay = 30;	// destroy on a timer to allow open animation + sounds
	protected int destroyDuration = 0;	// simple timing variables
	
	protected boolean shouldKill = false;	// gameplayscreen monitors this and removes as needed
	
	public Chest(int layer, int column, int row) {
		// int layer,String name, int x,int y,String file,String speech
		super(layer, "chest", column, row, "chests/chest_closed.png", "Why are you talking to a chest?");
		
		Texture opentexture = new Texture(Gdx.files.internal("chests/chest_opened.png")); 
		opentexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		openedSprite = new Sprite(opentexture);
	}
	
	public boolean open(Map map, Hero hero) {
		if(canOpen(map, hero)) {
			hero.removeKey();	// remove key first
			giveItemToPlayer(hero);
			
			// play audio here
			// animations in update
			
			this.buddyimg = openedSprite;
			return (opened = true);
		}
		
		return false;
	}
	
	public void giveItemToPlayer(Hero hero) {
		if(Math.random() < 0.05) {
			GameplayScreen.instance.alert("Oh, no! It was empty!");
		}
		
		if(Math.random() < 0.5) {
			giveObjectToPlayer(hero);
		} else {
			giveConsumableToPlayer(hero);
		}
	}

	private void giveConsumableToPlayer(Hero hero) {
		int m = WrapperEngine.CHEST_POTION_MODIFIER;
		double chance = Math.random();
		
		Consumable cons = null;
		if(chance > .66) {
			cons = new Consumable(getlayer(),"Greater Blue potion",m,m,0,m*2,0,0,"potionblue.png");
		} else if(chance > .33) {
			cons =new Consumable(getlayer(),"Greater Red potion",0,m,m,m,0,0,"potionred.png");
		} else {
			cons = new Consumable(getlayer(),"Greater Yellow potion",m*2,m,0,0,0,0,"potionyellow.png");
		}

		GameplayScreen.instance.givetohero(cons);
		GameplayScreen.instance.alert("You got a " + cons.getname() + "!");
	}

	private void giveObjectToPlayer(Hero hero) {
		Object obj = this.generateObject();
		if(obj == null) {
			GameplayScreen.instance.alert("The chest was empty!");
		} else {
			GameplayScreen.instance.givetohero(obj);
			GameplayScreen.instance.alert("You got a " + obj.getname());
		}
	}

	public boolean canOpen(Map map, TileOccupier to) {
		if(!opened && to instanceof Hero) {
			if(((Hero)to).hasKey() && getlayer() == to.getlayer()) {
				float xDiff = getabsolutecolumn(map) - to.getabsolutecolumn(map);
				float yDiff = getabsoluterow(map) - to.getabsoluterow(map);
				
				return Math.sqrt(xDiff * xDiff + yDiff * yDiff) <= 1.0f;
			}
		}
		
		return false;
	}
	
	@Override
	public String getname() {
		return opened ? "empty chest" : "chest";
	}
	
	@Override
	public String getdescription() {
		Chest.interacting = this;
		
		return opened ? "An opened chest. You already opened this, remember?" : "A treasure_chest!";
	}
	
	@Override
	public void update() {
		if(WrapperEngine.CHESTS_PERSIST) return;
		
		if(opened) {
			destroyDuration += 1;
			if(destroyDuration >= destroyDelay) {
				this.shouldKill = true;
			}
			
			if(destroyDuration % 2 == 0) {
				this.buddyimg.setColor(Color.RED);
			} else {
				this.buddyimg.setColor(Color.WHITE);
			}
		}
	}
	
	public boolean getShouldKill() { return this.shouldKill; }
	
	public Object generateObject() { // copied from wrapper engine
		Random randomGenerator = new Random();
		int objecttype = randomGenerator.nextInt(11);
		if (objecttype == 0) {
			return new Object(getlayer(), "long sword", "righthand", 10, 0, 10,
					0, 0, "longSword.png");
		} else if (objecttype == 1) {
			return new Object(getlayer(), "dagger", "righthand", 3, 0, 7, 0, 0,
					"dagger.png");
		} else if (objecttype == 2) {
			return new Object(getlayer(), "boots", "foot", 0, 6, 4, 0, 0,
					"boots.png");
		} else if (objecttype == 3) {
			return new Object(getlayer(), "heavy armor", "body", 0, 15, 10, 0,
					0, "heavyarmor.png");
		} else if (objecttype == 4) {
			return new Object(getlayer(), "helm", "head", 0, 4, 6, 0, 0,
					"helm.png");
		} else if (objecttype == 5) {
			return new Object(getlayer(), "mace", "lefthand", 7, 0, 8, 0, 0,
					"mace.png");
		} else if (objecttype == 6) {
			return new Object(getlayer(), "riot shield", "lefthand", 0, 9, 12,
					0, 0, "riotShield.png");
		} else if (objecttype == 7) {
			return new Object(getlayer(), "armor", "body", 0, 11, 7, 0, 0,
					"reflecArmor.png");
		} else if (objecttype == 8) {
			return new Object(getlayer(), "shield", "lefthand", 0, 7, 6, 0, 0,
					"shield.png");
		} else if (objecttype == 9) {
			return new Object(getlayer(), "skull cap", "head", 0, 5, 5, 0, 0,
					"skullcap.png");
		} else if (objecttype == 10) {
			return new Object(getlayer(), "great shield", "lefthand", 0, 12,
					11, 0, 0, "greatShield.png");
		}

		return null;
	}
}
