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

public class AccessToLayer {
	private int outcommingX;
	private int outcommingY;
	private int incommingX;
	private int incommingY;
	private int incommingLayer;
	public AccessToLayer(int inx,int iny, int outx, int outy,int inlayer) {
		this.outcommingX=outx;
		this.outcommingY=outy;
		this.incommingX=inx;
		this.incommingY=iny;
		this.incommingLayer=inlayer;
	}
	public int getOutcommingX() {
		return outcommingX;
	}
	public void setOutcommingX(int outcommingX) {
		this.outcommingX = outcommingX;
	}
	public int getOutcommingY() {
		return outcommingY;
	}
	public void setOutcommingY(int outcommingY) {
		this.outcommingY = outcommingY;
	}
	public int getIncommingX() {
		return incommingX;
	}
	public void setIncommingX(int incommingX) {
		this.incommingX = incommingX;
	}
	public int getIncommingY() {
		return incommingY;
	}
	public void setIncommingY(int incommingY) {
		this.incommingY = incommingY;
	}
	public int getIncommingLayer() {
		return incommingLayer;
	}
	public void setIncommingLayer(int incommingLayer) {
		this.incommingLayer = incommingLayer;
	}
}
