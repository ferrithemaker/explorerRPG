package com.game.libgdx.roguelikeengine;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "roguelikeengine-libgdx";
		cfg.useGL20 = false;
		cfg.width = GameEngine.WINDOWWITH;
		cfg.height = GameEngine.WINDOWHEIGHT;
		
		new LwjglApplication(new Explorer_libgdx(), cfg);
	}
}
