package com.brian.rpg.Model;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.brian.rpg.RPG;
import com.brian.rpg.Views.GameOverScreen;
import com.brian.rpg.Views.PlayScreen;

import java.util.ArrayList;

import static com.badlogic.gdx.Input.Keys.*;


public class Player extends Creature{

    //Player stats
    int experience = 0;
    int kills = 0;

    //Textures
    private TextureRegion wizardSprite;

    //Animations
    private Animation<TextureRegion> playerWalk;
    private Animation<TextureRegion> playerAttack;
    private Animation<TextureRegion> playerDeath;
    private float stateTimer = 0;

    //Basic Attack variables
    float basicAttackTimer = 0;
    boolean hasAttacked = false;
    boolean projectileFired = false;

    //Death variables
    float deathTimer = 0;

    //Player Screen states
    public boolean characterScreenDisplayed = false;
    public boolean inventoryDisplayed = false;
    public boolean spellbookDisplayed = false;

    //Spell Related
    ArrayList<String> spellBook = new ArrayList<String>();
    public String activeSpell;
    private double castDelay;

    //Spells
    SpellMagicMissile magicMissile = new SpellMagicMissile(screen, this);
    SpellFireball fireball = new SpellFireball(screen, this);
    SpellAcidBlast acidBlast = new SpellAcidBlast(screen, this);
    SpellAcidCloud acidCloud = new SpellAcidCloud(screen, this);
    SpellHorridWilting horridWilting = new SpellHorridWilting(screen, this);

    public Player(PlayScreen screen, int hp, int mana, String gameClass, Vector2 spawnPoint){
        super(screen, hp, mana, gameClass, spawnPoint);
        //Set player stats
        this.level = 1;

        this.fixture.setUserData(this);
        setCategoryFilter(RPG.PLAYER_BIT);
        this.currentState = State.IDLE;

        //Initialize player sprite to class
        if(this.gameClass.equals("Wizard")){
            //Create Sprite Textures
            this.sprite = new Sprite(screen.getWizardSpriteAtlas().findRegion("idle"));
            wizardSprite = new TextureRegion(this.sprite.getTexture(), 1078, 850, 342, 354);
            this.sprite.setBounds(1,1, 16, 16);

            //Set Animations
            playerWalk = new Animation<TextureRegion>(0.3f, screen.getWizardSpriteAtlas().findRegions("walk"), Animation.PlayMode.LOOP);
            playerAttack = new Animation<TextureRegion>(0.3f, screen.getWizardSpriteAtlas().findRegions("attack"), Animation.PlayMode.NORMAL);
            playerDeath = new Animation<TextureRegion>(0.3f, screen.getWizardSpriteAtlas().findRegions("dead"), Animation.PlayMode.NORMAL);

            //Initialize Spellbook with level 1 spell Magic Missile
            this.spellBook.add("Magic Missile");
            //Set active spell to magic missle
            this.activeSpell = this.spellBook.get(0);
        }
    }

    public void update(float delta){
        //System.out.println(this.box2body.getPosition());
        //Sets sprite position to center of box2body position so the sprite and the physics body are in the same space
        this.sprite.setPosition(box2body.getPosition().x - this.sprite.getWidth() / 2, box2body.getPosition().y - this.sprite.getHeight() / 2);

        //Move sprite to a more center-like position when attacking
        if(currentState == State.ATTACKING && currentDirection == Direction.LEFT){
            this.sprite.setPosition(box2body.getPosition().x - this.sprite.getWidth() / 2 - 10, box2body.getPosition().y - this.sprite.getHeight() / 2);
        }else if(currentState == State.ATTACKING && currentDirection == Direction.RIGHT){
            this.sprite.setPosition(box2body.getPosition().x - this.sprite.getWidth() / 2 + 10, box2body.getPosition().y - this.sprite.getHeight() / 2);
        }

        //Change Sprite on player movement
        this.sprite.setRegion(getFrame(delta));

        //Handle basic attack
        if(basicAttackTimer >= castDelay && castDelay != 0){
            basicAttackTimer = 0;
            hasAttacked = false;
            projectileFired = false;
            castDelay = 0;
        }

        if(hasAttacked){
            if(!projectileFired && stateTimer > 0.2){
                castDelay = castSpell();
                projectileFired = true;
            }

            basicAttackTimer = basicAttackTimer + delta;
        }

    }

