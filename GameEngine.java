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
	public static int TOTAL_X_TILES=200; // TOTAL TILES MUST BE MULTIPLE OF SCREEN TILES!!!!!!!!!
	public static int TOTAL_Y_TILES=300; // TOTAL TILES MUST BE MULTIPLE OF SCREEN TILES!!!!!!!!!
	public static int ON_SCREEN_TILES_X=20; // TOTAL TILES MUST BE MULTIPLE OF SCREEN TILES!!!!!!!!!
	public static int ON_SCREEN_TILES_Y=15; // TOTAL TILES MUST BE MULTIPLE OF SCREEN TILES!!!!!!!!!
	public static int TILE_X_SIZE=40;
	public static int TILE_Y_SIZE=40;
	public static int MAX_WALL_LENGTH=15;
	public static int MAX_LAKE_SIZE=15;
	public static int OPTION_MENU_X_SIZE=500;
	public static int WINDOWWITH=TILE_X_SIZE*ON_SCREEN_TILES_X+OPTION_MENU_X_SIZE;
	public static int WINDOWHEIGHT=TILE_Y_SIZE*ON_SCREEN_TILES_Y;
	public static int FPS=10;
	public static int INVENTORY_SIZE=10;
	public static String APP_NAME="Rogue explorer";
	public static int NUMBER_OF_WALLS=100;
	public static int NUMBER_OF_LAKES=50;
	public static int NUMBER_OF_BLOCKING_OBJECTS=1000;
	
	// variables
	private static Tile[][] tilelayout;
	private static int firstXtile; // defines current section of the map that is shown on screen
	private static int firstYtile; // defines current section of the map that is shown on screen
    
    

    private static Enemy_array badguys;
    private static Object_array availableobjects;
    private static Consumable_array availableconsumables;
    private static Hero prota;
    private static Map mapa;

    
	// START METHOD INITIALIZES ALL CLASSES OF THE GAME
	public  void start () {  
		// first tile position must be multiple of tile_size
		firstXtile=0;
		firstYtile=0;
		
		
		// create hero
        prota=new Hero("ferriman","human.gif");
		
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
	
	// TILE / MAP CLASS WRAPPER
	// gets
	public Map getmap() {
		return mapa;
	}
	public static int getfirstxtile() {
		return GameEngine.firstXtile;
	}
	public static int getfirstytile() {
		return GameEngine.firstYtile;
	}
	
	// sets / updates
	public void setfirstxtile(int value) {
		GameEngine.firstXtile=value;
	}
	public void setfirstytile(int value) {
		GameEngine.firstYtile=value;
	}
	
	
	// HERO CLASS WRAPPER
	public Hero gethero() {
		return prota;
	}
	// hero fight monster
	public static boolean fight(Enemy enemy) {
		boolean heroturn;
		int enemydicevalue;
		int herodicevalue;
		int enemyattackpower;
		int heroattackpower;
		Random randomGenerator = new Random();
		// decide who hits first
		if (enemy.getagility()>prota.getagility()) { heroturn=false; } else { heroturn=true; }
		// begin fight loop
		while (enemy.gethp()>0 && prota.gethp()>0) { // while somebody is alive
			enemydicevalue = randomGenerator.nextInt(5);
			herodicevalue = randomGenerator.nextInt(5);
			enemyattackpower=enemy.getforce()+enemydicevalue;
			heroattackpower=prota.getforce()+herodicevalue;
			if (heroturn==true) {
				// hero attack
				if (heroattackpower-enemy.getresist()>0) { // if do damage
					enemy.updatehp(0-(heroattackpower-enemy.getresist()));
					System.out.println("Hero turn:"+(heroattackpower-enemy.getresist()));
					System.out.println("Enemy HP:"+enemy.gethp());
					heroturn=false;
				} else {
					// if not
					System.out.println("Hero turn: no damage");
					heroturn=false;
				}
			} else {
				// enemy attack
				if (enemyattackpower-prota.getresist()>0) { // if do damage
					prota.updatehp(0-(enemyattackpower-prota.getresist()));
					System.out.println("Enemy turn:"+(enemyattackpower-prota.getresist()));
					System.out.println("prota HP:"+prota.gethp());
					heroturn=true;
				} else {
					// if not
					System.out.println("Enemy turn: no damage");
					heroturn=true;
				}
				
			}
		}
		// who win?
		if (enemy.gethp()<=0) { 
			prota.updateexperience(100);
			System.out.println("YOU WIN!");
			return true;
		} else { 
			System.out.println("YOU LOSE!");
			return false;
		}
	}
	public static void herodies() {
		firstXtile=0;
		firstYtile=0;
		prota.setrelativextile(1);
		prota.setrelativeytile(1);
	}
	
	// hero updates
	public static void heroup() {
		if (GameEngine.firstYtile+prota.getrelativeytile()>0) {
			if (prota.getrelativeytile()==0 && tilelayout[GameEngine.firstXtile+prota.getrelativextile()][GameEngine.firstYtile+prota.getrelativeytile()-1].isbloqued()==false) {		
				if (firstYtile>0) { prota.scrollup(); firstYtile -= GameEngine.ON_SCREEN_TILES_Y; }
			} else {
				if (tilelayout[GameEngine.firstXtile+prota.getrelativextile()][GameEngine.firstYtile+prota.getrelativeytile()-1].isbloqued()==false) {
					prota.up();
				}	
			}
		}
	}
		
	public static void herodown() {
		if (GameEngine.firstYtile+prota.getrelativeytile()<GameEngine.TOTAL_Y_TILES-1) {
			if (prota.getrelativeytile()==GameEngine.ON_SCREEN_TILES_Y-1 && tilelayout[GameEngine.firstXtile+prota.getrelativextile()][1+GameEngine.firstYtile+prota.getrelativeytile()].isbloqued()==false) {		
				if (firstYtile<GameEngine.TOTAL_Y_TILES-GameEngine.ON_SCREEN_TILES_Y) { prota.scrolldown(); firstYtile += GameEngine.ON_SCREEN_TILES_Y; }
			} else {
				if (tilelayout[GameEngine.firstXtile+prota.getrelativextile()][1+GameEngine.firstYtile+prota.getrelativeytile()].isbloqued()==false) {
					prota.down();
				}
			}
		}
	}
		
	public static void heroright() {
		if (GameEngine.firstXtile+prota.getrelativextile()<GameEngine.TOTAL_X_TILES-1) {
			if (prota.getrelativextile()==GameEngine.ON_SCREEN_TILES_X-1 && tilelayout[1+GameEngine.firstXtile+prota.getrelativextile()][GameEngine.firstYtile+prota.getrelativeytile()].isbloqued()==false) {
				if (firstXtile<GameEngine.TOTAL_X_TILES-GameEngine.ON_SCREEN_TILES_X) { prota.scrollrigth(); firstXtile += GameEngine.ON_SCREEN_TILES_X; }
			} else {
				if (tilelayout[1+GameEngine.firstXtile+prota.getrelativextile()][GameEngine.firstYtile+prota.getrelativeytile()].isbloqued()==false) {
					prota.right();
				}
			}
		}
	}
		
	public static void heroleft() {
		if (GameEngine.firstXtile+prota.getrelativextile()>0) {
			if (prota.getrelativextile()==0 && tilelayout[GameEngine.firstXtile+prota.getrelativextile()-1][GameEngine.firstYtile+prota.getrelativeytile()].isbloqued()==false) {
				if (firstXtile>0) { prota.scrollleft(); firstXtile -= GameEngine.ON_SCREEN_TILES_X; }
			} else {
				if (tilelayout[GameEngine.firstXtile+prota.getrelativextile()-1][GameEngine.firstYtile+prota.getrelativeytile()].isbloqued()==false) {
					prota.left();
				}
			}
		}
	}
	
	
	
	// ENEMY / ENEMY_ARRAY CLASS WRAPPER
	public ArrayList<Enemy> getenemies() {
		return badguys.getlist();
	}
	public static Enemy overenemy() {
		 return badguys.overenemy(prota.getrelativextile()+GameEngine.getfirstxtile(),prota.getrelativeytile()+GameEngine.getfirstytile());
	}
	public static void removeenemy(Enemy obj) {
		badguys.remove_enemy(obj);
	}
	public static void createrandomenemy() { // create a random enemy
		Random randomGenerator = new Random();
		// generates random position
		int x = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES);
		int y = randomGenerator.nextInt(GameEngine.TOTAL_Y_TILES);
		int enemytype = randomGenerator.nextInt(3); // random choose of enemy
		if (!tilelayout[x][y].isbloqued()) { // if there is empty space
			if (enemytype==0) {
				badguys.add_enemy(new Enemy("vortex",5,5,5,5,x,y,"vortex.gif"));
			}
			if (enemytype==1) {
				badguys.add_enemy(new Enemy("catharg",6,6,6,6,x,y,"cetharg.gif"));
			}
			if (enemytype==2) {
				badguys.add_enemy(new Enemy("assassin",7,8,7,7,x,y,"assassin.gif"));
			}
		}
	}
	public static void createenemy(String name,int ag,int str, int res, int lf, int x,int y,String file) {
		badguys.add_enemy(new Enemy(name,ag,str,res,lf,x,y,file));
	}
	
	// OBJECT CLASSES WRAPPER
	public ArrayList<Object> getobjects() {
		return availableobjects.getlist();
	}
	public static Object overobject() {
		 return availableobjects.overobject(prota.getrelativextile()+GameEngine.getfirstxtile(),prota.getrelativeytile()+GameEngine.getfirstytile());
	}
	public static void removeobject(Object obj) {
		availableobjects.remove_object(obj);
	}
	public static void createrandomobject() {
		Random randomGenerator = new Random();
		// generates random position
		int x = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES);
		int y = randomGenerator.nextInt(GameEngine.TOTAL_Y_TILES);
		int chances = randomGenerator.nextInt(100);
		int objecttype = randomGenerator.nextInt(11);
		if (!tilelayout[x][y].isbloqued()) { // if there is empty space
			if (objecttype==0) {
				if (chances<90) {
					availableobjects.add_object(new Object("long sword","righthand",10,0,1,x,y,"longSword.gif"));
				}
			}
			if (objecttype==1) {
				if (chances<90) {
					availableobjects.add_object(new Object("dagger","righthand",6,0,1,x,y,"dagger.gif"));
				}
			}
			if (objecttype==2) {
				if (chances<90) {
					availableobjects.add_object(new Object("boots","foot",0,6,1,x,y,"boots.gif"));
				}
			}
			if (objecttype==3) {
				if (chances<90) {
					availableobjects.add_object(new Object("heavy armor","body",0,15,1,x,y,"heavyKevlarArmor.gif"));
				}
			}
			if (objecttype==4) {
				if (chances<90) {
					availableobjects.add_object(new Object("helm","head",0,4,1,x,y,"helm.gif"));
				}
			}
			if (objecttype==5) {
				if (chances<90) {
					availableobjects.add_object(new Object("mace","lefthand",7,0,1,x,y,"mace.gif"));
				}
			}
			if (objecttype==6) {
				if (chances<90) {
					availableobjects.add_object(new Object("riot shield","lefthand",0,9,1,x,y,"riotShield.gif"));
				}
			}
			if (objecttype==7) {
				if (chances<90) {
					availableobjects.add_object(new Object("armor","body",0,11,1,x,y,"reflecArmor.gif"));
				}
			}
			if (objecttype==8) {
				if (chances<90) {
					availableobjects.add_object(new Object("shield","lefthand",0,7,1,x,y,"shield.gif"));
				}
			}
			if (objecttype==9) {
				if (chances<90) {
					availableobjects.add_object(new Object("skull cap","head",0,5,1,x,y,"skullcap.gif"));
				}
			}
			if (objecttype==10) {
				if (chances<90) {
					availableobjects.add_object(new Object("great shield","lefthand",0,12,1,x,y,"greatShield.gif"));
				}
			}
		}
	}
	public static void createobject(String name,String position,int attack, int defense, int durability,int x,int y,String file) {
		availableobjects.add_object(new Object(name,position,attack,defense,durability,x,y,file));
	}
	
	// CONSUMABLE CLASSES WRAPPER
	public ArrayList<Consumable> getconsumables() {
		return availableconsumables.getlist();
	}
	public static Consumable overconsumable() {
		 return availableconsumables.overconsumable(prota.getrelativextile()+GameEngine.getfirstxtile(),prota.getrelativeytile()+GameEngine.getfirstytile());
	}
	public static void removeconsumable(Consumable c) {
		availableconsumables.remove_consumable(c);
	}
	public static void addconsumable(Consumable c) {
		availableconsumables.add_consumable(c);
	}
	public static void createrandomconsumable() {
		Random randomGenerator = new Random();
		// generates random position
		int x = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES);
		int y = randomGenerator.nextInt(GameEngine.TOTAL_Y_TILES);
		int potiontype = randomGenerator.nextInt(3);
        if (potiontype==0) {
        	availableconsumables.add_consumable(new Consumable("Blue potion",1,1,x,y,"potionblue.gif"));
        }
        if (potiontype==1) {
        	availableconsumables.add_consumable(new Consumable("Red potion",1,1,x,y,"potionred.gif"));
        }
        if (potiontype==2) {
        	availableconsumables.add_consumable(new Consumable("Yellow potion",1,1,x,y,"potionyellow.gif"));
        }
    }
	public static void createconsumable(String name, int p_agility, int p_life,int x,int y,String file) {
        availableconsumables.add_consumable(new Consumable(name,p_agility,p_life,x,y,file));
	}
	
}
