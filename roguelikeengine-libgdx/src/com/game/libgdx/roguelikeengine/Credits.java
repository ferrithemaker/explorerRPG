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
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


public class Credits extends InputAdapter implements Screen {
	
	private Explorer_libgdx theGame;
	private SpriteBatch spriteBatch;
	private PopupInfoText screentext;
	private BitmapFont messagefont;
	private String text;
	private float fadein;
	
	public Credits(Explorer_libgdx g)
    {
            theGame = g;
            fadein=0;
    }
	
	@Override
    public void show()
    {
    	//layout=new Layout();
		
		FileHandle fontFile = Gdx.files.internal("fonts/minecraftia.ttf");
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
    	spriteBatch = new SpriteBatch();
    	//messagefont = new BitmapFont();
    	messagefont = generator.generateFont(20); // px
		text="CREDITS\nConcept & Programming: Ferran_Fabregas & Joshua_Byrom\nUI design: Manuela Sanfelix & Ferran_Fabregas\nTiles and sprites taken from http://opengameart.org/\n& http://animatedbattlers.wordpress.com\nReleased under GPL / Creative_Commons and copyrighted\nby their owners\nMusic from https://soundcloud.com/desperate-measurez/ \n& http://www.tannerhelland.com/music-directory/\nSound FX: http://www.freesound.org/\nFonts taken from http://www.fonts101.com/ & http://www.flamingtext.com/\nThanks to: Joseph Elliott.\nGame released under the terms of GNU GPL .";
    	// create a fight message info screen 
    	screentext=new PopupInfoText(0,0,"UI/credits.png",1280,704);
    	screentext.settextoffset(100, 250);
    	
    	screentext.addWordClickListener("Joshua_Byrom", new WordClickAction() {
			@Override
			public void onClicked(String word) {
				Gdx.net.openURI("http://www.joshbyrom.com");
			}
		});
    	screentext.addWordClickListener("Ferran_Fabregas", new WordClickAction() {
			@Override
			public void onClicked(String word) {
				Gdx.net.openURI("http://www.ferranfabregas.info");
			}
		}); 	
    	screentext.addWordClickListener("http://opengameart.org/", new WordClickAction() {
			@Override
			public void onClicked(String word) {
				Gdx.net.openURI("http://www.opengameart.org");
			}
		}); 	
    	screentext.addWordClickListener("http://animatedbattlers.wordpress.com", new WordClickAction() {
    		@Override
    		public void onClicked(String word) {
    			Gdx.net.openURI("http://animatedbattlers.wordpress.com");
    		}
    	});
    	screentext.addWordClickListener("https://soundcloud.com/desperate-measurez/", new WordClickAction() {
    		@Override
    		public void onClicked(String word) {
    			Gdx.net.openURI("https://soundcloud.com/desperate-measurez/");
    		}
    	});
    	screentext.addWordClickListener("http://www.tannerhelland.com/music-directory/", new WordClickAction() {
    		@Override
    		public void onClicked(String word) {
    			Gdx.net.openURI("http://www.tannerhelland.com/music-directory/");
    		}
    	});
    	screentext.addWordClickListener("http://www.fonts101.com/", new WordClickAction() {
    		@Override
    		public void onClicked(String word) {
    			Gdx.net.openURI("http://www.fonts101.com/");
    		}
    	});
    	screentext.addWordClickListener("http://www.flamingtext.com/", new WordClickAction() {
    		@Override
    		public void onClicked(String word) {
    			Gdx.net.openURI("http://www.flamingtext.com/");
    		}
    	});
    	screentext.addWordClickListener("GPL", new WordClickAction() {
    		@Override
    		public void onClicked(String word) {
    			Gdx.net.openURI("http://www.gnu.org/licenses/gpl.txt");
    		}
    	});
    	screentext.addWordClickListener("http://www.freesound.org/", new WordClickAction() {
        	@Override
        	public void onClicked(String word) {
        		Gdx.net.openURI("http://www.freesound.org/");
        	}
        });	
    	screentext.addWordClickListener("GNU", new WordClickAction() {
    		@Override
    		public void onClicked(String word) {
    			Gdx.net.openURI("http://www.gnu.org");
    		}
    	});
    	
    	screentext.addWordClickListener("Creative_Commons", new WordClickAction() {
    		@Override
    		public void onClicked(String word) {
    			Gdx.net.openURI("http://creativecommons.org/");
    		}
    	});
		Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta)
    {
            Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            spriteBatch.begin();
            fadein=fadein+0.003f;
            if (fadein>1.0f) { fadein=1.0f; }
            //spriteBatch.setColor(1.0f, 1.0f, 1.0f, fadein);
            screentext.drawScreen(spriteBatch, messagefont, text,fadein,30,Color.WHITE);
            spriteBatch.end();
            
            if(Gdx.input.justTouched() && !screentext.mouseOverElement()) {
                    theGame.setScreen(new SplashScreen(theGame));
            }
    }
    
    @Override
    public boolean touchUp(int x, int y, int point, int button) {
    	return screentext.onMouseClicked();
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
