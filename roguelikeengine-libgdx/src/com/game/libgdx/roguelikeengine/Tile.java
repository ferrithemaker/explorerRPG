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



import com.badlogic.gdx.graphics.g2d.Sprite;



public class Tile {
	private boolean bloqued;
	private Sprite tileimg;
	private boolean showimage;

	public Tile(boolean status) {
		this.bloqued=status;
		this.showimage=false;
		
	}
	public boolean isbloqued() {
		return this.bloqued;
	}
	public boolean isempty() {
		return !isbloqued();
	}
	public void block() {
		this.bloqued=true;
	}
	public void unblock() {
		this.bloqued=false;
	}
	public void settileimage(Sprite sprite) {
		this.tileimg=sprite;
		this.showimage=true;
	}
	public Sprite gettileimage() {
		return this.tileimg;
	}
	public boolean getshowimage() {
		return showimage;
	}
	public void setshowimage(boolean value) {
		this.showimage=value;
	}
}
