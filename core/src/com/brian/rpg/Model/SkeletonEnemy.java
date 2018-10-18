package com.brian.rpg.Model;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.brian.rpg.Views.PlayScreen;

public class SkeletonEnemy extends Creature {
    float stateTimer = 0;

    //Animations
    private Animation<TextureRegion> skeletonWalk;

    public SkeletonEnemy(PlayScreen screen, int hp, int mana, String gameClass, Vector2 spawnPoint){
        super(screen, hp, mana, gameClass, spawnPoint);
        this.experienceValue = 10;
        this.fixture.setUserData(this);
        //Setting sprite
        this.sprite = new Sprite(screen.getMonsters1SpriteAtlas().findRegion("Skeleton"));
        //Setting Animation
        skeletonWalk = new Animation<TextureRegion>(0.3f, screen.getMonsters1SpriteAtlas().findRegions("Skeleton"));
    }

    public SkeletonEnemy(PlayScreen screen, int hp, int mana, String gameClass, Vector2 spawnPoint, int size){
        super(screen, hp, mana, gameClass, spawnPoint);
        this.experienceValue = 10 + size;
        this.size = size;
        this.fixture.setUserData(this);
        //Setting sprite
        this.sprite = new Sprite(screen.getMonsters1SpriteAtlas().findRegion("Skeleton"));
        this.sprite.setSize(size, size);
        //Setting Animation
        skeletonWalk = new Animation<TextureRegion>(0.3f, screen.getMonsters1SpriteAtlas().findRegions("Skeleton"));
    }

    @Override
    public void update(float delta) {
        //Sets sprite position to center of box2body position so the sprite and the physics body are in the same space
        this.sprite.setPosition(box2body.getPosition().x - this.sprite.getWidth() / 2, box2body.getPosition().y - this.sprite.getHeight() / 2);

        //Move toward player
        Vector2 playerPosition = new Vector2(screen.getPlayer().box2body.getPosition().x, screen.getPlayer().box2body.getPosition().y);

        //Velocity equals target position - current position
        Vector2 velocity = new Vector2(playerPosition.x - box2body.getPosition().x, playerPosition.y - box2body.getPosition().y);

        this.box2body.setLinearVelocity(velocity);

        //Update Animation every frame
        this.sprite.setRegion(skeletonWalk.getKeyFrame(stateTimer, true));
        stateTimer += delta;
    }

    @Override
    public void onHit(){
        //If not a player destroy the creature
        if(box2body != null){
            this.hp -= 2;
            screen.getGameManager().get("Sounds/Bone Crushing.wav", Sound.class).play();
            if(this.hp <= 0) {
                screen.bodiesToDelete.add(box2body);
                deleteFlag = true;
                screen.spawnedCreatures.remove(this);

                //Award Experience
                screen.getPlayer().awardExperience(this.experienceValue);
                screen.getPlayer().increaseKillCount();
            }
        }

    }
}
