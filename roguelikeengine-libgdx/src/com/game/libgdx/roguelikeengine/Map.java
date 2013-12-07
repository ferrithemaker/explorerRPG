package com.game.libgdx.roguelikeengine;




import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;




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

public class Map {
	private Tile tilelayout[][];
    private Sprite blockedtile,freetile,water_img,rocks_img,fire_img,boulder_img,bones_img,cross_img,freedungeontile,door_img,web_img,altar_img,hole_img;
    private int firstXtile; // defines current section of the map that is shown on screen
	private int firstYtile; // defines current section of the map that is shown on screen
	private ArrayList<AccessToLayer> accesspoints;

	
	public Map() {
		
		blockedtile = new Sprite(new Texture(Gdx.files.internal("wall.png")));
		freetile= new Sprite(new Texture(Gdx.files.internal("herba.png")));
		freedungeontile= new Sprite(new Texture(Gdx.files.internal("dungeon.png")));
		water_img= new Sprite(new Texture(Gdx.files.internal("water.png")));
		door_img= new Sprite(new Texture(Gdx.files.internal("door.png")));
		web_img= new Sprite(new Texture(Gdx.files.internal("web.png")));
		altar_img= new Sprite(new Texture(Gdx.files.internal("altar.png")));
		hole_img= new Sprite(new Texture(Gdx.files.internal("hole.png")));
		rocks_img= new Sprite(new Texture(Gdx.files.internal("rocks.png")));
		bones_img = new Sprite(new Texture(Gdx.files.internal("bones.png")));
		boulder_img = new Sprite(new Texture(Gdx.files.internal("boulder.png")));
		fire_img= new Sprite(new Texture(Gdx.files.internal("fire.png")));
		cross_img = new Sprite(new Texture(Gdx.files.internal("graveyard.png")));
		// first tile position must be multiple of tile_size
		firstXtile=0;
		firstYtile=0;
		
		// create tile layout
        tilelayout = new Tile[WrapperEngine.TOTAL_X_TILES][WrapperEngine.TOTAL_Y_TILES];
        // create access point arraylist
        accesspoints=new ArrayList<AccessToLayer>();
	}
		// **** BEGIN MAP CREATION
		public void createrandommap() {
			// fill with freetiles
			 for (int xpos=0;xpos<WrapperEngine.TOTAL_X_TILES;xpos++) {
		        	for (int ypos=0;ypos<WrapperEngine.TOTAL_Y_TILES;ypos++) {
		        		tilelayout[xpos][ypos]= new Tile(false,freetile);
		        	}
		        }
			// create random walls
			for (int num=0; num<(int)(WrapperEngine.NUMBER_OF_WALLS/2);num++) {
				createrandomvwall();
				createrandomhwall();
			}
			// create individual elements
			for (int num=0; num<WrapperEngine.NUMBER_OF_BLOCKING_OBJECTS;num++) {
				createblockingelement();
			}
			
			// create lakes
			for (int num=0; num<WrapperEngine.NUMBER_OF_LAKES;num++) {
				createrandomlake();
			}
			createcementery();
		}
		
		public void createAccess(int inx,int iny, int outx, int outy,int layerout) {
			// data must be prevalidated, this method don't check it
			accesspoints.add(new AccessToLayer(inx,iny,outx,outy,layerout));
			tilelayout[inx][iny].updatetileimage(door_img);
		}
		
		public ArrayList<AccessToLayer> getAPs() {
			return accesspoints;
		}
		
