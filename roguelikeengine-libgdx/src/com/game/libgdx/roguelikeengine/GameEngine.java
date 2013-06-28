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



import java.util.ArrayList;
import java.util.Random;



// Map class is a wrapper class for all other basic classes

public class GameEngine {
	// constants
	public final static int TOTAL_X_TILES=320; // TOTAL TILES MUST BE MULTIPLE OF SCREEN TILES!!!!!!!!!
	public final static int TOTAL_Y_TILES=640; // TOTAL TILES MUST BE MULTIPLE OF SCREEN TILES!!!!!!!!!
	public final static int ON_SCREEN_TILES_X=15; // TOTAL TILES MUST BE MULTIPLE OF SCREEN TILES!!!!!!!!!
	public final static int ON_SCREEN_TILES_Y=10; // TOTAL TILES MUST BE MULTIPLE OF SCREEN TILES!!!!!!!!!
	public final static int TILE_X_SIZE=64;
	public final static int TILE_Y_SIZE=64;
	public final static int MAX_WALL_LENGTH=15;
	public final static int MAX_LAKE_SIZE=15;
	public final static int OPTION_MENU_X_SIZE=512;
	public final static int WINDOWWITH=TILE_X_SIZE*ON_SCREEN_TILES_X+OPTION_MENU_X_SIZE;
	public final static int WINDOWHEIGHT=TILE_Y_SIZE*ON_SCREEN_TILES_Y;
	public final static int FPS=18;
	public final static int INVENTORY_SIZE=10;
	public final static String APP_NAME="Rogue explorer";
	public final static int NUMBER_OF_WALLS=300;
	public final static int NUMBER_OF_LAKES=300;
	public final static int NUMBER_OF_BLOCKING_OBJECTS=1000;
	public final static int EXPERIENCE_NEXT_LEVEL_LIMIT=1000;
	
	// variables
	private Tile[][] tilelayout;
    private Enemy_array badguys;
    private Object_array availableobjects;
    private Consumable_array availableconsumables;
    private Hero prota;
    private Map mapa;

    
	// START METHOD INITIALIZES ALL CLASSES OF THE GAME
	public GameEngine () {  
			
		// create hero
        prota=new Hero("ferriman","holder_sprite.png");
		
        // create Map
        mapa=new Map();
        mapa.createrandommap();
        tilelayout=mapa.gettiles();
             
        // create initial empty enemy array
        badguys= new Enemy_array();
		
        // create initial empty object array
        availableobjects=new Object_array();
        
        // create initial empty consumable array
        availableconsumables=new Consumable_array();
	}
	
	// MAP CLASS WRAPPER
	public Map getmap() {
		return mapa;
	}
		
	// HERO CLASS WRAPPER
	public Hero gethero() {
		return prota;
	}
	
	public void herodies() {
		mapa.setfirstxtile(0);
		mapa.setfirstytile(0);
		prota.setrelativextile(1);
		prota.setrelativeytile(1);
		prota.updatehp(50);
	}
	
	// hero updates
	public void heroup() {
		
		if (mapa.getfirstytile()+prota.getrelativeytile()<GameEngine.TOTAL_Y_TILES-1) {
			if (prota.getrelativeytile()==GameEngine.ON_SCREEN_TILES_Y-1 && tilelayout[mapa.getfirstxtile()+prota.getrelativextile()][1+mapa.getfirstytile()+prota.getrelativeytile()].isbloqued()==false) {		
				if (mapa.getfirstytile()<GameEngine.TOTAL_Y_TILES-GameEngine.ON_SCREEN_TILES_Y) { prota.scrolldown();  mapa.setfirstytile(mapa.getfirstytile() + GameEngine.ON_SCREEN_TILES_Y); }
			} else {
				if (tilelayout[mapa.getfirstxtile()+prota.getrelativextile()][1+mapa.getfirstytile()+prota.getrelativeytile()].isbloqued()==false) {
					prota.down();
				}
			}
		}
	}
		
