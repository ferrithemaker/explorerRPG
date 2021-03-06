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
import java.util.regex.Pattern;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PopupInfoText {
	protected HashMap<String, LinkedList<WordRectangle>> renderWords = new HashMap<String, LinkedList<WordRectangle>>();
	protected HashMap<String, LinkedList<WordClickAction>> wordClickListeners = new HashMap<String, LinkedList<WordClickAction>>();
	
	private String mouseOverElement = "";
	
	private NinePatch background;
	private int x;
	private int y;
	private int width;
	private int height;
	
	private int textoffsetx;
	private int textoffsety;
	
	private int lineCount = 0;
	
	protected String lastMessage = "";
	protected String instructions = (Gdx.app.getType() == ApplicationType.Android ? "Touch " : "Click ") + " to Continue";
	protected Color instructionsColor = Color.GREEN;
	
	protected final String colorPattern = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
	
	public PopupInfoText (int x,int y, String file, int xsize,int ysize) {
		this.x=x;
		this.y=y;
		this.width=xsize;
		this.height=ysize;
		this.background = new NinePatch(new Texture(Gdx.files.internal(file)));
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
		drawScreen(batch, font, text, fadein, linedist, color, false);
	}
	public void drawScreen(SpriteBatch batch, BitmapFont font,String text,float fadein,int linedist,Color color, boolean resize) {
		float scaleX = font.getScaleX();
		float scaleY = font.getScaleY();
		
		update_x((int) (Gdx.graphics.getWidth() * 0.5f - this.width * 0.5f));
		update_y((int) (Gdx.graphics.getHeight() * 0.5f - this.height * 0.5f));
		
		mouseOverElement = "";
		if(!lastMessage.equals(text) || resize){
			this.clearRenderWords();
			
			lineCount = 0;
			
			int linepos = 0;
			int currentWidth = textoffsetx;
			int nextWidth = 0;
			int maxWidth = (int) (width - textoffsetx);
			
	 		String[] words = null;
	 		for (String line : text.split("\n")) {
	 			lineCount = lineCount + 1;
	 			
 				currentWidth = textoffsetx;
	 			
	 			words = line.split(" ");
	 			for(String word : words) {
	 				if(word.contains("\t")) {
	 					word = word.replace("\t", "    ");
	 				}
	 				
	 				Color wordColor = color;
	 				if(word.length() > 7) {
	 					String hex = word.substring(0, 7);

	 					if(Pattern.matches(colorPattern, hex)) {
	 						wordColor = this.hex2Rgb(hex);
	 						word = word.replace(hex, "");
	 					}
	 				}
	 				
	 				nextWidth = (int) (currentWidth + font.getBounds(word + " ").width);
	 				if(nextWidth > maxWidth) {
	 					currentWidth = textoffsetx;
	 					nextWidth = (int) (currentWidth + font.getBounds(word + " ").width);
	 					linepos = linepos + 1;
	 					lineCount = lineCount + 1;
	 				}
	
	 				int wordx = currentWidth;
	 				int wordy = height-((textoffsety)+(linepos*linedist));
	 				
	 				this.addWordToRender(word, new WordRectangle(wordx, wordy, font.getBounds(word + " ").width, font.getLineHeight()).withColor(wordColor));
	 				
	 				currentWidth = nextWidth;
	 			}
	 			
	    		linepos++;
	    		currentWidth = x + textoffsetx;
	    	}
		}
		
		this.background.draw(batch, x, y, width, height);
		
		while(((lineCount * font.getLineHeight() + lineCount * linedist) * font.getScaleY()) > height - textoffsety) {
			font.setScale(font.getScaleY() - 0.1f);
		}
		
		if(font.getScaleY() != scaleY) {
			drawScreen(batch, font, text, fadein, linedist, color, true);
			font.setScale(scaleX, scaleY);
			return;
		}
		
		boolean anyClickable = false;
		Set<Entry<String, LinkedList<WordRectangle>>> keys = renderWords.entrySet();
		for(Entry<String, LinkedList<WordRectangle>> entry : keys) {
			for(WordRectangle rect : entry.getValue()) {
				font.setColor(rect.color.r, rect.color.g, rect.color.b, fadein);
				
				if(wordClickListeners.containsKey(entry.getKey())) {
					if(mouseOverWord(rect, font)) {
						font.setColor(Color.BLUE.r, Color.BLUE.g, Color.BLUE.b, fadein);
						mouseOverElement = entry.getKey();
					} else {
						font.setColor(Color.CYAN);
						font.setColor(Color.CYAN.r, Color.CYAN.g, Color.CYAN.b, fadein);
					}
					
					anyClickable = true;
				}
				
				font.draw(batch, entry.getKey().replace("_", " "), rect.x+x, rect.y+y);
			}
		}
		
		if(!anyClickable) {
			Color c = font.getColor();
			font.setColor(instructionsColor.r, instructionsColor.g, instructionsColor.b, fadein);
			font.draw(batch, instructions, x + (width * 0.5f) - (font.getBounds(instructions).width * 0.5f), y + font.getBounds(instructions).height + Gdx.graphics.getHeight() * 0.05f);
			font.setColor(c);
		}
		
		lastMessage = text;
		font.setScale(scaleX, scaleY);
	}

	protected boolean mouseOverWord(Rectangle rect, BitmapFont font) {
		int mouseX = Gdx.input.getX();
		int mouseY = Gdx.graphics.getHeight() + ((int)font.getLineHeight()) - Gdx.input.getY();
		
		rect.x += x;
		rect.y += y;
		
		boolean result = rect.contains(mouseX, mouseY);
		
		rect.x -= x;
		rect.y -= y;
		
		return result;
	}
	
	protected boolean xyOverWord(Rectangle rect, int x, int y) {
		return rect.contains(x, y);
	}
	
	protected void addWordToRender(String word, WordRectangle position) {
		if(renderWords.containsKey(word)) {
			renderWords.get(word).push(position);
		} else {
			renderWords.put(word, new LinkedList<WordRectangle>());
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
		
		if(GameplayScreen.instance != null && GameplayScreen.instance.just_interact > 0 && new Rectangle(x, y, width, height).contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
			GameplayScreen.instance.just_interact = 0;
			return true;
		}
		
		
		return false;
	}

	public boolean mouseOverElement() {
		return wordClickListeners.containsKey(mouseOverElement);
	}

	public void handleClosed() {
		this.mouseOverElement = "";
	}
	
	@SuppressWarnings("serial")
	protected class WordRectangle extends Rectangle {
		public Color color; 
		
		public WordRectangle(float x, float y, float width, float height) {
			super(x, y, width, height);
		}
		
		public WordRectangle withColor(Color color) {
			this.color = color;
			return this;
		}
	}
	
	public Color hex2Rgb(String colorStr) {
	    return new Color(Integer.valueOf( colorStr.substring( 1, 3 ), 16 ), Integer.valueOf( colorStr.substring( 3, 5 ), 16 ), Integer.valueOf( colorStr.substring( 5, 7 ), 16 ), 1f );
	}
}
