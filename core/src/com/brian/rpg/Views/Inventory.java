package com.brian.rpg.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.brian.rpg.Model.Player;
import com.brian.rpg.RPG;

public class Inventory {
    private RPG game;
    private PlayScreen screen;
    private Player player;

    public Stage stage;
    private Viewport viewport;
    Skin skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));



    Texture inventoryTexture;
    Image inventoryImage;

    public Inventory(RPG game, PlayScreen screen, Player player, SpriteBatch batch){
        this.game = game;
        this.screen = screen;
        this.player = player;

        viewport = new FitViewport(800, 600, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        //Inventory Image
        inventoryTexture = (screen.getGameManager().get("GUI/Inventory.jpg", Texture.class));
        inventoryImage = new Image(inventoryTexture);
        inventoryImage.setScale(0.5f, 0.5f);

        stage.addActor(inventoryImage);
    }

    public void showInventory(){

    }

}


