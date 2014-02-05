package com.game.libgdx.roguelikeengine.rooms;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.game.libgdx.roguelikeengine.GameplayScreen;
import com.game.libgdx.roguelikeengine.Map;

public class CornerRoom extends Room {
	public CornerRoom() {
		super("House", ImageToRoomUtil.getRoomData("rooms/corner_room.png"), "rooms/chapel_floor.png", "rooms/chapel_wall.png", "rooms/chapel_wall_top.png",
				new Decoration(new Sprite(new Texture("greatShield.png")), Color.RED).asWall(),
				new Decoration(new Sprite(new Texture("decor/brick_breakup.png")), Color.GREEN).asChance(0.5).asFloor(),
				new Decoration(new Sprite(new Texture("decor/plant.png")), Color.GRAY).asChance(.7));
	}
	
	@Override
	public void onPlaced(Map map, int column, int row) {
		if(true) {
			// lock the room
			GameplayScreen.instance.createdoor(map, column+5,  row+23);
			GameplayScreen.instance.createdoor(map, column+1,  row+16);
			GameplayScreen.instance.createdoor(map, column+12,  row+14);
		}
	}

}
