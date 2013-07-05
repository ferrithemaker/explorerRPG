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

import java.util.ArrayList;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;






public class Explorer_libgdx extends Game {
	private SpriteBatch batch;
	//private Texture texture;
	
    // inventory status
    int object_inv_mode=0;
    int object_drop_mode=0;
    int eye_mode=0;
    int consumable_inv_mode=0;
    
   
    
    Enemy actualenemy; // enemy that i'm over
    Object actualobject; 
    Consumable actualconsumable;
    String fightstate="";
    ArrayList<Enemy> badguys;
    ArrayList<Object> availableobjects;
    ArrayList<Consumable> availableconsumables;
    
    Object_inventory objinv;
    Consumable_inventory consinv;
    
   
    
    // fight status
    int just_fight=0;
	
	@Override
	public void create() {		
		setScreen(new SplashScreen(this));
	}

	@Override
	public void render() {
		super.render();
		
	}
	
	
	
   
    
   
    // Original class methods
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		//texture.dispose();
	}
}
