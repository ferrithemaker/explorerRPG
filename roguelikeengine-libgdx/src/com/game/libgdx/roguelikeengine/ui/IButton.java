package com.game.libgdx.roguelikeengine.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public interface IButton {
	public void addActionListener(ButtonAction bca);
	public void removeActionListener(ButtonAction bca);
	
	public Vector2 getScreenCoordinates();
	public float getScreenX();
	public float getScreenY();
	
	public boolean getIsInScreenCoordinates(float x, float y);
	public boolean getIsInScreenCoordinates(Vector2 position);
	
	public boolean getIsMouseover();
	
	public float getRelativeX();
	public float getRelativeY();

	public boolean isDown();
	public void drawOnScreen(SpriteBatch batch);
	
	public void update();
	
	public void setX(float x);
	public void setY(float y);
	
	public float getWidth();
	public float getHeight();
	
	public void handleResize();
}
