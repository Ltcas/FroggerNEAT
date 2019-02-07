package com.mygdx.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Frogger;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Kittener";
		config.width = 480;
		config.height = 384;
		config.addIcon("core/assets/cat_front.png", Files.FileType.Internal7);
		new LwjglApplication(new Frogger(), config);
	}
}
