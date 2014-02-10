package com.game.libgdx.roguelikeengine.rooms;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.game.libgdx.roguelikeengine.GameplayScreen;
import com.game.libgdx.roguelikeengine.Map;

public class LargeRoom extends Room {
	public LargeRoom() {
		super("Large Complex", ImageToRoomUtil.getRoomData("rooms/large_room.png"), "rooms/chapel_floor.png", "rooms/chapel_wall.png", "rooms/chapel_wall_top.png",
				new Decoration(new Sprite(new Texture("greatShield.png")), Color.RED).asWall(),
				new Decoration(new Sprite(new Texture("decor/brick_breakup.png")), Color.GREEN).asChance(0.5).asFloor(),
				new Decoration(new Sprite(new Texture("decor/plant.png")), Color.GRAY).asChance(.7));
	}
	
	@Override
	public void onPlaced(Map map, int column, int row) {
		int myLayer = map.getlayer();
		int basementLayer = myLayer + 1;
		
		Map basementMap = GameplayScreen.instance.getmaplayer(basementLayer);
		if(basementMap != null) {
			int ac = column + 11;
			int ar = row + 26;
			
			Room basement = new LargeRoomBasement();
			basement.tryPlace(basementMap, column + 10, row + 12);

			GameplayScreen.instance.createDirectAccessPoint(myLayer, ac, ar);
		}
	}

}