    public TextureRegion getFrame(float delta){
        this.currentState = getState();

        TextureRegion region = wizardSprite;
        switch(currentState){
            case WALKING:
                region = playerWalk.getKeyFrame(stateTimer, false);
                this.sprite.setSize(16, 16);
                break;
            case IDLE:
                region = wizardSprite;
                this.sprite.setSize(16, 16);
                break;
            case ATTACKING:
                region = playerAttack.getKeyFrame(stateTimer, false);
                if(stateTimer > 0.3){
                    this.sprite.setSize(32, 16);
                }
                this.box2body.setLinearVelocity(0, 0);
                break;
            case DEAD:
                region = playerDeath.getKeyFrame(stateTimer, false);
                this.sprite.setSize(16, 16);
                //Prevent body from moving after death
                this.box2body.setType(BodyDef.BodyType.StaticBody);
                deathTimer += delta;

                //Save player's high score on death
                Preferences preferences = Gdx.app.getPreferences("Wizard");
                Integer experienceScore = preferences.getInteger("experience score");
                Integer playerLevelScore = preferences.getInteger("level score");

                if(experience > experienceScore){
                    preferences.putInteger("experience score", experience).flush();
                }

                if(level > playerLevelScore){
                    preferences.putInteger("level score", level).flush();
                }


                if(deathTimer > 5){
                    screen.getGame().setScreen(new GameOverScreen(screen.getGame()));
                    screen.dispose();
                }
                break;
        }

        if(currentState != State.IDLE){
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
            if(currentState == State.DEAD){
                return State.DEAD;
            }else if(hasAttacked){
                return State.ATTACKING;
            }else if(box2body.getLinearVelocity().x != 0 || box2body.getLinearVelocity().y != 0){
                return State.WALKING;
            }else{
                return State.IDLE;
            }
        }

    public void handleInput(float delta){

            //Debug
            if(Gdx.input.isKeyJustPressed(P)){
                System.out.println("Box2d: " + "X: " + this.box2body.getPosition().x + " Y: " + this.box2body.getPosition().y );
                System.out.println("X: " + this.sprite.getX() + " Y: " + this.sprite.getY());
            }

            if(Gdx.input.isKeyJustPressed(O)){
                level++;
            }

            //Keyboard controls
            if(!hasAttacked && currentState != State.DEAD) {
                if (Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
                    this.box2body.applyLinearImpulse(new Vector2(0, 50), this.box2body.getWorldCenter(), true);
                } else {
                    if (!Gdx.input.isKeyPressed(Input.Keys.S)) {
                        this.box2body.setLinearVelocity(this.box2body.getLinearVelocity().x, 0);
                    }
                }

                if (Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)) {
                    this.box2body.applyLinearImpulse(new Vector2(50, 0), this.box2body.getWorldCenter(), true);
                    this.currentDirection = Direction.RIGHT;
                } else {
                    if (!Gdx.input.isKeyPressed(Input.Keys.A)) {
                        this.box2body.setLinearVelocity(0, this.box2body.getLinearVelocity().y);
                    }
                }

                if (Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.W)) {
                    this.box2body.applyLinearImpulse(new Vector2(0, -50), this.box2body.getWorldCenter(), true);
                } else {
                    if (!Gdx.input.isKeyPressed(Input.Keys.W)) {
                        this.box2body.setLinearVelocity(this.box2body.getLinearVelocity().x, 0);
                    }
                }

                if (Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
                    this.box2body.applyLinearImpulse(new Vector2(-50, 0), this.box2body.getWorldCenter(), true);
                    this.currentDirection = Direction.LEFT;
                } else {
                    if (!Gdx.input.isKeyPressed(Input.Keys.D)) {
                        this.box2body.setLinearVelocity(0, this.box2body.getLinearVelocity().y);
                    }
                }
            }

            //Change spells
            if(Gdx.input.isKeyJustPressed(Q)){
                screen.getHud().equipNextSpell();
            }

            //Display Character Screen
            if(Gdx.input.isKeyJustPressed(C)){
                if(!characterScreenDisplayed) {
                    characterScreenDisplayed = true;
                }else{
                    characterScreenDisplayed = false;
                }
            }

            //Display Inventory
            if(Gdx.input.isKeyJustPressed(I)){
                if(!inventoryDisplayed){
                    inventoryDisplayed = true;
                }else{
                    inventoryDisplayed = false;
                }
            }