		public void createrandomdungeon() {
			// fill with freetiles
			 for (int xpos=0;xpos<WrapperEngine.TOTAL_X_TILES;xpos++) {
		        	for (int ypos=0;ypos<WrapperEngine.TOTAL_Y_TILES;ypos++) {
		        		tilelayout[xpos][ypos]= new Tile(false,freedungeontile);
		        	}
		        }
			// create random walls
			for (int num=0; num<(int)(WrapperEngine.NUMBER_OF_WALLS);num++) {
				createrandomvwall();
				createrandomhwall();
				createrandomvwall();
				createrandomhwall();
				createrandomvwall();
				createrandomhwall();
			}
			// create individual elements
			for (int num=0; num<WrapperEngine.NUMBER_OF_BLOCKING_OBJECTS;num++) {
				createdungeonblockingelement();
			}
		}
		public void createrandomhwall() {
			Random randomGenerator = new Random();
			int lenght = randomGenerator.nextInt(WrapperEngine.MAX_WALL_LENGTH);
			int start = randomGenerator.nextInt(WrapperEngine.TOTAL_X_TILES-WrapperEngine.MAX_WALL_LENGTH);
			int height = randomGenerator.nextInt(WrapperEngine.TOTAL_Y_TILES);
			for (int xpos=start;xpos<start+lenght;xpos++) {
	        	tilelayout[xpos][height].block();
	        	tilelayout[xpos][height].updatetileimage(blockedtile);	
	        }
		}
		public void createrandomvwall() {
			Random randomGenerator = new Random();
			int lenght = randomGenerator.nextInt(WrapperEngine.MAX_WALL_LENGTH);
			int start = randomGenerator.nextInt(WrapperEngine.TOTAL_Y_TILES-WrapperEngine.MAX_WALL_LENGTH);
			int width = randomGenerator.nextInt(WrapperEngine.TOTAL_X_TILES);
			for (int ypos=start;ypos<start+lenght;ypos++) {
	        	tilelayout[width][ypos].block();
	        	tilelayout[width][ypos].updatetileimage(blockedtile);	
	        }
			
		}
		public void createrandomlake() {
			Random randomGenerator = new Random();
			int lenght = randomGenerator.nextInt(WrapperEngine.MAX_LAKE_SIZE);
			int start_x = randomGenerator.nextInt(WrapperEngine.TOTAL_X_TILES-WrapperEngine.MAX_LAKE_SIZE);
			int start_y = randomGenerator.nextInt(WrapperEngine.TOTAL_Y_TILES-WrapperEngine.MAX_LAKE_SIZE);
			int width = randomGenerator.nextInt(WrapperEngine.MAX_LAKE_SIZE);
			for (int xpos=start_x;xpos<start_x+lenght;xpos++) {
				for (int ypos=start_y;ypos<start_y+width;ypos++) {
					tilelayout[xpos][ypos].block();
					tilelayout[xpos][ypos].updatetileimage(water_img);
				}
	        }
			
		}
		public void createblockingelement() {
			Random randomGenerator = new Random();
			int x = randomGenerator.nextInt(WrapperEngine.TOTAL_X_TILES);
			int y = randomGenerator.nextInt(WrapperEngine.TOTAL_Y_TILES);
			int element= randomGenerator.nextInt(4);
			tilelayout[x][y].block();
			if (element==0) {
				tilelayout[x][y].updatetileimage(rocks_img);
			}
			if (element==1) {
				tilelayout[x][y].updatetileimage(boulder_img);
			}
			if (element==2) {
				tilelayout[x][y].updatetileimage(bones_img);
			}
			if (element==3) {
				tilelayout[x][y].updatetileimage(fire_img);
			}
		}
		public void createdungeonblockingelement() {
			Random randomGenerator = new Random();
			int x = randomGenerator.nextInt(WrapperEngine.TOTAL_X_TILES);
			int y = randomGenerator.nextInt(WrapperEngine.TOTAL_Y_TILES);
			int element= randomGenerator.nextInt(3);
			tilelayout[x][y].block();
			if (element==0) {
				tilelayout[x][y].updatetileimage(altar_img);
			}
			if (element==1) {
				tilelayout[x][y].updatetileimage(hole_img);
			}
			if (element==2) {
				tilelayout[x][y].updatetileimage(web_img);
			}
		}
		public void createcementery() {
			Random randomGenerator = new Random();
			for (int x=0;x<11;x=x+2) {
				for (int y=0;y<11;y=y+2) {
					int chances = randomGenerator.nextInt(2);
					if (chances==0) {
						tilelayout[x][y].block();
						tilelayout[x][y].updatetileimage(cross_img);
					}
				}
			}
		}
		// **** END MAP CREATION
		// gets
		public Tile[][] gettiles() {
			return tilelayout;
		}
		public int getfirstxtile() {
			return firstXtile;
		}
		public int getfirstytile() {
			return firstYtile;
		}
		
				
		// sets / updates
		public void setfirstxtile(int value) {
			firstXtile=value;
		}
		public void setfirstytile(int value) {
			firstYtile=value;
		}
		
		public boolean istileempty(int x, int y) {
			try {
				return tilelayout[x][y].isempty();
			} catch(ArrayIndexOutOfBoundsException e) {
				return false;
			}
		}
		
}