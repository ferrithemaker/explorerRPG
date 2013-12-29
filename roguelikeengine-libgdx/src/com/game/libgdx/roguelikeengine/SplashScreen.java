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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


public class SplashScreen implements Screen {
	
	private Explorer_libgdx theGame;
	private SpriteBatch spriteBatch;
	private PopupInfoText screentext;
	private BitmapFont messagefont;
	private String text;
	private float fadein;
	
	public SplashScreen(Explorer_libgdx g)
    {
            theGame = g;
            fadein=0;
    }
	
	@Override
    public void show()
    {
    	//layout=new Layout();
		
		FileHandle fontFile = Gdx.files.internal("fonts/diabloheavy.ttf");
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
    	spriteBatch = new SpriteBatch();
    	//messagefont = new BitmapFont();
    	messagefont = generator.generateFont(40); // px
    	messagefont.setColor(Color.YELLOW);
		//messagefont.setScale(3f); 
		text="WELCOME TO EXPLORER TEST GAME.\nYou awake in the andor graveyard, \ntrapped in the great gardens of andor.\nThe only way to leave \nis using the amulet of willing.\nYou must recover it defeating megaboss.\nTo defeat megaboss, you must upgrade\nyour character killing monsters,\ntaking objects and drinking potions.\n\nGood luck.";
    	// create a fight message info screen 
    	screentext=new PopupInfoText(0,0,"UI/splashscreen.png",1280,704);
    	screentext.settextoffset(100, 150);
    }

    @Override
    public void render(float delta)
    {
            Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            spriteBatch.begin();
            fadein=fadein+0.001f;
            if (fadein>1.0f) { fadein=1.0f; }
            spriteBatch.setColor(1.0f, 1.0f, 1.0f, fadein);
            screentext.drawScreen(spriteBatch, messagefont, text,fadein);
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
