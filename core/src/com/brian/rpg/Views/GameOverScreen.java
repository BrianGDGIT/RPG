package com.brian.rpg.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.brian.rpg.Model.Player;
import com.brian.rpg.RPG;

public class GameOverScreen implements Screen {
    final RPG game;
    private Stage stage;

    //Table Buttons
    Skin skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));

    TextButton newGame = new TextButton("New Game", skin);
    TextButton highScores = new TextButton("High Scores", skin);
    TextButton exit = new TextButton("Exit", skin);

    Texture gameOverTexture = new Texture("GameOver.png");
    Image gameOverImage = new Image(gameOverTexture);

    public GameOverScreen(RPG game, Boolean isHighScore){
        this.game = game;



        //Create stage
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //Add items to stage
        Table table = new Table();
        table.setFillParent(true);

        stage.addActor(table);

        //Handle high score achievement message

        if(isHighScore){
            Label highScoreMessage = new Label("High score achieved!", skin);
            highScoreMessage.setFontScale(5.0f);
            table.add(highScoreMessage);
            table.row().pad(100, 0, 10, 0);
        }


        //Add items to the stage table
        table.add(gameOverImage);
        table.row().pad(100, 0, 10, 0);
        table.add(newGame).fillX().uniform();
        table.row().pad(0, 0, 10, 0);
        table.add(highScores).fillX().uniform();
        table.row().pad(0, 0, 10, 0);
        table.add(exit).fillX().uniformX();

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
        if(newGame.isPressed()){
            game.setScreen(new PlayScreen(game));
            dispose();
        }

        if(highScores.isPressed()){
            game.setScreen(new HighscoreScreen(game));
            dispose();
        }

        if(exit.isPressed()){
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {

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
