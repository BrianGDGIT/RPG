package com.brian.rpg.Model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.brian.rpg.Views.PlayScreen;

public class SkeletonEnemy extends Creature {
    private static final int FRAME_COLS = 8;
    private static final int FRAME_ROWS = 8;

    public SkeletonEnemy(PlayScreen screen, int hp, int mana, String gameClass, Vector2 spawnPoint){
        super(screen, hp, mana, gameClass, spawnPoint);
        //Setting sprite
        this.sprite = new Sprite(screen.getMonsters1SpriteAtlas().findRegion("Skeleton (1)"));
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
    }
}
