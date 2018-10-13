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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.brian.rpg.Model.Player;
import com.brian.rpg.RPG;

import static com.badlogic.gdx.Input.Keys.I;

public class InventoryScreen {
    private RPG game;

    public Stage stage;
    private Viewport viewport;
    private Player player;

    Label levelLabel;

    public InventoryScreen(RPG game, Player player, SpriteBatch batch){
        this.game = game;

        viewport = new FitViewport(800, 600, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table table = new Table();
        table.setFillParent(true);
        table.top();

        stage.addActor(table);

        levelLabel = new Label("Level: " + player.getLevel(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(levelLabel).expandX();
    }

}
