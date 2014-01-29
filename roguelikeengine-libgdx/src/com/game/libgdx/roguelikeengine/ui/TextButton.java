package com.game.libgdx.roguelikeengine.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextButton extends BaseButton {

	public TextButton(String text, float relativeX, float relativeY, float width, float height) {
		super(relativeX, relativeY, width, height, false);

		setBackgroundColor(Color.WHITE);
		setForegroundColor(Color.BLACK);
		setText(text);
	}

	@Override
	public void onUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRender(SpriteBatch batch) {
		// TODO Auto-generated method stub

	}

}
