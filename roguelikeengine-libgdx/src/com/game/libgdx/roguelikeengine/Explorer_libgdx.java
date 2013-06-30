package com.game.libgdx.roguelikeengine;



import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;






public class Explorer_libgdx implements ApplicationListener {
	private SpriteBatch batch;
	private Texture texture;
	private BitmapFont genericfont;
	private BitmapFont messagefont;

	private Map mapa;
	private Hero prota;
	private GameEngine game;
    private Layout layout; 
    private Tile[][] tilelayout;
    // inventory status
    int object_inv_mode=0;
    int object_drop_mode=0;
    int consumable_inv_mode=0;
    
    private int realXcoord;
    private int realYcoord;
    
    Enemy actualenemy; // enemy that i'm over
    Object actualobject; 
    Consumable actualconsumable;
    String fightstate="";
    ArrayList<Enemy> badguys;
    ArrayList<Object> availableobjects;
    ArrayList<Consumable> availableconsumables;
    
    Object_inventory objinv;
    Consumable_inventory consinv;
    
    // fight status
    int just_fight=0;
	
	@Override
	public void create() {		
		batch = new SpriteBatch();
		genericfont = new BitmapFont();
		messagefont = new BitmapFont();
        // create tile layout
        game = new GameEngine();
        mapa=game.getmap();
        layout=new Layout();
        prota = game.gethero();
        tilelayout = mapa.gettiles();
        badguys= game.getenemies();
        availableobjects=game.getobjects();
        availableconsumables=game.getconsumables();
        
        // empty enemy object that hold enemy. Same as object and consumable.
        actualenemy= new Enemy();
        actualobject= new Object();
        actualconsumable= new Consumable();
        
        objinv= new Object_inventory();
        consinv= new Consumable_inventory();
		
		messagefont.setColor(Color.YELLOW);
		messagefont.setScale(2f);
        //camera = new OrthographicCamera(1, h/w);
		//batch = new SpriteBatch();
		
		//texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		//texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		//TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
		
		//sprite = new Sprite(region);
		//sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		//sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		//sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
        
        // create final boss
        boolean boss_created=false;
		while (boss_created==false) {
			Random randomGenerator = new Random();
			int x = randomGenerator.nextInt(GameEngine.TOTAL_X_TILES);
			int y = randomGenerator.nextInt(GameEngine.TOTAL_Y_TILES);
			if (!tilelayout[x][y].isbloqued()) { // if there is empty space
				game.createenemy("megaboss", 43, 46, 51, 310, x, y,"orc.png");
				boss_created=true;
			}
				
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render() {
		
		// update method
		update();
		frameratecontrol();
		// draw
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	    batch.begin();
	    
	    // draw character menu background
	 	batch.draw(layout.getmenubackground(),GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X,0);
	 	
	 	// draw action menu background
	 	batch.draw(layout.getactionmenu(),0,GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y);
	 	 	
	 	// draw hero information
	 	genericfont.draw(batch,"Hi "+prota.getname()+"!", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-30);
	 	genericfont.draw(batch,"Experience: "+prota.getexperience(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-50);
	 	genericfont.draw(batch,"Life Points: "+prota.gethp(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-70);
	 	genericfont.draw(batch,"Resistance: "+prota.getresist(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-90);
	 	genericfont.draw(batch,"Agility: "+prota.getagility(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-110);
	 	genericfont.draw(batch,"Force: "+prota.getforce(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-130);
	 	genericfont.draw(batch,"Wear:", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-150);
	 	if (prota.gethead().getname()!=null) {
	 		genericfont.draw(batch,"Head: "+prota.gethead().getname()+" At:+"+prota.gethead().getattack()+" Df:+"+prota.gethead().getdefense()+" Dur:"+prota.gethead().getdurability(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-170);
        } else {
        	genericfont.draw(batch,"Head: nothing", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-170);

        }
        if (prota.getlefthand().getname()!=null) {
        	genericfont.draw(batch,"Left hand: "+prota.getlefthand().getname()+" At:+"+prota.getlefthand().getattack()+" Df:+"+prota.getlefthand().getdefense()+" Dur:"+prota.getlefthand().getdurability(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-190);
        } else {
        	genericfont.draw(batch,"Left hand: nothing", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-190);

        }
        if (prota.getrighthand().getname()!=null) {
        	genericfont.draw(batch,"Right hand: "+prota.getrighthand().getname()+" At:+"+prota.getrighthand().getattack()+" Df:+"+prota.getrighthand().getdefense()+" Dur:"+prota.getrighthand().getdurability(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-210);
        } else {
        	genericfont.draw(batch,"Right hand: nothing", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-210);

        }
        if (prota.getbody().getname()!=null) {
        	genericfont.draw(batch,"Body: "+prota.getbody().getname()+" At:+"+prota.getbody().getattack()+" Df:+"+prota.getbody().getdefense()+" Dur:"+prota.getbody().getdurability(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-230);
        } else {
        	genericfont.draw(batch,"Body: nothing", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-230);

        }
        if (prota.getfoot().getname()!=null) {
        	genericfont.draw(batch,"Foot: "+prota.getfoot().getname()+" At:+"+prota.getfoot().getattack()+" Df:+"+prota.getfoot().getdefense()+" Dur:"+prota.getfoot().getdurability(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-250);
        } else {
        	genericfont.draw(batch,"Foot: nothing", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-250);
        }
        
        // overenemy description
        
        if (actualenemy!=null) {
        	if (actualenemy.getname()!=null) {
        		genericfont.draw(batch,"Enemy: "+actualenemy.getname(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-400);
        		genericfont.draw(batch,"Life Points: "+actualenemy.gethp(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-420);
        		genericfont.draw(batch,"Resistance: "+actualenemy.getresist(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-440);
        		genericfont.draw(batch,"Agility: "+actualenemy.getagility(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-460);
        		genericfont.draw(batch,"Force: "+actualenemy.getforce(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-480);
        	}
        }
        
        // overconsumable description
        
        if (actualconsumable!=null) {
        	if (actualconsumable.getname()!=null) {
        		genericfont.draw(batch,"Consumable: "+actualconsumable.getname(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-400);
        		genericfont.draw(batch,"+ Life Points: "+actualconsumable.getpoweruplife(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-420);
        		genericfont.draw(batch,"+ Agility Points: "+actualconsumable.getpowerupagility(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-440);
        	}
        }
        
        // overobject description
        
        if (actualobject!=null) {
        	if (actualobject.getname()!=null) {
        		genericfont.draw(batch,"Object: "+actualobject.getname(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-400);
        		genericfont.draw(batch,"+ defense: "+actualobject.getdefense(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-420);
        		genericfont.draw(batch,"+ offense: "+actualobject.getattack(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-440);
        	}
        }
        
        
        
        // draw background tiles 
        int relativex=0;
        for (int xpos=mapa.getfirstxtile();xpos<(mapa.getfirstxtile()+GameEngine.ON_SCREEN_TILES_X);xpos++) {
        	int relativey=0;
        	for (int ypos=mapa.getfirstytile();ypos<(mapa.getfirstytile()+GameEngine.ON_SCREEN_TILES_Y);ypos++) {
        			batch.draw(tilelayout[xpos][ypos].gettileimage(),relativex*GameEngine.TILE_X_SIZE,relativey*GameEngine.TILE_Y_SIZE);
        			relativey++;
        	}
        	relativex++;
        }
        
        // draw enemies
        ListIterator<Enemy> bgiterator = badguys.listIterator();
        while (bgiterator.hasNext()) {
        	//System.out.println("entra");
        	Enemy bguy=bgiterator.next();
        	//System.out.println(bguy.getabsolutex());
        	if (bguy.enemyonscreen(mapa.getfirstxtile(), mapa.getfirstytile())==true) {
        		// draw enemy image
        		
    			batch.draw(bguy.getsprite(),(bguy.getabsolutex()-mapa.getfirstxtile())*GameEngine.TILE_X_SIZE,(bguy.getabsolutey()-mapa.getfirstytile())*GameEngine.TILE_Y_SIZE);       		
        	}
        }
        
        // draw objects
        ListIterator<Object> objiterator = availableobjects.listIterator();
        while (objiterator.hasNext()) {
        	//System.out.println("entra");
        	Object obj=objiterator.next();
        	//System.out.println(bguy.getabsolutex());
        	if (obj.objectonscreen(mapa.getfirstxtile(), mapa.getfirstytile())==true) {
        		// draw enemy image
        		
    			batch.draw(obj.getsprite(),(obj.getabsolutex()-mapa.getfirstxtile())*GameEngine.TILE_X_SIZE,(obj.getabsolutey()-mapa.getfirstytile())*GameEngine.TILE_Y_SIZE);       		
        	}
        }
        
        // draw consumables
        ListIterator<Consumable> consumableiterator = availableconsumables.listIterator();
        while (consumableiterator.hasNext()) {
        	//System.out.println("entra");
        	Consumable consumable=consumableiterator.next();
        	//System.out.println(bguy.getabsolutex());
        	if (consumable.consumableonscreen(mapa.getfirstxtile(), mapa.getfirstytile())==true) {
        		// draw enemy image		
    			batch.draw(consumable.getsprite(),(consumable.getabsolutex()-mapa.getfirstxtile())*GameEngine.TILE_X_SIZE,(consumable.getabsolutey()-mapa.getfirstytile())*GameEngine.TILE_Y_SIZE);       		
        	}
        }
        
        // draw hero
        batch.draw(prota.getsprite(), prota.getrelativextile()*GameEngine.TILE_X_SIZE, prota.getrelativeytile()*GameEngine.TILE_Y_SIZE);
		
        // draw fight result
        if (just_fight==1) {
        	batch.draw(layout.gettextbackground(),50,(GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-400);
        	int linepos=0;
        	for (String line : fightstate.split("\n")) {
        		messagefont.draw(batch,line, 100, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-(200+(linepos*40)));
        		linepos++;
        	}
    			
        }
        // draw debug mode info
        genericfont.draw(batch, "Screen Mouse X:"+Gdx.input.getX()+" Projected Mouse X: "+realXcoord, 20, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-20);
        genericfont.draw(batch, "Screen Mouse Y:"+Gdx.input.getY()+" Projected Mouse Y: "+realYcoord, 20, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-40);
        genericfont.draw(batch, "I'm at X: "+mapa.getfirstxtile()+" Y: "+mapa.getfirstytile(), 20, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-60);
        genericfont.draw(batch, "Real screen size X:"+Gdx.graphics.getWidth(), 20, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-80);
        genericfont.draw(batch, "Real screen size Y:"+Gdx.graphics.getHeight(), 20, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-100);
       
        
		
        
        // draw object inventory
        genericfont.draw(batch,"Object inventory", 1000, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-330);

        for (int i=0;i<GameEngine.INVENTORY_SIZE;i++) {
        	if (objinv.get_object(i)!=null) {
        		genericfont.draw(batch,"Obj slot "+i+":"+objinv.get_object(i).getname(), 1000, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-(360+(i*20)));
        	} else {
        		genericfont.draw(batch,"Obj slot "+i+": available", 1000, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-(360+(i*20)));

        	}
        }
        
        // draw consumable inventory
        genericfont.draw(batch,"Consumable inventory", 1000, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-40);

        for (int i=0;i<GameEngine.INVENTORY_SIZE;i++) {
        	if (consinv.get_consumable(i)!=null) {
        		genericfont.draw(batch,"Cons slot "+i+":"+consinv.get_consumable(i).getname(), 1000, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-(70+(i*20)));
        	} else {
        		genericfont.draw(batch,"Cons slot "+i+": available", 1000, (GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y)-(70+(i*20)));

        	}
        }
        
        batch.end();
	}
	void frameratecontrol() {
		//  delay for each frame  -   time it took for one frame 
		long time = System.currentTimeMillis(); 
		time = (1000 / GameEngine.FPS) - (System.currentTimeMillis() - time); 
        
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
    	realXcoord=(int)((float)Gdx.input.getX()*(float)((float)GameEngine.WINDOWWIDTH/(float)Gdx.graphics.getWidth()));
		realYcoord=(int)((float)Gdx.input.getY()*(float)((float)GameEngine.WINDOWHEIGHT/(float)Gdx.graphics.getHeight()))*-1+(GameEngine.WINDOWHEIGHT);
    	if (GameEngine.ANDROID_MENU_BAR_ENABLE) { // i don't like this hardcoded way to do things
    		realYcoord=realYcoord+GameEngine.ANDROID_MENU_BAR_SIZE;
    	}
    	// mouse events control
    	if (Gdx.input.isTouched()) {
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
    			
    		}
    		// LOOK BUTTON! 
    		if (realXcoord>192 && realXcoord<256 && realYcoord>640 && realYcoord<704) {
    			look();
    		}
    	}
    	
    	// key events control
    	if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)) { goright(); } 
        if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT)) { goleft(); }
        if (Gdx.input.isKeyPressed(Keys.DPAD_UP)) { goup(); } 
        if (Gdx.input.isKeyPressed(Keys.DPAD_DOWN)) { godown();}
        if (Gdx.input.isKeyPressed(Keys.D)) { look(); }
        if (Gdx.input.isKeyPressed(Keys.H)) { fight(); }
        if (Gdx.input.isKeyPressed(Keys.G)) { take(); }
        
        if (Gdx.input.isKeyPressed(Keys.O)) 
        { 
        	// ENABLE OBJECT INVENTORY MODE
        	object_inv_mode=1;
    		consumable_inv_mode=0;
    		object_drop_mode=0;
    		just_fight=0;
        }
        if (Gdx.input.isKeyPressed(Keys.C)) 
        { 
        	// ENABLE CONSUMABLE INVENTORY MODE
        	object_inv_mode=0;
    		consumable_inv_mode=1;
    		object_drop_mode=0;
    		just_fight=0;
        }
        // OBJECT INVENTORY ACTIONS
        if (Gdx.input.isKeyPressed(Keys.NUM_1) && object_inv_mode==1) {
        	getobject(objinv.get_object(1),1);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_2) && object_inv_mode==1) {
        	getobject(objinv.get_object(2),2);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_3) && object_inv_mode==1) {
        	getobject(objinv.get_object(3),3);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_4) && object_inv_mode==1) {
        	getobject(objinv.get_object(4),4);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_5) && object_inv_mode==1) {
        	getobject(objinv.get_object(5),5);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_6) && object_inv_mode==1) {
        	getobject(objinv.get_object(6),6);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_7) && object_inv_mode==1) {
        	getobject(objinv.get_object(7),7);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_8) && object_inv_mode==1) {
        	getobject(objinv.get_object(8),8);
        }
        //if (Gdx.input.isKeyPressed(Keys.NUM_9) && object_inv_mode==1) {
        //	getobject(objinv.get_object(9),9);
        //}
        if (Gdx.input.isKeyPressed(Keys.NUM_0) && object_inv_mode==1) {
        	getobject(objinv.get_object(0),0);
        }
        // Q (DROP) INVENTORY OBJECT
        if (Gdx.input.isKeyPressed(Keys.Q)) 
        { 
        	// ENABLE CONSUMABLE INVENTORY MODE
        	object_inv_mode=0;
    		consumable_inv_mode=0;
    		object_drop_mode=1;
    		just_fight=0;
        }
        // OBJECT DROP INVENTORY ACTIONS
        if (Gdx.input.isKeyPressed(Keys.NUM_1) && object_drop_mode==1) {
        	objinv.delete_object(1);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_2) && object_drop_mode==1) {
        	objinv.delete_object(2);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_3) && object_drop_mode==1) {
        	objinv.delete_object(3);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_4) && object_drop_mode==1) {
        	objinv.delete_object(4);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_5) && object_drop_mode==1) {
        	objinv.delete_object(5);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_6) && object_drop_mode==1) {
        	objinv.delete_object(6);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_7) && object_drop_mode==1) {
        	objinv.delete_object(7);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_8) && object_drop_mode==1) {
        	objinv.delete_object(8);
        }
        //if (Gdx.input.isKeyPressed(Keys.NUM_9) && object_drop_mode==1) {
        //	objinv.delete_object(9);
        //}
        if (Gdx.input.isKeyPressed(Keys.NUM_0) && object_drop_mode==1) {
        	objinv.delete_object(0);
        }
        // CONSUMABLE INVENTORY ACTIONS
        if (Gdx.input.isKeyPressed(Keys.NUM_1) && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(1));
        	consinv.delete_consumable(1);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_2) && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(2));
        	consinv.delete_consumable(2);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_3) && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(3));
        	consinv.delete_consumable(3);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_4) && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(4));
        	consinv.delete_consumable(4);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_5) && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(5));
        	consinv.delete_consumable(5);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_6) && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(6));
        	consinv.delete_consumable(6);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_7) && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(7));
        	consinv.delete_consumable(7);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_8) && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(8));
        	consinv.delete_consumable(8);
        }
        //if (Gdx.input.isKeyPressed(Keys.NUM_9) && consumable_inv_mode==1) {
        //	getconsumable(consinv.get_consumable(9));
        //	consinv.delete_consumable(9);
        //}
        if (Gdx.input.isKeyPressed(Keys.NUM_0) && consumable_inv_mode==1) {
        	getconsumable(consinv.get_consumable(0));
        	consinv.delete_consumable(0);
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
			//resultoffight=prota.fight(actualenemy);
			resultoffight=prota.hit(actualenemy);
			//System.out.println("FIGHT!");
			// if hero wins
			if (resultoffight=="ENEMYDEAD") {
				// if you win
				prota.updateexperience(100);
				//System.out.println("YOU WIN!");
				if (actualenemy.getname()=="megaboss") {
						fightstate="You get the amulet, you win the game!!";
				} else {
					fightstate="Great! You win the battle!!";
				}
				
				game.removeenemy(actualenemy);
			} 
			if (resultoffight=="HERODEAD") {
				game.herodies();
				fightstate="You lose the battle, you are in the graveyard!";
			}
			if (resultoffight!="ENEMYDEAD" && resultoffight!="HERODEAD") {
				fightstate=resultoffight;
			}
		just_fight=1;
		}
    }
    void goup() {
    	object_inv_mode=0;
    	object_drop_mode=0;
		consumable_inv_mode=0;
		just_fight=0;
    	actualenemy=null;
    	actualconsumable=null;
    	actualobject=null;
    	game.heroup();
    }
    void godown() {
    	object_inv_mode=0;
    	object_drop_mode=0;
		consumable_inv_mode=0;
		just_fight=0;
    	actualenemy=null;
    	actualconsumable=null;
    	actualobject=null;
    	game.herodown();
    }
    void goleft() {
    	object_inv_mode=0;
    	object_drop_mode=0;
		consumable_inv_mode=0;
		just_fight=0;
    	actualenemy=null;
    	actualconsumable=null;
    	actualobject=null;
    	game.heroleft();
    }
    void goright() {
    	just_fight=0;
		object_inv_mode=0;
		object_drop_mode=0;
		consumable_inv_mode=0;
		actualenemy=null;
		actualconsumable=null;
		actualobject=null;
		game.heroright();
    }
    void look() {
    	object_inv_mode=0;
    	object_drop_mode=0;
		consumable_inv_mode=0;
		just_fight=0;
    	actualenemy=game.overenemy(); // get the enemy (if exist)
    	actualconsumable=game.overconsumable(); // get the consumable (if exist)
    	actualobject=game.overobject(); // get the object (if exist)
    }
    void take() {
    	object_inv_mode=0;
		consumable_inv_mode=0;
		object_drop_mode=0;
		just_fight=0;
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
}
