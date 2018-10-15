package com.brian.rpg.Views;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.brian.rpg.Model.Player;

public class HUD extends Stage {
    public Stage stage;
    private Player player;
    private PlayScreen screen;

    Float timeSinceLastClick = 0f;
    Boolean buttonClicked = false;

    public Button button;
    Image image;
    Texture texture;
    Drawable drawable;

    public HUD(Player player){
        this.player = player;
        texture = new Texture(Gdx.files.internal("GUI/robe.png"));
        image = new Image(texture);

        button = new Button(image.getDrawable());
        button.setPosition(1800, 50);
        //Set button position on Android. It's different for some reason even though I'm using the same viewPort???
        if(Gdx.app.getType() == Application.ApplicationType.Android){
            button.setPosition(2500, 50);
            button.setSize(100, 100);
        }

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        stage.addActor(button);
    }

    public void checkHudClicks(){

        timeSinceLastClick += Gdx.graphics.getDeltaTime();

        if(!player.inventoryDisplayed && button.isPressed() && timeSinceLastClick > 1){
            player.inventoryDisplayed = true;
            System.out.println("Test");
            timeSinceLastClick = 0f;
        }else if(player.inventoryDisplayed && button.isPressed() && timeSinceLastClick > 1){
            player.inventoryDisplayed = false;
            timeSinceLastClick = 0f;
        }
    }

}
