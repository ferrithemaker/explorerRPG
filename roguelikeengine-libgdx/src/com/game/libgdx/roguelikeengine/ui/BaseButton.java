package com.game.libgdx.roguelikeengine.ui;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class BaseButton implements IVisibleButton {
	public static final int BUTTON_STATE_DOWN = 0;
	public static final int BUTTON_STATE_UP = 1;
	
	protected LinkedList<ButtonAction> actionListeners = new LinkedList<ButtonAction>();
	
	protected Rectangle screenRectangle;
	protected float relativeX;
	protected float relativeY;
	
	protected float width;
	protected float height;
	
	protected boolean mouseOver = false;
	protected int buttonState = BUTTON_STATE_UP;
	
	protected long timeMouseOver = 0L;
	
	protected BitmapFont font;
	private String text;
	private Color backgroundColor;
	private Color foregroundColor;
	
	private NinePatch ninePatcherBackground;
	
	private float textX;
	private float textY;
	
	public BaseButton(float relativeX, float relativeY, float width, float height) { this(relativeX, relativeY, width, height, true); }
	public BaseButton(float relativeX, float relativeY, float width, float height, boolean fitOnScreen) {
		this.relativeX = Math.max(0, Math.min(1, relativeX));
		this.relativeY = Math.max(0, Math.min(1, relativeY));
		
		this.width = width;
		this.height = height;
		
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		
		float potentialX = screenWidth * this.relativeX;
		float potentialY = screenHeight * this.relativeY;
		
		if(fitOnScreen) {
			potentialX = Math.min(potentialX, screenWidth - width);
			potentialY = Math.min(potentialY, screenHeight - height);
			
			this.width = Math.min(this.width, screenWidth);
			this.height = Math.min(this.height, screenHeight);
		}
		
		this.screenRectangle = new Rectangle(potentialX, potentialY, this.width, this.height);
	}
	
	@Override
	public void addActionListener(ButtonAction bca) {
		actionListeners.add(bca);
	}

	@Override
	public void removeActionListener(ButtonAction bca) {
		actionListeners.remove(bca);
	}

	@Override
	public Vector2 getScreenCoordinates() {
		return new Vector2(this.screenRectangle.getX(), this.screenRectangle.getY());
	}

	@Override
	public float getScreenX() {
		return this.screenRectangle.getX();
	}

	@Override
	public float getScreenY() {
		return this.screenRectangle.getY();
	}

	@Override
	public boolean getIsInScreenCoordinates(float x, float y) {
		return this.screenRectangle.contains(x, y);
	}

	@Override
	public boolean getIsInScreenCoordinates(Vector2 position) {
		return this.screenRectangle.contains(position);
	}

	@Override
	public boolean getIsMouseover() {
		return mouseOver;
	}

	@Override
	public float getRelativeX() {
		return this.relativeX;
	}

	@Override
	public float getRelativeY() {
		return this.relativeY;
	}

	@Override
	public boolean isDown() {
		return buttonState == BaseButton.BUTTON_STATE_DOWN;
	}

	@Override
	public void drawOnScreen(SpriteBatch batch) {
		if(backgroundColor != null) {
			this.ninePatcherBackground.draw(batch, getScreenX(), getScreenY(), width, height);
		}
		
		if(foregroundColor != null && text != "") {
			font.draw(batch, text, getScreenX() + textX, getScreenY() + textY);
		}
		
		onRender(batch);
	}

	@Override
	public void update() {
		float mouseX = Gdx.input.getX();
		float invertedMouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
		boolean over = this.screenRectangle.contains(mouseX, invertedMouseY);
		
		if(!mouseOver && over) {
			mouseOver = true;
			
			for(ButtonAction ba : actionListeners) {
				ba.onMouseEnter(this);
			}
			
			timeMouseOver = 0;
		} else if(mouseOver && !over) {
			mouseOver = false;
			buttonState = BaseButton.BUTTON_STATE_UP;
			this.ninePatcherBackground = new NinePatch(new Texture("UI/ninePatchButtonUp.png"), backgroundColor);
			
			for(ButtonAction ba : actionListeners) {
				ba.onMouseExit(this);
			}
		}
		
		if(over) {
			timeMouseOver += 1L;
			
			if(Gdx.input.isTouched()) {
				buttonState = BaseButton.BUTTON_STATE_DOWN;
				this.ninePatcherBackground = new NinePatch(new Texture("UI/ninePatchButtonDown.png"), backgroundColor);
			} else if(buttonState == BaseButton.BUTTON_STATE_DOWN) {
				buttonState = BaseButton.BUTTON_STATE_UP;
				this.ninePatcherBackground = new NinePatch(new Texture("UI/ninePatchButtonUp.png"), backgroundColor);
				
				for(ButtonAction ba : actionListeners) {
					ba.onClicked(this);
				}
			}
		}
		
		onUpdate();
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) {
		if(text != this.text) {
			this.text = text;
			
			{	// setup font
				font = new BitmapFont();

				while(font.getScaleX() > 0.1f && font.getBounds(text).width > width - 10) {
					font.setScale(font.getScaleX() - 0.1f);
				}
			}
			
			float textWidth = font.getBounds(text).width;
			textX = width * 0.5f - textWidth * 0.5f;
			
			float textHeight = font.getLineHeight() * font.getScaleY();
			textY = height * 0.5f + textHeight * 0.5f;
		}
	}

	@Override
	public Color getBackgroundColor() {
		return this.backgroundColor;
	}

	@Override
	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
		this.ninePatcherBackground = new NinePatch(new Texture("UI/ninePatchButtonUp.png"), backgroundColor);
	}
	
	@Override
	public Color getForegroundColor() {
		return this.foregroundColor;
	}

	@Override
	public void setForegroundColor(Color color) {
		this.foregroundColor = color;
	}
	
	public abstract void onUpdate();
	public abstract void onRender(SpriteBatch batch);
}
