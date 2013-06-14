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
	private BufferedImage sprite;
	private Object head;
	private Object lefthand;
	private Object righthand;
	private Object body;
	private Object foot;
	
	public Hero(String name,BufferedImage sprite) {
		// initial set-up
		this.agility=1; 
		this.force=100; // offense
		this.resist=1; // defense
		this.life=100; // hp
		this.exp=1; // experience
		this.relative_y_tile=1;
		this.relative_x_tile=1;
		this.name=name;
		this.head = new Object(); // null object
		this.body = new Object(); // null object
		this.lefthand = new Object(); // null object
		this.righthand = new Object(); // null object
		this.foot = new Object(); // null object
		this.sprite=sprite;
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
		}
	}
	public void down() {
		if (this.relative_y_tile<GameEngine.ON_SCREEN_TILES_Y-1) {
			this.relative_y_tile += 1;
		}
	}
	public void left() {
		if (this.relative_x_tile>0) {
			this.relative_x_tile -= 1;
		}
	}
	public void right() {
		if (this.relative_x_tile<GameEngine.ON_SCREEN_TILES_X-1) {
			this.relative_x_tile += 1;
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
	
}
