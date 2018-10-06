package com.brian.rpg.Model;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.brian.rpg.RPG;
import com.brian.rpg.Views.PlayScreen;


public class Player extends Creature{

    //Textures
    private TextureRegion wizardSprite;

    //Animations
    private Animation<TextureRegion> playerWalk;
    private float stateTimer = 0;

    //Basic Attack variables
    float basicAttackTimer = 0;
    boolean hasAttacked = false;

    public Player(World world, PlayScreen screen, int hp, int mana, String gameClass, Sprite sprite){
        super(world, screen, hp, mana, gameClass, sprite);
        this.currentState = State.IDLE;
        this.previousState = State.IDLE;

        //Initialize player sprite to class
        if(this.gameClass.equals("Wizard")){
            //Create Sprite Textures
            wizardSprite = new TextureRegion(this.sprite.getTexture(), 1078, 850, 342, 354);
            this.sprite.setBounds(1,1, 16, 16);
            this.sprite.setRegion(wizardSprite);
            //Set size
            this.sprite.setSize(16, 16);

            //Set Walking Animation
            playerWalk = new Animation<TextureRegion>(0.3f, screen.getWizardSpriteAtlas().findRegions("walk"), Animation.PlayMode.LOOP);
        }
    }

    public void update(float delta){
        //Sets sprite position to center of box2body position so the sprite and the physics body are in the same space
        this.sprite.setPosition(box2body.getPosition().x - this.sprite.getWidth() / 2, box2body.getPosition().y - this.sprite.getHeight() /2);

        //Change Sprite on player movement
        this.sprite.setRegion(getFrame(delta));

        //Handle basic attack
        if(basicAttackTimer >= 3){
            basicAttackTimer = 0;
            hasAttacked = false;
        }

        if(hasAttacked){
            basicAttackTimer = basicAttackTimer + delta;
        }
    }

    public TextureRegion getFrame(float delta){
        this.currentState = getState();

        TextureRegion region = wizardSprite;
        switch(currentState){
            case WALKING:
                region = playerWalk.getKeyFrame(stateTimer, false);
                break;
            case IDLE:
                region = wizardSprite;
        }

        if(currentState == State.WALKING){
            stateTimer = stateTimer + delta;
        }else{
            //Reset timer on new state transition
            stateTimer = 0;
        }

        //Change Sprite direction when needed
        if(this.currentDirection == Direction.LEFT && !region.isFlipX()){
            region.flip(true, false);
        }else if(this.currentDirection == Direction.RIGHT && region.isFlipX()){
            region.flip(true, false);
        }else{
            region.flip(false, false);
        }



        return region;
    }

    public State getState(){
            if(box2body.getLinearVelocity().x != 0 || box2body.getLinearVelocity().y != 0){
                return State.WALKING;
            }else{
                return State.IDLE;
            }
        }

    public void handleInput(float delta){
            Vector3 touchPos = new Vector3();

            //Keyboard controls
            if(Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S)){
                this.box2body.applyLinearImpulse(new Vector2(0, 50), this.box2body.getWorldCenter(), true);
            }else{
                if(!Gdx.input.isKeyPressed(Input.Keys.S)){
                    this.box2body.setLinearVelocity(this.box2body.getLinearVelocity().x, 0);
                }
            }

            if(Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)){
                this.box2body.applyLinearImpulse(new Vector2(50, 0), this.box2body.getWorldCenter(), true);
                this.currentDirection = Direction.RIGHT;
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
                this.currentDirection = Direction.LEFT;
            }else{
                if(!Gdx.input.isKeyPressed(Input.Keys.D)) {
                    this.box2body.setLinearVelocity(0, this.box2body.getLinearVelocity().y);
                }
            }

            //Attack
            if(Gdx.input.isTouched() && !hasAttacked){
                StaffProjectile staffProjectile = new StaffProjectile(world, screen,box2body.getPosition().x + 5, box2body.getPosition().y + 5);
                screen.projectilesToRender(staffProjectile);
                hasAttacked = true;
            }


            //Android Controls
            if(Gdx.app.getType() == Application.ApplicationType.Android){
                //Touch
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                screen.getGameCamera().unproject(touchPos);

                //Accelerometer
                Float accelX = Gdx.input.getAccelerometerX();
                Float accelY = Gdx.input.getAccelerometerY();
                //Find touchpos relative to player object and move that direction
                //Right
                if(accelY > 2){
                    this.box2body.applyLinearImpulse(new Vector2(50, 0), this.box2body.getWorldCenter(), true);
                    this.currentDirection = Direction.RIGHT;
                }
                if(accelY < -2){
                    this.box2body.applyLinearImpulse(new Vector2(-50, 0), this.box2body.getWorldCenter(), true);
                    this.currentDirection = Direction.LEFT;
                }
                if(accelX > 6){
                    this.box2body.applyLinearImpulse(new Vector2(0, -50), this.box2body.getWorldCenter(), true);
                }
                if(accelX < 2){
                    this.box2body.applyLinearImpulse(new Vector2(0, 50), this.box2body.getWorldCenter(), true);
                }

            }

                //if(touchPos.x > box2body.getPosition().x + 50 ){
//                    this.box2body.applyLinearImpulse(new Vector2(50, 0), this.box2body.getWorldCenter(), true);
//                    this.currentDirection = Direction.RIGHT;
//                }
//                //Left
//                if(touchPos.x < box2body.getPosition().x - 50){
//                    this.box2body.applyLinearImpulse(new Vector2(-50, 0), this.box2body.getWorldCenter(), true);
//                    this.currentDirection = Direction.LEFT;
//                }
//                //Up
//                if(touchPos.y > box2body.getPosition().y + 50){
//                    this.box2body.applyLinearImpulse(new Vector2(0, 50), this.box2body.getWorldCenter(), true);
//                }
//                //Down
//                if(touchPos.y < box2body.getPosition().y - 50){
//                    this.box2body.applyLinearImpulse(new Vector2(0, -50), this.box2body.getWorldCenter(), true);
//                }


    }

}
