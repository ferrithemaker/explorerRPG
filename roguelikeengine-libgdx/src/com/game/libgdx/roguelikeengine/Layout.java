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
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Layout {
	private Sprite menu_img;
    private Sprite actionmenu_img;
    private Sprite arrowdown_img;
    private Sprite arrowup_img;
    private Sprite arrowleft_img;
    private Sprite arrowright_img;
    private Sprite androidcommands_img;
	// screen methods
    public Layout() {
    	// tiles
		menu_img = new Sprite(new Texture(Gdx.files.internal("UI/charactermenu.png")),WrapperEngine.OPTION_MENU_X_SIZE,WrapperEngine.WINDOWHEIGHT);
		actionmenu_img= new Sprite(new Texture(Gdx.files.internal("UI/actionmenu.png")),WrapperEngine.ON_SCREEN_TILES_X*WrapperEngine.TILE_X_SIZE,64);
		arrowdown_img= new Sprite(new Texture(Gdx.files.internal("UI/arrowdown.png")),64,64);
		arrowup_img= new Sprite(new Texture(Gdx.files.internal("UI/arrowup.png")),64,64);
		arrowleft_img= new Sprite(new Texture(Gdx.files.internal("UI/arrowleft.png")),64,64);
		arrowright_img= new Sprite(new Texture(Gdx.files.internal("UI/arrowright.png")),64,64);
		androidcommands_img= new Sprite(new Texture(Gdx.files.internal("UI/androidcommands.png")),128,256);
		
    }
	public Sprite getmenubackground() {
		return menu_img;
	}
	public Sprite getactionmenu() {
		return actionmenu_img;
	}
	public Sprite getarrowdown() {
		return arrowdown_img;
	}
	public Sprite getarrowup() {
		return arrowup_img;
	}
	public Sprite getarrowleft() {
		return arrowleft_img;
	}
	public Sprite getarrowright() {
		return arrowright_img;
	}
	public Sprite getandroidcommands() {
		return androidcommands_img;
	}

}
