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
import java.util.ListIterator;
import java.util.Random;
import java.lang.Thread;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;






public class GameplayScreen extends InputAdapter implements Screen  {
	public static GameplayScreen instance = null;
	
	private SpriteBatch batch;
	//private Texture texture;
	private BitmapFont genericfont;
	private BitmapFont messagefont;

	private Map[] maplayers = new Map[WrapperEngine.NUMBER_OF_MAP_LAYERS]; // new dynamic layer system
	private Map activemap;
	private Hero prota;
	private WrapperEngine game;
    private Layout layout; 
    
    // inventory status and modes
    int object_inv_mode=0;
    int object_drop_mode=0;
    int eye_mode=0;
    int debug_mode=0;
    int consumable_inv_mode=0;
    
   
    
    private int realXcoord;
    private int realYcoord;
    
    Enemy actualenemy; // enemy that i'm over
    Buddy actualbuddy;
    Object actualobject; 
    Consumable actualconsumable;
    String interactionoutput="";
    ArrayList<Enemy> badguys;
    ArrayList<Buddy> goodguys;
    ArrayList<Object> availableobjects;
    ArrayList<Consumable> availableconsumables;
    
    Object_inventory objinv;
    Consumable_inventory consinv;
    
    private PopupInfoText screentext;
    
    // fight status
    int just_interact=0;
    
    
    
    public GameplayScreen() {
    	
    	if(instance == null) instance = this;
    }
	
    @Override
    public void hide() {
    	
    }
    
	@Override
	public void show() {		
		init();
		
        placeboss();
		
	}

	/**
	 * 
	 */
	protected void placeboss() {
		// create final boss
        boolean boss_created=false;
		while (boss_created==false) {
			Random randomGenerator = new Random();
			int x = randomGenerator.nextInt(WrapperEngine.TOTAL_X_TILES);
			int y = randomGenerator.nextInt(WrapperEngine.TOTAL_Y_TILES);
			if (maplayers[0].gettiles()[x][y].isbloqued()) { // if there is empty space
				game.createenemy(0,"megaboss", 43, 46, 51, 310, x, y,"orc.png");
				boss_created=true;
			}
				
		}
	}

