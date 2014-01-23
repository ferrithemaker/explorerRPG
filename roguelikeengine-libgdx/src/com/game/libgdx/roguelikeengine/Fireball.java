package com.game.libgdx.roguelikeengine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Fireball extends Bullet {
	protected static Texture tex;
	
	protected int min = 4;
	protected int damage = 0;
	
	protected Sprite sprite;
	protected int halfHeight;
	protected int halfWidth;
	
	protected int flipDelay = 4;
	protected int flipDuration = 0;
	
	public Fireball(Map map, TileOccupier source, Directions direction) {
		super(map, source, direction);
		
		damage = (int) (min + Math.random() * 10);
		
		if(tex == null) {
			tex = new Texture(Gdx.files.internal("fireball.png")); 
			tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		sprite = new Sprite(tex);
		halfHeight = (int) (sprite.getWidth() * 0.5f);
		halfWidth = (int) (sprite.getHeight() * 0.5f);
		
		switch(direction) {
		case NORTH:
			sprite.rotate(270f);
			break;
		case SOUTH:
			sprite.rotate(90f);
			break;
		case WEST:
			sprite.flip(true, false);
			break;
		case EAST:
		default:
			break;
		}
	}

	@Override
	public void onUpdate() {
		Directions overall = direction.getOverallDirection();
		
		sprite.setX(position.x + (overall == Directions.NORTH_OR_SOUTH ? halfWidth : 0) - (map.getfirstxtile() * WrapperEngine.TILE_X_SIZE));
		sprite.setY(position.y + (overall == Directions.EAST_OR_WEST ? halfHeight : 0) - (map.getfirstytile() * WrapperEngine.TILE_Y_SIZE));
		
		flipDuration += 1;
		if(flipDuration >= flipDelay) {
			flipDuration = 0;

			sprite.flip(false, true);
		}
	}

	@Override
	public void onRender(SpriteBatch batch) {
		sprite.draw(batch);
	}

	@Override
	public void onHit(TileOccupier source, TileOccupier hit) {
		if(hit instanceof Enemy) {
			GameplayScreen.instance.alert(source.getname() + " cast Fireball on " + hit.getname() + " for " + damage + " damage!");
			Enemy enemy = ((Enemy) hit);
			enemy.updatehp(damage);
			
			if(enemy.gethp() <= 0) {
				GameplayScreen.instance.killEnemy(enemy);
			}
		}

		finished = true;
		
		if(source instanceof Hero) {
			GameplayScreen.instance.enemyTurn();
		}
	}

}
