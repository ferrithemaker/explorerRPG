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





public class Hero implements TileOccupier {
	private int agility;
	private int force;
	private int relative_x_tile;	
	private int relative_y_tile;
	private String name;
	private int life;
	private int maxlife;
	private int level=1;
	private int exp;
	private int resist;
	private int current_sprite_position; // sprite that will be shown
	//private Sprite sprite;
	private Texture texture;
	private Object head;
	private Object lefthand;
	private Object righthand;
	private Object body;
	private Object foot;

	

	public Hero(WrapperEngine engine, String name, String file) {
		

		// initial set-up
		this.agility=4; 
		this.force=5; // offense
		this.resist=3; // defense
		this.life=this.maxlife=100; // hp
		this.exp=1; // experience
		this.relative_y_tile=1;
		this.relative_x_tile=1;
		this.name=name;
		init_sprite_pos(); // initial sprite position, every number corresponds to a sprite. Each set of sprites has different configuration
		this.head = new Object(); // empty object
		this.body = new Object(); // empty object
		this.lefthand = new Object(); // empty object
		this.righthand = new Object(); // empty object
		this.foot = new Object(); // empty object
		this.texture= new Texture(Gdx.files.internal(file));
		
	}

	// hero gets
	public String getname() {
		return this.name;
	}
	public int getexperience() {
		return this.exp;
	}
	public int getresist() {
		return this.resist+this.head.getdefense()+this.lefthand.getdefense()+this.righthand.getdefense()+this.body.getdefense()+this.foot.getdefense()+((int)(this.exp/WrapperEngine.EXPERIENCE_NEXT_LEVEL_LIMIT));
	}
	public int getforce() {
		return this.force+this.head.getattack()+this.lefthand.getattack()+this.righthand.getattack()+this.body.getattack()+this.foot.getattack()+((int)(this.exp/WrapperEngine.EXPERIENCE_NEXT_LEVEL_LIMIT));
	}
	public int getagility() {
		return this.agility+((int)(this.exp/WrapperEngine.EXPERIENCE_NEXT_LEVEL_LIMIT));
	}
	public int gethp() {
		return this.life;
	}
	public int getlevel() {
		return this.level;
	}
	public int getrelativextile() {
		return this.relative_x_tile;
	}
	public int getrelativeytile() {
		return this.relative_y_tile;
	}
	public Object gethead() {
		return this.head;
	}
	public Object getlefthand() {
		return this.lefthand;
	}
	public Object getrighthand() {
		return this.righthand;
	}
	public Object getbody() {
		return this.body;
	}
	public Object getfoot() {
		return this.foot;
	}
	public Sprite getimage() {
		return new Sprite(texture);
	}
	public float percentlife() {
		return ((float)(float) this.life / (float)this.maxlife ) * 100;
	}


	// hero sets / updates
	public void sethead(Object obj) {
		this.head=obj;
	}
	public void setlefthand(Object obj) {
		this.lefthand=obj;
	}
	public void setrighthand(Object obj) {
		this.righthand=obj;
	}
	public void setbody(Object obj) {
		this.body=obj;
	}
	public void setfoot(Object obj) {
		this.foot=obj;
	}
	public void decayhead() {
		 if (gethead().getname()!=null) { // if object exist
			 gethead().reducedurability(1);
		 }
	}
	public void decaylefthand() {
		 if (getlefthand().getname()!=null) { // if object exist
			 getlefthand().reducedurability(1);
		 }
	}
	public void decayrighthand() {
		if (getrighthand().getname()!=null) { // if object exist
			 getrighthand().reducedurability(1);
		 }
	}
	public void decaybody() {
		if (getbody().getname()!=null) { // if object exist
			 getbody().reducedurability(1);
		 }
	}
	public void decayfoot() {
		if (getfoot().getname()!=null) { // if object exist
			 getfoot().reducedurability(1);
		 }
	}
	public void randomdecay() {
		Random randomGenerator = new Random();
		int object_2_decay = randomGenerator.nextInt(5);
		switch (object_2_decay) {
		case 0:
			decayfoot();
			break;
		case 1:
			decayhead();
			break;
		case 2:
			decaylefthand();
			break;
		case 3:
			decayrighthand();
			break;
		case 4:
			decaybody();
			break;
		}
	}

	public void setrelativextile(int value) {
		this.relative_x_tile=value;
		//this.engine.onplayermove();
	}
	public void setrelativeytile(int value) {
		this.relative_y_tile=value;
		//this.engine.onplayermove();
	}
	public void updateagility(int value) {
		this.agility=this.agility+value;
	}
	public void updateexperience(int value) {
		this.exp=this.exp+value;
		
	// check for level up
	}
	public void updatehp(int value) {
		this.life=Math.min(this.maxlife, this.life+value);
	}
	public void updatelevel() {
		this.level++;
	}
	public void updateforce(int value) {
		this.force=this.force+value;
	}
	public void updateresist(int value) {
		this.resist=this.resist+value;
	}

	// hero position updates
	public void up(Map map) {
		if (this.relative_y_tile>0) {
			this.setrelativeytile(this.relative_y_tile - 1);
			sprite_goup();
		} else {
			scrollup();
			map.scrollup();
		}
	}
	public void down(Map map) {
		if (this.relative_y_tile<WrapperEngine.ON_SCREEN_TILES_Y-1) {
			this.setrelativeytile(this.relative_y_tile + 1);
			sprite_godown();
		} else {
			scrolldown();
			map.scrolldown();
		}
	}
	public void left(Map map) {
		if (this.relative_x_tile>0) {
			this.setrelativextile(this.relative_x_tile - 1);
			sprite_goleft();
		} else {
			scrollleft();
			map.scrollleft();
		}
	}
	public void right(Map map) {
		if (this.relative_x_tile<WrapperEngine.ON_SCREEN_TILES_X-1) {
			this.setrelativextile(this.relative_x_tile + 1);
			sprite_goright();
		} else {
			scrollright();
			map.scrollright();
		}
	}
	
