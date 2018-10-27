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
    public Button inventoryButton;
    public Button spellbookButton;
    public Button activeSpellButton;


    Image characterSheetimage;
    Image inventoryImage;
    Image spellbookImage;
    Image magicMissileImage;
    Image fireBallImage;
    Image acidBlastImage;
    Image horridWiltingImage;
    Texture characterSheettexture;
    Texture inventoryTexture;
    Texture spellBookTexture;
    Texture magicMissileTexture;
    Texture fireBallTexture;
    Texture acidBlastTexture;
    Texture horridWiltingTexture;

    public HUD(PlayScreen screen){
        this.screen = screen;
        this.player = screen.getPlayer();

        //Character Sheet Button
        characterSheettexture = screen.getGameManager().get("GUI/wizard-face.png", Texture.class);
        characterSheetimage = new Image(characterSheettexture);
        characterSheetButton = new Button(characterSheetimage.getDrawable());
        characterSheetButton.setPosition(1800, 50);
        //Set button position on Android. It's different for some reason even though I'm using the same viewPort???
        if(Gdx.app.getType() == Application.ApplicationType.Android){
            characterSheetButton.setPosition(2500, 50);
            characterSheetButton.setSize(100, 100);
        }

        //Inventory Button
        inventoryTexture = screen.getGameManager().get("GUI/robe.png", Texture.class);
        inventoryImage = new Image(inventoryTexture);
        inventoryButton = new Button(inventoryImage.getDrawable());
        inventoryButton.setPosition(1700, 50);
        //Set button position on Android. It's different for some reason even though I'm using the same viewPort???
        if(Gdx.app.getType() == Application.ApplicationType.Android){
            inventoryButton.setPosition(2300, 50);
            inventoryButton.setSize(100, 100);
        }

        //SpellBook button
        spellBookTexture = screen.getGameManager().get("GUI/spell-book.png", Texture.class);
        spellbookImage = new Image(spellBookTexture);
        spellbookButton = new Button(spellbookImage.getDrawable());
        spellbookButton.setPosition(1600, 50);
        if(Gdx.app.getType() == Application.ApplicationType.Android){
            spellbookButton.setPosition( 2100, 50);
            spellbookButton.setSize(100, 100);
        }

        //Magic Missile
        magicMissileTexture = screen.getGameManager().get("GUI/magicmissile.png", Texture.class);
        magicMissileImage = new Image(magicMissileTexture);

        //Fireball
        fireBallTexture = screen.getGameManager().get("GUI/fireball.png", Texture.class);
        fireBallImage = new Image(fireBallTexture);

        //Acid Blast
        acidBlastTexture = screen.getGameManager().get("GUI/acidblast.png", Texture.class);
        acidBlastImage = new Image(acidBlastTexture);

        //Horrid Wilting
        horridWiltingTexture = screen.getGameManager().get("GUI/horridwilting.png", Texture.class);
        horridWiltingImage = new Image(horridWiltingTexture);

        activeSpellButton = new Button(magicMissileImage.getDrawable());
        activeSpellButton.setPosition(1500, 50);
        if(Gdx.app.getType() == Application.ApplicationType.Android){
            activeSpellButton.setPosition(1900, 50);
            activeSpellButton.setSize(100, 100);
        }


        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        stage.addActor(characterSheetButton);
        stage.addActor(inventoryButton);
        stage.addActor(spellbookButton);
        stage.addActor(activeSpellButton);
    }

    public void checkHudClicks(){

        timeSinceLastClick += Gdx.graphics.getDeltaTime();

        //Character sheet button
        if(!player.characterScreenDisplayed && characterSheetButton.isPressed() && timeSinceLastClick > 0.5){
            player.characterScreenDisplayed = true;
            timeSinceLastClick = 0f;
        }else if(player.characterScreenDisplayed && characterSheetButton.isPressed() && timeSinceLastClick > 0.5){
            player.characterScreenDisplayed = false;
            timeSinceLastClick = 0f;
        }

        //Inventory button
        if(!player.inventoryDisplayed && inventoryButton.isPressed() && timeSinceLastClick > 0.5){
            player.inventoryDisplayed = true;
            timeSinceLastClick = 0f;
        }else if(player.inventoryDisplayed && inventoryButton.isPressed() && timeSinceLastClick > 0.5){
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
            equipNextSpell();
            timeSinceLastClick = 0f;
        }
    }

    public void equipNextSpell(){
        //Sets active spell to spellbook index of activeSpell + 1
        int currentSpellIndex = 0;


        //Find activeSpell index in player spellbook arraylist
        for(String tempSpell : player.getSpellBook()){
            if(player.activeSpell.equals(tempSpell)){
                currentSpellIndex = player.getSpellBook().indexOf(tempSpell);
            }
        }

        //Use that index to iterate to + 1 index in the array

        try {
            Button.ButtonStyle style = activeSpellButton.getStyle();
            if (player.getSpellBook().get(currentSpellIndex + 1).equals("Fireball")) {
                player.activeSpell = "Fireball";
                style.up = fireBallImage.getDrawable();
            }else if(player.getSpellBook().get(currentSpellIndex + 1).equals("Acid Blast")){
                player.activeSpell = "Acid Blast";
                style.up = acidBlastImage.getDrawable();
            }else if(player.getSpellBook().get(currentSpellIndex + 1).equals("Horrid Wilting")){
                player.activeSpell = "Horrid Wilting";
                style.up = horridWiltingImage.getDrawable();
            }
        }catch(IndexOutOfBoundsException e){
            player.activeSpell = "Magic Missile";
            Button.ButtonStyle style = activeSpellButton.getStyle();
            style.up = magicMissileImage.getDrawable();
        }
    }

}
