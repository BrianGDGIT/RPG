package com.brian.rpg;

import android.os.Bundle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.brian.rpg.RPG;
import de.golfgl.gdxgamesvcs.GpgsClient;

public class AndroidLauncher extends AndroidApplication {
	private GpgsClient gpgsClient;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = true;
		config.useCompass = false;
		//Implemented Google play library
		RPG game = new RPG();
		game.gsClient = new GpgsClient().initialize(this, false);


		initialize(game, config);
	}
}
