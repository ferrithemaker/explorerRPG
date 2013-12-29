package com.game.libgdx.roguelikeengine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class BackgroundMusic {
	
	static Music musicoutside;
	static Music musicdungeon;
	static Music musicfight;
	static Boolean playingfight;
	static void  setup() {
		musicoutside = Gdx.audio.newMusic(Gdx.files.internal("music/FromHere.ogg"));
		musicdungeon = Gdx.audio.newMusic(Gdx.files.internal("music/Deeper.ogg"));
		musicfight = Gdx.audio.newMusic(Gdx.files.internal("music/Defiance.ogg"));
		playingfight=false;
	}
	static void startoutside() {
		musicoutside.setLooping(true);
		musicoutside.setVolume(0.3f);
		musicoutside.play();
	}
	static void startdungeon() {
		musicdungeon.setLooping(true);
		musicdungeon.setVolume(0.3f);
		musicdungeon.play();
	}
	static void stopoutside() {
		musicoutside.stop();
	}
	static void stopdungeon() {
		musicdungeon.stop();
	}
	static void startfight() {
		musicfight.setLooping(true);
		musicfight.setVolume(0.3f);
		musicfight.play();
	}
	static void stopfight() {
		musicfight.stop();
	}
	static void stopall() {
		stopfight();
		stopdungeon();
		stopoutside();
	}
	
}
