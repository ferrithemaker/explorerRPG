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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class SplashScreen implements Screen {
	
	private Explorer_libgdx theGame;
	private SpriteBatch spriteBatch;
	private PopupInfoText screentext;
	private BitmapFont messagefont;
	private String text;
	private float fadein;
	private Rectangle viewport;
	
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
		text="WELCOME TO THE EXPLORER GAME.\nYou awake in the land of andor, \ntrapped in the great gardens of andor.\nThe only way to leave \nis using the amulet of willing.\nYou must recover it defeating megaboss.\nTo defeat megaboss, you must upgrade\nyour character killing monsters,\ntaking objects and drinking potions.\n\nGood luck.";
    	// create a fight message info screen 
    	screentext=new PopupInfoText(0,0,"UI/splashscreen.png",Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
    	screentext.settextoffset((int)(Gdx.graphics.getHeight() * .1f), (int)(Gdx.graphics.getHeight() * .2f));
    }

    @Override
    public void render(float delta)
    {
    		// set viewport
    		
        	Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,(int) viewport.width, (int) viewport.height);
        	Gdx.gl.glClearColor(0, 0, 0, 1);
    		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            spriteBatch.begin();
            fadein=fadein+0.001f;
            if (fadein>1.0f) { fadein=1.0f; }
            spriteBatch.setColor(1.0f, 1.0f, 1.0f, fadein);
            screentext.drawScreen(spriteBatch, messagefont, text,fadein,40,Color.YELLOW);
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
    public void resize(int width,int height) {
    	// calculate new viewport
        
    	float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);
        if(aspectRatio > WrapperEngine.ASPECT_RATIO)
        {
            scale = (float)height/(float)WrapperEngine.VIRTUAL_HEIGHT;
            crop.x = (width - WrapperEngine.VIRTUAL_WIDTH*scale)/2f;
        }
        else if(aspectRatio < WrapperEngine.ASPECT_RATIO)
        {
            scale = (float)width/(float)WrapperEngine.VIRTUAL_WIDTH;
            crop.y = (height - WrapperEngine.VIRTUAL_HEIGHT*scale)/2f;
        }
        else
        {
            scale = (float)width/(float)WrapperEngine.VIRTUAL_WIDTH;
        }

        float w = (float)WrapperEngine.VIRTUAL_WIDTH*scale;
        float h = (float)WrapperEngine.VIRTUAL_HEIGHT*scale;
        viewport = new Rectangle(crop.x, crop.y, w, h);
    	
    }

}
