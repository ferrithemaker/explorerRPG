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

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;






public class Explorer_libgdx extends Game {
	private SpriteBatch batch;
	
	
	
	@Override
	public void create() {
		BackgroundMusic.setup();
		BackgroundMusic.startoutside();
		setScreen(new Credits(this));
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
		if(batch != null) batch.dispose();
	}
}
