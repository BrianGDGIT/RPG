package com.brian.rpg.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.brian.rpg.Views.PlayScreen;

public class SkeletonEnemy extends Creature {
    float stateTimer = 0;
    boolean isZombie = false;
    int moanTimer = 0;
    float aiTimer = 5;



    //Animations
    private Animation<TextureRegion> skeletonWalk;

    public SkeletonEnemy(PlayScreen screen, int hp, int mana, String gameClass, Vector2 spawnPoint){
        super(screen, hp, mana, gameClass, spawnPoint);
        experienceValue = 10;
        speed = 1;
        fixture.setUserData(this);
        //Setting sprite
        sprite = new Sprite(screen.getMonsters1SpriteAtlas().findRegion("Skeleton"));
        //Setting Animation
        skeletonWalk = new Animation<TextureRegion>(0.3f, screen.getMonsters1SpriteAtlas().findRegions("Skeleton"));
    }

    public SkeletonEnemy(PlayScreen screen, int hp, int mana, String gameClass, Vector2 spawnPoint, int size){
        super(screen, hp, mana, gameClass, spawnPoint);
        experienceValue = 10 + size;
        speed = 1;
        this.size = size;
        fixture.getShape().setRadius(size / 2.3f);
        fixture.setUserData(this);
        //Setting sprite
        sprite = new Sprite(screen.getMonsters1SpriteAtlas().findRegion("Skeleton"));
        sprite.setSize(size, size);
        //Setting Animation
        skeletonWalk = new Animation<TextureRegion>(0.3f, screen.getMonsters1SpriteAtlas().findRegions("Skeleton"));
    }

    public SkeletonEnemy(PlayScreen screen, int hp, int mana, String gameClass, Vector2 spawnPoint, int size, Color color ){
        super(screen, hp, mana, gameClass, spawnPoint);
        experienceValue = 10 + size;
        this.size = size;
        speed = 0.3f;
        isZombie = true;
        fixture.getShape().setRadius(size / 2.3f);
        fixture.setUserData(this);
        //Setting sprite
        sprite = new Sprite(screen.getMonsters1SpriteAtlas().findRegion("Skeleton"));
        sprite.setColor(color);
        //Setting Animation
        skeletonWalk = new Animation<TextureRegion>(0.3f, screen.getMonsters1SpriteAtlas().findRegions("Skeleton"));
    }

    @Override
    public void update(float delta) {
        Vector2 skeletonPosition = new Vector2(box2body.getPosition().x, box2body.getPosition().y);
        Vector2 playerPosition = new Vector2(screen.getPlayer().box2body.getPosition().x, screen.getPlayer().box2body.getPosition().y);

        //Sets sprite position to center of box2body position so the sprite and the physics body are in the same space
        sprite.setPosition(box2body.getPosition().x - sprite.getWidth() / 2, box2body.getPosition().y - sprite.getHeight() / 2);


        //Velocity equals target position - current position
        Vector2 velocity = new Vector2(playerPosition.x - box2body.getPosition().x, playerPosition.y - box2body.getPosition().y);

        //Wander AI
        if(aiTimer >= 5 && currentState != State.ATTACKING){
            int wanderRangeX = MathUtils.random(100);
            int wanderRangeY = MathUtils.random(100);
            int randomDirectionCheck = MathUtils.random(3);
            //Depending on direction check move the enemy different directions
            //This insures the enemy moves in a variety of directions
            if(randomDirectionCheck == 0) {
                velocity = new Vector2(skeletonPosition.x + wanderRangeX - skeletonPosition.x, skeletonPosition.y + wanderRangeY - skeletonPosition.y);
            }else if(randomDirectionCheck == 1){
                velocity = new Vector2(skeletonPosition.x - wanderRangeX - skeletonPosition.x, skeletonPosition.y - wanderRangeY - skeletonPosition.y);
            }else if(randomDirectionCheck == 2){
                velocity = new Vector2(skeletonPosition.x + wanderRangeX - skeletonPosition.x, skeletonPosition.y - wanderRangeY - skeletonPosition.y);
            }else{
                velocity = new Vector2(skeletonPosition.x - wanderRangeX - skeletonPosition.x, skeletonPosition.y + wanderRangeY - skeletonPosition.y);
            }
            box2body.setLinearVelocity(velocity.scl(speed));
            aiTimer = 0;
        }

        //Attack AI
        if(skeletonPosition.dst(playerPosition) < 100) {
            currentState = State.ATTACKING;
            box2body.setLinearVelocity(velocity.scl(speed));
        }else{
            currentState = State.IDLE;
        }

        //Update Animation every frame
        sprite.setRegion(skeletonWalk.getKeyFrame(stateTimer, true));
        stateTimer += delta;

        //Zombie moan check
        if(isZombie && moanTimer > 500 && box2body.getPosition().dst(screen.getPlayer().box2body.getPosition()) < 50) {
            screen.getGameManager().get("Sounds/zombie-moan.wav", Music.class).play();
            moanTimer = 0;
        }

        moanTimer++;
        aiTimer+= delta;
    }

    @Override
    public void onHit(int damage){
        //If not a player destroy the creature
        if(box2body != null){
            hp -= damage;
            if(!isZombie) {
                screen.getGameManager().get("Sounds/Bone Crushing.wav", Sound.class).play();
            }else{
                screen.getGameManager().get("Sounds/zombie-grunt.wav", Sound.class).play();
            }
            if(hp <= 0) {
                screen.bodiesToDelete.add(box2body);
                deleteFlag = true;
                screen.spawnedCreatures.remove(this);

                //Award Experience
                screen.getPlayer().awardExperience(experienceValue);
                screen.getPlayer().increaseKillCount();
            }
        }

    }
}
