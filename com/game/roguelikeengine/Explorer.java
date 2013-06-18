package com.game.roguelikeengine;

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


import java.awt.*; 
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;


import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;


public class Explorer extends JFrame
{        
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	boolean isRunning = true; 
    
    BufferedImage backBuffer;

    Insets insets; 
    Inputhandler input;
    Tile[][] tilelayout;
    GameEngine game;
    Hero prota;
    Map mapa;
    Enemy actualenemy; // enemy that i'm over
    Object actualobject; 
    Consumable actualconsumable;
    String fightstate="";
    ArrayList<Enemy> badguys;
    ArrayList<Object> availableobjects;
    ArrayList<Consumable> availableconsumables;
    
    Object_inventory objinv;
    Consumable_inventory consinv;
    
    
    int x = 0;
    int y= 0;
    
    // inventory status
    int object_inv_mode=0;
    int object_drop_mode=0;
    int consumable_inv_mode=0;
    
    // fight status
    int just_fight=0;
	
	public static void main(String[] args)	{ 
        	Explorer explorertest = new Explorer(); 
        	explorertest.run(); 
            System.exit(0);       
    } 
        
        /** 
         * This method starts the game and runs it in a loop 
         */ 
        public void run() 
        { 
        	initialize(); 
            
            while(isRunning) 
            { 
                    long time = System.currentTimeMillis(); 
                    
                    update(); 
                    draw(); 
                    
                    //  delay for each frame  -   time it took for one frame 
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
            
            setVisible(false); 
                
        } 
        
        /** 
         * This method will set up everything need to run 
         */ 
        void initialize() 
        { 
        	setTitle(GameEngine.APP_NAME); 
            setSize(GameEngine.WINDOWWITH, GameEngine.WINDOWHEIGHT); 
            setResizable(false); 
            setDefaultCloseOperation(EXIT_ON_CLOSE); 
            setVisible(true);
            
            // create tile layout
            game = new GameEngine();
            mapa=game.getmap();
            tilelayout = mapa.gettiles();
            prota = game.gethero();
            badguys= game.getenemies();
            availableobjects=game.getobjects();
            availableconsumables=game.getconsumables();
            insets = getInsets(); 
            setSize(insets.left + GameEngine.WINDOWWITH + insets.right, 
                            insets.top + GameEngine.WINDOWHEIGHT + insets.bottom); 
            
            backBuffer = new BufferedImage(GameEngine.WINDOWWITH, GameEngine.WINDOWHEIGHT, BufferedImage.TYPE_INT_RGB); 
            // empty enemy that hold enemy, object and consumable
            actualenemy= new Enemy();
            actualobject= new Object();
            actualconsumable= new Consumable();
            
            objinv= new Object_inventory();
            consinv= new Consumable_inventory();
            
            
            // key handler
            input = new Inputhandler(this);
            
        } 
        
        /** 
         * This method will check for input, move things 
         * around and check for win conditions, etc 
         */ 
        void update() 
        { 
        	// random elements generator
        	Random randomGenerator = new Random();
        	int number=randomGenerator.nextInt(3);
        	if (number==0) { // create enemy
        		game.createrandomenemy();
        	}
        	if (number==1) { // create consumable
        		game.createrandomconsumable();
        		
        	}
        	if (number==2) { // create object
        		game.createrandomobject();
        	}
        	
        	// key events control
        	if (input.isKeyDown(KeyEvent.VK_RIGHT)) 
            { 
        		just_fight=0;
        		object_inv_mode=0;
        		object_drop_mode=0;
        		consumable_inv_mode=0;
        		actualenemy=null;
        		actualconsumable=null;
        		actualobject=null;
        		game.heroright();
        		System.out.println(mapa.getfirstxtile()+"|"+mapa.getfirstytile());
            } 
            if (input.isKeyDown(KeyEvent.VK_LEFT)) 
            { 
            	object_inv_mode=0;
            	object_drop_mode=0;
        		consumable_inv_mode=0;
        		just_fight=0;
            	actualenemy=null;
            	actualconsumable=null;
            	actualobject=null;
            	game.heroleft();
        		System.out.println(mapa.getfirstxtile()+"|"+mapa.getfirstytile());

            }
            if (input.isKeyDown(KeyEvent.VK_UP)) 
            { 
            	object_inv_mode=0;
            	object_drop_mode=0;
        		consumable_inv_mode=0;
        		just_fight=0;
            	actualenemy=null;
            	actualconsumable=null;
            	actualobject=null;
            	game.heroup();
        		System.out.println(mapa.getfirstxtile()+"|"+mapa.getfirstytile());

            } 
            if (input.isKeyDown(KeyEvent.VK_DOWN)) 
            { 
            	object_inv_mode=0;
            	object_drop_mode=0;
        		consumable_inv_mode=0;
        		just_fight=0;
            	actualenemy=null;
            	actualconsumable=null;
            	actualobject=null;
            	game.herodown();
        		System.out.println(mapa.getfirstxtile()+"|"+mapa.getfirstytile());

            }
            if (input.isKeyDown(KeyEvent.VK_D)) 
            { 
            	object_inv_mode=0;
            	object_drop_mode=0;
        		consumable_inv_mode=0;
        		just_fight=0;
            	actualenemy=game.overenemy(); // get the enemy (if exist)
            	actualconsumable=game.overconsumable(); // get the consumable (if exist)
            	actualobject=game.overobject(); // get the object (if exist)
            }
            
            if (input.isKeyDown(KeyEvent.VK_O)) 
            { 
            	// ENABLE OBJECT INVENTORY MODE
            	object_inv_mode=1;
        		consumable_inv_mode=0;
        		object_drop_mode=0;
        		just_fight=0;
            }
            if (input.isKeyDown(KeyEvent.VK_C)) 
            { 
            	// ENABLE CONSUMABLE INVENTORY MODE
            	object_inv_mode=0;
        		consumable_inv_mode=1;
        		object_drop_mode=0;
        		just_fight=0;
            }
            // OBJECT INVENTORY ACTIONS
            if (input.isKeyDown(KeyEvent.VK_1) && object_inv_mode==1) {
            	getobject(objinv.get_object(1),1);
            }
            if (input.isKeyDown(KeyEvent.VK_2) && object_inv_mode==1) {
            	getobject(objinv.get_object(2),2);
            }
            if (input.isKeyDown(KeyEvent.VK_3) && object_inv_mode==1) {
            	getobject(objinv.get_object(3),3);
            }
            if (input.isKeyDown(KeyEvent.VK_4) && object_inv_mode==1) {
            	getobject(objinv.get_object(4),4);
            }
            if (input.isKeyDown(KeyEvent.VK_5) && object_inv_mode==1) {
            	getobject(objinv.get_object(5),5);
            }
            if (input.isKeyDown(KeyEvent.VK_6) && object_inv_mode==1) {
            	getobject(objinv.get_object(6),6);
            }
            if (input.isKeyDown(KeyEvent.VK_7) && object_inv_mode==1) {
            	getobject(objinv.get_object(7),7);
            }
            if (input.isKeyDown(KeyEvent.VK_8) && object_inv_mode==1) {
            	getobject(objinv.get_object(8),8);
            }
            if (input.isKeyDown(KeyEvent.VK_9) && object_inv_mode==1) {
            	getobject(objinv.get_object(9),9);
            }
            if (input.isKeyDown(KeyEvent.VK_0) && object_inv_mode==1) {
            	getobject(objinv.get_object(0),0);
            }
            // Q (DROP) INVENTORY OBJECT
            if (input.isKeyDown(KeyEvent.VK_Q)) 
            { 
            	// ENABLE CONSUMABLE INVENTORY MODE
            	object_inv_mode=0;
        		consumable_inv_mode=0;
        		object_drop_mode=1;
        		just_fight=0;
            }
            // OBJECT DROP INVENTORY ACTIONS
            if (input.isKeyDown(KeyEvent.VK_1) && object_drop_mode==1) {
            	objinv.delete_object(1);
            }
            if (input.isKeyDown(KeyEvent.VK_2) && object_drop_mode==1) {
            	objinv.delete_object(2);
            }
            if (input.isKeyDown(KeyEvent.VK_3) && object_drop_mode==1) {
            	objinv.delete_object(3);
            }
            if (input.isKeyDown(KeyEvent.VK_4) && object_drop_mode==1) {
            	objinv.delete_object(4);
            }
            if (input.isKeyDown(KeyEvent.VK_5) && object_drop_mode==1) {
            	objinv.delete_object(5);
            }
            if (input.isKeyDown(KeyEvent.VK_6) && object_drop_mode==1) {
            	objinv.delete_object(6);
            }
            if (input.isKeyDown(KeyEvent.VK_7) && object_drop_mode==1) {
            	objinv.delete_object(7);
            }
            if (input.isKeyDown(KeyEvent.VK_8) && object_drop_mode==1) {
            	objinv.delete_object(8);
            }
            if (input.isKeyDown(KeyEvent.VK_9) && object_drop_mode==1) {
            	objinv.delete_object(9);
            }
            if (input.isKeyDown(KeyEvent.VK_0) && object_drop_mode==1) {
            	objinv.delete_object(0);
            }
            // CONSUMABLE INVENTORY ACTIONS
            if (input.isKeyDown(KeyEvent.VK_1) && consumable_inv_mode==1) {
            	getconsumable(consinv.get_consumable(1));
            	consinv.delete_consumable(1);
            }
            if (input.isKeyDown(KeyEvent.VK_2) && consumable_inv_mode==1) {
            	getconsumable(consinv.get_consumable(2));
            	consinv.delete_consumable(2);
            }
            if (input.isKeyDown(KeyEvent.VK_3) && consumable_inv_mode==1) {
            	getconsumable(consinv.get_consumable(3));
            	consinv.delete_consumable(3);
            }
            if (input.isKeyDown(KeyEvent.VK_4) && consumable_inv_mode==1) {
            	getconsumable(consinv.get_consumable(4));
            	consinv.delete_consumable(4);
            }
            if (input.isKeyDown(KeyEvent.VK_5) && consumable_inv_mode==1) {
            	getconsumable(consinv.get_consumable(5));
            	consinv.delete_consumable(5);
            }
            if (input.isKeyDown(KeyEvent.VK_6) && consumable_inv_mode==1) {
            	getconsumable(consinv.get_consumable(6));
            	consinv.delete_consumable(6);
            }
            if (input.isKeyDown(KeyEvent.VK_7) && consumable_inv_mode==1) {
            	getconsumable(consinv.get_consumable(7));
            	consinv.delete_consumable(7);
            }
            if (input.isKeyDown(KeyEvent.VK_8) && consumable_inv_mode==1) {
            	getconsumable(consinv.get_consumable(8));
            	consinv.delete_consumable(8);
            }
            if (input.isKeyDown(KeyEvent.VK_9) && consumable_inv_mode==1) {
            	getconsumable(consinv.get_consumable(9));
            	consinv.delete_consumable(9);
            }
            if (input.isKeyDown(KeyEvent.VK_0) && consumable_inv_mode==1) {
            	getconsumable(consinv.get_consumable(0));
            	consinv.delete_consumable(0);
            }
            if (input.isKeyDown(KeyEvent.VK_G)) 
            { 	
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
            if (input.isKeyDown(KeyEvent.VK_H)) 
            { 
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
        				game.removeenemy(actualenemy);
        				//System.out.println("YOU WIN!");
        				fightstate="Great! You Win the Battle!!";
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
        /** 
         * This method will draw everything 
         */ 
        void draw() 
        { 
        	Graphics g = getGraphics(); 
            
            Graphics bbg = backBuffer.getGraphics(); 
            
            bbg.setColor(Color.WHITE);
            bbg.setFont(new Font("Serif", Font.BOLD, 12));
            bbg.fillRect(0, 0, GameEngine.WINDOWWITH, GameEngine.WINDOWHEIGHT);
            
            // draw menu background
			bbg.drawImage(mapa.getmenubackground(),800,0,null);

            bbg.setColor(Color.BLACK); 
            bbg.fillRect(GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X, 0, 5, GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y);
            // draw hero information
            //bbg.drawString("** "+GameEngine.APP_NAME+" **", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, 30);
            bbg.drawString("Hi "+prota.getname()+"!", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, 50);
            bbg.drawString("Experience: "+prota.getexperience(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, 70);
            bbg.drawString("Life Points: "+prota.gethp(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, 90);
            bbg.drawString("Resistance: "+prota.getresist(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,110);
            bbg.drawString("Agility: "+prota.getagility(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,130);
            bbg.drawString("Force: "+prota.getforce(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,150);
            bbg.drawString("Wear:", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,170);
            if (prota.gethead().getname()!=null) {
            	bbg.drawString("Head: "+prota.gethead().getname()+" At:+"+prota.gethead().getattack()+" Df:+"+prota.gethead().getdefense()+" Dur:"+prota.gethead().getdurability(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,190);
            } else {
            	bbg.drawString("Head: nothing", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,190);

            }
            if (prota.getlefthand().getname()!=null) {
            	bbg.drawString("Left hand: "+prota.getlefthand().getname()+" At:+"+prota.getlefthand().getattack()+" Df:+"+prota.getlefthand().getdefense()+" Dur:"+prota.getlefthand().getdurability(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,210);
            } else {
            	bbg.drawString("Lelft hand: nothing", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,210);

            }
            if (prota.getrighthand().getname()!=null) {
            	bbg.drawString("Right hand: "+prota.getrighthand().getname()+" At:+"+prota.getrighthand().getattack()+" Df:+"+prota.getrighthand().getdefense()+" Dur:"+prota.getrighthand().getdurability(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,230);
            } else {
            	bbg.drawString("Right hand: nothing", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,230);

            }
            if (prota.getbody().getname()!=null) {
            	bbg.drawString("Body: "+prota.getbody().getname()+" At:+"+prota.getbody().getattack()+" Df:+"+prota.getbody().getdefense()+" Dur:"+prota.getbody().getdurability(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,250);
            } else {
            	bbg.drawString("Body: nothing", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,250);

            }
            if (prota.getfoot().getname()!=null) {
            	bbg.drawString("Foot: "+prota.getfoot().getname()+" At:+"+prota.getfoot().getattack()+" Df:+"+prota.getfoot().getdefense()+" Dur:"+prota.getfoot().getdurability(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,270);
            } else {
            	bbg.drawString("Foot: nothing", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,270);
            }
            // overenemy description
            
            if (actualenemy!=null) {
            	if (actualenemy.getname()!=null) {
            		bbg.drawString("Enemy: "+actualenemy.getname(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, 400);
            		bbg.drawString("Life Points: "+actualenemy.gethp(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,420);
            		bbg.drawString("Resistance: "+actualenemy.getresist(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,440);
            		bbg.drawString("Agility: "+actualenemy.getagility(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,460);
            		bbg.drawString("Force: "+actualenemy.getforce(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,480);
            	}
            }
            
            // overconsumable description
            
            if (actualconsumable!=null) {
            	if (actualconsumable.getname()!=null) {
            		bbg.drawString("Consumable: "+actualconsumable.getname(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, 400);
            		bbg.drawString("+ Life Points: "+actualconsumable.getpoweruplife(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,420);
            		bbg.drawString("+ Agility Points: "+actualconsumable.getpowerupagility(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,440);
            	}
            }
            
            // overobject description
            
            if (actualobject!=null) {
            	if (actualobject.getname()!=null) {
            		bbg.drawString("Object: "+actualobject.getname(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, 400);
            		bbg.drawString("+ defense: "+actualobject.getdefense(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,420);
            		bbg.drawString("+ offense: "+actualobject.getattack(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,440);
            	}
            }
            
            // draw object inventory
    		bbg.drawString("Object inventory", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+320, 320);

            for (int i=0;i<10;i++) {
            	if (objinv.get_object(i)!=null) {
            		bbg.drawString("Obj slot "+i+":"+objinv.get_object(i).getname(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+320, 350+(i*20));
            	} else {
            		bbg.drawString("Obj slot "+i+": available", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+320, 350+(i*20));

            	}
            }
            
            // draw consumable inventory
    		bbg.drawString("Consumable inventory", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+320, 40);

            for (int i=0;i<10;i++) {
            	if (consinv.get_consumable(i)!=null) {
            		bbg.drawString("Cons slot "+i+":"+consinv.get_consumable(i).getname(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+320, 70+(i*20));
            	} else {
            		bbg.drawString("Cons slot "+i+": available", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+320, 70+(i*20));

            	}
            }
            
            // draw background tiles 
            
            int relativex=0;
            for (int xpos=mapa.getfirstxtile();xpos<(mapa.getfirstxtile()+GameEngine.ON_SCREEN_TILES_X);xpos++) {
            	int relativey=0;
            	for (int ypos=mapa.getfirstytile();ypos<(mapa.getfirstytile()+GameEngine.ON_SCREEN_TILES_Y);ypos++) {
            			bbg.drawImage(tilelayout[xpos][ypos].gettileimage(),relativex*GameEngine.TILE_X_SIZE,relativey*GameEngine.TILE_Y_SIZE,null);
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
            		
        			bbg.drawImage(bguy.getsprite(),(bguy.getabsolutex()-mapa.getfirstxtile())*GameEngine.TILE_X_SIZE,(bguy.getabsolutey()-mapa.getfirstytile())*GameEngine.TILE_Y_SIZE,null);       		
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
            		
        			bbg.drawImage(obj.getsprite(),(obj.getabsolutex()-mapa.getfirstxtile())*GameEngine.TILE_X_SIZE,(obj.getabsolutey()-mapa.getfirstytile())*GameEngine.TILE_Y_SIZE,null);       		
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
        			bbg.drawImage(consumable.getsprite(),(consumable.getabsolutex()-mapa.getfirstxtile())*GameEngine.TILE_X_SIZE,(consumable.getabsolutey()-mapa.getfirstytile())*GameEngine.TILE_Y_SIZE,null);       		
            	}
            }
            
            // draw hero
            //bbg.drawImage(prota.getimage(),prota.getrelativextile()*GameEngine.TILE_X_SIZE,prota.getrelativeytile()*GameEngine.TILE_Y_SIZE,null);
            bbg.drawImage(prota.getimage(), prota.getrelativextile()*GameEngine.TILE_X_SIZE, prota.getrelativeytile()*GameEngine.TILE_Y_SIZE, (prota.getrelativextile()*GameEngine.TILE_X_SIZE)+40, (prota.getrelativeytile()*GameEngine.TILE_Y_SIZE)+40, prota.getxspriteposition(), prota.getyspriteposition(), prota.getxspriteposition()+40, prota.getyspriteposition()+40, null);
            
            // draw fight result
            if (just_fight==1) {
            	bbg.drawImage(mapa.gettextbackground(),50,100,null);
            	bbg.setColor(Color.YELLOW);
                bbg.setFont(new Font("Serif", Font.BOLD, 30));
        		game.drawString(bbg,fightstate, 100, 150); // override drawstring, allows newline
            }
            
            // draw on the backbuffer
            g.drawImage(backBuffer, insets.left, insets.top, this);
            
            
        } 
        
} 