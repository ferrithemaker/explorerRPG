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

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.game.libgdx.roguelikeengine.rooms.Chapel;
import com.game.libgdx.roguelikeengine.rooms.CornerRoom;
import com.game.libgdx.roguelikeengine.rooms.LargeRoom;



// Map class is a wrapper class for all other basic classes

public class WrapperEngine {
	// constants
	public final static int BUILD_NUMBER=95;
	public final static int ON_SCREEN_TILES_X=13;	// TODO:: this should be based on screen size
	public final static int ON_SCREEN_TILES_Y=10;	// TODO:: this should be based on screen size
	public final static int X_SCREENS=30;
	public final static int Y_SCREENS=30;
	public final static int TOTAL_X_TILES=X_SCREENS*ON_SCREEN_TILES_X;	
	public final static int TOTAL_Y_TILES=Y_SCREENS*ON_SCREEN_TILES_Y;	
	public final static int TILE_X_SIZE=64;
	public final static int TILE_Y_SIZE=64;
	public final static int MAX_WALL_LENGTH=12;
	public final static int MAX_LAKE_SIZE=12;
	public final static int OPTION_MENU_X_SIZE=448;	// TODO:: this should be based on screen size
	public final static int ACTIONS_MENU_Y_SIZE=64;	// TODO:: this should be based on screen size
	public final static int WINDOWWIDTH=TILE_X_SIZE*ON_SCREEN_TILES_X+OPTION_MENU_X_SIZE;	// TODO:: this should be based on screen size
	public final static int WINDOWHEIGHT=TILE_Y_SIZE*ON_SCREEN_TILES_Y+ACTIONS_MENU_Y_SIZE;	// TODO:: this should be based on screen size
	public final static int FPS=18;
	public final static int INVENTORY_SIZE=10;
	public final static String APP_NAME="Rogue explorer testing game (libgdx)";
	public final static int NUMBER_OF_WALLS=300;
	public final static int NUMBER_OF_WALLS_DUNGEON=1000;
	public final static int NUMBER_OF_LAKES=300;
	public final static int NUMBER_OF_BLOCKING_OBJECTS=8000;
	public final static int EXPERIENCE_NEXT_LEVEL_LIMIT=1000;
	public final static int NUMBER_OF_ENEMIES_PER_LOOP=20;
	public final static int NUMBER_OF_OBJECTS_PER_LOOP=5;
	public final static int NUMBER_OF_CONSUMABLES_PER_LOOP=5;
	public final static int MAX_ENEMIES=1000;
	public final static int MAX_OBJECTS=1000;
	public final static int MAX_CONSUMABLES=1000;
	public final static boolean STOPSONFIRE = true;
	public final static int KEY_DROP_RATE = 100;	// 0 - 100 chance
	public final static boolean CHESTS_PERSIST = false;
	public final static int CHEST_POTION_MODIFIER = 2; // chests cost a key, so potions from chests should be stronger ?
	
