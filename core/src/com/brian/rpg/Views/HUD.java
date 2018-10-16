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
import com.brian.rpg.RPG;

public class HUD extends Stage {
    public Stage stage;
    private Player player;
    private PlayScreen screen;

    Float timeSinceLastClick = 0f;
    Boolean buttonClicked = false;

    public Button characterSheetButton;
    public Button spellbookButton;


    Image image;
    Image spellbookImage;
    Texture texture;
    Texture spellBookTexture;
    Drawable drawable;

    public HUD(PlayScreen screen){
        this.screen = screen;
        this.player = screen.getPlayer();
        texture = new Texture(Gdx.files.internal("GUI/robe.png"));
        spellBookTexture = screen.getGameManager().get("GUI/spell-book.png", Texture.class);
        image = new Image(texture);
        spellbookImage = new Image(spellBookTexture);

        characterSheetButton = new Button(image.getDrawable());
        characterSheetButton.setPosition(1800, 50);
        //Set button position on Android. It's different for some reason even though I'm using the same viewPort???
        if(Gdx.app.getType() == Application.ApplicationType.Android){
            characterSheetButton.setPosition(2500, 50);
            characterSheetButton.setSize(100, 100);
        }

        spellbookButton = new Button(spellbookImage.getDrawable());
        spellbookButton.setPosition(1700, 50);
        if(Gdx.app.getType() == Application.ApplicationType.Android){
            spellbookButton.setPosition( 2400, 50);
            spellbookButton.setSize(100, 100);
        }

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        stage.addActor(characterSheetButton);
        stage.addActor(spellbookButton);
    }

    public void checkHudClicks(){

        timeSinceLastClick += Gdx.graphics.getDeltaTime();

        if(!player.inventoryDisplayed && characterSheetButton.isPressed() && timeSinceLastClick > 1){
            player.inventoryDisplayed = true;
            timeSinceLastClick = 0f;
        }else if(player.inventoryDisplayed && characterSheetButton.isPressed() && timeSinceLastClick > 1){
            player.inventoryDisplayed = false;
            timeSinceLastClick = 0f;
        }

        if(!player.spellbookDisplayed && spellbookButton.isPressed() && timeSinceLastClick > 1){
            player.spellbookDisplayed = true;
            timeSinceLastClick = 0f;
        }else if(player.spellbookDisplayed && spellbookButton.isPressed() && timeSinceLastClick > 1){
            player.spellbookDisplayed = false;
            timeSinceLastClick = 0f;
        }
    }

}
