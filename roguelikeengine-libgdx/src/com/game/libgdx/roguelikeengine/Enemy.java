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
	
	public Enemy(String name,int baseagility,int basestrength, int baseresist, int basehp, int x,int y,String file) {
		// initial set-up
		this.agility=baseagility;
		this.force=basestrength;
		this.life=this.maxlife=basehp;
		this.name=name;
		this.resist=baseresist;
		this.absolute_x=x;
		this.absolute_y=y;
		this.enemyimg = new Sprite(new Texture(Gdx.files.internal(file)));
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
		/*
		for (int x=xinitpos;x<xinitpos+GameEngine.ON_SCREEN_TILES_X;x++) {
			for (int y=yinitpos;y<yinitpos+GameEngine.ON_SCREEN_TILES_Y;y++) {
				if (absolute_x==x && absolute_y==y) { return true; }
			}
		}
		
		return false;
		*/
		
		return absolute_x >= xinitpos && absolute_x < xinitpos + GameEngine.ON_SCREEN_TILES_X &&
			   absolute_y >= yinitpos && absolute_y < yinitpos + GameEngine.ON_SCREEN_TILES_Y;
	}
	
	public boolean overenemy(int x,int y) {
		return (this.absolute_x==x && this.absolute_y==y);
	}
	
	
	// hero in range = true, at this point
	public void onplayermove(Hero hero) {
		Random random = new Random();
		boolean movetowards = random.nextInt(50) < chancetowin(hero);
		
		System.out.println(name + " at " + absolute_x + ", " + absolute_y);
		if(movetowards) {
			if(absolute_x == GameplayScreen.instance.getabsolutextile(hero) &&
			   absolute_y == GameplayScreen.instance.getabsoluteytile(hero)) {
				// waited on player
			} else {
				if(Math.abs(absolute_x - GameplayScreen.instance.getabsolutextile(hero)) >
				   Math.abs(absolute_y - GameplayScreen.instance.getabsoluteytile(hero))) {
					movetowardsx(hero);
				} else {
					movetowardsy(hero);
				}
			}
			
			// attack the player?
			System.out.println("  --is moving towards player!");
		} else {
			if(random.nextBoolean()) { // 50% chance to do nothing || run
				if(Math.abs(absolute_x - GameplayScreen.instance.getabsolutextile(hero)) <
				   Math.abs(absolute_y - GameplayScreen.instance.getabsoluteytile(hero))) {
					moveawayx(hero);
				} else {
					moveawayy(hero);
				}
				System.out.println("  -- is moving away from player!");
			} // else do nothing
			System.out.println("  -- decided to do nothing.");
		}
		

		System.out.println("  -- at " + absolute_x + ", " + absolute_y);
	}
	
	public void movetowardsx(Hero hero) {
		if(this.absolute_x < hero.getabsolutextile() && GameplayScreen.instance.getmap().istileempty(absolute_x + 1, absolute_y)) {
			this.absolute_x += 1;
		} else if(GameplayScreen.instance.getmap().istileempty(absolute_x - 1, absolute_y)) {
			this.absolute_x -= 1;
		} else {
			System.out.println("  --  couldn't move x!");
		}
		enforcenonpenetrationconstraints();
	}

	public void moveawayx(Hero hero) {
		if(this.absolute_x < hero.getabsolutextile() && GameplayScreen.instance.getmap().istileempty(absolute_x - 1, absolute_y)) {
			this.absolute_x -= 1;
		} else if(GameplayScreen.instance.getmap().istileempty(absolute_x + 1, absolute_y)) {
			this.absolute_x += 1;
		} else {
			System.out.println("  --  couldn't move x!");
		}
		enforcenonpenetrationconstraints();
	}
	
	public void movetowardsy(Hero hero) {
		if(this.absolute_y < hero.getabsoluteytile() && GameplayScreen.instance.getmap().istileempty(absolute_x, absolute_y + 1)) {
			this.absolute_y += 1;
		} else if(GameplayScreen.instance.getmap().istileempty(absolute_x, absolute_y - 1)) {
			this.absolute_y -= 1;
		} else {
			System.out.println("  --  couldn't move y!");
		}
		enforcenonpenetrationconstraints();
	}
	
	public void moveawayy(Hero hero) {
		if(this.absolute_y < hero.getabsoluteytile() && GameplayScreen.instance.getmap().istileempty(absolute_x, absolute_y - 1)) {
			this.absolute_y -= 1;
		} else if(GameplayScreen.instance.getmap().istileempty(absolute_x, absolute_y + 1)) {
			this.absolute_y += 1;
		} else {

			System.out.println("  --  couldn't move y!");
		}
		enforcenonpenetrationconstraints();
	}
	
	private void enforcenonpenetrationconstraints() {
		// TODO check with map, my array, the game engine, or 
		// something else to make sure the move I tried to make 
		// did not force me into a wall or other object
	}

	public int getabsolutedistancesq(int x, int y) {
		int x1 = absolute_x;
		int y1 = absolute_y;
		
		int x2 = x - x1;
		int y2 = y - y1;
		
		return x2 * x2 + y2 * y2;
	}
	
	public int getabsolutedistance(int x, int y) {
		return (int) Math.sqrt(getabsolutedistancesq(x, y));
	}
}
