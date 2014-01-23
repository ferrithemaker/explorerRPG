package com.game.libgdx.roguelikeengine.rooms;

import com.game.libgdx.roguelikeengine.GameplayScreen;
import com.game.libgdx.roguelikeengine.Map;
import com.game.libgdx.roguelikeengine.WordClickAction;

public class Chapel extends Room {
	public Chapel() {
		super("chapel", ImageToRoomUtil.getRoomData("rooms/chapel.png"), "rooms/chapel_floor.png", "rooms/chapel_wall.png", "rooms/chapel_wall_top.png");
	}

	@Override
	public void onPlaced(Map map, int column, int row) {
		GameplayScreen.instance.createpriest(column + 10, row + 1, "This is a temple.");
		
		GameplayScreen.instance.getScreentext().addWordClickListener("temple.", new WordClickAction() {
			@Override
			public void onClicked(String word) {
				GameplayScreen.instance.alert("Go inside to gain knowledge and health.");
			}
		});
	}
}
