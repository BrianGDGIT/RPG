package com.brian.rpg.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.brian.rpg.RPG;

public class HighscoreScreen implements Screen {
    final RPG game;
    private Stage stage;

    //Table Buttons
    Skin skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));


    TextButton back = new TextButton("Back", skin);



    public HighscoreScreen(final RPG game){
        this.game = game;
        Preferences preferences = Gdx.app.getPreferences("Wizard");

        Integer levelValue = preferences.getInteger("level score");
        String level = levelValue.toString();

        Integer experienceValue = preferences.getInteger("experience score");
        String experience = experienceValue.toString();

        //Create Labels
        Label title = new Label("High Score", skin);
        Label highScoreLevel = new Label("Level", skin);
        Label highScoreExperience = new Label("Experience", skin);
        Label levelLabel = new Label(level, skin);
        Label experienceLabel = new Label(experience, skin);

        //Set Label style
        title.setFontScale(2.5f);
        highScoreLevel.setFontScale(2.0f);
        highScoreExperience.setFontScale(2.0f);
        levelLabel.setFontScale(2.0f);
        experienceLabel.setFontScale(2.0f);

        //Create stage
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //Add items to stage
        Table table = new Table();
        table.setFillParent(true);


        //table.setDebug(true);
        stage.addActor(table);

        //Add items to the stage table
        table.add(title);
        table.row().pad(20, 0, 10, 0);
        table.add(highScoreLevel).left();
        table.add(levelLabel);
        table.row().pad(20, 0, 10, 0);
        table.add(highScoreExperience).left();
        table.add(experienceLabel).fillX();
        table.row().pad(20, 0, 10, 0);
        table.add(back).fillX().uniform();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        //Change Screen
        if(back.isPressed()){
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
