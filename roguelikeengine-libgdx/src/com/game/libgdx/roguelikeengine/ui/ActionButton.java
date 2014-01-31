package com.game.libgdx.roguelikeengine.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.libgdx.roguelikeengine.GameplayScreen;

public class ActionButton extends BaseButton implements ButtonAction {
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final int QUIT = 4;
	public static final int LOOK = 5;
	public static final int TAKE = 6;
	public static final int DROP = 7;
	public static final int MAGIC = 8;
	public static final int FIGHT = 9;
	public static final int TALK = 10;
	
	public static Texture[] textures;
	static {
		textures = new Texture[] {
			new Texture("UI/upButton.png"),
			new Texture("UI/downButton.png"),
			new Texture("UI/leftButton.png"),
			new Texture("UI/rightButton.png"),
			new Texture("UI/exitButton.png"),
			new Texture("UI/lookButton.png"),
			new Texture("UI/takeButton.png"),
			new Texture("UI/dropButton.png"),
			new Texture("UI/magicButton.png"),
			new Texture("UI/fightButton.png"),
			new Texture("UI/talkButton.png")
		};
	}
	
	protected int action;
	
	public ActionButton(int action, float width, float height) {
		this(action, 0f, 0f, width, height);
	}
	
	public ActionButton(int action, float relativeX, float relativeY, float width, float height) {
		super(relativeX, relativeY, width, height);
		
		this.action = action;
		
		this.upTexture = ActionButton.textures[action];
		this.downTexture = ActionButton.textures[action];
		
		setBackgroundColor(Color.WHITE);
		
		this.addActionListener(this);
	}

	@Override
	public void onUpdate() {

	}

	@Override
	public void onRender(SpriteBatch batch) {

	}

	@Override
	public void onClicked(IButton button) {
		switch(this.action) {
		case LEFT:
			GameplayScreen.instance.goleft();
			break;
		case RIGHT:
			GameplayScreen.instance.goright();
			break;
		case DOWN:
			GameplayScreen.instance.godown();
			break;
		case UP:
			GameplayScreen.instance.goup();
			break;
		case FIGHT:
			GameplayScreen.instance.fight();
			break;
		case TAKE:
			GameplayScreen.instance.take();
			break;
		case DROP:
			GameplayScreen.instance.drop();
			break;
		case TALK:
			GameplayScreen.instance.talk();
			break;
		case LOOK:
			GameplayScreen.instance.look();
			break;
		case MAGIC:
			GameplayScreen.instance.magic();
			break;
		case QUIT:
			GameplayScreen.instance.exit();
			break;
		default:
			break;
		}
	}

	@Override
	public void onMouseEnter(IButton button) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseExit(IButton button) {
		// TODO Auto-generated method stub
		
	}
}