	public void herodown() {
		if (mapa.getfirstytile()+prota.getrelativeytile()>0) {
			if (prota.getrelativeytile()==0 && tilelayout[mapa.getfirstxtile()+prota.getrelativextile()][mapa.getfirstytile()+prota.getrelativeytile()-1].isbloqued()==false) {		
				if (mapa.getfirstytile()>0) { prota.scrollup(); mapa.setfirstytile(mapa.getfirstytile() - GameEngine.ON_SCREEN_TILES_Y); }
			} else {
				if (tilelayout[mapa.getfirstxtile()+prota.getrelativextile()][mapa.getfirstytile()+prota.getrelativeytile()-1].isbloqued()==false) {
					prota.up();
				}	
			}
		}
	}
		
	public void heroright() {
		if (mapa.getfirstxtile()+prota.getrelativextile()<GameEngine.TOTAL_X_TILES-1) {
			if (prota.getrelativextile()==GameEngine.ON_SCREEN_TILES_X-1 && tilelayout[1+mapa.getfirstxtile()+prota.getrelativextile()][mapa.getfirstytile()+prota.getrelativeytile()].isbloqued()==false) {
				if (mapa.getfirstxtile()<GameEngine.TOTAL_X_TILES-GameEngine.ON_SCREEN_TILES_X) { prota.scrollrigth(); mapa.setfirstxtile(mapa.getfirstxtile() + GameEngine.ON_SCREEN_TILES_X); }
			} else {
				if (tilelayout[1+mapa.getfirstxtile()+prota.getrelativextile()][mapa.getfirstytile()+prota.getrelativeytile()].isbloqued()==false) {
					prota.right();
				}
			}
		}
	}
		
