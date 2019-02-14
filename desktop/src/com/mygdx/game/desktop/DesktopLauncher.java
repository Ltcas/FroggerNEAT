package com.mygdx.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Kitten;

/**
 * Launches the game of "Kittener", which is based upon the classic arcade game "Frogger".
 * @author Chance Simmons and Brandon Townsend
 * @version 14 February 2019
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		// Initial Window title, width, height, and icon.
		config.title = "Kittener";
		config.width = 480;
		config.height = 384;
		config.addIcon("core/assets/cat_front.png", Files.FileType.Internal);

		// Creation of the game logic object.
		new LwjglApplication(new Kitten(config.width,config.height), config);
	}
}
