package com.brian.rpg.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.brian.rpg.RPG;

public class MainMenuScreen implements Screen {
    final RPG game;
    private Stage stage;

    //Table Buttons
    Skin skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));

    TextButton newGame = new TextButton("New Game", skin);
    TextButton exit = new TextButton("Exit", skin);

    public MainMenuScreen(final RPG game){
        this.game = game;

        //Create stage
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);



        //Add items to stage
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        //Add items to the stage table
        table.add(newGame).fillX().uniform();
        table.row().pad(10, 0, 10, 0);
        table.add(exit).fillX().uniformX();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
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
    }
}
