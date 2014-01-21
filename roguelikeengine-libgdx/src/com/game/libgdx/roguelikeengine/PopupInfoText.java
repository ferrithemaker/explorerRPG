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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class PopupInfoText {
	protected HashMap<String, LinkedList<Rectangle>> renderWords = new HashMap<String, LinkedList<Rectangle>>();
	protected HashMap<String, LinkedList<WordClickAction>> wordClickListeners = new HashMap<String, LinkedList<WordClickAction>>();
	
	private String mouseOverElement = "";
	
	private Sprite background;
	private int x;
	private int y;
	private int xsize;
	private int ysize;
	private int textoffsetx;
	private int textoffsety;
	
	protected String lastMessage = "";
	
	public PopupInfoText (int x,int y, String file, int xsize,int ysize) {
		this.x=x;
		this.y=y;
		this.xsize=xsize;
		this.ysize=ysize;
		this.background = new Sprite(new Texture(Gdx.files.internal(file)),this.xsize,this.ysize);
	}
	
	// sets

	public void update_x(int x) {
		this.x=x;
	}
	public void update_y(int y) {
		this.y=y;
	}
	public void settextoffset(int x, int y) {
		this.textoffsetx=x;
		this.textoffsety=y;
	}
	// gets
	public int get_x() {
		return this.x;
	}
	public int get_y() {
		return this.y;
	}
	
	public void drawScreen(SpriteBatch batch, BitmapFont font,String text,float fadein,int linedist,Color color) {
		this.background.setPosition(x,y);
		
		mouseOverElement = "";
		if(!lastMessage.equals(text)){
			this.clearRenderWords();
			
			int linepos=0;
			int currentWidth = x + textoffsetx;
			int nextWidth = 0;
			int maxWidth = (int) (this.x + this.background.getWidth() - textoffsetx);
			
	 		String[] words = null;
	 		for (String line : text.split("\n")) {
	 			font.setColor(color.r, color.g, color.b, fadein);
	 			
	 			words = line.split(" ");
	 			for(String word : words) {
	 				if(word.contains("\t")) {
	 					word = word.replace("\t", "    ");
	 				}
	 				
	 				
	 				nextWidth = (int) (currentWidth + font.getBounds(word + " ").width);
	 				if(nextWidth > maxWidth) {
	 					currentWidth = x + textoffsetx;
	 					nextWidth = (int) (currentWidth + font.getBounds(word + " ").width);
	 					linepos = linepos + 1;
	 				}
	
	 				int wordx = currentWidth;
	 				int wordy = (y+ysize)-((textoffsety)+(linepos*linedist));
	 				
	 				this.addWordToRender(word, new Rectangle(wordx, wordy, font.getBounds(word + " ").width, font.getLineHeight()));
	 				
	 				currentWidth = nextWidth;
	 			}
	 			
	    		linepos++;
	    		currentWidth = x + textoffsetx;
	    	}
		}
 		
		this.background.draw(batch,fadein);
		Set<Entry<String, LinkedList<Rectangle>>> keys = renderWords.entrySet();
		for(Entry<String, LinkedList<Rectangle>> entry : keys) {
			for(Rectangle rect : entry.getValue()) {
				font.setColor(color.r, color.g, color.b, fadein);
				if(wordClickListeners.containsKey(entry.getKey())) {
					if(mouseOverWord(rect, font)) {
						font.setColor(Color.BLUE.r, Color.BLUE.g, Color.BLUE.b, fadein);
						mouseOverElement = entry.getKey();
					} else {
						font.setColor(Color.CYAN);
						font.setColor(Color.CYAN.r, Color.CYAN.g, Color.CYAN.b, fadein);
					}
				}
				
				font.draw(batch, entry.getKey().replace("_", " "), rect.x, rect.y);
			}
		}
		
		lastMessage = text;
	}

	protected boolean mouseOverWord(Rectangle rect, BitmapFont font) {
		int mouseX = Gdx.input.getX();
		int mouseY = Gdx.graphics.getHeight() + ((int)font.getLineHeight()) - Gdx.input.getY();
		return rect.contains(mouseX, mouseY);
	}
	
	protected boolean xyOverWord(Rectangle rect, int x, int y) {
		return rect.contains(x, y);
	}
	
	protected void addWordToRender(String word, Rectangle position) {
		if(renderWords.containsKey(word)) {
			renderWords.get(word).push(position);
		} else {
			renderWords.put(word, new LinkedList<Rectangle>());
			addWordToRender(word, position);
		}
	}
	
	protected void clearRenderWords() {
		renderWords.clear();
	}
	
	public void addWordClickListener(String word, WordClickAction listener) {
		if(wordClickListeners.containsKey(word)) {
			wordClickListeners.get(word).push(listener);
		} else {
			wordClickListeners.put(word, new LinkedList<WordClickAction>());
			addWordClickListener(word, listener);
		}
	}
	
	public void removeWordClickListener(String word, WordClickAction listener) {
		if(wordClickListeners.containsKey(word)) {
			wordClickListeners.get(word).remove(listener);
		}
	}
	
	public boolean onMouseClicked() {
		if(wordClickListeners.containsKey(mouseOverElement)) {
			for(WordClickAction action : wordClickListeners.get(mouseOverElement)) {
				action.onClicked(mouseOverElement);
			}
			
			return true;
		}
		
		return false;
	}

	public boolean mouseOverElement() {
		return wordClickListeners.containsKey(mouseOverElement);
	}
}
