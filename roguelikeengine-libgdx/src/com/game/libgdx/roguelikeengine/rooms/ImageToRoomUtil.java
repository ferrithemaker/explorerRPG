package com.game.libgdx.roguelikeengine.rooms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;

public class ImageToRoomUtil {
	public static int[][] getRoomData(String file) {
		Pixmap map = new Pixmap(Gdx.files.internal(file));

		int w = map.getWidth();
		int h = map.getHeight();
		
		int[][] result = new int[w][h];
		
		for(int i = 0; i < w; ++i) {
			for(int j = 0; j < h; ++j) {
				result[i][j] = map.getPixel(i, j);
			}
		}
		
		return result;
	}
}