	// viewport resize
    public static final int VIRTUAL_WIDTH = WrapperEngine.WINDOWWIDTH;
    public static final int VIRTUAL_HEIGHT = WrapperEngine.WINDOWHEIGHT;
    public static final float ASPECT_RATIO = (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;
	
	// output OS
	public static String OUTPUT_OS="desktop"; // desktop OR android 
	public static boolean debuging_android_on_windows = false;
	static {
		switch(Gdx.app.getType()) {
		case Android:
			OUTPUT_OS="android";
			break;
		case Applet:
			break;
		case Desktop:
			OUTPUT_OS="desktop";
			break;
		case WebGL:
			break;
		case iOS:
			break;
		default:
			break;
		}
		
		if(debuging_android_on_windows && OUTPUT_OS.equals("desktop")) {
			OUTPUT_OS="android";
		}
	}
	
	// dynamic layers 
	public final static int NUMBER_OF_MAP_LAYERS=3;
	public final static int NUMBER_OF_ACCESSPOINTS=300;
	
	// keyboard delay in milis
	public final static int KEYBOARD_MILLIS_DELAY=50;
	
	// android specific constants
	public final static int ANDROID_MENU_BAR_SIZE=43;
	
	// variables
    private Enemy_array badguys;
    private Buddy_array goodguys;
    private Object_array availableobjects;
    private Consumable_array availableconsumables;
    private Hero prota;
    private Map[] maplayers = new Map[WrapperEngine.NUMBER_OF_MAP_LAYERS]; // new dynamic layer system
    private Map activemap;
    private int layer;
    private int numberOfAP; // number of access points created


    
	// START METHOD INITIALIZES ALL CLASSES OF THE GAME
	public WrapperEngine () {   
		// setup initial layer
        layer=0;
		// create initial empty enemy array
        badguys= new Enemy_array();
        
        // create initial empty buddy array
        goodguys= new Buddy_array();
		
        // create initial empty object array
        availableobjects=new Object_array();
        
        // create initial empty consumable array
        availableconsumables=new Consumable_array();
	}
	
	public void init() {
		// create hero
        prota=new Hero(this, "ferriman","holder_sprite.png");
		
        // create Maps
        maplayers[0]= new Map(0).init();
        maplayers[1]= new Map(1).init();
        maplayers[2]= new Map(2).init();
        

		System.out.println(new Chapel().tryPlace(maplayers[0], 9, 2));
		System.out.println(new CornerRoom().tryPlace(maplayers[0], 46, 2));
		System.out.println(new LargeRoom().tryPlace(maplayers[0], 65, 54));

        maplayers[0].createrandommap();
        maplayers[1].createrandomdungeon();
        maplayers[2].createrandomdungeon();
        maplayers[0].isdungeon(false);
        maplayers[1].isdungeon(true);
        maplayers[2].isdungeon(true);
        
        numberOfAP=0; // current number of AP

        activemap=maplayers[0];
        
        
        //createAllAP
        createAllAccessLayers();
        
        insurevalidplayerposition();
	}

	/**
	 *  Moves the player up one tile at a time until the player is on valid, empty land.
	 */
	protected void insurevalidplayerposition() {
		int x = prota.getrelativextile(activemap);
        int y = prota.getrelativeytile(activemap);
        while(activemap.gettiles()[x][y].isbloqued()) {
        	prota.setrelativeytile(activemap, ++y);
        }
	}
	
	
	public void delay(int timemillis) {
		try {
			Thread.sleep(timemillis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// MAP CLASS WRAPPER
	public Map getmaplayer(int value) {
		return maplayers[value];
	}
	
	public void createAllAccessLayers() {
		int creationok;
		while (numberOfAP!=WrapperEngine.NUMBER_OF_ACCESSPOINTS) {
			creationok=createLayerAccess();
			if (creationok==1) { 
				numberOfAP++; 
				//System.out.println("AP created");
			}
		}
	}
	
	public int createLayerAccess() {
		Random randomGenerator = new Random();
		int inx = randomGenerator.nextInt(WrapperEngine.TOTAL_X_TILES);
		int iny = randomGenerator.nextInt(WrapperEngine.TOTAL_Y_TILES);
		int outx = randomGenerator.nextInt(WrapperEngine.TOTAL_X_TILES);
		int outy = randomGenerator.nextInt(WrapperEngine.TOTAL_Y_TILES);
		int outlayer = randomGenerator.nextInt(WrapperEngine.NUMBER_OF_MAP_LAYERS);
		int inlayer = randomGenerator.nextInt(WrapperEngine.NUMBER_OF_MAP_LAYERS);
		if (!maplayers[inlayer].gettiles()[inx][iny].isbloqued() && maplayers[inlayer].gettiles()[inx][iny].canPCG() &&
				!maplayers[outlayer].gettiles()[outx][outy].isbloqued() && maplayers[outlayer].gettiles()[outx][outy].canPCG() &&
				inlayer!=outlayer) {
			// if constraints are right
			if (maplayers[inlayer].isdungeon()) {
				maplayers[inlayer].createAccessDungeon(inx,iny,outx,outy,outlayer); // first socket
			} else {
				maplayers[inlayer].createAccessOutside(inx,iny,outx,outy,outlayer); // first socket
			}
			if (maplayers[outlayer].isdungeon()) {
				maplayers[outlayer].createAccessDungeon(outx,outy,inx,iny,inlayer); // and the reverse
			} else {
				maplayers[outlayer].createAccessOutside(outx,outy,inx,iny,inlayer); // and the reverse
			}
			System.out.println("AP created on layer "+inlayer+":"+inx+"|"+iny+"|"+outx+"|"+outy+"|"+outlayer);
			return 1; // sockets created
		} else {
			return 0; // socket not created
		}
	}
	
	
	public int getlayer() { // this is the actual layer
		return layer; // this variable has the actual layer
	}
	
	public void changelayer(AccessToLayer atl,int rx,int ry) {
		GameplayScreen.instance.stopPathing();
		
		int numberofXscreens;
		int numberofYscreens;
		numberofXscreens=atl.getOutcommingX()/ON_SCREEN_TILES_X;
		numberofYscreens=atl.getOutcommingY()/ON_SCREEN_TILES_Y;
		this.layer=atl.getIncommingLayer();
		activemap=maplayers[this.layer];
		activemap.setfirstxtile(numberofXscreens*ON_SCREEN_TILES_X);
		activemap.setfirstytile(numberofYscreens*ON_SCREEN_TILES_Y);
		if (activemap.isdungeon()) {
			BackgroundMusic.stopall();
			BackgroundMusic.startdungeon();
		} else {
			BackgroundMusic.stopall();
			BackgroundMusic.startoutside();
		}
		
	}
		
	// HERO CLASS WRAPPER
	public Hero gethero() {
		return prota;
	}
	
	public int heroabsolutex() {
		return prota.getabsolutecolumn(activemap);
	}
	
	public int heroabsolutey() {
		return prota.getabsoluterow(activemap);
	}
	
	public void herodies() {
		maplayers[layer].setfirstxtile(0);
		maplayers[layer].setfirstytile(0);
		prota.setrelativextile(activemap, 1);
		prota.setrelativeytile(activemap, 1);
		prota.updatehp(50);
	}
	
	// hero updates
	public boolean heroup() {
		Tile tile = activemap.getup(prota.gettile(activemap));
		
		if(tile == null) {
			return false; // edge of map
		}
		
		if (!tile.isbloqued()) {
			prota.down(activemap);
			prota.onStep();
			return true;
		} else {
			GameplayScreen.instance.alert("The way is blocked by " + tile.getblocker().getdescription());
		}
		
		return false;
	}
		
	public boolean herodown() {
		Tile tile = activemap.getdown(prota.gettile(activemap));
		
		if(tile == null) {
			return false; // edge of map
		}

		if (!tile.isbloqued()) {
			prota.up(activemap);
			prota.onStep();
			return true;
		} else {
			GameplayScreen.instance.alert("The way is blocked by " + tile.getblocker().getdescription());
		}
		
		return false;
	}
		
	public boolean heroright() {
		Tile tile = activemap.getright(prota.gettile(activemap));
		
		if(tile == null) {
			return false; // edge of map
		}
		
		if (!tile.isbloqued()) {
			prota.right(activemap);
			prota.onStep();
			return true;
		} else {
			GameplayScreen.instance.alert("The way is blocked by " + tile.getblocker().getdescription());
		}
		
		return false;
	}
		
	public boolean heroleft() {
		Tile tile = activemap.getleft(prota.gettile(activemap));
		
		if(tile == null) {
			return false; // edge of map
		}
		
		if (!tile.isbloqued()) {
			prota.left(activemap);
			prota.onStep();
			return true;
		} else {
			GameplayScreen.instance.alert("The way is blocked by " + tile.getblocker().getdescription());
		}
		
		return false;
	}
	
	
	// ENEMY / ENEMY_ARRAY CLASS WRAPPER
	public ArrayList<Enemy> getenemies() {
		return badguys.getlist();
	}
	public Enemy overenemy() {
		 return badguys.overenemy(prota.getabsolutecolumn(activemap), prota.getabsoluterow(activemap),layer);
	}
	public Enemy nexttoenemy() {
		return badguys.nextotoenemy(prota.getabsolutecolumn(activemap), prota.getabsoluterow(activemap),layer);
	}
	public void removeenemy(Enemy obj) {
		badguys.remove_enemy(obj);
	}
	public void activateenemies(int absx,int absy) {
		ArrayList<Enemy> enemylist=badguys.getlist();
		for (Enemy enemy: enemylist ) {
			if (enemy.enemyonscreen(absx, absy,layer)) { enemy.activate(); }
		}
	}
	public void moveenemies() {
		// absolute position of hero
		int heroabsx=prota.getabsolutecolumn(activemap);
		int heroabsy=prota.getabsoluterow(activemap);
		int enemyabsx;
		int enemyabsy;
		boolean moved;
		ArrayList<Enemy> enemylist=badguys.getlist();
		for (Enemy enemy: enemylist ) {
			moved=false;
			
			if(enemy.getlayer() != this.layer) {
				continue;
			}
			
			enemyabsx=enemy.getabsolutex();
			enemyabsy=enemy.getabsolutey();
			// go right
			if (heroabsx>enemyabsx && enemyabsx<TOTAL_X_TILES-1 && moved==false && (enemyabsx+1!=heroabsx || enemyabsy!=heroabsy)) { // boundary control
				if (activemap.gettiles()[enemyabsx+1][enemyabsy].isbloqued()==false) {
					if (enemy.isactive()) { 
						// unblock last current position
						activemap.gettiles()[enemyabsx][enemyabsy].unblock();
						// block current position
						activemap.gettiles()[enemyabsx+1][enemyabsy].block(enemy);
						// move
						enemy.setabsolutex(enemyabsx+1); 
						moved=true; }
				}
			}
			// go left
			if (heroabsx<enemyabsx && enemyabsx>0 && moved==false && (enemyabsx-1!=heroabsx || enemyabsy!=heroabsy)) { // boundary control
				if (activemap.gettiles()[enemyabsx-1][enemyabsy].isbloqued()==false) {
					if (enemy.isactive()) { 
						// unblock last current position
						activemap.gettiles()[enemyabsx][enemyabsy].unblock();
						// block current position
						activemap.gettiles()[enemyabsx-1][enemyabsy].block(enemy);
						// move
						enemy.setabsolutex(enemyabsx-1); 
						moved=true; 
						}
				}
			}
			// go up
			if (heroabsy>enemyabsy && enemyabsy<TOTAL_Y_TILES-1 && moved==false && (enemyabsx!=heroabsx || enemyabsy+1!=heroabsy)) { // boundary control
				if (activemap.gettiles()[enemyabsx][enemyabsy+1].isbloqued()==false) {
					if (enemy.isactive()) { 
						// unblock last current position
						activemap.gettiles()[enemyabsx][enemyabsy].unblock();
						// block current position
						activemap.gettiles()[enemyabsx][enemyabsy+1].block(enemy);
						// move
						enemy.setabsolutey(enemyabsy+1); 
						moved=true;
						}
				}
			}
			// go down
			if (heroabsy<enemyabsy && enemyabsy>0 && moved==false && (enemyabsx!=heroabsx || enemyabsy-1!=heroabsy)) { // boundary control
				if (activemap.gettiles()[enemyabsx][enemyabsy-1].isbloqued()==false) {
					if (enemy.isactive()) { 
						// unblock last current position
						activemap.gettiles()[enemyabsx][enemyabsy].unblock();
						// block current position
						activemap.gettiles()[enemyabsx][enemyabsy-1].block(enemy);
						// move
						enemy.setabsolutey(enemyabsy-1);
						moved=true; 
						}
				}
			}
		}
	}
	public int numberofenemiesonscreen(int absx,int absy) {
		int x=0;
		ArrayList<Enemy> enemylist=badguys.getlist();
		for (Enemy enemy: enemylist ) {
			if (enemy.enemyonscreen(absx, absy,layer)) { x++; }
		}
		return x;
	}
	public void createrandomenemy() { // create a random enemy
		int i;
		if (badguys.getlist().size()<MAX_ENEMIES) {
		for (i=0;i<NUMBER_OF_ENEMIES_PER_LOOP;i++) {
			Random randomGenerator = new Random();
			// generates random position
			int x = randomGenerator.nextInt(WrapperEngine.TOTAL_X_TILES);
			int y = randomGenerator.nextInt(WrapperEngine.TOTAL_Y_TILES);
			int randomlayer=randomGenerator.nextInt(WrapperEngine.NUMBER_OF_MAP_LAYERS);
			int enemytype = randomGenerator.nextInt(6); // random enemy choose
			
			Enemy enemy = null;
			if (!maplayers[randomlayer].gettiles()[x][y].isbloqued()) { // if there is empty space
				if (enemytype==0) {
					badguys.add_enemy((enemy = new Enemy(randomlayer,"vortex",2,5,3,20,x,y,"vortex2.png")));
				}
				if (enemytype==1) {
					badguys.add_enemy((enemy = new Enemy(randomlayer,"catharg",3,6,4,40,x,y,"cetharg.png")));
				}
				if (enemytype==2) {
					badguys.add_enemy((enemy = new Enemy(randomlayer,"assassin",3,8,1,30,x,y,"assassin.png")));
				}
				if (enemytype==3) {
					badguys.add_enemy((enemy = new Enemy(randomlayer,"giant rat",1,8,8,40,x,y,"giantrat2.png")));
				}
				if (enemytype==4) {
					badguys.add_enemy((enemy = new Enemy(randomlayer,"medusa",5,4,5,30,x,y,"medusa.png")));
				}
				if (enemytype==5) {
					badguys.add_enemy((enemy = new Enemy(randomlayer,"warlock",8,5,5,25,x,y,"warlock.png")));
				}
			}
			
			// block the enemy tile
			if(enemy != null) maplayers[randomlayer].blocktile(x, y, enemy);
		}
		}
	}
	public void createenemy(int layer,String name,int ag,int str, int res, int lf, int x,int y,String file) {
		Enemy enemy = null;
		badguys.add_enemy((enemy = new Enemy(layer,name,ag,str,res,lf,x,y,file)));
		// block tile
		maplayers[layer].blocktile(x, y, enemy);
	}
	
	// BUDDY CLASS WRAPPER
	
	public ArrayList<Buddy> getbuddies() {
		return goodguys.getlist();
	}
	public Buddy overbuddy() {
		 return goodguys.overbuddy(prota.getabsolutecolumn(activemap), prota.getabsoluterow(activemap));
	}
	public void removebuddy(Buddy obj) {
		goodguys.remove_buddy(obj);
	}
	public Buddy nexttobuddy() {
		return goodguys.nextotobuddy(prota.getabsolutecolumn(activemap), prota.getabsoluterow(activemap));
	}
	public void createrandombuddy() { // create a random enemy
		int i;
		for (i=0;i<NUMBER_OF_ENEMIES_PER_LOOP;i++) {
			Random randomGenerator = new Random();
			// generates random position
			int x = randomGenerator.nextInt(WrapperEngine.TOTAL_X_TILES);
			int y = randomGenerator.nextInt(WrapperEngine.TOTAL_Y_TILES);
			int randomlayer=randomGenerator.nextInt(WrapperEngine.NUMBER_OF_MAP_LAYERS);
			//int buddytype = randomGenerator.nextInt(6); // random enemy choose
			if (!maplayers[randomlayer].gettiles()[x][y].isbloqued()) { // if there is empty space
				// random buddy creation
			}
		}
	}
	public Buddy createbuddy(int layer,String name, int x,int y,String file,String speech) {
		Buddy buddy = null;
		goodguys.add_buddy((buddy = new Buddy(layer,name,x,y,file,speech)));
		// block tile
		maplayers[layer].blocktile(x, y, buddy);
		return buddy;
	}
	
	
	// OBJECT CLASSES WRAPPER
	public ArrayList<Object> getobjects() {
		return availableobjects.getlist();
	}
	public Object overobject() {
		 return availableobjects.overobject(prota.getabsolutecolumn(activemap), prota.getabsoluterow(activemap));
	}
	public void removeobject(Object obj) {
		availableobjects.remove_object(obj);
	}
	public boolean hasobject(Object obj) {
		for(Object current : availableobjects.getlist()) {
			if(current.equals(obj)) {
				return true;
			}
		}
		
		return false;
	}
	public void addobject(Object obj, int layer, int column, int row) {
		obj.setabsolutex(column);
		obj.setabsolutey(row);
		obj.setlayer(layer);
		availableobjects.add_object(obj);
	}
	public void createkeyobject(int lay, int xpos, int ypos) {
		availableobjects.add_object(new Object(lay,"key","none",0,0,0,xpos,ypos,"chests/key.png"));
	}
	
	/**
	 * 
	 * @param randpos is really random? (boolean)
	 * @param lay if not random, define layer
	 * @param xpos if not random, define x
	 * @param ypos if not random, define y
	 * @param loops if not random, define number of elements per loop
	 */
	public void createrandomobject(boolean randpos,int lay,int xpos,int ypos,int loops) {	// should return something to indicate something was generated?
		int i;
		int x=xpos;
		int y=ypos;
		int randomlayer=lay;
		Random randomGenerator = new Random();
		if (availableobjects.getlist().size()<MAX_OBJECTS || !randpos) {
		for (i=0;i<loops;i++) {
			// generates random position if needed
			if (randpos) {
				x = randomGenerator.nextInt(WrapperEngine.TOTAL_X_TILES);
				y = randomGenerator.nextInt(WrapperEngine.TOTAL_Y_TILES);
				randomlayer=randomGenerator.nextInt(WrapperEngine.NUMBER_OF_MAP_LAYERS);
			}
			int chances = randomGenerator.nextInt(100);
			int objecttype = randomGenerator.nextInt(11);
			if (!maplayers[randomlayer].gettiles()[x][y].isbloqued()) { // if there is empty space
				if (objecttype==0) {
					if (chances<90) {
						availableobjects.add_object(new Object(randomlayer,"long sword","righthand",10,0,10,x,y,"longSword.png"));
					}
				}
				if (objecttype==1) {
					if (chances<90) {
						availableobjects.add_object(new Object(randomlayer,"dagger","righthand",3,0,7,x,y,"dagger.png"));	
					}
				}
				if (objecttype==2) {
					if (chances<90) {
						availableobjects.add_object(new Object(randomlayer,"boots","foot",0,6,4,x,y,"boots.png"));
					}
				}
				if (objecttype==3) {
					if (chances<90) {
						availableobjects.add_object(new Object(randomlayer,"heavy armor","body",0,15,10,x,y,"heavyarmor.png"));
					}
				}
				if (objecttype==4) {
					if (chances<90) {
						availableobjects.add_object(new Object(randomlayer,"helm","head",0,4,6,x,y,"helm.png"));
					}
				}
				if (objecttype==5) {
					if (chances<90) {
						availableobjects.add_object(new Object(randomlayer,"mace","lefthand",7,0,8,x,y,"mace.png"));
					}
				}
				if (objecttype==6) {
					if (chances<90) {
						availableobjects.add_object(new Object(randomlayer,"riot shield","lefthand",0,9,12,x,y,"riotShield.png"));
					}
				}
				if (objecttype==7) {
					if (chances<90) {
						availableobjects.add_object(new Object(randomlayer,"armor","body",0,11,7,x,y,"reflecArmor.png"));
					}
				}
				if (objecttype==8) {
					if (chances<90) {
						availableobjects.add_object(new Object(randomlayer,"shield","lefthand",0,7,6,x,y,"shield.png"));
					}
				}
				if (objecttype==9) {
					if (chances<90) {
						availableobjects.add_object(new Object(randomlayer,"skull cap","head",0,5,5,x,y,"skullcap.png"));
					}
				}
				if (objecttype==10) {
					if (chances<90) {
						availableobjects.add_object(new Object(randomlayer,"great shield","lefthand",0,12,11,x,y,"greatShield.png"));
					}
				}
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
		 return availableconsumables.overconsumable(prota.getabsolutecolumn(activemap), prota.getabsoluterow(activemap));
	}
	public void removeconsumable(Consumable c) {
		availableconsumables.remove_consumable(c);
	}
	
	public void addconsumable(Consumable c, int layer, int column, int row) {
		c.setlayer(layer);
		c.setabsolutex(column);
		c.setabsolutey(row);
		availableconsumables.add_consumable(c);
	}
	public void addconsumable(Consumable c) {
		this.addconsumable(c, c.getlayer(), c.getabsolutex(), c.getabsolutey());
	}
	/**
	 * 
	 * @param randpos is really random? (boolean)
	 * @param lay if not random, define layer
	 * @param xpos if not random, define x
	 * @param ypos if not random, define y
	 * @param loops if not random, define number of elements per loop
	 */
	public void createrandomconsumable(boolean randpos,int lay, int xpos, int ypos, int loops) {
		int i;
		int x=xpos;
		int y=ypos;
		int randomlayer=lay;
		if (availableconsumables.getlist().size()<MAX_CONSUMABLES || !randpos) {
		for (i=0;i<loops;i++) {
			Random randomGenerator = new Random();
			// generates random position
			if (randpos) {
				x = randomGenerator.nextInt(WrapperEngine.TOTAL_X_TILES);
				y = randomGenerator.nextInt(WrapperEngine.TOTAL_Y_TILES);
				randomlayer=randomGenerator.nextInt(WrapperEngine.NUMBER_OF_MAP_LAYERS);
			}
			
			int potiontype = randomGenerator.nextInt(3);
			if (!maplayers[randomlayer].gettiles()[x][y].isbloqued()) { // if there is empty space
				if (potiontype==0) {
					availableconsumables.add_consumable(new Consumable(randomlayer,"Blue potion",1,1,0,2,x,y,"potionblue.png"));	
				}
				if (potiontype==1) {
					availableconsumables.add_consumable(new Consumable(randomlayer,"Red potion",0,1,1,1,x,y,"potionred.png"));
				}
				if (potiontype==2) {
					availableconsumables.add_consumable(new Consumable(randomlayer,"Yellow potion",2,1,0,0,x,y,"potionyellow.png"));		
				}
			}
		}
		}
    }
	public void createconsumable(int layer,String name, int p_agility, int p_life,int force, int resist,int x,int y,String file) {
        availableconsumables.add_consumable(new Consumable(layer,name,p_agility,p_life,force,resist,x,y,file));
	}

	public Map getactivemap() {
		return activemap;
	}

	public Buddy createchest(int layer, int i, int j) {
		Buddy buddy = null;
		goodguys.add_buddy((buddy = new Chest(layer, i, j)));
		// block tile
		maplayers[layer].blocktile(i, j, buddy);
		return buddy;
	}
	
	public Buddy createDoor(int layer, int i, int j) {
		Buddy buddy = null;
		goodguys.add_buddy((buddy = new Door(layer, i, j)));
		maplayers[layer].blocktile(i, j, buddy);
		return buddy;
	}

	public boolean hasconsumable(Consumable cons) {
		for(Consumable current : this.availableconsumables.getlist()) {
			if(current.equals(cons)) {
				return true;
			}
		}
		
		return false;
	}
}
