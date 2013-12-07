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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen {
	
	private Explorer_libgdx theGame;
	private SpriteBatch spriteBatch;
	private PopupInfoText screentext;
	private BitmapFont messagefont;
	private String text;
	
	public SplashScreen(Explorer_libgdx g)
    {
            theGame = g;
    }
	
	@Override
    public void show()
    {
    	//layout=new Layout();    
    	spriteBatch = new SpriteBatch();
    	messagefont = new BitmapFont();
    	messagefont.setColor(Color.YELLOW);
		messagefont.setScale(3f);
		text="WELCOME TO EXPLORER TEST GAME.\nYou awake in the andor graveyard, \ntrapped in the great gardens of andor.\nThe only way to leave \nis using the amulet of willing.\nYou must recover it defeating megaboss.\nTo defeat megaboss, you must upgrade\nyour character killing monsters,\ntaking objects and drinking potions.\n\nGood luck.";
    	// create a fight message info screen 
    	screentext=new PopupInfoText(0,0,"splashscreen.png",1280,704);
    	screentext.settextoffset(200, 150);
    }

    @Override
    public void render(float delta)
    {
            Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            spriteBatch.begin();
            screentext.drawScreen(spriteBatch, messagefont, text);
            spriteBatch.end();
            
            if(Gdx.input.justTouched())
                    theGame.setScreen(new GameplayScreen());
    }
    
     
    @Override
    public void pause() {
    	
    }
    @Override
    public void hide() {
    	
    }
    @Override
    public void resume() {
    	
    }
    @Override
    public void dispose() {
    	if(spriteBatch != null) spriteBatch.dispose();
    	
    }
    @Override
    public void resize(int x,int y) {
    	
    }

}
