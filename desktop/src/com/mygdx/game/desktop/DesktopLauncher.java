package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Frogger;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();


		config.title = "Kittener";
		config.width = 480;
		config.height = 384;

		//Gdx.audio.newAudioDevice(44100, false);

		new LwjglApplication(new Frogger(), config);
	}
}
