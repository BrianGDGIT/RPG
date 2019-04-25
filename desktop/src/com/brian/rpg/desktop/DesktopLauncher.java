package com.brian.rpg.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.brian.rpg.RPG;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Wizard";
		config.width = 1920;
		config.height = 1080;
		new LwjglApplication(new RPG(), config);
	}
}
