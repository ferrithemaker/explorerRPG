/*
    Copyright (C) 2013  Ferran Fàbregas (ferri.fc@gmail.com)

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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

// Map class is a wrapper class for all other basic classes
// every Map has 1000x1000 tile matrix
public class GameEngine {
	// constants
	public static int TOTAL_X_TILES=1000;
	public static int TOTAL_Y_TILES=1000;
	public static int ON_SCREEN_TILES_X=20;
	public static int ON_SCREEN_TILES_Y=15;
	public static int TILE_X_SIZE=40;
	public static int TILE_Y_SIZE=40;
	public static int MAX_WALL_LENGTH=70;
	//public static int MAX_LAKE_WIDTH=70;
	public static int OPTION_MENU_X_SIZE=400;
	public static int WINDOWWITH=TILE_X_SIZE*ON_SCREEN_TILES_X+OPTION_MENU_X_SIZE;
	public static int WINDOWHEIGHT=TILE_Y_SIZE*ON_SCREEN_TILES_Y;
	public static int FPS=10;
	
	// variables
	private static Tile[][] tilelayout;
	private static int firstXtile;
	private static int firstYtile;
    private static BufferedImage freetile;
    private static BufferedImage blockedtile;
    private static BufferedImage vortex_img;
    private static BufferedImage cetharg_img;
    private static BufferedImage assassin_img;
    private static BufferedImage longsword_img;
    private static BufferedImage potion_img;
    private static BufferedImage water_img;
    private static BufferedImage rocks_img;
    private static BufferedImage boulder_img;
    private static BufferedImage bones_img;
    private static BufferedImage fire_img;
    private static BufferedImage cross_img;
    private static BufferedImage boots_img;
    
    private static BufferedImage dagger_img;
    private static BufferedImage rat_img;
    private static BufferedImage greatshield_img;
    private static BufferedImage heavyarmor_img;
    private static BufferedImage helm_img;
    private static BufferedImage mace_img;
    private static BufferedImage maniac_img;
    private static BufferedImage medusa_img;
    private static BufferedImage orc_img;
    private static BufferedImage midarmor_img;
    private static BufferedImage shield_img;
    private static BufferedImage skullcap_img;
    private static BufferedImage riotshield_img;
    private static BufferedImage warlock_img;


    private static Enemy_array badguys;
    private static Object_array availableobjects;
    private static Consumable_array availableconsumables;
    private static Hero prota;

    
	// START METHOD INITIALIZES ALL CLASSES OF THE GAME
	public  void start () {  
		// first tile position must be multiple of tile_size
		firstXtile=0;
		firstYtile=0;
		// load all images
		try {
			
		    
			// enemies
			vortex_img=ImageIO.read(new File("vortex.gif"));
			cetharg_img=ImageIO.read(new File("cetharg.gif"));
	        assassin_img=ImageIO.read(new File("assassin.gif"));
	        warlock_img=ImageIO.read(new File("warlock.gif"));
	        rat_img=ImageIO.read(new File("giantRat.gif"));
	        maniac_img=ImageIO.read(new File("maniac.gif"));
	        medusa_img=ImageIO.read(new File("medusa.gif"));
	        orc_img=ImageIO.read(new File("orc.gif"));
	        
	        // tiles
	        blockedtile = ImageIO.read(new File("roomWall33.gif"));
			freetile = ImageIO.read(new File("herba.jpg"));
			water_img = ImageIO.read(new File("water.gif"));
			rocks_img = ImageIO.read(new File("rocks.jpg"));
			bones_img = ImageIO.read(new File("bones.jpg"));
			boulder_img= ImageIO.read(new File("boulder.jpg"));
			fire_img= ImageIO.read(new File("fire.jpg"));
			cross_img= ImageIO.read(new File("cross.jpg"));

			// consumables
			potion_img=ImageIO.read(new File("potion.gif"));
			
			// objects
			longsword_img=ImageIO.read(new File("longSword.gif"));
			boots_img=ImageIO.read(new File("boots.gif"));
			dagger_img=ImageIO.read(new File("dagger.gif"));
			greatshield_img=ImageIO.read(new File("greatShield.gif"));
			heavyarmor_img=ImageIO.read(new File("heavyKevlarArmor.gif"));
			helm_img=ImageIO.read(new File("helm.gif"));
			mace_img=ImageIO.read(new File("mace.gif"));
			midarmor_img=ImageIO.read(new File("riotShield.gif"));
			shield_img=ImageIO.read(new File("shield.gif"));
			skullcap_img=ImageIO.read(new File("skullcap.gif"));
			riotshield_img=ImageIO.read(new File("reflecArmor.gif"));
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// create hero
        prota=new Hero();
		// create tile layout
        tilelayout = new Tile[GameEngine.TOTAL_X_TILES][GameEngine.TOTAL_Y_TILES];
        createrandommap();
       
        
        
        // create initial enemy array
        badguys= new Enemy_array();
		
        //System.out.println(badguys.size());
        
        // create initial object array
        
        availableobjects=new Object_array();
        
        // create initial consumable array
        
        availableconsumables=new Consumable_array();

        
	}
	
	// TILE CLASS WRAPPER (ALMOST ALL TILE METHODS ARE IMPLEMENTED AND HANDLED BY MAP CLASS, INCLUDING TILE SPRITES)
	public Tile[][] getmap() {
		return GameEngine.tilelayout;
	}
	public static int getfirstxtile() {
		return GameEngine.firstXtile;
	}
	public static int getfirstytile() {
		return GameEngine.firstYtile;
	}
	public void setfirstxtile(int value) {
		GameEngine.firstXtile=value;
	}
	public void setfirstytile(int value) {
		GameEngine.firstYtile=value;
	}
	public static void createrandommap() {
		// fill with freetiles
		 for (int xpos=0;xpos<1000;xpos++) {
	        	for (int ypos=0;ypos<1000;ypos++) {
	        		tilelayout[xpos][ypos]= new Tile(false,freetile);
	        	}
	        }
		// create 2000x2 random walls
		for (int num=0; num<2000;num++) {
			createrandomvwall();
			createrandomhwall();
		}
		// create 2000 individual elements
		for (int num=0; num<2000;num++) {
			createblockingelement();
		}
		
		// create 200 lakes
		for (int num=0; num<200;num++) {
			createrandomlake();
		}
		createcementery();
	}
	public static void createrandomhwall() {
		Random randomGenerator = new Random();
		int lenght = randomGenerator.nextInt(GameEngine.MAX_WALL_LENGTH);
		int start = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES-GameEngine.MAX_WALL_LENGTH);
		int height = randomGenerator.nextInt(GameEngine.TOTAL_Y_TILES);
		for (int xpos=start;xpos<start+lenght;xpos++) {
        	tilelayout[xpos][height].block();
        	tilelayout[xpos][height].updatetileimage(blockedtile);	
        }
	}
	public static void createrandomvwall() {
		Random randomGenerator = new Random();
		int lenght = randomGenerator.nextInt(GameEngine.MAX_WALL_LENGTH);
		int start = randomGenerator.nextInt(GameEngine.TOTAL_Y_TILES-GameEngine.MAX_WALL_LENGTH);
		int width = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES);
		for (int ypos=start;ypos<start+lenght;ypos++) {
        	tilelayout[width][ypos].block();
        	tilelayout[width][ypos].updatetileimage(blockedtile);	
        }
		
	}
	public static void createrandomlake() {
		Random randomGenerator = new Random();
		int lenght = randomGenerator.nextInt(GameEngine.MAX_WALL_LENGTH);
		int start_x = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES-GameEngine.MAX_WALL_LENGTH);
		int start_y = randomGenerator.nextInt(GameEngine.TOTAL_Y_TILES-GameEngine.MAX_WALL_LENGTH);
		int width = randomGenerator.nextInt(GameEngine.MAX_WALL_LENGTH);
		for (int xpos=start_x;xpos<start_x+lenght;xpos++) {
			for (int ypos=start_y;ypos<start_y+width;ypos++) {
				tilelayout[xpos][ypos].block();
				tilelayout[xpos][ypos].updatetileimage(water_img);
			}
        }
		
	}
	public static void createblockingelement() {
		Random randomGenerator = new Random();
		int x = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES);
		int y = randomGenerator.nextInt(GameEngine.TOTAL_Y_TILES);
		int element= randomGenerator.nextInt(4);
		tilelayout[x][y].block();
		if (element==0) {
			tilelayout[x][y].updatetileimage(rocks_img);
		}
		if (element==1) {
			tilelayout[x][y].updatetileimage(boulder_img);
		}
		if (element==2) {
			tilelayout[x][y].updatetileimage(bones_img);
		}
		if (element==3) {
			tilelayout[x][y].updatetileimage(fire_img);
		}
	}
	public static void createcementery() {
		for (int x=0;x<11;x=x+2) {
			for (int y=0;y<11;y=y+2) {
				tilelayout[x][y].block();
				tilelayout[x][y].updatetileimage(cross_img);
			}
		}
	}
	
	// HERO CLASS WRAPPER
	public Hero gethero() {
		return GameEngine.prota;
	}
	// hero fight monster
	public static boolean fight(Enemy enemy) {
		// test fighting
		//prota.updateexperience(100);
		boolean heroturn;
		int enemydicevalue;
		int herodicevalue;
		int enemyattackpower;
		int heroattackpower;
		Random randomGenerator = new Random();
		// decide who hits first
		if (enemy.getagility()>prota.getagility()) { heroturn=false; } else { heroturn=true; }
		// begin fight loop
		while (enemy.getlp()>0 && prota.getlp()>0) { // while somebody live
			enemydicevalue = randomGenerator.nextInt(5);
			herodicevalue = randomGenerator.nextInt(5);
			enemyattackpower=enemy.getforce()+enemydicevalue;
			heroattackpower=prota.getforce()+herodicevalue;
			if (heroturn==true) {
				// hero attack
				if (heroattackpower-enemy.getresist()>0) { // if do damage
					enemy.updatelp(0-(heroattackpower-enemy.getresist()));
					System.out.println("Hero turn:"+(heroattackpower-enemy.getresist()));
					System.out.println("Enemy LP:"+enemy.getlp());
					heroturn=false;
				} else {
					// if not
					System.out.println("Hero turn: no damage");
					heroturn=false;
				}
			} else {
				// enemy attack
				if (enemyattackpower-prota.getresist()>0) { // if do damage
					prota.updatelp(0-(enemyattackpower-prota.getresist()));
					System.out.println("Enemy turn:"+(enemyattackpower-prota.getresist()));
					System.out.println("prota LP:"+prota.getlp());
					heroturn=true;
				} else {
					// if not
					System.out.println("Enemy turn: no damage");
					heroturn=true;
				}
				
			}
		}
		// who win?
		if (enemy.getlp()<=0) { 
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
		if (GameEngine.firstYtile+prota.getrelativeytile()<999) {
			if (prota.getrelativeytile()==GameEngine.ON_SCREEN_TILES_Y-1 && tilelayout[GameEngine.firstXtile+prota.getrelativextile()][1+GameEngine.firstYtile+prota.getrelativeytile()].isbloqued()==false) {		
				if (firstYtile<1000-GameEngine.ON_SCREEN_TILES_Y) { prota.scrolldown(); firstYtile += GameEngine.ON_SCREEN_TILES_Y; }
			} else {
				if (tilelayout[GameEngine.firstXtile+prota.getrelativextile()][1+GameEngine.firstYtile+prota.getrelativeytile()].isbloqued()==false) {
					prota.down();
				}
			}
		}
	}
		
	public static void heroright() {
		if (GameEngine.firstXtile+prota.getrelativextile()<999) {
			if (prota.getrelativextile()==GameEngine.ON_SCREEN_TILES_X-1 && tilelayout[1+GameEngine.firstXtile+prota.getrelativextile()][GameEngine.firstYtile+prota.getrelativeytile()].isbloqued()==false) {
				if (firstXtile<1000-GameEngine.ON_SCREEN_TILES_X) { prota.scrollrigth(); firstXtile += GameEngine.ON_SCREEN_TILES_X; }
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
	public static void createenemy() {
		Random randomGenerator = new Random();
		// generates random position
		int x = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES);
		int y = randomGenerator.nextInt(GameEngine.ON_SCREEN_TILES_Y);
		int enemytype = randomGenerator.nextInt(3);
		if (!tilelayout[x][y].isbloqued()) { // if there is empty space
			if (enemytype==0) {
				badguys.add_enemy(new Enemy("vortex",5,5,5,5,x,y,vortex_img));
			}
			if (enemytype==1) {
				badguys.add_enemy(new Enemy("catharg",6,6,6,6,x,y,cetharg_img));
			}
			if (enemytype==2) {
				badguys.add_enemy(new Enemy("assassin",7,8,7,7,x,y,assassin_img));
			}
		}
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
	public static void createobject() {
		Random randomGenerator = new Random();
		// generates random position
		int x = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES);
		int y = randomGenerator.nextInt(GameEngine.ON_SCREEN_TILES_Y);
		int chances = randomGenerator.nextInt(100);
		int objecttype = randomGenerator.nextInt(11);
		if (!tilelayout[x][y].isbloqued()) { // if there is empty space
			if (objecttype==0) {
				if (chances<90) {
					availableobjects.add_object(new Object("long sword","righthand",10,0,1,x,y,longsword_img));
				}
			}
			if (objecttype==1) {
				if (chances<90) {
					availableobjects.add_object(new Object("dagger","righthand",6,0,1,x,y,dagger_img));
				}
			}
			if (objecttype==2) {
				if (chances<90) {
					availableobjects.add_object(new Object("boots","foot",0,6,1,x,y,boots_img));
				}
			}
			if (objecttype==3) {
				if (chances<90) {
					availableobjects.add_object(new Object("heavy armor","body",0,15,1,x,y,heavyarmor_img));
				}
			}
			if (objecttype==4) {
				if (chances<90) {
					availableobjects.add_object(new Object("helm","head",0,4,1,x,y,helm_img));
				}
			}
			if (objecttype==5) {
				if (chances<90) {
					availableobjects.add_object(new Object("mace","lefthand",7,0,1,x,y,mace_img));
				}
			}
			if (objecttype==6) {
				if (chances<90) {
					availableobjects.add_object(new Object("riot shield","lefthand",0,9,1,x,y,riotshield_img));
				}
			}
			if (objecttype==7) {
				if (chances<90) {
					availableobjects.add_object(new Object("armor","body",0,11,1,x,y,midarmor_img));
				}
			}
			if (objecttype==8) {
				if (chances<90) {
					availableobjects.add_object(new Object("shield","lefthand",0,7,1,x,y,shield_img));
				}
			}
			if (objecttype==9) {
				if (chances<90) {
					availableobjects.add_object(new Object("skull cap","head",0,5,1,x,y,skullcap_img));
				}
			}
			if (objecttype==10) {
				if (chances<90) {
					availableobjects.add_object(new Object("great shield","lefthand",0,12,1,x,y,greatshield_img));
				}
			}
		}
       

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
	public static void createconsumable() {
		Random randomGenerator = new Random();
		// generates random position
		int x = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES);
		int y = randomGenerator.nextInt(GameEngine.ON_SCREEN_TILES_Y);
        availableconsumables.add_consumable(new Consumable("potion",1,1,x,y,potion_img));
	}
	
}
