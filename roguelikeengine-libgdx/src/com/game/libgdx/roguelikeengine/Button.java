package com.game.libgdx.roguelikeengine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Button {
	private Rectangle area;
	private Rectangle screen;
	private Sprite button_img;
	
	public Button(float x, float y, int w, int h,String file) {
		x = Math.max(0f, Math.min(1f, x));
		y = Math.max(0f, Math.min(1f, y));
		
		System.out.println(x + ", " + y);
		this.area= new Rectangle(x, y, w, h);
		
		Vector2 p = this.getscreencoordinates();
		this.screen = new Rectangle(p.x, p.y, area.getWidth(), area.getHeight());
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
	
	public Vector2 getscreencoordinates() {
		return new Vector2(this.area.getX() * Gdx.graphics.getWidth(), this.area.getY() * Gdx.graphics.getHeight());
	}
	
	public boolean inscreencoordinates(int x, int y) {
		Gdx.app.log("Button", "Testing coordinates " + x + ", " + y + ", against rectangle " + screen + " at " + this.getscreencoordinates());
		return screen.contains(x, y);
	}
	
	public boolean mouseover() {
		return this.inscreencoordinates(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
	}
	
	public Sprite getsprite() {
		return this.button_img;
	}
	
	public boolean contains(int x,int y) {
		return area.contains((float)x,(float)y);
	}

}
