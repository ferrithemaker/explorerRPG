package com.game.libgdx.roguelikeengine.ui;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HBox extends BaseButton {
	protected LinkedList<IButton> elements = new LinkedList<IButton>();
	
	protected float maxWidth = 0f;
	protected float maxHeight = 0f;
	
	public HBox(float relativeX, float relativeY, float width) {
		super(relativeX, relativeY, width, Gdx.graphics.getHeight(), false);
	}
	
	
	public HBox addElement(IButton button) {
		elements.add(button);
		
		positionElements();
		return this;
	}
	
	protected void positionElements() {
		float nextX = 0f;
		float nextY = 0f;
		
		float w = 0f;
		
		maxHeight = 0f;
		maxWidth = 0f;
		for(IButton elem : elements) {
			w = elem.getWidth();
			
			
			if(nextX + w >= width) {
				nextX = 0f;
				
				nextY += maxHeight;
				maxHeight = 0f;
			}
			
			elem.setX(getScreenX()+nextX);
			elem.setY(getScreenY()+nextY);
			
			nextX += w;
			maxHeight = Math.max(maxHeight, elem.getHeight());
			maxWidth = Math.max(maxWidth, nextX);
		}
		
		maxHeight += nextY;
	}

	public HBox finalizeHBox() {	// shrink down to size of buttons
		this.width = maxWidth;
		this.height = maxHeight;
		
		handleResize();
		
		return this;
	}
	
	public HBox removeElement(IButton button) {
		elements.remove(button);
		
		positionElements();
		return this;
	}

	@Override
	public void onUpdate() {
		for(IButton elem : elements) {
			elem.update();
		}
	}

	@Override
	public void onRender(SpriteBatch batch) {
		for(IButton elem : elements) {
			elem.drawOnScreen(batch);
		}
	}
	
	@Override
	public boolean getIsMouseover() {
		for(IButton elem : elements) {
			if(elem.getIsMouseover()) return true;
		}
		
		return false;
	}
}
