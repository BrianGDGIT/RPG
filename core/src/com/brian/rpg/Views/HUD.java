package com.brian.rpg.Views;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.brian.rpg.Model.Player;


public class HUD extends Stage {
    public Stage stage;
    private Player player;
    private PlayScreen screen;

    Float timeSinceLastClick = 0f;
    Boolean buttonClicked = false;

    public Button characterSheetButton;
    public Button spellbookButton;
    public Button activeSpellButton;


    Image image;
    Image spellbookImage;
    Image magicMissileImage;
    Image fireBallImage;
    Texture texture;
    Texture spellBookTexture;
    Texture magicMissileTexture;
    Texture fireBallTexture;

    public HUD(PlayScreen screen){
        this.screen = screen;
        this.player = screen.getPlayer();

        //Character Sheet Button
        texture = new Texture(Gdx.files.internal("GUI/robe.png"));
        image = new Image(texture);
        characterSheetButton = new Button(image.getDrawable());
        characterSheetButton.setPosition(1800, 50);
        //Set button position on Android. It's different for some reason even though I'm using the same viewPort???
        if(Gdx.app.getType() == Application.ApplicationType.Android){
            characterSheetButton.setPosition(2500, 50);
            characterSheetButton.setSize(100, 100);
        }

        //SpellBook button
        spellBookTexture = screen.getGameManager().get("GUI/spell-book.png", Texture.class);
        spellbookImage = new Image(spellBookTexture);
        spellbookButton = new Button(spellbookImage.getDrawable());
        spellbookButton.setPosition(1700, 50);
        if(Gdx.app.getType() == Application.ApplicationType.Android){
            spellbookButton.setPosition( 2300, 50);
            spellbookButton.setSize(100, 100);
        }

        //Magic Missile
        magicMissileTexture = screen.getGameManager().get("GUI/magicmissile.png", Texture.class);
        magicMissileImage = new Image(magicMissileTexture);

        //Fireball
        fireBallTexture = screen.getGameManager().get("GUI/fireball.png", Texture.class);
        fireBallImage = new Image(fireBallTexture);

        activeSpellButton = new Button(magicMissileImage.getDrawable());
        activeSpellButton.setPosition(1600, 50);
        if(Gdx.app.getType() == Application.ApplicationType.Android){
            activeSpellButton.setPosition(2100, 50);
            activeSpellButton.setSize(100, 100);
        }


        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        stage.addActor(characterSheetButton);
        stage.addActor(spellbookButton);
        stage.addActor(activeSpellButton);
    }

    public void checkHudClicks(){

        timeSinceLastClick += Gdx.graphics.getDeltaTime();

        //Character sheet button
        if(!player.inventoryDisplayed && characterSheetButton.isPressed() && timeSinceLastClick > 0.5){
            player.inventoryDisplayed = true;
            timeSinceLastClick = 0f;
        }else if(player.inventoryDisplayed && characterSheetButton.isPressed() && timeSinceLastClick > 0.5){
            player.inventoryDisplayed = false;
            timeSinceLastClick = 0f;
        }

        //Spellbook button
        if(!player.spellbookDisplayed && spellbookButton.isPressed() && timeSinceLastClick > 0.5){
            player.spellbookDisplayed = true;
            timeSinceLastClick = 0f;
        }else if(player.spellbookDisplayed && spellbookButton.isPressed() && timeSinceLastClick > 0.5){
            player.spellbookDisplayed = false;
            timeSinceLastClick = 0f;
        }

        //ActiveSpell button
        if(activeSpellButton.isPressed() && timeSinceLastClick > 0.5){
            //Sets active spell to spellbook index of activeSpell + 1

//            try{
//                player.activeSpell = player.getSpellBook().get(player.getSpellBook().indexOf(player.activeSpell + 1));
//            }catch(ArrayIndexOutOfBoundsException e){
//                player.activeSpell = player.getSpellBook().get(0);
//            }


            if(player.getSpellBook().contains("Fireball")){;
                player.activeSpell = "Fireball";
                Button.ButtonStyle style = activeSpellButton.getStyle();
                style.up = fireBallImage.getDrawable();
            }
        }
    }

}