	/**
	 * 
	 */
	protected void init() {
		Gdx.input.setInputProcessor(this);
		batch = new SpriteBatch();
		// fonts setup
		FileHandle fontFile = Gdx.files.internal("diabloheavy.ttf");
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
		//genericfont = new BitmapFont();
		//messagefont = new BitmapFont();
		messagefont = generator.generateFont(30); // px
    	messagefont.setColor(Color.YELLOW);
    	genericfont = generator.generateFont(14); // px
    	genericfont.setColor(Color.WHITE);
		// create tile layout
        game = new WrapperEngine();
        maplayers[0]=game.getmaplayer(0);
        maplayers[1]=game.getmaplayer(1);
        maplayers[2]=game.getmaplayer(2);
        layout=new Layout();
        prota = game.gethero();
        badguys= game.getenemies();
        goodguys= game.getbuddies();
        availableobjects=game.getobjects();
        availableconsumables=game.getconsumables();
        
        // empty enemy object that hold enemy. Same as object and consumable.
        actualbuddy= new Buddy();
        actualenemy= new Enemy();
        actualobject= new Object();
        actualconsumable= new Consumable();
        
        objinv= new Object_inventory();
        consinv= new Consumable_inventory();
		
        // create testing buddy
        game.createbuddy(0,"Priest", 0,0,"buddy1.png","Hi, my friend. I hope you enjoy your trip!");
        
		// create a message info screen 
		screentext=new PopupInfoText(100,(WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-400,"text_background.png",1000,300);
		screentext.settextoffset(50, 50);
	}

	@Override
	public void render(float delta) {
		
		
		update();
		
		frameratecontrol();
		// draw
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	    batch.begin();
	    
	    drawinterface();
	 	
	 	// draw equipment
	 	drawequipment();
        
        // overenemy description
        drawdescriptions();
        
        

        //layercontrol();
        activemap=maplayers[game.getlayer()];
        
     	// dwaw background
        drawbackground();
        
        // draw static blocked tiles 
        drawtiles();
        
        // draw enemies
        drawenemies();
        
        // draw buddies
        drawbuddies();

        // draw consumables
        drawconsumables();
        
        // draw objects
        drawobjects();
        
        // draw hero
        batch.draw(prota.getsprite(), prota.getrelativextile()*WrapperEngine.TILE_X_SIZE, prota.getrelativeytile()*WrapperEngine.TILE_Y_SIZE);
	
        
        
        
        // draw fight result
        if (just_interact==1) {
        	screentext.drawScreen(batch, messagefont,interactionoutput,1.0f);	
        }
        

        // draw debug mode info
        if (debug_mode==1) {
        	drawdebug();
        }
        
		// draw version build
        genericfont.draw(batch, "Development build 69", 10, (WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-620);

        
        // draw object inventory
        drawinventory();
        
        batch.end();
	}

	/**
	 * 
	 */
	protected void drawinterface() {
		// draw character menu background
	 	batch.draw(layout.getmenubackground(),WrapperEngine.TILE_X_SIZE*WrapperEngine.ON_SCREEN_TILES_X,0);
	 	
	 	// draw action menu background
	 	batch.draw(layout.getactionmenu(),0,WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y);
	 	 	
	 	// draw hero information
	 	//genericfont.draw(batch,"Hi "+prota.getname()+"!", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-30);
	 	genericfont.draw(batch,"Experience: "+prota.getexperience(), (WrapperEngine.TILE_X_SIZE*WrapperEngine.ON_SCREEN_TILES_X)+25, (WrapperEngine.WINDOWHEIGHT)-20);
	 	genericfont.draw(batch,"Life Points: "+prota.gethp(), (WrapperEngine.TILE_X_SIZE*WrapperEngine.ON_SCREEN_TILES_X)+25, (WrapperEngine.WINDOWHEIGHT)-40);
	 	genericfont.draw(batch,"Resistance: "+prota.getresist(), (WrapperEngine.TILE_X_SIZE*WrapperEngine.ON_SCREEN_TILES_X)+25,(WrapperEngine.WINDOWHEIGHT)-60);
	 	genericfont.draw(batch,"Agility: "+prota.getagility(), (WrapperEngine.TILE_X_SIZE*WrapperEngine.ON_SCREEN_TILES_X)+25,(WrapperEngine.WINDOWHEIGHT)-80);
	 	genericfont.draw(batch,"Force: "+prota.getforce(), (WrapperEngine.TILE_X_SIZE*WrapperEngine.ON_SCREEN_TILES_X)+25,(WrapperEngine.WINDOWHEIGHT)-100);
	}

	/**
	 * 
	 */
	protected void drawinventory() {
		for (int i=0;i<WrapperEngine.INVENTORY_SIZE;i++) {
        	if (objinv.get_object(i)!=null) {
        		//genericfont.draw(batch,"Obj slot "+i+":"+objinv.get_object(i).getname(), 1000, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-(360+(i*20)));
                batch.draw(objinv.get_object(i).getsprite(), 1216,640-(i*64));

        	} else {
        		//genericfont.draw(batch,"Obj slot "+i+": available", 1000, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-(360+(i*20)));

        	}
        }
        
        // draw consumable inventory
        
        for (int i=0;i<WrapperEngine.INVENTORY_SIZE;i++) {
        	if (consinv.get_consumable(i)!=null) {
        		//genericfont.draw(batch,"Cons slot "+i+":"+consinv.get_consumable(i).getname(), 1000, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-(70+(i*20)));
        		batch.draw(consinv.get_consumable(i).getsprite(), 1152,640-(i*64));
        	} else {
        		//genericfont.draw(batch,"Cons slot "+i+": available", 1000, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-(70+(i*20)));

        	}
        }
	}
	
	protected void drawbackground() {
        for (int xpos=0;xpos<WrapperEngine.ON_SCREEN_TILES_X;xpos++) {
        	for (int ypos=0;ypos<WrapperEngine.ON_SCREEN_TILES_Y;ypos++) {
        				if (activemap.isdungeon()) {
        					batch.draw(activemap.getfreedungeontile(),xpos*WrapperEngine.TILE_X_SIZE,ypos*WrapperEngine.TILE_Y_SIZE);
        				} else {
        					batch.draw(activemap.getfreetile(),xpos*WrapperEngine.TILE_X_SIZE,ypos*WrapperEngine.TILE_Y_SIZE);
        				}
        	}
        }
		
	}

	/**
	 * 
	 */
	protected void drawdebug() {
		genericfont.draw(batch, "Screen Mouse X:"+Gdx.input.getX()+" Projected Mouse X: "+realXcoord, 20, (WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-20);
		genericfont.draw(batch, "Screen Mouse Y:"+Gdx.input.getY()+" Projected Mouse Y: "+realYcoord, 20, (WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-40);
		genericfont.draw(batch, "I'm at Screen X: "+ maplayers[game.getlayer()].getfirstxtile()+" Y: "+maplayers[game.getlayer()].getfirstytile(), 20, (WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-60);
		genericfont.draw(batch, "I'm at tile X: "+ (maplayers[game.getlayer()].getfirstxtile()+prota.getrelativextile())+" Y: "+(maplayers[game.getlayer()].getfirstytile()+prota.getrelativeytile()), 20, (WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-160);

		genericfont.draw(batch, "Real screen size X:"+Gdx.graphics.getWidth(), 20, (WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-80);
		genericfont.draw(batch, "Real screen size Y:"+Gdx.graphics.getHeight(), 20, (WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-100);
		
		genericfont.draw(batch, "Eye mode:"+eye_mode, 20, (WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-120);
		genericfont.draw(batch, "Drop mode:"+object_drop_mode, 20, (WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-140);
		genericfont.draw(batch, "Layer:"+game.getlayer(), 20, (WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-180);
		genericfont.draw(batch, "Framerate:"+(int)(1/Gdx.graphics.getDeltaTime())+" FPS", 20, (WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-200);

		int i=0;
		for (AccessToLayer atl: maplayers[game.getlayer()].getAPs()) {
			i++;
			genericfont.draw(batch, "Door on layer "+game.getlayer()+" to layer "+atl.getIncommingLayer()+" at "+atl.getIncommingX()+","+atl.getIncommingY(), 440, (WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-(20*i));			
		}
	}

	/**
	 * 
	 */
	protected void drawconsumables() {
		ListIterator<Consumable> consumableiterator = availableconsumables.listIterator();
        while (consumableiterator.hasNext()) {
        	//System.out.println("entra");
        	Consumable consumable=consumableiterator.next();
        	//System.out.println(bguy.getabsolutex());
        	if (consumable.consumableonscreen(maplayers[game.getlayer()].getfirstxtile(), maplayers[game.getlayer()].getfirstytile())==true) {
        		// draw consumable image		
    			if (consumable.getlayer()==game.getlayer()) {
    				batch.draw(consumable.getsprite(),(consumable.getabsolutex()-maplayers[game.getlayer()].getfirstxtile())*WrapperEngine.TILE_X_SIZE,(consumable.getabsolutey()-maplayers[game.getlayer()].getfirstytile())*WrapperEngine.TILE_Y_SIZE);       		
    			}
    		}
        }
	}

	/**
	 * 
	 */
	protected void drawobjects() {
		ListIterator<Object> objiterator = availableobjects.listIterator();
        while (objiterator.hasNext()) {
        	//System.out.println("entra");
        	Object obj=objiterator.next();
        	//System.out.println(bguy.getabsolutex());
        	if (obj.objectonscreen(maplayers[game.getlayer()].getfirstxtile(), maplayers[game.getlayer()].getfirstytile())==true) {
        		// draw object image
        		if (obj.getlayer()==game.getlayer()) { // if it is the correct layer
        			batch.draw(obj.getsprite(),(obj.getabsolutex()-maplayers[game.getlayer()].getfirstxtile())*WrapperEngine.TILE_X_SIZE,(obj.getabsolutey()-maplayers[game.getlayer()].getfirstytile())*WrapperEngine.TILE_Y_SIZE);       		
        		}
        	}
        }
	}

	/**
	 * 
	 */
	protected void drawenemies() {
		ListIterator<Enemy> bgiterator = badguys.listIterator();
        while (bgiterator.hasNext()) {
        	//System.out.println("entra");
        	Enemy bguy=bgiterator.next();
        	//System.out.println(bguy.getabsolutex());
        	if (bguy.enemyonscreen(maplayers[game.getlayer()].getfirstxtile(), maplayers[game.getlayer()].getfirstytile())==true) {
        		// draw enemy image if the layer is correct
        		if (bguy.getlayer()==game.getlayer()) {
        			Sprite enemysprite=bguy.getsprite();
        			enemysprite.setPosition(getrelativeenemyxtileposition(bguy), getrelativeenemyytileposition(bguy));
        			//batch.draw(bguy.getsprite(),getrelativextileposition(bguy),getrelativeytileposition(bguy));
        			enemysprite.draw(batch);
        		}
        	}
        }
	}
	
	protected void drawbuddies() {
		ListIterator<Buddy> bgiterator = goodguys.listIterator();
        while (bgiterator.hasNext()) {
        	//System.out.println("entra");
        	Buddy bguy=bgiterator.next();
        	//System.out.println(bguy.getabsolutex());
        	if (bguy.buddyonscreen(maplayers[game.getlayer()].getfirstxtile(), maplayers[game.getlayer()].getfirstytile())==true) {
        		// draw buddie image if the layer is correct
        		if (bguy.getlayer()==game.getlayer()) {
        			Sprite buddysprite=bguy.getsprite();
        			buddysprite.setPosition(getrelativebuddyxtileposition(bguy), getrelativebuddyytileposition(bguy));
        			//batch.draw(bguy.getsprite(),getrelativextileposition(bguy),getrelativeytileposition(bguy));
        			buddysprite.draw(batch);
        		}
        	}
        }
	}

	/**
	 * @param bguy
	 * @return
	 */
	public int getrelativeenemyytileposition(Enemy bguy) {
		return (bguy.getabsolutey()-maplayers[game.getlayer()].getfirstytile())*WrapperEngine.TILE_Y_SIZE;
	}
	
	public int getrelativebuddyytileposition(Buddy gguy) {
		return (gguy.getabsolutey()-maplayers[game.getlayer()].getfirstytile())*WrapperEngine.TILE_Y_SIZE;
	}

	/**
	 * @param bguy
	 * @return
	 */
	public int getrelativeenemyxtileposition(Enemy bguy) {
		return (bguy.getabsolutex()-maplayers[game.getlayer()].getfirstxtile())*WrapperEngine.TILE_X_SIZE;
	}
	
	public int getrelativebuddyxtileposition(Buddy gguy) {
		return (gguy.getabsolutex()-maplayers[game.getlayer()].getfirstxtile())*WrapperEngine.TILE_X_SIZE;
	}
	
	public int getabsolutextile(Hero hero) {
		return hero.getrelativextile() + (maplayers[game.getlayer()] == null ? 0 : maplayers[game.getlayer()].getfirstxtile());
	}
	
	public int getabsoluteytile(Hero hero) {
		return hero.getrelativeytile() + (maplayers[game.getlayer()] == null ? 0 :  maplayers[game.getlayer()].getfirstytile());
	}
	
	// layer control
	public void APCheck() {
		System.out.println("Hero Absolute x:"+(prota.getrelativextile()+maplayers[game.getlayer()].getfirstxtile()));
 		System.out.println("Hero Absolute y:"+(prota.getrelativeytile()+maplayers[game.getlayer()].getfirstytile()));
		for (AccessToLayer atl: maplayers[game.getlayer()].getAPs()) {
			//System.out.println("Door on layer"+game.getlayer()+":"+atl.getOutcommingX()+","+atl.getOutcommingY());
			if ((prota.getrelativextile()+maplayers[game.getlayer()].getfirstxtile())==atl.getIncommingX() && (prota.getrelativeytile()+maplayers[game.getlayer()].getfirstytile())==atl.getIncommingY()) {
	     		game.changelayer(atl,prota.getrelativextile(),prota.getrelativeytile()); //changelayer
	     		System.out.println("Over the DOOR!");
	     		//prota.setabsolutextile(atl.getIncommingX());
			}
		}
		
	}
	
	protected void drawtiles() {
		int relativex=0;
        for (int xpos=maplayers[game.getlayer()].getfirstxtile();xpos<(maplayers[game.getlayer()].getfirstxtile()+WrapperEngine.ON_SCREEN_TILES_X);xpos++) {
        	int relativey=0;
        	for (int ypos=maplayers[game.getlayer()].getfirstytile();ypos<(maplayers[game.getlayer()].getfirstytile()+WrapperEngine.ON_SCREEN_TILES_Y);ypos++) {
        			if (activemap.gettiles()[xpos][ypos].getshowimage()) {
        				batch.draw(activemap.gettiles()[xpos][ypos].gettileimage(),relativex*WrapperEngine.TILE_X_SIZE,relativey*WrapperEngine.TILE_Y_SIZE);
        			}
        			relativey++;
        	}
        	relativex++;
        }
	}

	protected void drawdescriptions() {
		if (actualenemy!=null) {
        	if (actualenemy.getname()!=null) {
        		genericfont.draw(batch,"Enemy: "+actualenemy.getname(), (WrapperEngine.TILE_X_SIZE*WrapperEngine.ON_SCREEN_TILES_X)+25, (WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-400);
        		genericfont.draw(batch,"Life Points: "+actualenemy.gethp(), (WrapperEngine.TILE_X_SIZE*WrapperEngine.ON_SCREEN_TILES_X)+25,(WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-420);
        		genericfont.draw(batch,"Resistance: "+actualenemy.getresist(), (WrapperEngine.TILE_X_SIZE*WrapperEngine.ON_SCREEN_TILES_X)+25,(WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-440);
        		genericfont.draw(batch,"Agility: "+actualenemy.getagility(), (WrapperEngine.TILE_X_SIZE*WrapperEngine.ON_SCREEN_TILES_X)+25,(WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-460);
        		genericfont.draw(batch,"Force: "+actualenemy.getforce(), (WrapperEngine.TILE_X_SIZE*WrapperEngine.ON_SCREEN_TILES_X)+25,(WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-480);
        	}
        }
        
        // overconsumable description
        
        if (actualconsumable!=null) {
        	if (actualconsumable.getname()!=null) {
        		genericfont.draw(batch,"Consumable: "+actualconsumable.getname(), (WrapperEngine.TILE_X_SIZE*WrapperEngine.ON_SCREEN_TILES_X)+25, (WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-400);
        		genericfont.draw(batch,"+ Life Points: "+actualconsumable.getpoweruplife(), (WrapperEngine.TILE_X_SIZE*WrapperEngine.ON_SCREEN_TILES_X)+25,(WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-420);
        		genericfont.draw(batch,"+ Agility Points: "+actualconsumable.getpowerupagility(), (WrapperEngine.TILE_X_SIZE*WrapperEngine.ON_SCREEN_TILES_X)+25,(WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-440);
        	}
        }
        
        // overobject description
        
        if (actualobject!=null) {
        	if (actualobject.getname()!=null) {
        		genericfont.draw(batch,"Object: "+actualobject.getname(), (WrapperEngine.TILE_X_SIZE*WrapperEngine.ON_SCREEN_TILES_X)+25, (WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-400);
        		genericfont.draw(batch,"+ defense: "+actualobject.getdefense(), (WrapperEngine.TILE_X_SIZE*WrapperEngine.ON_SCREEN_TILES_X)+25,(WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-420);
        		genericfont.draw(batch,"+ offense: "+actualobject.getattack(), (WrapperEngine.TILE_X_SIZE*WrapperEngine.ON_SCREEN_TILES_X)+25,(WrapperEngine.TILE_Y_SIZE*WrapperEngine.ON_SCREEN_TILES_Y)-440);
        	}
        }
	}

	/**
	 * 
	 */
	protected void drawequipment() {
		if (prota.gethead().getname()!=null) {
	 		batch.draw(prota.gethead().getsprite(),970,545);
	 		//genericfont.draw(batch,prota.gethead().getname(), 930,619);
	 		genericfont.draw(batch,"A:+"+prota.gethead().getattack()+" D:+"+prota.gethead().getdefense()+" Dur:"+prota.gethead().getdurability(), 927,535);
        } else {
        	//genericfont.draw(batch,"Head: nothing", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-170);

        }
        if (prota.getlefthand().getname()!=null) {
        	batch.draw(prota.getlefthand().getsprite(),1050,448);
        	//genericfont.draw(batch,prota.getlefthand().getname(), 1018,532 );
        	genericfont.draw(batch,"A:+"+prota.getlefthand().getattack()+" D:+"+prota.getlefthand().getdefense()+" Dur:"+prota.getlefthand().getdurability(), 1010,428 );
        } else {
        	//genericfont.draw(batch,"Left hand: nothing", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-190);

        }
        if (prota.getrighthand().getname()!=null) {
        	batch.draw(prota.getrighthand().getsprite(),872,448);
        	//genericfont.draw(batch,prota.getrighthand().getname(), 842,532);
        	genericfont.draw(batch,"A:+"+prota.getrighthand().getattack()+" D:+"+prota.getrighthand().getdefense()+" Dur:"+prota.getrighthand().getdurability(), 849,428);
        } else {
        	//genericfont.draw(batch,"Right hand: nothing", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-210);

        }
        if (prota.getbody().getname()!=null) {
        	batch.draw(prota.getbody().getsprite(),971,448);
        	//genericfont.draw(batch,prota.getbody().getname(),931,532);
        	genericfont.draw(batch,"A:+"+prota.getbody().getattack()+" D:+"+prota.getbody().getdefense()+" Dur:"+prota.getbody().getdurability(),931,442);
        } else {
        	//genericfont.draw(batch,"Body: nothing", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-230);

        }
        if (prota.getfoot().getname()!=null) {
        	batch.draw(prota.getfoot().getsprite(),971,350);
        	//genericfont.draw(batch,prota.getfoot().getname(),931,425);
        	genericfont.draw(batch,"A:+"+prota.getfoot().getattack()+" D:+"+prota.getfoot().getdefense()+" Dur:"+prota.getfoot().getdurability(),931,330);
        } else {
        	//genericfont.draw(batch,"Foot: nothing", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-250);
        }
	}
	void frameratecontrol() {
		//  delay for each frame  -   time it took for one frame 
		long time = (1000 / WrapperEngine.FPS); 
        
        if (time > 0) 
        { 
                try 
                { 
                        Thread.sleep(time); 
                } 
                catch(Exception e){} 
        } 
	}
	
	void update()
    { 
		// random elements generator
    	Random randomGenerator = new Random();
    	int number=randomGenerator.nextInt(6); // 50% chances to create something
    	if (number==0) { // create enemy
    		game.createrandomenemy();
    	}
    	if (number==1) { // create consumable
    		game.createrandomconsumable();
    		
    	}
    	if (number==2) { // create object
    		game.createrandomobject();
    	}
    	// get relative mouse coord instead of real ones
    	realXcoord=(int)((float)Gdx.input.getX()*(float)((float)WrapperEngine.WINDOWWIDTH/(float)Gdx.graphics.getWidth()));
		realYcoord=(int)((float)Gdx.input.getY()*(float)((float)WrapperEngine.WINDOWHEIGHT/(float)Gdx.graphics.getHeight()))*-1+(WrapperEngine.WINDOWHEIGHT);
    	if (WrapperEngine.ANDROID_MENU_BAR_ENABLE) { // I don't like this hardcoded way to do things
    		realYcoord=realYcoord+WrapperEngine.ANDROID_MENU_BAR_SIZE;
    	}
    	// mouse events control
    	handlemouseinput(); 
    	// end mouse events control
    	
    	// key events control
    	handlekeyboardinput();
        
        	
    }
	
	// Keyboard event handler
	
	@Override
	public boolean keyUp (int keycode) {
		if (keycode==Keys.P) { // debug mode
			if (debug_mode==0) { debug_mode=1; } else { 
				debug_mode=0; 
			}
		}
		if (keycode==Keys.L) {
			look();
		}
		if (keycode==Keys.H) {
			fight();
		}
		if (keycode==Keys.T) {
			talk();
		}
		if (keycode==Keys.G) {
			take();
		}
		if (keycode==Keys.D) {
			drop();
		}
		if (keycode==Keys.Z) {
			dispose();
		}
		if (keycode==Keys.O) {
			// ENABLE OBJECT INVENTORY MODE
        	object_inv_mode=1;
    		consumable_inv_mode=0;
    		object_drop_mode=0;
    		just_interact=0;
		}
		if (keycode==Keys.C) {
			// ENABLE CONSUMABLE INVENTORY MODE
        	object_inv_mode=0;
    		consumable_inv_mode=1;
    		object_drop_mode=0;
    		just_interact=0;
		}
		
		// OBJECT INVENTORY ACTIONS
        if (keycode==Keys.NUM_1 && object_inv_mode==1) {
        	getobject(objinv.get_object(1),1);
        }
        if (keycode==Keys.NUM_2 && object_inv_mode==1) {
        	getobject(objinv.get_object(2),2);
        }
        if (keycode==Keys.NUM_3 && object_inv_mode==1) {
        	getobject(objinv.get_object(3),3);
        }
        if (keycode==Keys.NUM_4 && object_inv_mode==1) {
        	getobject(objinv.get_object(4),4);
        }
        if (keycode==Keys.NUM_5 && object_inv_mode==1) {
        	getobject(objinv.get_object(5),5);
        }
        if (keycode==Keys.NUM_6 && object_inv_mode==1) {
        	getobject(objinv.get_object(6),6);
        }
        if (keycode==Keys.NUM_7 && object_inv_mode==1) {
        	getobject(objinv.get_object(7),7);
        }
        if (keycode==Keys.NUM_8 && object_inv_mode==1) {
        	getobject(objinv.get_object(8),8);
        }
        if (keycode==Keys.NUM_9 && object_inv_mode==1) {
        	getobject(objinv.get_object(9),9);
        }
        if (keycode==Keys.NUM_0 && object_inv_mode==1) {
        	getobject(objinv.get_object(0),0);
        }
        
        // OBJECT DROP INVENTORY ACTIONS
        if (keycode==Keys.NUM_1 && object_drop_mode==1) {
        	objinv.delete_object(1);
        }
        if (keycode==Keys.NUM_2 && object_drop_mode==1) {
        	objinv.delete_object(2);
        }
        if (keycode==Keys.NUM_3 && object_drop_mode==1) {
        	objinv.delete_object(3);
        }
        if (keycode==Keys.NUM_4 && object_drop_mode==1) {
        	objinv.delete_object(4);
        }
        if (keycode==Keys.NUM_5 && object_drop_mode==1) {
        	objinv.delete_object(5);
        }
        if (keycode==Keys.NUM_6 && object_drop_mode==1) {
        	objinv.delete_object(6);
        }
        if (keycode==Keys.NUM_7 && object_drop_mode==1) {
        	objinv.delete_object(7);
        }
        if (keycode==Keys.NUM_8 && object_drop_mode==1) {
        	objinv.delete_object(8);
        }
        if (keycode==Keys.NUM_9 && object_drop_mode==1) {
        	objinv.delete_object(9);
        }
        if (keycode==Keys.NUM_0 && object_drop_mode==1) {
        	objinv.delete_object(0);
        }
        // CONSUMABLE INVENTORY ACTIONS
        if (keycode==Keys.NUM_1 && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(1));
        	consinv.delete_consumable(1);
        }
        if (keycode==Keys.NUM_2 && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(2));
        	consinv.delete_consumable(2);
        }
        if (keycode==Keys.NUM_3 && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(3));
        	consinv.delete_consumable(3);
        }
        if (keycode==Keys.NUM_4 && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(4));
        	consinv.delete_consumable(4);
        }
        
        if (keycode==Keys.NUM_5 && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(5));
        	consinv.delete_consumable(5);
        }
        if (keycode==Keys.NUM_6 && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(6));
        	consinv.delete_consumable(6);
        }
        if (keycode==Keys.NUM_7 && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(7));
        	consinv.delete_consumable(7);
        }
        if (keycode==Keys.NUM_8 && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(8));
        	consinv.delete_consumable(8);
        }
        if (keycode==Keys.NUM_9 && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(9));
        	consinv.delete_consumable(9);
        }
        if (keycode==Keys.NUM_0 && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(0));
        	consinv.delete_consumable(0);
        }
		return false;
	}

	protected void handlekeyboardinput() {
		if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)) { goright(); } 
        if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT)) { goleft(); }
        if (Gdx.input.isKeyPressed(Keys.DPAD_UP)) { goup(); } 
        if (Gdx.input.isKeyPressed(Keys.DPAD_DOWN)) { godown();}
	}

	/**
	 * 
	 */
	protected void handlemouseinput() {
		if (Gdx.input.isTouched()) {
    		// EXIT BUTTON!
    		if (realXcoord>512 && realXcoord<576 && realYcoord>640 && realYcoord<704) {
    			dispose();
    		}
    		// HIT BUTTON!
    		if (realXcoord>0 && realXcoord<64 && realYcoord>640 && realYcoord<704) {
    			fight();
    		}
    		// directions
    		// LEFT BUTTON!
    		if (realXcoord>576 && realXcoord<640 && realYcoord>640 && realYcoord<704) {
    			goleft();
    		}
    		// RIGHT BUTTON!
    		if (realXcoord>768 && realXcoord<832 && realYcoord>640 && realYcoord<704) {
    			goright();
    		}
    		// UP BUTTON!
    		if (realXcoord>704 && realXcoord<768 && realYcoord>640 && realYcoord<704) {
    			goup();
    		}
    		// DOWN BUTTON!
    		if (realXcoord>640 && realXcoord<704 && realYcoord>640 && realYcoord<704) {
    			godown();
    		}
    		// TAKE BUTTON!
    		if (realXcoord>64 && realXcoord<128 && realYcoord>640 && realYcoord<704) {
    			take();
    		}
    		// DROP BUTTON!
    		if (realXcoord>128 && realXcoord<192 && realYcoord>640 &&  realYcoord<704) {
    			drop();
    		}
    		// LOOK BUTTON! 
    		if (realXcoord>192 && realXcoord<256 && realYcoord>640 && realYcoord<704) {
    			look();
    		}
    		// TALK BUTTON! 
    		if (realXcoord>256 && realXcoord<320 && realYcoord>640 && realYcoord<704) {
    			talk();
    		}
    		// CONSUMABLE INVENTORY ACTIONS
    		for (int i=0;i<WrapperEngine.INVENTORY_SIZE;i++) {
    			if (realXcoord>1152 && realXcoord<1216 && realYcoord>640-(64*i) && realYcoord<704-(64*i) && eye_mode==0) {
    				getconsumable(consinv.get_consumable(i));
    				consinv.delete_consumable(i);
    			}
            }
    		// OBJECT INVENTORY ACTIONS
    		for (int i=0;i<WrapperEngine.INVENTORY_SIZE;i++) {
    			if (realXcoord>1216 && realXcoord<1280 && realYcoord>640-(64*i) && realYcoord<704-(64*i) && object_drop_mode==0 && eye_mode==0) {
    				getobject(objinv.get_object(i),i);
    			}
            }
    		// OBJECT INVENTORY DROP
    		for (int i=0;i<WrapperEngine.INVENTORY_SIZE;i++) {
    			if (realXcoord>1216 && realXcoord<1280 && realYcoord>640-(64*i) && realYcoord<704-(64*i) && object_drop_mode==1  && eye_mode==0) {
    				objinv.delete_object(i);
    			}
            }
    		// EYEMODE OBJECT INVENTORY
    		for (int i=0;i<WrapperEngine.INVENTORY_SIZE;i++) {
    			if (realXcoord>1216 && realXcoord<1280 && realYcoord>640-(64*i) && realYcoord<704-(64*i) && eye_mode==1) {
    				actualobject=objinv.get_object(i);
    			}
            }
    		// EYEMODE CONSUMABLE INVENTORY
    		for (int i=0;i<WrapperEngine.INVENTORY_SIZE;i++) {
    			if (realXcoord>1152 && realXcoord<1216 && realYcoord>640-(64*i) && realYcoord<704-(64*i) && eye_mode==1) {
    				actualconsumable=consinv.get_consumable(i);
    			}
            }	
    		
    	}
	}    
    void getobject(Object obj,int pos) {
    	if (obj!=null) {
			// if object exists
			if (obj.getposition()=="head") {
				if (prota.gethead().getname()==null) {
					prota.sethead(obj);
					objinv.delete_object(pos);
				} else {
					objinv.set_object(pos,prota.gethead());
					prota.sethead(obj);
				}
			}
			if (obj.getposition()=="righthand") {
				if (prota.getrighthand().getname()==null) {
					prota.setrighthand(obj);
					objinv.delete_object(pos);
				} else {
					objinv.set_object(pos,prota.getrighthand());
					prota.setrighthand(obj);
				}
			}
			if (obj.getposition()=="lefthand") {
				if (prota.getlefthand().getname()==null) {
					prota.setlefthand(obj);
					objinv.delete_object(pos);
				} else {
					objinv.set_object(pos,prota.getlefthand());
					prota.setlefthand(obj);
				}	
			}
			if (obj.getposition()=="body") {
				if (prota.getbody().getname()==null) {
					prota.setbody(obj);
					objinv.delete_object(pos);
				} else {
					objinv.set_object(pos,prota.getbody());
					prota.setbody(obj);
				}
			}
			if (obj.getposition()=="foot") {
				if (prota.getfoot().getname()==null) {
					prota.setfoot(obj);
					objinv.delete_object(pos);
				} else {
					objinv.set_object(pos,prota.getfoot());
					prota.setfoot(obj);
				}
			}
    	}
    }
    
    void getconsumable(Consumable obj) {
    	if (obj!=null) {
			// if consumable exists
			prota.updateagility(obj.getpowerupagility());
			prota.updatehp(obj.getpoweruplife());
    	}
    }
    void fight() {
    	object_inv_mode=0;
		consumable_inv_mode=0;
		object_drop_mode=0;
    	//boolean resultoffight=false;
		String resultoffight;
    	actualenemy=game.overenemy(); // get the enemy (if exist)
		if (actualenemy.getname()!=null) {
			if (!BackgroundMusic.playingfight) {
	    		BackgroundMusic.stopall();
	    		BackgroundMusic.startfight();
	    		BackgroundMusic.playingfight=true;
	    	}
			//resultoffight=prota.fight(actualenemy);
			resultoffight=prota.hit(actualenemy);
			//System.out.println("FIGHT!");
			// if hero wins
			if (resultoffight=="ENEMYDEAD") {
				// if you win
				prota.updateexperience(100);
				//System.out.println("YOU WIN!");
				if (actualenemy.getname()=="megaboss") {
						interactionoutput="You get the amulet, you win the game!!";
				} else {
					interactionoutput="Great! You win the battle!!";
					BackgroundMusic.stopall();
		    		if (activemap.isdungeon()) {
		    			BackgroundMusic.startdungeon();
		    		} else {
		    			BackgroundMusic.startoutside();
		    		}
		    		BackgroundMusic.playingfight=false;
				}
				
				game.removeenemy(actualenemy);
			} 
			if (resultoffight=="HERODEAD") {
				BackgroundMusic.stopall();
	    		BackgroundMusic.startoutside();
	    		BackgroundMusic.playingfight=false;
				game.herodies();
				interactionoutput="You lose the battle, you are in the graveyard!";
			}
			if (resultoffight!="ENEMYDEAD" && resultoffight!="HERODEAD") {
				interactionoutput=resultoffight;
			}
		just_interact=1;
		}
    }
    void goup() {
    	object_inv_mode=0;
    	object_drop_mode=0;
		consumable_inv_mode=0;
		eye_mode=0;
		just_interact=0;
    	actualenemy=null;
    	actualconsumable=null;
    	actualobject=null;
    	APCheck();
    	game.heroup();
    }
    void godown() {
    	object_inv_mode=0;
    	object_drop_mode=0;
		consumable_inv_mode=0;
		eye_mode=0;
		just_interact=0;
    	actualenemy=null;
    	actualconsumable=null;
    	actualobject=null;
    	APCheck();
    	game.herodown();
    }
    void goleft() {
    	object_inv_mode=0;
    	object_drop_mode=0;
		consumable_inv_mode=0;
		eye_mode=0;
		just_interact=0;
    	actualenemy=null;
    	actualconsumable=null;
    	actualobject=null;
    	APCheck();
    	game.heroleft();
    }
    void goright() {
    	eye_mode=0;
    	just_interact=0;
		object_inv_mode=0;
		object_drop_mode=0;
		consumable_inv_mode=0;
		actualenemy=null;
		actualconsumable=null;
		actualobject=null;
		APCheck();
		game.heroright();
    }
    void look() {
    	eye_mode=1;
    	object_inv_mode=0;
    	object_drop_mode=0;
		consumable_inv_mode=0;
		just_interact=0;
    	actualenemy=game.overenemy(); // get the enemy (if exist)
    	actualconsumable=game.overconsumable(); // get the consumable (if exist)
    	actualobject=game.overobject(); // get the object (if exist)
    }
    void talk() {
    	eye_mode=0;
    	object_inv_mode=0;
		consumable_inv_mode=0;
		object_drop_mode=0;
		actualenemy=game.overenemy(); // get the enemy (if exist)
		actualbuddy=game.overbuddy(); // get the buddy (if exist)
		if (actualenemy.getname()!=null) {
			interactionoutput=actualenemy.talk();
			just_interact=1;
		}
		if (actualbuddy.getname()!=null) {
			interactionoutput=actualbuddy.talk();
			just_interact=1;
		}
    }
    void take() {
    	eye_mode=0;
    	object_inv_mode=0;
		consumable_inv_mode=0;
		object_drop_mode=0;
		just_interact=0;
    	// get consumable into inventory
		actualconsumable=game.overconsumable(); // get the consumable (if exist)
		if (actualconsumable.getname()!=null) {
			// if consumable exists
			if (consinv.getfreeslot()!=-1) {
				consinv.set_consumable(consinv.getfreeslot(), actualconsumable);	
				game.removeconsumable(actualconsumable);
			}
		}
		// get object into inventory
		actualobject=game.overobject(); // get the consumable (if exist)
		if (actualobject.getname()!=null) {
			if (objinv.getfreeslot()!=-1) {
				objinv.set_object(objinv.getfreeslot(), actualobject);
				game.removeobject(actualobject);
			}
		}
    }
    
    void drop() {
		// ENABLE CONSUMABLE INVENTORY MODE
    	eye_mode=0;
    	object_inv_mode=0;
		consumable_inv_mode=0;
		object_drop_mode=1;
		just_interact=0;
	}
    // Original class methods
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	@Override
	public void dispose() {
		if(batch != null) batch.dispose();
		Gdx.app.exit();
	}

	public Map getmaplayer(int value) {
		return maplayers[value];
	}
	
}
