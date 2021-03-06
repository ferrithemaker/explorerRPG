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
	private Sprite tiledecoration;
	private boolean showimage;

	private TileOccupier blocker;
	
	private int column;
	private int row;
	
	private boolean canpcg = true;
	
	public Tile(int column, int row, boolean status) {
		this.bloqued=status;
		this.showimage=false;
		
		this.column = column;
		this.row = row;
	}
	
	public boolean isbloqued() {
		return this.bloqued;
	}
	public boolean isempty() {
		return !isbloqued();
	}
	public void block(TileOccupier blocker) {
		this.bloqued=true;
		this.blocker = blocker;
	}
	
	public TileOccupier getblocker() {
		return this.blocker;
	}
	
	public void unblock() {
		this.bloqued=false;
		this.blocker = null;
	}
	public void settileimage(Sprite sprite) {
		this.tileimg=sprite;
		this.showimage=true;
	}
	public Sprite gettileimage() {
		return this.tileimg;
	}
	public void settiledecoration(Sprite sprite) {
		this.tiledecoration=sprite;
		this.showimage=true;
	}
	public Sprite gettiledecoration() {
		return this.tiledecoration;
	}
	public boolean getshowimage() {
		return showimage;
	}
	public void setshowimage(boolean value) {
		this.showimage=value;
	}
	
	public int getcolumn() { return column; }
	public int getrow() { return row; }

	public boolean canPCG() {
		return canpcg;
	}

	public void setPCG(boolean canpcg) {
		this.canpcg = canpcg;
	}
	
	@Override
	public String toString() {
		return "Tile " + column + ", " + row;
	}
}
