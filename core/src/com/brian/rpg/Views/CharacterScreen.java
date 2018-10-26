package com.brian.rpg.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.brian.rpg.Model.Player;
import com.brian.rpg.RPG;

import static com.badlogic.gdx.Input.Keys.I;

public class CharacterScreen {
    private RPG game;

    public Stage stage;
    private Viewport viewport;
    Skin skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));

    private Player player;

    TextArea textArea;

    public CharacterScreen(RPG game, Player player, SpriteBatch batch){
        this.game = game;
        this.player = player;

        viewport = new FitViewport(800, 600, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        textArea = new TextArea("", skin);
        textArea.setSize(250, 250);
        textArea.setPosition( 300, 350);

        stage.addActor(textArea);
    }

    public void showCharacterScreen(){
        textArea.setText("***Character Sheet***" + '\n' + '\n' + "Class: " + player.getGameClass() + '\n' + "Level: " + player.getLevel() + '\n' + "Experience: " + player.getExperience() + '\n' + '\n' + "Next Level: " + player.evaluateExpToNextLevel() + " experience" + '\n' + "Total Kills: " + player.getKills() + '\n' + '\n' + "Active Spell: " + player.getActiveSpell());
    }

    public void showSpellbook(){
        textArea.setText("***Spellbook***" + '\n');
        for(String spell : player.getSpellBook()){
            textArea.appendText('\n' + spell);
        }

    }

}
