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
	public final static int ON_SCREEN_TILES_X=13;
	public final static int ON_SCREEN_TILES_Y=10;
	public final static int X_SCREENS=30;
	public final static int Y_SCREENS=30;
	public final static int TOTAL_X_TILES=X_SCREENS*ON_SCREEN_TILES_X;
	public final static int TOTAL_Y_TILES=Y_SCREENS*ON_SCREEN_TILES_Y;
	public final static int TILE_X_SIZE=64;
	public final static int TILE_Y_SIZE=64;
	public final static int MAX_WALL_LENGTH=12;
	public final static int MAX_LAKE_SIZE=12;
	public final static int OPTION_MENU_X_SIZE=448;
	public final static int ACTIONS_MENU_Y_SIZE=64;
	public final static int WINDOWWIDTH=TILE_X_SIZE*ON_SCREEN_TILES_X+OPTION_MENU_X_SIZE;
	public final static int WINDOWHEIGHT=TILE_Y_SIZE*ON_SCREEN_TILES_Y+ACTIONS_MENU_Y_SIZE;
	public final static int FPS=18;
	public final static int INVENTORY_SIZE=10;
	public final static String APP_NAME="Rogue explorer testing game (libgdx)";
	public final static int NUMBER_OF_WALLS=300;
	public final static int NUMBER_OF_LAKES=300;
	public final static int NUMBER_OF_BLOCKING_OBJECTS=1000;
	public final static int EXPERIENCE_NEXT_LEVEL_LIMIT=1000;
	
	// dynamic layers 
	public final static int NUMBER_OF_MAP_LAYERS=2;
	// default entry coords for dungeons
	public final static int LAYER_0_ENTRY_XPOS=1;
	public final static int LAYER_0_ENTRY_YPOS=0;
	public final static int LAYER_1_ENTRY_XPOS=1;
	public final static int LAYER_1_ENTRY_YPOS=3;
	
	// android specific constants
	public final static int ANDROID_MENU_BAR_SIZE=43;
	public final static boolean ANDROID_MENU_BAR_ENABLE=false;
	
	// variables
	//private Tile[][] tilelayout; // deprecated, must be removed when dynamic layer system will be finished 
	//private Tile[][] tilelayoutdungeon; // deprecated, must be removed when dynamic layer system will be finished 
	private Tile[][][] layertiles = new Tile[2][][]; // new dynamic layer system ([layer number][x][y])
	private Tile[][] selectedtiles;
    private Enemy_array badguys;
    private Object_array availableobjects;
    private Consumable_array availableconsumables;
    private Hero prota;
    //private Map mapa; // deprecated, must be removed when dynamic layer system will be finished 
    //private Map dungeon; // deprecated, must be removed when dynamic layer system will be finished 
    private Map[] maplayers = new Map[2]; // new dynamic layer system
    private int layer;


    
	// START METHOD INITIALIZES ALL CLASSES OF THE GAME
	public GameEngine () {  
			
		// create hero
        prota=new Hero(this, "ferriman","holder_sprite.png");
		
        // create Map
        
        maplayers[0]= new Map();
        maplayers[1]= new Map();
        maplayers[0].createrandommap();
        maplayers[1].createrandomdungeon();
        layertiles[0]=maplayers[0].gettiles();
        layertiles[1]=maplayers[1].gettiles();
        
        //mapa.createrandommap();
        //tilelayout=mapa.gettiles();
        
        // create Map
        //dungeon=new Map();
        //dungeon.createrandomdungeon();
        //tilelayoutdungeon=dungeon.gettiles();
        
        // setup initial layer
        layer=0;
        selectedtiles=layertiles[0];
        
        // create initial empty enemy array
        badguys= new Enemy_array();
		
        // create initial empty object array
        availableobjects=new Object_array();
        
        // create initial empty consumable array
        availableconsumables=new Consumable_array();
        
        insurevalidplayerposition();
	}

	/**
	 *  Moves the player up one tile at a time until the player is on valid, empty land.
	 */
	protected void insurevalidplayerposition() {
		int x = prota.getrelativextile();
        int y = prota.getrelativeytile();
        while(selectedtiles[x][y].isbloqued()) {
        	prota.setrelativeytile(++y);
        }
	}
	
	// MAP CLASS WRAPPER
	public Map getmaplayer(int value) {
		return maplayers[value];
	}
	
	/*public Map getdungeon() { // deprecated, use getmaplayer
		return dungeon;
	}*/
	public int getlayer() { // this is the actual layer
		return layer; // this variable has the actual layer
	}
	public void layerup() {
		layer++;
		if (layer>NUMBER_OF_MAP_LAYERS-1) layer=NUMBER_OF_MAP_LAYERS-1;
		selectedtiles=layertiles[layer];
	}
	public void layerdown() {
		layer--;
		if (layer<0) layer=0;
		selectedtiles=layertiles[layer];
	}
		
	// HERO CLASS WRAPPER
	public Hero gethero() {
		return prota;
	}
	
	public void herodies() {
		maplayers[layer].setfirstxtile(0);
		maplayers[layer].setfirstytile(0);
		prota.setrelativextile(1);
		prota.setrelativeytile(1);
		prota.updatehp(50);
	}
	
	// hero updates
	public void heroup() {
		
		if (maplayers[layer].getfirstytile()+prota.getrelativeytile()<GameEngine.TOTAL_Y_TILES-1) {
			if (prota.getrelativeytile()==GameEngine.ON_SCREEN_TILES_Y-1 && selectedtiles[maplayers[layer].getfirstxtile()+prota.getrelativextile()][1+maplayers[layer].getfirstytile()+prota.getrelativeytile()].isbloqued()==false) {		
				if (maplayers[layer].getfirstytile()<GameEngine.TOTAL_Y_TILES-GameEngine.ON_SCREEN_TILES_Y) { prota.scrolldown();  maplayers[layer].setfirstytile(maplayers[layer].getfirstytile() + GameEngine.ON_SCREEN_TILES_Y); }
			} else {
				if (selectedtiles[maplayers[layer].getfirstxtile()+prota.getrelativextile()][1+maplayers[layer].getfirstytile()+prota.getrelativeytile()].isbloqued()==false) {
					prota.down();
				}
			}
		}
	}
		
	public void herodown() {
		if (maplayers[layer].getfirstytile()+prota.getrelativeytile()>0) {
			if (prota.getrelativeytile()==0 && selectedtiles[maplayers[layer].getfirstxtile()+prota.getrelativextile()][maplayers[layer].getfirstytile()+prota.getrelativeytile()-1].isbloqued()==false) {		
				if (maplayers[layer].getfirstytile()>0) { prota.scrollup(); maplayers[layer].setfirstytile(maplayers[layer].getfirstytile() - GameEngine.ON_SCREEN_TILES_Y); }
			} else {
				if (selectedtiles[maplayers[layer].getfirstxtile()+prota.getrelativextile()][maplayers[layer].getfirstytile()+prota.getrelativeytile()-1].isbloqued()==false) {
					prota.up();
				}	
			}
		}
	}
		
	public void heroright() {
		if (maplayers[layer].getfirstxtile()+prota.getrelativextile()<GameEngine.TOTAL_X_TILES-1) {
			if (prota.getrelativextile()==GameEngine.ON_SCREEN_TILES_X-1 && selectedtiles[1+maplayers[layer].getfirstxtile()+prota.getrelativextile()][maplayers[layer].getfirstytile()+prota.getrelativeytile()].isbloqued()==false) {
				if (maplayers[layer].getfirstxtile()<GameEngine.TOTAL_X_TILES-GameEngine.ON_SCREEN_TILES_X) { prota.scrollrigth(); maplayers[layer].setfirstxtile(maplayers[layer].getfirstxtile() + GameEngine.ON_SCREEN_TILES_X); }
			} else {
				if (selectedtiles[1+maplayers[layer].getfirstxtile()+prota.getrelativextile()][maplayers[layer].getfirstytile()+prota.getrelativeytile()].isbloqued()==false) {
					prota.right();
				}
			}
		}
	}
		
	public void heroleft() {
		if (maplayers[layer].getfirstxtile()+prota.getrelativextile()>0) {
			if (prota.getrelativextile()==0 && selectedtiles[maplayers[layer].getfirstxtile()+prota.getrelativextile()-1][maplayers[layer].getfirstytile()+prota.getrelativeytile()].isbloqued()==false) {
				if (maplayers[layer].getfirstxtile()>0) { prota.scrollleft(); maplayers[layer].setfirstxtile(maplayers[layer].getfirstxtile() - GameEngine.ON_SCREEN_TILES_X); }
			} else {
				if (selectedtiles[maplayers[layer].getfirstxtile()+prota.getrelativextile()-1][maplayers[layer].getfirstytile()+prota.getrelativeytile()].isbloqued()==false) {
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
		 return badguys.overenemy(prota.getrelativextile()+maplayers[layer].getfirstxtile(),prota.getrelativeytile()+maplayers[layer].getfirstytile());
	}
	public void removeenemy(Enemy obj) {
		badguys.remove_enemy(obj);
	}
	public void createrandomenemy() { // create a random enemy
		Random randomGenerator = new Random();
		// generates random position
		int x = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES);
		int y = randomGenerator.nextInt(GameEngine.TOTAL_Y_TILES);
		int x2 = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES);
		int y2 = randomGenerator.nextInt(GameEngine.TOTAL_Y_TILES);
		int enemytype = randomGenerator.nextInt(6); // random enemy choose
		if (!selectedtiles[x][y].isbloqued()) { // if there is empty space
			if (enemytype==0) {
				badguys.add_enemy(new Enemy(0,"vortex",2,5,3,20,x,y,"vortex2.png"));
				if (!selectedtiles[x2][y2].isbloqued()) 
					badguys.add_enemy(new Enemy(1,"vortex",2,5,3,20,x2,y2,"vortex2.png"));
			}
			if (enemytype==1) {
				badguys.add_enemy(new Enemy(0,"catharg",3,6,4,40,x,y,"cetharg.png"));
				if (!selectedtiles[x2][y2].isbloqued()) 
					badguys.add_enemy(new Enemy(1,"catharg",3,6,4,40,x2,y2,"cetharg.png"));
			}
			if (enemytype==2) {
				badguys.add_enemy(new Enemy(0,"assassin",3,8,1,30,x,y,"assassin.png"));
				if (!selectedtiles[x2][y2].isbloqued()) 
					badguys.add_enemy(new Enemy(1,"assassin",3,8,1,30,x2,y2,"assassin.png"));
			}
			if (enemytype==3) {
				badguys.add_enemy(new Enemy(0,"giant rat",1,8,8,40,x,y,"giantrat2.png"));
				if (!selectedtiles[x2][y2].isbloqued()) 
					badguys.add_enemy(new Enemy(1,"giant rat",1,8,8,40,x2,y2,"giantrat2.png"));
			}
			if (enemytype==4) {
				badguys.add_enemy(new Enemy(0,"medusa",5,4,5,30,x,y,"medusa.png"));
				if (!selectedtiles[x2][y2].isbloqued()) 
					badguys.add_enemy(new Enemy(1,"medusa",5,4,5,30,x2,y2,"medusa.png"));
			}
			if (enemytype==5) {
				badguys.add_enemy(new Enemy(0,"warlock",8,5,5,25,x,y,"warlock.png"));
				if (!selectedtiles[x2][y2].isbloqued()) 
					badguys.add_enemy(new Enemy(1,"warlock",8,5,5,25,x2,y2,"warlock.png"));
			}
		}
	}
	public void createenemy(int layer,String name,int ag,int str, int res, int lf, int x,int y,String file) {
		badguys.add_enemy(new Enemy(layer,name,ag,str,res,lf,x,y,file));
	}
	
	// OBJECT CLASSES WRAPPER
	public ArrayList<Object> getobjects() {
		return availableobjects.getlist();
	}
	public Object overobject() {
		 return availableobjects.overobject(prota.getrelativextile()+maplayers[layer].getfirstxtile(),prota.getrelativeytile()+maplayers[layer].getfirstytile());
	}
	public void removeobject(Object obj) {
		availableobjects.remove_object(obj);
	}
	public void createrandomobject() {	// should return something to indicate something was generated?
		Random randomGenerator = new Random();
		// generates random position
		int x = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES);
		int y = randomGenerator.nextInt(GameEngine.TOTAL_Y_TILES);
		int x2 = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES);
		int y2 = randomGenerator.nextInt(GameEngine.TOTAL_Y_TILES);
		int chances = randomGenerator.nextInt(100);
		int objecttype = randomGenerator.nextInt(11);
		if (!selectedtiles[x][y].isbloqued()) { // if there is empty space
			if (objecttype==0) {
				if (chances<90) {
					availableobjects.add_object(new Object(0,"long sword","righthand",10,0,10,x,y,"longSword.png"));
					if (!selectedtiles[x2][y2].isbloqued()) // make sure second item is placed on an empty space, too
						availableobjects.add_object(new Object(1,"long sword","righthand",10,0,10,x2,y2,"longSword.png"));
					// return true? 
				}
			}
			if (objecttype==1) {
				if (chances<90) {
					availableobjects.add_object(new Object(0,"dagger","righthand",3,0,7,x,y,"dagger.png"));
					if (!selectedtiles[x2][y2].isbloqued()) 
						availableobjects.add_object(new Object(1,"dagger","righthand",3,0,7,x2,y2,"dagger.png"));
				}
			}
			if (objecttype==2) {
				if (chances<90) {
					availableobjects.add_object(new Object(0,"boots","foot",0,6,4,x,y,"boots.png"));
					if (!selectedtiles[x2][y2].isbloqued()) 
						availableobjects.add_object(new Object(1,"boots","foot",0,6,4,x2,y2,"boots.png"));					
				}
			}
			if (objecttype==3) {
				if (chances<90) {
					availableobjects.add_object(new Object(0,"heavy armor","body",0,15,10,x,y,"heavyarmor.png"));
					if (!selectedtiles[x2][y2].isbloqued()) 
						availableobjects.add_object(new Object(1,"heavy armor","body",0,15,10,x2,y2,"heavyarmor.png"));
				}
			}
			if (objecttype==4) {
				if (chances<90) {
					availableobjects.add_object(new Object(0,"helm","head",0,4,6,x,y,"helm.png"));
					if (!selectedtiles[x2][y2].isbloqued()) 
						availableobjects.add_object(new Object(1,"helm","head",0,4,6,x2,y2,"helm.png"));
				}
			}
			if (objecttype==5) {
				if (chances<90) {
					availableobjects.add_object(new Object(0,"mace","lefthand",7,0,8,x,y,"mace.png"));
					if (!selectedtiles[x2][y2].isbloqued()) 
						availableobjects.add_object(new Object(1,"mace","lefthand",7,0,8,x2,y2,"mace.png"));
				}
			}
			if (objecttype==6) {
				if (chances<90) {
					availableobjects.add_object(new Object(0,"riot shield","lefthand",0,9,12,x,y,"riotShield.png"));
					if (!selectedtiles[x2][y2].isbloqued()) 
						availableobjects.add_object(new Object(1,"riot shield","lefthand",0,9,12,x2,y2,"riotShield.png"));
				}
			}
			if (objecttype==7) {
				if (chances<90) {
					availableobjects.add_object(new Object(0,"armor","body",0,11,7,x,y,"reflecArmor.png"));
					if (!selectedtiles[x2][y2].isbloqued()) 
						availableobjects.add_object(new Object(1,"armor","body",0,11,7,x2,y2,"reflecArmor.png"));
				}
			}
			if (objecttype==8) {
				if (chances<90) {
					availableobjects.add_object(new Object(0,"shield","lefthand",0,7,6,x,y,"shield.png"));
					if (!selectedtiles[x2][y2].isbloqued()) 
						availableobjects.add_object(new Object(1,"shield","lefthand",0,7,6,x2,y2,"shield.png"));
				}
			}
			if (objecttype==9) {
				if (chances<90) {
					availableobjects.add_object(new Object(0,"skull cap","head",0,5,5,x,y,"skullcap.png"));
					if (!selectedtiles[x2][y2].isbloqued()) 
						availableobjects.add_object(new Object(1,"skull cap","head",0,5,5,x2,y2,"skullcap.png"));
				}
			}
			if (objecttype==10) {
				if (chances<90) {
					availableobjects.add_object(new Object(0,"great shield","lefthand",0,12,11,x,y,"greatShield.png"));
					if (!selectedtiles[x2][y2].isbloqued()) 
						availableobjects.add_object(new Object(1,"great shield","lefthand",0,12,11,x2,y2,"greatShield.png"));
				}
			}
		}
	}
	
	public void createobject(int layer,String name,String position,int attack, int defense, int durability,int x,int y,String file) {
		availableobjects.add_object(new Object(layer,name,position,attack,defense,durability,x,y,file));
	}
	
	// CONSUMABLE CLASSES WRAPPER
	public ArrayList<Consumable> getconsumables() {
		return availableconsumables.getlist();
	}
	public Consumable overconsumable() {
		 return availableconsumables.overconsumable(prota.getrelativextile()+maplayers[layer].getfirstxtile(),prota.getrelativeytile()+maplayers[layer].getfirstytile());
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
		int x2 = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES);
		int y2 = randomGenerator.nextInt(GameEngine.TOTAL_Y_TILES);
		int potiontype = randomGenerator.nextInt(3);
		if (!selectedtiles[x][y].isbloqued()) { // if there is empty space
			if (potiontype==0) {
				availableconsumables.add_consumable(new Consumable(0,"Blue potion",1,1,0,2,x,y,"potionblue.png"));
				if (!selectedtiles[x2][y2].isbloqued()) 
					availableconsumables.add_consumable(new Consumable(1,"Blue potion",1,1,0,2,x2,y2,"potionblue.png"));

			}
			if (potiontype==1) {
				availableconsumables.add_consumable(new Consumable(0,"Red potion",0,1,1,1,x,y,"potionred.png"));
				if (!selectedtiles[x2][y2].isbloqued()) 
					availableconsumables.add_consumable(new Consumable(1,"Red potion",0,1,1,1,x2,y2,"potionred.png"));

			}
			if (potiontype==2) {
				availableconsumables.add_consumable(new Consumable(0,"Yellow potion",2,1,0,0,x,y,"potionyellow.png"));
				if (!selectedtiles[x2][y2].isbloqued()) 
					availableconsumables.add_consumable(new Consumable(1,"Yellow potion",2,1,0,0,x2,y2,"potionyellow.png"));

			}
		}
    }
	public void createconsumable(int layer,String name, int p_agility, int p_life,int force, int resist,int x,int y,String file) {
        availableconsumables.add_consumable(new Consumable(layer,name,p_agility,p_life,force,resist,x,y,file));
	}

	/*
	 *  Called from GameScreen
	 */
	public void update() {
		this.badguys.update();
		
	}
	
	public void onplayermove() {
		//this.badguys.onplayermove(prota);
	}
	
}
