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
    private Sprite blockedtile,freetile,rocks_img,fire_img,boulder_img,bones_img,cross_img,freedungeontile,door_img,web_img,altar_img,hole_img;
    private Sprite waterUL_img,waterUR_img, waterDL_img, waterDR_img, water_img;
    private Sprite upperwall_img,frontwall_img;
    private Sprite stair_img;
    private Sprite statue1_img,statue2_img,statue3_img;
    private Sprite rip_img,tree_img,cofin_img;
    private int firstXtile; // defines current section of the map that is shown on screen
	private int firstYtile; // defines current section of the map that is shown on screen
	private ArrayList<AccessToLayer> accesspoints;
	private boolean isDungeon;

	
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
		waterDR_img = new Sprite(new Texture(Gdx.files.internal("waterDR.png")));
		waterDL_img = new Sprite(new Texture(Gdx.files.internal("waterDL.png")));
		waterUR_img= new Sprite(new Texture(Gdx.files.internal("waterUR.png")));
		waterUL_img = new Sprite(new Texture(Gdx.files.internal("waterUL.png")));
		upperwall_img= new Sprite(new Texture(Gdx.files.internal("upperwall.png")));
		frontwall_img = new Sprite(new Texture(Gdx.files.internal("frontwall.png")));
		cofin_img = new Sprite(new Texture(Gdx.files.internal("cofin.png")));
		rip_img= new Sprite(new Texture(Gdx.files.internal("rip.png")));
		tree_img= new Sprite(new Texture(Gdx.files.internal("tree.png")));
		stair_img= new Sprite(new Texture(Gdx.files.internal("stair.png")));
		statue1_img= new Sprite(new Texture(Gdx.files.internal("statue.png")));
		statue2_img= new Sprite(new Texture(Gdx.files.internal("statue2.png")));
		statue3_img= new Sprite(new Texture(Gdx.files.internal("statue3.png")));
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
		        		tilelayout[xpos][ypos]= new Tile(false);
		        	}
		        }
			
			
			// create lakes
			for (int num=0; num<WrapperEngine.NUMBER_OF_LAKES;num++) {
				createrandomlake();
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
			//createcementery();
		}
		
		public void createAccessDungeon(int inx,int iny, int outx, int outy,int layerout) {
			// data must be prevalidated, this method don't check it
			accesspoints.add(new AccessToLayer(inx,iny,outx,outy,layerout));
			tilelayout[inx][iny].settileimage(stair_img);
		}
		public void createAccessOutside(int inx,int iny, int outx, int outy,int layerout) {
			// data must be prevalidated, this method don't check it
			accesspoints.add(new AccessToLayer(inx,iny,outx,outy,layerout));
			tilelayout[inx][iny].settileimage(stair_img);
		}
		
		public ArrayList<AccessToLayer> getLayerAccess() {
			return accesspoints;
		}
		
		public void createrandomdungeon() {
			// fill with freetiles
			 for (int xpos=0;xpos<WrapperEngine.TOTAL_X_TILES;xpos++) {
		        	for (int ypos=0;ypos<WrapperEngine.TOTAL_Y_TILES;ypos++) {
		        		tilelayout[xpos][ypos]= new Tile(false);
		        	}
		        }
			// create random walls
			for (int num=0; num<(int)(WrapperEngine.NUMBER_OF_WALLS_DUNGEON/2);num++) {
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
			int lenght = randomGenerator.nextInt(WrapperEngine.MAX_WALL_LENGTH-2)+2;
			int start = randomGenerator.nextInt(WrapperEngine.TOTAL_X_TILES-WrapperEngine.MAX_WALL_LENGTH);
			int height = randomGenerator.nextInt(WrapperEngine.TOTAL_Y_TILES-1);
			if (areaisempty(start,height,lenght,2)) {
				for (int xpos=start;xpos<start+lenght;xpos++) {
					tilelayout[xpos][height+1].block();
					tilelayout[xpos][height+1].settileimage(upperwall_img);
					tilelayout[xpos][height].block();
					tilelayout[xpos][height].settileimage(frontwall_img);
				}
			}
		}
		
		public void createrandomvwall() {
			Random randomGenerator = new Random();
			int lenght = randomGenerator.nextInt(WrapperEngine.MAX_WALL_LENGTH-2)+2;
			int start = randomGenerator.nextInt(WrapperEngine.TOTAL_Y_TILES-WrapperEngine.MAX_WALL_LENGTH);
			int width = randomGenerator.nextInt(WrapperEngine.TOTAL_X_TILES);
			if (areaisempty(width,start,1,lenght)) {
				for (int ypos=start;ypos<start+lenght;ypos++) {
					tilelayout[width][ypos].block();
					tilelayout[width][ypos].settileimage(upperwall_img);	
				}
			tilelayout[width][start].settileimage(frontwall_img);
			tilelayout[width][start].block();
			}
		}
		public void createrandomlake() {
			Random randomGenerator = new Random();
			int lenght = (randomGenerator.nextInt(WrapperEngine.MAX_LAKE_SIZE-2)+2);
			int start_x = randomGenerator.nextInt(WrapperEngine.TOTAL_X_TILES-WrapperEngine.MAX_LAKE_SIZE);
			int start_y = randomGenerator.nextInt(WrapperEngine.TOTAL_Y_TILES-WrapperEngine.MAX_LAKE_SIZE);
			int width = (randomGenerator.nextInt(WrapperEngine.MAX_LAKE_SIZE-2)+2);
			if (areaisempty(start_x,start_y,lenght,width)) {
				for (int xpos=start_x;xpos<start_x+lenght;xpos++) {
					for (int ypos=start_y;ypos<start_y+width;ypos++) {
						tilelayout[xpos][ypos].block();
						tilelayout[xpos][ypos].settileimage(water_img);
					}
				}
			// lake corners
			tilelayout[start_x][start_y].settileimage(waterDL_img);
			tilelayout[start_x+lenght-1][start_y].settileimage(waterDR_img);
			tilelayout[start_x][start_y+width-1].settileimage(waterUL_img);
			tilelayout[start_x+lenght-1][start_y+width-1].settileimage(waterUR_img);
			}
		}
		
		public void createblockingelement() {
			Random randomGenerator = new Random();
			int x = randomGenerator.nextInt(WrapperEngine.TOTAL_X_TILES);
			int y = randomGenerator.nextInt(WrapperEngine.TOTAL_Y_TILES);
			int element= randomGenerator.nextInt(8);
			if (!tilelayout[x][y].isbloqued()) {
				tilelayout[x][y].block();
				if (element==0) {
					tilelayout[x][y].settileimage(rocks_img);
				}
				if (element==1) {
					tilelayout[x][y].settileimage(boulder_img);
				}
				if (element==2) {
					tilelayout[x][y].settileimage(bones_img);
				}
				if (element==3) {
					tilelayout[x][y].settileimage(rip_img);
				}
				if (element==4) {
					tilelayout[x][y].settileimage(cofin_img);
				}
				if (element==5) {
					tilelayout[x][y].settileimage(tree_img);
				}
				if (element==6) {
					tilelayout[x][y].settileimage(fire_img);
				}
				if (element==7) {
					tilelayout[x][y].settileimage(statue1_img);
				}
				/*
				if (element==8) {
					tilelayout[x][y].settileimage(statue2_img);
				}
				if (element==9) {
					tilelayout[x][y].settileimage(statue3_img);
				}*/
			}
		}
		public void createdungeonblockingelement() {
			Random randomGenerator = new Random();
			int x = randomGenerator.nextInt(WrapperEngine.TOTAL_X_TILES);
			int y = randomGenerator.nextInt(WrapperEngine.TOTAL_Y_TILES);
			int element= randomGenerator.nextInt(7);
			if (!tilelayout[x][y].isbloqued()) {
				tilelayout[x][y].block();
				if (element==0) {
					tilelayout[x][y].settileimage(altar_img);
				}
				if (element==1) {
					tilelayout[x][y].settileimage(hole_img);
				}
				if (element==2) {
					tilelayout[x][y].settileimage(web_img);
				}
				if (element==3) {
					tilelayout[x][y].settileimage(cofin_img);
				}
				if (element==4) {
					tilelayout[x][y].settileimage(rip_img);
				}
				if (element==5) {
					tilelayout[x][y].settileimage(tree_img);
				}
				if (element==6) {
					tilelayout[x][y].settileimage(statue1_img);
				}
				/*
				if (element==7) {
					tilelayout[x][y].settileimage(statue2_img);
				}
				if (element==8) {
					tilelayout[x][y].settileimage(statue3_img);
				}*/
			}
		}
		
		public boolean areaisempty(int xorig,int yorig,int lenght, int width) {
			boolean result=true;
			for (int x=xorig;x<xorig+lenght;x++) {
				for (int y=yorig;y<yorig+width;y++) {
					if (tilelayout[x][y].isbloqued()) { result=false; }  
				}
			}
			return result;
		}
		
		public void blocktile(int x, int y) {
			tilelayout[x][y].block();
		}
		
		public void unblocktile(int x, int y) {
			tilelayout[x][y].unblock();
		}
		
		public Tile getfirstavailabletilearround(int x,int y) {
			Tile freetile = null;
			if (!tilelayout[x-1][y].isbloqued() && x>0) {
				freetile=tilelayout[x-1][y];
			}
			if (!tilelayout[x+1][y].isbloqued() && x<WrapperEngine.TOTAL_X_TILES) {
				freetile=tilelayout[x+1][y];
			}
			if (!tilelayout[x][y+1].isbloqued() && y<WrapperEngine.TOTAL_Y_TILES) {
				freetile=tilelayout[x][y+1];
			}
			if (!tilelayout[x][y-1].isbloqued() && y>0) {
				freetile=tilelayout[x][y-1];
			}
			return freetile;
			
		}
		
		public void createcementery() {
			Random randomGenerator = new Random();
			for (int x=0;x<11;x=x+2) {
				for (int y=0;y<11;y=y+2) {
					int chances = randomGenerator.nextInt(2);
					int image = randomGenerator.nextInt(2);
					if (chances==0) {
						tilelayout[x][y].block();
						if (image==0) {
							tilelayout[x][y].settileimage(cross_img);
						}
						if (image==1) {
							tilelayout[x][y].settileimage(rip_img);
						}
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
		public boolean isdungeon() {
			return isDungeon;
		}
		public Sprite getfreetile() {
			return freetile;
		}
		public Sprite getfreedungeontile() {
			return freedungeontile;
		}
		
				
		// sets / updates
		public void setfirstxtile(int value) {
			firstXtile=value;
		}
		public void setfirstytile(int value) {
			firstYtile=value;
		}
		public void isdungeon(boolean value) {
			isDungeon=value;
		}
		
		public boolean istileempty(int x, int y) {
			try {
				return tilelayout[x][y].isempty();
			} catch(ArrayIndexOutOfBoundsException e) {
				return false;
			}
		}
		
}