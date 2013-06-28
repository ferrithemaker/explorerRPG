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
    private Sprite backgroundtext_img;
	// screen methods
    public Layout() {
    	// tiles
		menu_img = new Sprite(new Texture(Gdx.files.internal("menu.png")),512,640);
		backgroundtext_img= new Sprite(new Texture(Gdx.files.internal("text_background.png")),1000,300);
    }
	public Sprite getmenubackground() {
		return menu_img;
	}
	public Sprite gettextbackground() {
		return backgroundtext_img;
	}

}
