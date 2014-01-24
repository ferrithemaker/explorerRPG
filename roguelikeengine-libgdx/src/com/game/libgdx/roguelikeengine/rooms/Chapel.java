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
		GameplayScreen.instance.createpriest(column + 2, row + 16, "Some areas can only be accessed by a key");
		
		GameplayScreen.instance.createchest(column + 2, row + 14);
		
		GameplayScreen.instance.createchest(column + 19, row + 15);
		GameplayScreen.instance.createchest(column + 20, row + 15);
		GameplayScreen.instance.createchest(column + 21, row + 15);
		
		GameplayScreen.instance.getScreentext().addWordClickListener("temple.", new WordClickAction() {
			@Override
			public void onClicked(String word) {
				GameplayScreen.instance.alert("Go inside to gain knowledge and health.");
			}
		});
		
		GameplayScreen.instance.getScreentext().addWordClickListener("key", new WordClickAction() {
			@Override
			public void onClicked(String word) {
				GameplayScreen.instance.alert("Keys can be used to unlock doors and treasure chests. They can be found by defeating bad guys.");
			}
		});
	}
}