            //Display Spellbook
            if(Gdx.input.isKeyJustPressed(B)){
                if(!spellbookDisplayed){
                    spellbookDisplayed = true;
                }else {
                    spellbookDisplayed = false;
                }
            }



            //Attack
            if(Gdx.input.isTouched() && !hasAttacked && currentState != State.DEAD && !screen.getHud().characterSheetButton.isPressed() && !screen.getHud().inventoryButton.isPressed() && !screen.getHud().spellbookButton.isPressed() && !screen.getHud().activeSpellButton.isPressed()){
                hasAttacked = true;
            }


            //Android Controls
            if(Gdx.app.getType() == Application.ApplicationType.Android && !hasAttacked && currentState != State.DEAD){
                //Accelerometer Movement
                Float accelX = Gdx.input.getAccelerometerX();
                Float accelY = Gdx.input.getAccelerometerY();

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
    }

    private Double castSpell(){
        Vector3 touchPos = new Vector3();
        float createX;
        float createY;

        //Cast delay of spell being cast
        //Default 1.5 but should always be set below
        double castDelay = 1.5;

        //Getting touch position, unprojecting coords to game coords, normalizing and passing as velocity
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        touchPos = screen.getGameCamera().unproject(touchPos);
        Vector2 velocity = new Vector2(touchPos.x, touchPos.y);
        velocity.sub(this.box2body.getPosition().x + 5, this.box2body.getPosition().y + 5);
        velocity = velocity.nor();

        //Checks touched position, if the position is to the right of the player the projectile is created to the right of the player
        //else it is created to the left of the player
        //This prevents the projectile from hitting the player
        //Also sets player State to either LEFT OR RIGHT so that the player is facing toward touched position
        if(velocity.x > 0){
            this.currentDirection = Creature.Direction.RIGHT;
            createX = this.box2body.getPosition().x + 5;
        }else{
            this.currentDirection = Creature.Direction.LEFT;
            createX = this.box2body.getPosition().x - 10;
        }

        if(velocity.y < 0){
            createY = this.box2body.getPosition().y - 5;
        }else{
            createY = this.box2body.getPosition().y + 5;
        }

        //The potential spells being cast

        if(this.activeSpell.equals("Magic Missile")){
            castDelay = magicMissile.castMagicMissile(createX, createY, velocity);
        }

        if(this.activeSpell.equals("Fireball")){
            castDelay = fireball.castFireball(createX, createY, velocity);
        }

        if(this.activeSpell.equals("Acid Blast")){
            castDelay = acidBlast.castAcidBlast(createX, createY, velocity);
        }

        if(this.activeSpell.equals("Acid Cloud")){
            castDelay = acidCloud.castAcidCloud(createX, createY, velocity);
        }

        if(this.activeSpell.equals("Horrid Wilting")){
            castDelay = horridWilting.castHorridWilting(touchPos.x, touchPos.y, velocity);
        }

        return castDelay;
    }



    public void onHit(){
        this.currentState = State.DEAD;
    }

    public int getExperience(){return this.experience;}
    public int getKills(){return kills;}
    public String getActiveSpell(){return this.activeSpell;}
    public ArrayList<String> getSpellBook(){return this.spellBook;}

    public void awardExperience(int experience){
        //Increase player experience on creature death
        this.experience += experience;
        evaluatePlayerLevel();
    }

    private void evaluatePlayerLevel(){
        if(this.level == 1){
            if(this.experience > 99){
                levelUp();
            }
        }else if(this.level == 2){
            if(this.experience > 199){
                levelUp();
            }
        }else if(this.level == 3){
            if(this.experience > 499){
                levelUp();
            }
        }else if(this.level == 4) {
            if (this.experience > 999) {
                levelUp();
            }
        }
    }

    private void levelUp(){
        this.level++;
        //Play Levelup sound
        screen.getGameManager().get("Sounds/blessing.wav", Sound.class).play();
    }

    public int evaluateExpToNextLevel(){
        switch(this.level){
            case 1:
                return 100 - this.experience;
            case 2:
                return 200 - this.experience;
            case 3:
                return 500 - this.experience;
            case 4:
                return 1000 - this.experience;
            default: return 0;
        }
    }


    public void increaseKillCount(){
        this.kills++;
    }
}
