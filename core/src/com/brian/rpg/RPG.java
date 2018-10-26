package com.brian.rpg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brian.rpg.Views.MainMenuScreen;
import com.brian.rpg.Views.PlayScreen;

import java.*;

public class RPG extends Game {
	public static final int V_WIDTH = 1920;
	public static final int V_HEIGHT = 1080;

	public static final short PROJECTILE_BIT = 2;
	public static final short WALL_BIT = 4;
	public static final short CREATURE_BIT = 6;
	public static final short PLAYER_BIT = 8;
	public static final short ITEM_BIT = 16;
	public static final short CHEST_BIT = 32;
	public static final short AREATRANSITION_BIT = 64;

	public SpriteBatch batch;

	private AssetManager manager;

	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("Sounds/magic1.wav", Sound.class);
		manager.load("Sounds/blessing.wav", Sound.class);
		manager.load("Sounds/Fireball.wav", Sound.class);
		manager.load("Sounds/Explosion.wav", Sound.class);
		manager.load("Sounds/Bone Crushing.wav", Sound.class);
		manager.load("Sounds/orc-34-hit.wav", Sound.class);
		manager.load("Sounds/orc-32-death.wav", Sound.class);
		manager.load("Sounds/zombie-grunt.wav", Sound.class);
		manager.load("Sounds/HotSizzling.wav", Sound.class);
		manager.load("Sounds/zombie-moan.wav", Music.class);
		manager.load("sprites/vortex_spritesheet.png", Texture.class);
		manager.load("sprites/11_fire_spritesheet.png", Texture.class);
		manager.load("sprites/16_sunburn_spritesheet.png", Texture.class);
		manager.load("sprites/17_felspell_spritesheet.png", Texture.class);
		manager.load("GUI/magicmissile.png", Texture.class);
		manager.load("GUI/fireball.png", Texture.class);
		manager.load("GUI/acidblast.png", Texture.class);
		manager.load("GUI/spell-book.png", Texture.class);
		manager.load("GUI/Inventory.jpg", Texture.class);
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
		manager.dispose();
	}

	public AssetManager getManager(){return manager;}

}
