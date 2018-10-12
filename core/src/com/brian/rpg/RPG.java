package com.brian.rpg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brian.rpg.Views.MainMenuScreen;
import java.*;

public class RPG extends Game {
	public static final int V_WIDTH = 1920;
	public static final int V_HEIGHT = 1080;

	public SpriteBatch batch;
	public BitmapFont font;

	private AssetManager manager;

	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("Sounds/magic1.wav", Sound.class);
		manager.finishLoading();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public AssetManager getManager(){return manager;}

	public void setupScreenRender(){

	}
}
