package com.brian.rpg.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.brian.rpg.Model.Player;
import com.brian.rpg.RPG;

import static com.badlogic.gdx.Input.Keys.I;

public class InventoryScreen {
    private RPG game;

    public Stage stage;
    private Viewport viewport;
    Skin skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));

    private Player player;

    Label levelLabel;
    TextArea textArea;

    public InventoryScreen(RPG game, Player player, SpriteBatch batch){
        this.game = game;
        this.player = player;

        viewport = new FitViewport(800, 600, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        textArea = new TextArea("Class: " + player.getGameClass() + '\n' + "Level: " + player.getLevel() + '\n' + "Experience: " + player.getExperience() + '\n' + "Total Kills: " + player.getKills(), skin);
        textArea.setSize(250, 250);
        textArea.setPosition( 300, 350);

        stage.addActor(textArea);
    }

    public void update(){
        textArea = new TextArea("Class: " + player.getGameClass() + '\n' + "Level: " + player.getLevel() + '\n' + "Experience: " + player.getExperience() + '\n' + "Total Kills: " + player.getKills(), skin);
        textArea.setSize(250, 250);
        textArea.setPosition( 300, 350);
        stage.addActor(textArea);
    }

}
