package com.game.libgdx.roguelikeengine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Screen_text {

	private Sprite background;
	private int x;
	private int y;
	private int xsize;
	private int ysize;
	
	public Screen_text (int x,int y, String file, int xsize,int ysize) {
		this.x=x;
		this.y=y;
		this.xsize=xsize;
		this.ysize=ysize;
		this.background = new Sprite(new Texture(Gdx.files.internal(file)),xsize,ysize);
	}
	
	// sets

	public void update_x(int x) {
		this.x=x;
	}
	public void update_y(int y) {
		this.y=y;
	}
	// gets
	public int get_x() {
		return this.x;
	}
	public int get_y() {
		return this.y;
	}
	public void drawScreen(SpriteBatch batch, BitmapFont font,String text) {
 		batch.draw(this.background,x,y);
 		int linepos=0;
 		//font.draw(batch,this.text, x,y);
 		for (String line : text.split("\n")) {
    		font.draw(batch,line, x+50,(y+ysize)-(50+(linepos*40)));
    		linepos++;
    	}
	}

	

}
