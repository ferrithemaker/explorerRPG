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
    private Sprite androidcommands_img;
    private Sprite androiddirections_img;
    private Sprite energybar_img;
    private Sprite redbar_img;
	// screen methods
    public Layout() {
    	// tiles
		menu_img = new Sprite(new Texture(Gdx.files.internal("UI/charactermenu.png")),WrapperEngine.OPTION_MENU_X_SIZE,WrapperEngine.WINDOWHEIGHT);
		actionmenu_img= new Sprite(new Texture(Gdx.files.internal("UI/actionmenu.png")),WrapperEngine.ON_SCREEN_TILES_X*WrapperEngine.TILE_X_SIZE,64);
		androidcommands_img= new Sprite(new Texture(Gdx.files.internal("UI/androidcommands.png")),128,256);
		androiddirections_img= new Sprite(new Texture(Gdx.files.internal("UI/androiddirections.png")),256,256);
		energybar_img= new Sprite(new Texture(Gdx.files.internal("UI/energybar.png")),60,5);
		redbar_img= new Sprite(new Texture(Gdx.files.internal("UI/redbar.png")),6,5);
		
    }
	public Sprite getmenubackground() {
		return menu_img;
	}
	public Sprite getactionmenu() {
		return actionmenu_img;
	}
	public Sprite getandroidcommands() {
		return androidcommands_img;
	}
	public Sprite getandroiddirections() {
		return androiddirections_img;
	}
	public Sprite getenergybar() {
		return energybar_img;
	}
	public Sprite getredbar() {
		return redbar_img;
	}

}
