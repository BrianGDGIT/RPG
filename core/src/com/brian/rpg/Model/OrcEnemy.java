package com.brian.rpg.Model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.brian.rpg.Views.PlayScreen;

public class OrcEnemy extends Creature {
    float stateTimer = 0;

    //Animations
    private Animation<TextureRegion> orcWalk;

    public OrcEnemy(PlayScreen screen, int hp, int mana, String gameClass, Vector2 spawnPoint){
        super(screen, hp, mana, gameClass, spawnPoint);
        this.experienceValue = 10;
        this.fixture.setUserData(this);
        //Setting sprite
        this.sprite = new Sprite(screen.getMonsters1SpriteAtlas().findRegion("Orc"));
        //Setting Animation
        orcWalk = new Animation<TextureRegion>(0.3f, screen.getMonsters1SpriteAtlas().findRegions("Orc"));
    }

    public OrcEnemy(PlayScreen screen, int hp, int mana, String gameClass, Vector2 spawnPoint, int size){
        super(screen, hp, mana, gameClass, spawnPoint);
        this.experienceValue = 10 + size;
        this.size = size;
        this.fixture.setUserData(this);
        //Setting sprite
        this.sprite = new Sprite(screen.getMonsters1SpriteAtlas().findRegion("Orc"));
        this.sprite.setSize(size, size);
        //Setting Animation
        orcWalk = new Animation<TextureRegion>(0.3f, screen.getMonsters1SpriteAtlas().findRegions("Orc"));
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
        this.sprite.setRegion(orcWalk.getKeyFrame(stateTimer, true));
        stateTimer += delta;
    }
}