	public void heroleft() {
		if (mapa.getfirstxtile()+prota.getrelativextile()>0) {
			if (prota.getrelativextile()==0 && tilelayout[mapa.getfirstxtile()+prota.getrelativextile()-1][mapa.getfirstytile()+prota.getrelativeytile()].isbloqued()==false) {
				if (mapa.getfirstxtile()>0) { prota.scrollleft(); mapa.setfirstxtile(mapa.getfirstxtile() - GameEngine.ON_SCREEN_TILES_X); }
			} else {
				if (tilelayout[mapa.getfirstxtile()+prota.getrelativextile()-1][mapa.getfirstytile()+prota.getrelativeytile()].isbloqued()==false) {
					prota.left();
				}
			}
		}
	}
	
	
	// ENEMY / ENEMY_ARRAY CLASS WRAPPER
	public ArrayList<Enemy> getenemies() {
		return badguys.getlist();
	}
	public Enemy overenemy() {
		 return badguys.overenemy(prota.getrelativextile()+mapa.getfirstxtile(),prota.getrelativeytile()+mapa.getfirstytile());
	}
	public void removeenemy(Enemy obj) {
		badguys.remove_enemy(obj);
	}
	public void createrandomenemy() { // create a random enemy
		Random randomGenerator = new Random();
		// generates random position
		int x = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES);
		int y = randomGenerator.nextInt(GameEngine.TOTAL_Y_TILES);
		int enemytype = randomGenerator.nextInt(6); // random enemy choose
		if (!tilelayout[x][y].isbloqued()) { // if there is empty space
			if (enemytype==0) {
				badguys.add_enemy(new Enemy("vortex",2,5,3,20,x,y,"vortex.png"));
			}
			if (enemytype==1) {
				badguys.add_enemy(new Enemy("catharg",3,6,4,40,x,y,"cetharg.png"));
			}
			if (enemytype==2) {
				badguys.add_enemy(new Enemy("assassin",3,8,1,30,x,y,"assassin.png"));
			}
			if (enemytype==3) {
				badguys.add_enemy(new Enemy("giant rat",1,10,8,60,x,y,"giantRat.png"));
			}
			if (enemytype==4) {
				badguys.add_enemy(new Enemy("medusa",5,4,5,30,x,y,"medusa.png"));
			}
			if (enemytype==5) {
				badguys.add_enemy(new Enemy("warlock",8,5,5,25,x,y,"warlock.png"));
			}
		}
	}
	public void createenemy(String name,int ag,int str, int res, int lf, int x,int y,String file) {
		badguys.add_enemy(new Enemy(name,ag,str,res,lf,x,y,file));
	}
	
	// OBJECT CLASSES WRAPPER
	public ArrayList<Object> getobjects() {
		return availableobjects.getlist();
	}
	public Object overobject() {
		 return availableobjects.overobject(prota.getrelativextile()+mapa.getfirstxtile(),prota.getrelativeytile()+mapa.getfirstytile());
	}
	public void removeobject(Object obj) {
		availableobjects.remove_object(obj);
	}
	public void createrandomobject() {
		Random randomGenerator = new Random();
		// generates random position
		int x = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES);
		int y = randomGenerator.nextInt(GameEngine.TOTAL_Y_TILES);
		int chances = randomGenerator.nextInt(100);
		int objecttype = randomGenerator.nextInt(11);
		if (!tilelayout[x][y].isbloqued()) { // if there is empty space
			if (objecttype==0) {
				if (chances<90) {
					availableobjects.add_object(new Object("long sword","righthand",10,0,10,x,y,"longSword.png"));
				}
			}
			if (objecttype==1) {
				if (chances<90) {
					availableobjects.add_object(new Object("dagger","righthand",3,0,7,x,y,"dagger.png"));
				}
			}
			if (objecttype==2) {
				if (chances<90) {
					availableobjects.add_object(new Object("boots","foot",0,6,4,x,y,"boots.png"));
				}
			}
			if (objecttype==3) {
				if (chances<90) {
					availableobjects.add_object(new Object("heavy armor","body",0,15,10,x,y,"heavyarmor.png"));
				}
			}
			if (objecttype==4) {
				if (chances<90) {
					availableobjects.add_object(new Object("helm","head",0,4,6,x,y,"helm.png"));
				}
			}
			if (objecttype==5) {
				if (chances<90) {
					availableobjects.add_object(new Object("mace","lefthand",7,0,8,x,y,"mace.png"));
				}
			}
			if (objecttype==6) {
				if (chances<90) {
					availableobjects.add_object(new Object("riot shield","lefthand",0,9,12,x,y,"riotShield.png"));
				}
			}
			if (objecttype==7) {
				if (chances<90) {
					availableobjects.add_object(new Object("armor","body",0,11,7,x,y,"reflecArmor.png"));
				}
			}
			if (objecttype==8) {
				if (chances<90) {
					availableobjects.add_object(new Object("shield","lefthand",0,7,6,x,y,"shield.png"));
				}
			}
			if (objecttype==9) {
				if (chances<90) {
					availableobjects.add_object(new Object("skull cap","head",0,5,5,x,y,"skullcap.png"));
				}
			}
			if (objecttype==10) {
				if (chances<90) {
					availableobjects.add_object(new Object("great shield","lefthand",0,12,11,x,y,"greatShield.png"));
				}
			}
		}
	}
	public void createobject(String name,String position,int attack, int defense, int durability,int x,int y,String file) {
		availableobjects.add_object(new Object(name,position,attack,defense,durability,x,y,file));
	}
	
	// CONSUMABLE CLASSES WRAPPER
	public ArrayList<Consumable> getconsumables() {
		return availableconsumables.getlist();
	}
	public Consumable overconsumable() {
		 return availableconsumables.overconsumable(prota.getrelativextile()+mapa.getfirstxtile(),prota.getrelativeytile()+mapa.getfirstytile());
	}
	public void removeconsumable(Consumable c) {
		availableconsumables.remove_consumable(c);
	}
	public void addconsumable(Consumable c) {
		availableconsumables.add_consumable(c);
	}
	public void createrandomconsumable() {
		Random randomGenerator = new Random();
		// generates random position
		int x = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES);
		int y = randomGenerator.nextInt(GameEngine.TOTAL_Y_TILES);
		int potiontype = randomGenerator.nextInt(3);
        if (potiontype==0) {
        	availableconsumables.add_consumable(new Consumable("Blue potion",1,1,0,2,x,y,"potionblue.png"));
        }
        if (potiontype==1) {
        	availableconsumables.add_consumable(new Consumable("Red potion",0,1,1,1,x,y,"potionred.png"));
        }
        if (potiontype==2) {
        	availableconsumables.add_consumable(new Consumable("Yellow potion",2,1,0,0,x,y,"potionyellow.png"));
        }
    }
	public void createconsumable(String name, int p_agility, int p_life,int force, int resist,int x,int y,String file) {
        availableconsumables.add_consumable(new Consumable(name,p_agility,p_life,force,resist,x,y,file));
	}
	
	
	
}
