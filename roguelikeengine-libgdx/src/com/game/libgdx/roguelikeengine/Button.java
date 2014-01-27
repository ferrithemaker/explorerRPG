package com.game.libgdx.roguelikeengine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Button {
	private Rectangle area;
	private Sprite button_img;
	
	public Button(int x,int y,int w,int h,String file) {
		// initial set-up
		this.area= new Rectangle((float)x,(float)y,(float)w,(float)h);
		this.button_img= new Sprite(new Texture(Gdx.files.internal(file)),w,h);
	}
	
	public Rectangle getarea() {
		return this.area;
	}
	
	public int getx() {
		return (int)this.area.getX();
	}
	
	public int gety() {
		return (int)this.area.getY();
	}
	
	
	public Sprite getsprite() {
		return this.button_img;
	}
	
	public boolean contains(int x,int y) {
		return area.contains((float)x,(float)y);
	}

}
