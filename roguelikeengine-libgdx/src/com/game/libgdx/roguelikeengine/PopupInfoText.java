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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PopupInfoText {

	private Sprite background;
	private int x;
	private int y;
	private int xsize;
	private int ysize;
	private int textoffsetx;
	private int textoffsety;
	
	public PopupInfoText (int x,int y, String file, int xsize,int ysize) {
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
	public void settextoffset(int x, int y) {
		this.textoffsetx=x;
		this.textoffsety=y;
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
    		font.draw(batch,line, x+(textoffsetx),(y+ysize)-((textoffsety)+(linepos*40)));
    		linepos++;
    	}
	}

	

}
