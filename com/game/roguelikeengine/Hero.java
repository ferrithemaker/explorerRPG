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


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;




public class Hero {
	private int agility;
	private int force;
	private int relative_x_tile;
	private int relative_y_tile;
	private String name;
	private int life;
	private int level;
	private int exp;
	private int resist;
	private int current_sprite_position;
	private BufferedImage sprite;
	private Object head;
	private Object lefthand;
	private Object righthand;
	private Object body;
	private Object foot;
	
	public Hero(String name,String file) {
		// initial set-up
		this.agility=1; 
		this.force=100; // offense
		this.resist=1; // defense
		this.life=100; // hp
		this.exp=1; // experience
		this.relative_y_tile=1;
		this.relative_x_tile=1;
		this.name=name;
		this.current_sprite_position=7;
		this.head = new Object(); // null object
		this.body = new Object(); // null object
		this.lefthand = new Object(); // null object
		this.righthand = new Object(); // null object
		this.foot = new Object(); // null object
		try {
			// hero
			this.sprite=ImageIO.read(new File(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// hero gets
	public String getname() {
		return this.name;
	}
	public int getexperience() {
		return this.exp;
	}
	public int getresist() {
		return this.resist+this.head.getdefense()+this.lefthand.getdefense()+this.righthand.getdefense()+this.body.getdefense()+this.foot.getdefense();
	}
	public int getforce() {
		return this.force+this.head.getattack()+this.lefthand.getattack()+this.righthand.getattack()+this.body.getattack()+this.foot.getattack();
	}
	public int getagility() {
		return this.agility;
	}
	public int gethp() {
		return this.life;
	}
	public int getlevel() {
		return this.level;
	}
	public int getrelativextile() {
		return this.relative_x_tile;
	}
	public int getrelativeytile() {
		return this.relative_y_tile;
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
	public BufferedImage getimage() {
		return this.sprite;
	}
	public int getyspriteposition() {
		if (current_sprite_position==1 || current_sprite_position==2 || current_sprite_position==3) {
			return 0;
		}
		if (current_sprite_position==4 || current_sprite_position==5 || current_sprite_position==6) {
			return 40;
		}
		if (current_sprite_position==7 || current_sprite_position==8 || current_sprite_position==9) {
			return 80;
		}
		if (current_sprite_position==10 || current_sprite_position==11 || current_sprite_position==12) {
			return 120;
		}
		return 0;
	}
	public int getxspriteposition() {
		if (current_sprite_position==1 || current_sprite_position==4 || current_sprite_position==7 || current_sprite_position==10 ) {
			return 0;
		}
		if (current_sprite_position==2 || current_sprite_position==5 || current_sprite_position==8 || current_sprite_position==11) {
			return 40;
		}
		if (current_sprite_position==3 || current_sprite_position==6 || current_sprite_position==9 || current_sprite_position==12) {
			return 80;
		}
		return 0;
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
	public void setrelativextile(int value) {
		this.relative_x_tile=value;
	}
	public void setrelativeytile(int value) {
		this.relative_y_tile=value;
	}
	public void updateagility(int value) {
		this.agility=this.agility+value;
	}
	public void updateexperience(int value) {
		this.exp=this.exp+value;
	}
	public void updatehp(int value) {
		this.life=this.life+value;
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
	public void up() {
		if (this.relative_y_tile>0) {
			this.relative_y_tile -= 1;
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
		}
	}
	public void down() {
		if (this.relative_y_tile<GameEngine.ON_SCREEN_TILES_Y-1) {
			this.relative_y_tile += 1;
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
		}
	}
	public void left() {
		if (this.relative_x_tile>0) {
			this.relative_x_tile -= 1;
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
		}
	}
	public void right() {
		if (this.relative_x_tile<GameEngine.ON_SCREEN_TILES_X-1) {
			this.relative_x_tile += 1;
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
		}
	}
	public void scrollup() {
		this.relative_y_tile=GameEngine.ON_SCREEN_TILES_Y-1;
	}
	public void scrolldown() {
		this.relative_y_tile=0;
	}
	public void scrollleft() {
		this.relative_x_tile=GameEngine.ON_SCREEN_TILES_X-1;
	}
	public void scrollrigth() {
		this.relative_x_tile=0;
	}
	
	// fight
	// hero fights monster
	public boolean fight(Enemy enemy) {
		boolean heroturn;
		int enemydicevalue;
		int herodicevalue;
		int enemyattackpower;
		int heroattackpower;
		Random randomGenerator = new Random();
		// decide who hits first
		if (enemy.getagility()>this.agility) { heroturn=false; } else { heroturn=true; }
		// begin fight loop
		while (enemy.gethp()>0 && this.life>0) { // while somebody is alive
			enemydicevalue = randomGenerator.nextInt(5);
			herodicevalue = randomGenerator.nextInt(5);
			enemyattackpower=enemy.getforce()+enemydicevalue;
			heroattackpower=this.force+herodicevalue;
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
				if (enemyattackpower-this.resist>0) { // if do damage
					this.life=this.life-(enemyattackpower-this.resist);
					System.out.println("Enemy turn:"+(enemyattackpower-this.resist));
					System.out.println("prota HP:"+this.life);
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
			this.exp=this.exp+100;
			System.out.println("YOU WIN!");
			return true;
		} else { 
			System.out.println("YOU LOSE!");
			return false;
		}
	}
	
}