	public void scrollup() {
		this.setrelativeytile(WrapperEngine.ON_SCREEN_TILES_Y-1);
	}
	public void scrolldown() {
		this.setrelativeytile(0);
	}
	public void scrollleft() {
		this.setrelativextile(WrapperEngine.ON_SCREEN_TILES_X-1);
	}
	public void scrollright() {
		this.setrelativextile(0);
	}

	// fight
	// hero hit enemy
	public String hit(Enemy enemy) {
		Random randomGenerator = new Random();
		int herohit= (((int)(Math.ceil((double)((double)this.agility/(double)3)))*this.force)-enemy.getresist());
		int enemyhit=(((int)(Math.ceil((double)((double)enemy.getagility()/(double)3)))*enemy.getforce())-this.resist);
		int heromodifier = randomGenerator.nextInt(this.agility); // random component based on agility
		int enemymodifier = randomGenerator.nextInt(enemy.getagility()); // random component based on agility

		herohit=Math.max(0, herohit+heromodifier);

		enemyhit=Math.max(0, enemyhit+enemymodifier);
		enemy.updatehp(herohit); // hero hits enemy

		randomdecay(); // durability decreases
		if (enemy.gethp()>0) { // if enemy is alive
			this.life=Math.max(0, this.life-enemyhit); // enemy hits hero
			randomdecay(); // durability decreases, twice?
		} else {
			return "ENEMYDEAD";
		}

		if (this.life<=0) {
			return "HERODEAD";
		}

		return this.name+" deal "+herohit+" damage points\nto "+enemy.getname()+"\nand "+enemy.getname()+" deal "+enemyhit+" damage points\nto "+this.name;

	}


	// *** BEGIN hero sprite management. you MUST modify it with your own sprite behavior
	private void init_sprite_pos() {	// why does the naming scheme change here?
		current_sprite_position=7;
	}
	private void sprite_godown() {
		switch(current_sprite_position) {
		case 10:
			current_sprite_position=11;
			break;
		case 11:
			current_sprite_position=12;
			break;
		case 12:
			current_sprite_position=10;
			break;
		default:
			current_sprite_position=10;
			break;
		}
	}
	private void sprite_goup() {
		switch(current_sprite_position) {
		case 1:
			current_sprite_position=2;
			break;
		case 2:
			current_sprite_position=3;
			break;
		case 3:
			current_sprite_position=1;
			break;
		default:
			current_sprite_position=1;
			break;
		}	
	}
	private void sprite_goleft() {
		switch(current_sprite_position) {
		case 4:
			current_sprite_position=5;
			break;
		case 5:
			current_sprite_position=6;
			break;
		case 6:
			current_sprite_position=4;
			break;
		default:
			current_sprite_position=4;
			break;
		}
	}
	private void sprite_goright() {
		switch(current_sprite_position) {
		case 7:
			current_sprite_position=8;
			break;
		case 8:
			current_sprite_position=9;
			break;
		case 9:
			current_sprite_position=7;
			break;
		default:
			current_sprite_position=7;
			break;
		}
	}
	public int getyspriteposition() {
		if (current_sprite_position==1 || current_sprite_position==2 || current_sprite_position==3) {
			return WrapperEngine.TILE_Y_SIZE*0;
		}
		if (current_sprite_position==4 || current_sprite_position==5 || current_sprite_position==6) {
			return WrapperEngine.TILE_Y_SIZE*1;
		}
		if (current_sprite_position==7 || current_sprite_position==8 || current_sprite_position==9) {
			return WrapperEngine.TILE_Y_SIZE*2;
		}
		if (current_sprite_position==10 || current_sprite_position==11 || current_sprite_position==12) {
			return WrapperEngine.TILE_Y_SIZE*3;
		}
		return 0;
	}
	public int getxspriteposition() {
		if (current_sprite_position==1 || current_sprite_position==4 || current_sprite_position==7 || current_sprite_position==10 ) {
			return WrapperEngine.TILE_X_SIZE*0;
		}
		if (current_sprite_position==2 || current_sprite_position==5 || current_sprite_position==8 || current_sprite_position==11) {
			return WrapperEngine.TILE_X_SIZE*1;
		}
		if (current_sprite_position==3 || current_sprite_position==6 || current_sprite_position==9 || current_sprite_position==12) {
			return WrapperEngine.TILE_X_SIZE*2;
		}
		return 0;
	}
	public Sprite getsprite() {
        return new Sprite(texture,getxspriteposition(),getyspriteposition(),WrapperEngine.TILE_X_SIZE,WrapperEngine.TILE_Y_SIZE);
	}
	// *** END hero sprite management. you MUST modify it with your own sprite behavior

	@Override
	public String getdescription() {
		return "The Hero of our tale.";
	}
	
	public int getabsolutex(Map map) {
		return map.getfirstxtile() + this.relative_x_tile;
	}
	public int getabsolutey(Map map) {
		return map.getfirstytile() + this.relative_y_tile;
	}
	
	public Tile gettile(Map map) {
		return map.gettileat(getabsolutex(map), getabsolutey(map));
	}
}
