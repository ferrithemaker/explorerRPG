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





public class Hero implements MovingTileOccupier {
	private int agility;
	private int force;
	//private int relative_x_tile;	
	//private int relative_y_tile;
	
	private int abs_x;
	private int abs_y;
	
	private String name;
	private int life;
	private int lifePerStep = 1;
	private int maxlife;
	private int maxMagic;
	private int magic;
	private int magicPerStep = 1;
	private int magicCost = 11;
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

	protected Directions direction = Directions.EAST;
	protected WrapperEngine engine;

	public Hero(WrapperEngine engine, String name, String file) {
		this.engine = engine;

		// initial set-up
		this.agility=4; 
		this.force=5; // offense
		this.resist=3; // defense
		this.life=this.maxlife=100; // hp
		this.magic=this.maxMagic=100;
		this.exp=1; // experience
		//this.relative_y_tile=1;
		//this.relative_x_tile=1;
		
		this.abs_x = 1;
		this.abs_y = 1;
		
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
	public int getmagic() {
		return this.magic;
	}
	public int getlevel() {
		return this.level;
	}
	public int getrelativextile(Map map) {
		return this.abs_x - map.getfirstxtile();
	}
	public int getrelativeytile(Map map) {
		return this.abs_y - map.getfirstytile();
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

	public void setrelativextile(Map map, int value) {
		this.abs_x = map.getfirstxtile() + value;
	}
	public void setrelativeytile(Map map, int value) {
		this.abs_y = map.getfirstytile() + value;
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
	public void updatemagic(int value) {
		this.magic=Math.max(0, Math.min(this.maxMagic, this.magic+value));
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
		this.abs_y = Math.max(0, this.abs_y - 1);
		changeDirection(Directions.NORTH);
	}
	public void down(Map map) {
		this.abs_y = Math.min(WrapperEngine.TOTAL_Y_TILES - 1, this.abs_y + 1);
		changeDirection(Directions.SOUTH);
	}
	public void left(Map map) {
		this.abs_x = Math.max(0, this.abs_x - 1);
		changeDirection(Directions.WEST);
	}
	public void right(Map map) {
		this.abs_x = Math.min(WrapperEngine.TOTAL_X_TILES-1, this.abs_x + 1);
		changeDirection(Directions.EAST);
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

		String result = this.name+" did "+herohit+" damage points to " + enemy.getname() + " \n#FF0000";

		for(String namePart : enemy.getname().split(" ")) {
			result += "#FF0000" + namePart + " ";
		}

		result += "#FF0000did #FF0000"+enemyhit+" #FF0000damage #FF0000points #FF0000to #FF0000"+this.name;

		return result;
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
		direction = Directions.NORTH;	// libgdx y is inverted
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
		direction = Directions.SOUTH;
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
		direction = Directions.WEST;
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
		direction = Directions.EAST;
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
		return this.abs_x;
	}
	public int getabsolutey(Map map) {
		return this.abs_y;
	}
	
	public Tile gettile(Map map) {
		return map.gettileat(getabsolutex(map), getabsolutey(map));
	}
	
	protected void onStep() {
		this.updatemagic(magicPerStep);
		this.updatehp(lifePerStep);
	}
	
	public Bullet fireBullet() {
		Bullet result = null;
		if(this.magic >= this.magicCost) {
			result = new Fireball(engine.getactivemap(), this, this.direction);
			this.magic -= this.magicCost;
		}
		
		return result;
	}
	
	public void changeDirection(Directions direction) {
		switch (direction) {
		case NORTH:
			this.sprite_goup();
			break;
		case SOUTH:
			this.sprite_godown();
			break;
		case EAST:
			this.sprite_goright();
			break;
		case WEST:
		default:
			this.sprite_goleft();
		}
		
		this.direction = direction;
	}

	@Override
	public int getabsolutecolumn(Map map) {
		return this.getabsolutex(map);
	}

	@Override
	public int getabsoluterow(Map map) {
		return this.getabsolutey(map);
	}
	
	public int getlayer() {
		return engine.getlayer();
	}

	public float percentmagic() {
		return ((float)(float) this.magic / (float)this.maxMagic ) * 100;
	}
	
	public Directions getDirection() { return direction; }

	public boolean hasKey() {
		Object[] inv = GameplayScreen.instance.getobjinv().getinventory();
		for(Object obj : inv) {
			if(obj != null && obj.getname().toLowerCase().contains("key")) {
				return true;
			}
		}

		
		return false;
	}
	
	public boolean removeKey() {
		Object toRemove = null;
		
		Object[] inv = GameplayScreen.instance.getobjinv().getinventory();
		for(Object obj : inv) {
			if(obj != null && obj.getname().toLowerCase().contains("key")) {
				toRemove = obj;
				break;
			}
		}

		if(toRemove != null) {
			GameplayScreen.instance.getobjinv().delete_object(toRemove);
			return true;
		}
		
		return false;
	}
	
	public void update() {}

	@Override
	public boolean goLeft(Map map) {
		//return engine.heroleft();
		return GameplayScreen.instance.goleft();
	}

	@Override
	public boolean goRight(Map map) {
		//return engine.heroright();
		return GameplayScreen.instance.goright();
	}

	@Override
	public boolean goUp(Map map) {
		//return engine.herodown();
		return GameplayScreen.instance.godown();
	}

	@Override
	public boolean goDown(Map map) {
		//return engine.heroup();
		return GameplayScreen.instance.goup();
	}
	
	public void moveInto(Map map, Chest chest) {
		if(chest.getabsolutecolumn(map) < getabsolutecolumn(map)) {
			goLeft(map);
		} else if(chest.getabsolutecolumn(map) < getabsolutecolumn(map)) {
			goRight(map);
		}
		
		if(chest.getabsoluterow(map) < getabsoluterow(map)) {
			goDown(map);
		} else if(chest.getabsoluterow(map) < getabsoluterow(map)) {
			goUp(map);
		}
	}
}
