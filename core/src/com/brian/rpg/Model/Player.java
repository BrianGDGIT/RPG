package com.brian.rpg.Model;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.brian.rpg.Views.PlayScreen;


public class Player extends Creature{

    //Textures
    private TextureRegion wizardSprite;

    //Animations
    private Animation<TextureRegion> playerWalk;
    private float stateTimer = 0;


    public Player(World world, PlayScreen screen, int hp, int mana, String gameClass, Sprite sprite){
        super(world, screen, hp, mana, gameClass, sprite);
        this.currentState = State.IDLE;
        this.previousState = State.IDLE;

        //Initialize player sprite to class
        if(this.gameClass.equals("Wizard")){
            //Create Sprite Textures
            wizardSprite = new TextureRegion(this.sprite.getTexture(), 1078, 850, 342, 354);
            this.sprite.setBounds(1,1, 342, 354);
            this.sprite.setRegion(wizardSprite);
            //Set size
            this.sprite.setSize(16, 16);

            //Set Walking Animation
            playerWalk = new Animation<TextureRegion>(0.3f, screen.getWizardSpriteAtlas().findRegions("walk"), Animation.PlayMode.LOOP);
        }
    }

    public void update(float delta){
        this.sprite.setPosition(box2body.getPosition().x - this.sprite.getWidth() / 2, box2body.getPosition().y - this.sprite.getHeight() /2);

        //Change Sprite on player movement
        this.sprite.setRegion(getFrame(delta));
    }

    public TextureRegion getFrame(float delta){
        this.currentState = getState();

        TextureRegion region = wizardSprite;
        switch(currentState){
            case WALKING_RIGHT:
                region = playerWalk.getKeyFrame(stateTimer, true);
                break;
            case WALKING_LEFT:
                region = playerWalk.getKeyFrame(stateTimer, true);
                break;
            case IDLE:
                region = wizardSprite;
        }

//        if(box2body.getLinearVelocity().x < 0 && !region.isFlipX()){
//            region.flip(true, false);
//        }


        if(currentState == State.WALKING_RIGHT || currentState == State.WALKING_LEFT){
            stateTimer = stateTimer + delta;
        }else{
            //Reset timer on new state transition
            stateTimer = 0;
        }

        //Check if idle sprite needs to be flipped after stopping animation
        if(currentState == State.WALKING_RIGHT && region.isFlipX()){
            region.flip(true, false);
        }else if(currentState == State.WALKING_LEFT && !region.isFlipX()){
            region.flip(true, false);
        }else if(previousState == State.WALKING_LEFT && currentState == State.IDLE && !region.isFlipX()){
            region.flip(true, false);
        }else if(previousState == State.WALKING_RIGHT && currentState == State.IDLE && region.isFlipX() ){
            region.flip(true, false);
        }

        return region;
    }

    public State getState(){
        if(box2body.getLinearVelocity().x > 0){
            previousState = currentState;
            return State.WALKING_RIGHT;
        }else if(box2body.getLinearVelocity().x < 0){
            previousState = currentState;
            return State.WALKING_LEFT;
        }else{
            return State.IDLE;
        }
    }

    public void handleInput(float delta){
            //Keyboard controls
            if(Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S)){
                this.box2body.applyLinearImpulse(new Vector2(0, 50), this.box2body.getWorldCenter(), true);
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
