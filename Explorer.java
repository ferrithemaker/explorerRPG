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

import java.awt.*; 
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.Iterator;
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
    GameEngine map;
    Hero prota;
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
    int consumable_inv_mode=0;
	
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
        	setTitle("Explorer"); 
            setSize(GameEngine.WINDOWWITH, GameEngine.WINDOWHEIGHT); 
            setResizable(false); 
            setDefaultCloseOperation(EXIT_ON_CLOSE); 
            setVisible(true);
            
            // create tile layout
            map = new GameEngine();
            map.start();
            tilelayout = map.getmap();
            prota = map.gethero();
            badguys= map.getenemies();
            availableobjects=map.getobjects();
            availableconsumables=map.getconsumables();
            insets = getInsets(); 
            setSize(insets.left + GameEngine.WINDOWWITH + insets.right, 
                            insets.top + GameEngine.WINDOWHEIGHT + insets.bottom); 
            
            backBuffer = new BufferedImage(GameEngine.WINDOWWITH, GameEngine.WINDOWHEIGHT, BufferedImage.TYPE_INT_RGB); 
            // empty enemy that hold enemy, object and consumable
            actualenemy= new Enemy();
            actualobject= new Object();
            actualconsumable= new Consumable();
            
            
            
            
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
        	int number=randomGenerator.nextInt(6);
        	if (number==1) { // create enemy
        		GameEngine.createenemy();
        	}
        	if (number==2) { // create consumable
        		GameEngine.createconsumable();
        		
        	}
        	if (number==3) { // create object
        		GameEngine.createobject();
        	}
        	
        	// key events control
        	if (input.isKeyDown(KeyEvent.VK_RIGHT)) 
            { 
        		object_inv_mode=0;
        		consumable_inv_mode=0;
        		actualenemy=null;
        		actualconsumable=null;
        		actualobject=null;
        		GameEngine.heroright();
        		System.out.println(GameEngine.getfirstxtile()+"|"+GameEngine.getfirstytile());
            } 
            if (input.isKeyDown(KeyEvent.VK_LEFT)) 
            { 
            	object_inv_mode=0;
        		consumable_inv_mode=0;
            	actualenemy=null;
            	actualconsumable=null;
            	actualobject=null;
            	GameEngine.heroleft();
        		System.out.println(GameEngine.getfirstxtile()+"|"+GameEngine.getfirstytile());

            }
            if (input.isKeyDown(KeyEvent.VK_UP)) 
            { 
            	object_inv_mode=0;
        		consumable_inv_mode=0;
            	actualenemy=null;
            	actualconsumable=null;
            	actualobject=null;
            	GameEngine.heroup();
        		System.out.println(GameEngine.getfirstxtile()+"|"+GameEngine.getfirstytile());

            } 
            if (input.isKeyDown(KeyEvent.VK_DOWN)) 
            { 
            	object_inv_mode=0;
        		consumable_inv_mode=0;
            	actualenemy=null;
            	actualconsumable=null;
            	actualobject=null;
            	GameEngine.herodown();
        		System.out.println(GameEngine.getfirstxtile()+"|"+GameEngine.getfirstytile());

            }
            if (input.isKeyDown(KeyEvent.VK_D)) 
            { 
            	object_inv_mode=0;
        		consumable_inv_mode=0;
            	actualenemy=GameEngine.overenemy(); // get the enemy (if exist)
            	actualconsumable=GameEngine.overconsumable(); // get the consumable (if exist)
            	actualobject=GameEngine.overobject(); // get the object (if exist)
            }
            /*if (input.isKeyDown(KeyEvent.VK_C)) 
            { 
            	// TEST KEY TO CREATE ELEMENTS
            	try {
            		GameEngine.addconsumable(new Consumable("potion",1,1,28,14,ImageIO.read(new File("potion.gif"))));
            	} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            	
            } */
            if (input.isKeyDown(KeyEvent.VK_O)) 
            { 
            	// ENABLE OBJECT INVENTORY MODE
            	object_inv_mode=1;
        		consumable_inv_mode=0;
            }
            if (input.isKeyDown(KeyEvent.VK_C)) 
            { 
            	// ENABLE CONSUMABLE INVENTORY MODE
            	object_inv_mode=0;
        		consumable_inv_mode=1;
            }
            // OBJECT INVENTORY ACTIONS
            if (input.isKeyDown(KeyEvent.VK_1) && object_inv_mode==1) {
            	getobject(objinv.get_object(1));
            	objinv.delete_object(1);
            }
            if (input.isKeyDown(KeyEvent.VK_2) && object_inv_mode==1) {
            	getobject(objinv.get_object(2));
            	objinv.delete_object(2);
            }
            if (input.isKeyDown(KeyEvent.VK_3) && object_inv_mode==1) {
            	getobject(objinv.get_object(3));
            	objinv.delete_object(3);
            }
            if (input.isKeyDown(KeyEvent.VK_4) && object_inv_mode==1) {
            	getobject(objinv.get_object(4));
            	objinv.delete_object(4);
            }
            if (input.isKeyDown(KeyEvent.VK_5) && object_inv_mode==1) {
            	getobject(objinv.get_object(5));
            	objinv.delete_object(5);
            }
            if (input.isKeyDown(KeyEvent.VK_6) && object_inv_mode==1) {
            	getobject(objinv.get_object(6));
            	objinv.delete_object(6);
            }
            if (input.isKeyDown(KeyEvent.VK_7) && object_inv_mode==1) {
            	getobject(objinv.get_object(7));
            	objinv.delete_object(7);
            }
            if (input.isKeyDown(KeyEvent.VK_8) && object_inv_mode==1) {
            	getobject(objinv.get_object(8));
            	objinv.delete_object(8);
            }
            if (input.isKeyDown(KeyEvent.VK_9) && object_inv_mode==1) {
            	getobject(objinv.get_object(9));
            	objinv.delete_object(9);
            }
            if (input.isKeyDown(KeyEvent.VK_0) && object_inv_mode==1) {
            	getobject(objinv.get_object(0));
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
            	// get consumable
        		actualconsumable=GameEngine.overconsumable(); // get the consumable (if exist)
        		if (actualconsumable.getname()!=null) {
        			// if consumable exists
        			//prota.updateagility(actualconsumable.getpowerupagility());
        			//prota.updatelp(actualconsumable.getpoweruplife());
        			//GameEngine.removeconsumable(actualconsumable);
        			if (consinv.getfreeslot()!=-1) {
        				consinv.set_consumable(consinv.getfreeslot(), actualconsumable);	
        				GameEngine.removeconsumable(actualconsumable);
        			}
        			
        			
        		}
        		// get object
        		actualobject=GameEngine.overobject(); // get the consumable (if exist)
        		if (actualobject.getname()!=null) {
        			// if object exists
        			/*if (actualobject.getposition()=="head") {
        				prota.sethead(actualobject);
        			}
        			if (actualobject.getposition()=="righthand") {
        				prota.setrighthand(actualobject);
        			}
        			if (actualobject.getposition()=="lefthand") {
        				prota.setlefthand(actualobject);
        			}
        			if (actualobject.getposition()=="body") {
        				prota.setbody(actualobject);
        			}
        			if (actualobject.getposition()=="foot") {
        				prota.setfoot(actualobject);
        			}
        			GameEngine.removeobject(actualobject); */
        			if (objinv.getfreeslot()!=-1) {
        				objinv.set_object(objinv.getfreeslot(), actualobject);
        				GameEngine.removeobject(actualobject);
        			}
        		}
        		
        		
            }
            if (input.isKeyDown(KeyEvent.VK_F)) 
            { 
            	object_inv_mode=0;
        		consumable_inv_mode=0;
            	boolean resultoffight=false;
            	actualenemy=GameEngine.overenemy(); // get the enemy (if exist)
        		if (actualenemy.getname()!=null) {
        			resultoffight=GameEngine.fight(actualenemy);
        			System.out.println("FIGHT!");
        			// if hero wins
        			if (resultoffight==true) {
        				// if you win
        				GameEngine.removeenemy(actualenemy);
        				//System.out.println("YOU WIN!");
        				fightstate="You Win the Battle!!";
        			} else {
        				// if you lose
        				GameEngine.herodies();
        				fightstate="You lose the battle!!";
        				
        				
        			}
        			
        		}
            }
        }    
        void getobject(Object obj) {
        	if (obj.getname()!=null) {
    			// if object exists
    			if (obj.getposition()=="head") {
    				prota.sethead(obj);
    			}
    			if (obj.getposition()=="righthand") {
    				prota.setrighthand(obj);
    			}
    			if (obj.getposition()=="lefthand") {
    				prota.setlefthand(obj);
    			}
    			if (obj.getposition()=="body") {
    				prota.setbody(obj);
    			}
    			if (obj.getposition()=="foot") {
    				prota.setfoot(obj);
    			}
        	}
        }
        void getconsumable(Consumable obj) {
        	if (obj.getname()!=null) {
    			// if consumable exists
    			prota.updateagility(obj.getpowerupagility());
    			prota.updatelp(obj.getpoweruplife());
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
            bbg.fillRect(0, 0, GameEngine.WINDOWWITH, GameEngine.WINDOWHEIGHT); 
            
            bbg.setColor(Color.BLACK); 
            bbg.fillRect(GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X, 0, 5, GameEngine.TILE_Y_SIZE*GameEngine.ON_SCREEN_TILES_Y);
            // draw hero information
            bbg.drawString("Explorer Menu", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, 30);
            bbg.drawString("Hi "+prota.getname()+"!", (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, 50);
            bbg.drawString("Experience: "+prota.getexperience(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, 70);
            bbg.drawString("Life Points: "+prota.getlp(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, 90);
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
            		bbg.drawString("Life Points: "+actualenemy.getlp(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25,420);
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
            
            for (int i=0;i<10;i++) {
            	bbg.drawString("Obj slot "+i+":"+objinv.get_object(i).getname(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, 400);
            }
            
            // draw consumable inventory
            
            for (int i=0;i<10;i++) {
            	bbg.drawString("Obj slot "+i+":"+consinv.get_consumable(i).getname(), (GameEngine.TILE_X_SIZE*GameEngine.ON_SCREEN_TILES_X)+25, 400);
            }
            
            // draw background tiles 
            
            int relativex=0;
            for (int xpos=GameEngine.getfirstxtile();xpos<(GameEngine.getfirstxtile()+GameEngine.ON_SCREEN_TILES_X);xpos++) {
            	int relativey=0;
            	for (int ypos=GameEngine.getfirstytile();ypos<(GameEngine.getfirstytile()+GameEngine.ON_SCREEN_TILES_Y);ypos++) {
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
            	if (bguy.enemyonscreen(GameEngine.getfirstxtile(), GameEngine.getfirstytile())==true) {
            		// draw enemy image
            		
        			bbg.drawImage(bguy.getsprite(),(bguy.getabsolutex()-GameEngine.getfirstxtile())*GameEngine.TILE_X_SIZE,(bguy.getabsolutey()-GameEngine.getfirstytile())*GameEngine.TILE_Y_SIZE,null);       		
            	}
            }
            
            // draw objects
            ListIterator<Object> objiterator = availableobjects.listIterator();
            while (objiterator.hasNext()) {
            	//System.out.println("entra");
            	Object obj=objiterator.next();
            	//System.out.println(bguy.getabsolutex());
            	if (obj.objectonscreen(GameEngine.getfirstxtile(), GameEngine.getfirstytile())==true) {
            		// draw enemy image
            		
        			bbg.drawImage(obj.getsprite(),(obj.getabsolutex()-GameEngine.getfirstxtile())*GameEngine.TILE_X_SIZE,(obj.getabsolutey()-GameEngine.getfirstytile())*GameEngine.TILE_Y_SIZE,null);       		
            	}
            }
            
            // draw consumables
            ListIterator<Consumable> consumableiterator = availableconsumables.listIterator();
            while (consumableiterator.hasNext()) {
            	//System.out.println("entra");
            	Consumable consumable=consumableiterator.next();
            	//System.out.println(bguy.getabsolutex());
            	if (consumable.consumableonscreen(GameEngine.getfirstxtile(), GameEngine.getfirstytile())==true) {
            		// draw enemy image
            		
        			bbg.drawImage(consumable.getsprite(),(consumable.getabsolutex()-GameEngine.getfirstxtile())*GameEngine.TILE_X_SIZE,(consumable.getabsolutey()-GameEngine.getfirstytile())*GameEngine.TILE_Y_SIZE,null);       		
            	}
            }
            
            // draw hero
            bbg.drawImage(prota.getimage(),prota.getrelativextile()*GameEngine.TILE_X_SIZE,prota.getrelativeytile()*GameEngine.TILE_Y_SIZE,null);
            g.drawImage(backBuffer, insets.left, insets.top, this);   
        } 
} 