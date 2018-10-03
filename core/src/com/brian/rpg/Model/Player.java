package com.brian.rpg.Model;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.brian.rpg.Views.PlayScreen;

public class Player extends Creature{

    //Textures
    private TextureRegion wizardIdle1;


    public Player(World world, PlayScreen screen, int hp, int mana, String gameClass, Sprite sprite){
        super(world, screen, hp, mana, gameClass, sprite);


        //Initialize player sprite to class
        if(this.gameClass.equals("Wizard")){
            this.sprite = sprite;
            //Create Sprite Textures
            wizardIdle1 = new TextureRegion(this.sprite.getTexture(), 1078, 850, 342, 354);
            this.sprite.setBounds(1,1, 342, 354);
            this.sprite.setRegion(wizardIdle1);
        }
    }

    public void update(float delta){
        this.sprite.setPosition(box2body.getPosition().x - this.sprite.getWidth() / 2, box2body.getPosition().y - this.sprite.getHeight() /2);

        //Change Sprite on player movement

    }

    public void handleInput(float delta){
            //Keyboard controls
            if(Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S)){
                this.box2body.applyLinearImpulse(new Vector2(0, 50), this.box2body.getWorldCenter(), true);
            }else{
                if(!Gdx.input.isKeyPressed(Input.Keys.S)){
                    //player.box2body.setLinearVelocity(vel.x, 0);
                }
            }

            if(Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)){
                this.box2body.applyLinearImpulse(new Vector2(50, 0), this.box2body.getWorldCenter(), true);
            }else{
                if(!Gdx.input.isKeyPressed(Input.Keys.A)){
                    this.box2body.setLinearVelocity(0, this.box2body.getLinearVelocity().y);
                }
            }

            if(Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.W)){
                this.box2body.applyLinearImpulse(new Vector2(0, -50), this.box2body.getWorldCenter(), true);
            }else{
                if(!Gdx.input.isKeyPressed(Input.Keys.W)) {
                    this.box2body.setLinearVelocity(this.box2body.getLinearVelocity().x, 0);
                }
            }

            if(Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)){
                this.box2body.applyLinearImpulse(new Vector2(-50, 0), this.box2body.getWorldCenter(), true);
            }else{
                if(!Gdx.input.isKeyPressed(Input.Keys.D)) {
                    this.box2body.setLinearVelocity(0, this.box2body.getLinearVelocity().y);
                }
            }
    }






}
