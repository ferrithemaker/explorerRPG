package com.game.libgdx.roguelikeengine.ui;

import com.badlogic.gdx.graphics.Color;

public interface IVisibleButton extends IButton {
	public String getText();
	public void setText(String text);
	
	public Color getBackgroundColor();
	public void setBackgroundColor(Color color);
	
	public Color getForegroundColor();
	public void setForegroundColor(Color color);
	
}
