package com.game.libgdx.roguelikeengine.rooms;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Decoration {
	public String value;
	public Sprite sprite;
	
	public boolean isFloor = false;
	public boolean isWall = false;
	
	public double chance = 1.;
	
	// this class is designed to be passed into the room constructor and uses chaining and public variables by design
	public Decoration(Sprite sprite, Color color) {
		this(sprite, Color.rgba8888(color));
	}
	public Decoration(Sprite sprite, int value) {
		this(sprite, value+"");
	}
	public Decoration(Sprite sprite, long value) {
		this(sprite, Decoration.colorFromHex(value));
	}
	public Decoration(Sprite sprite, String value) {
		this.value = value;
		this.sprite = sprite;
	}
	public Decoration asWall() {
		this.isWall = true;
		return this;
	}
	
	public Decoration asFloor() {
		this.isFloor = true;
		return this;
	}
	
	public Decoration asChance(double chance) {
		this.chance = chance;
		return this;
	}
	
	public boolean passCheck() {
		return Math.random() < this.chance;
	}

	private static int colorFromHex(long hex) {
		float a = (hex & 0xFF000000L) >> 24;
		float r = (hex & 0xFF0000L) >> 16;
		float g = (hex & 0xFF00L) >> 8;
		float b = (hex & 0xFFL);

		return Color.rgba8888(r / 255f, g / 255f, b / 255f, a / 255f);
	}
}
