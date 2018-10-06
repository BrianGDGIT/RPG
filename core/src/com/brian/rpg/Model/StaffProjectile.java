package com.brian.rpg.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.brian.rpg.RPG;
import com.brian.rpg.Views.PlayScreen;

public class StaffProjectile extends Projectile {
    private static final int FRAME_COLS = 8;
    private static final int FRAME_ROWS = 8;

    Animation<TextureRegion> staffProjectileAnimation;

    public StaffProjectile(World world, PlayScreen screen, float createX, float createY){
        super(world, screen, createX, createY);
        this.stateTimer = 0;
        this.projectileDelay = 3;
        this.projectileLife = 3;
        texture = new Texture("sprites/vortex_spritesheet.png");

        //Use split function to create an array of Textures
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / FRAME_COLS,
                                                    texture.getHeight() / FRAME_ROWS);

        //Place the regions into an array
        TextureRegion[] staffFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for(int i = 0; i < FRAME_ROWS; i++){
            for(int j = 0; j < FRAME_COLS; j++){
                staffFrames[index++] = tmp[i][j];
            }
        }

        //Initialize animation
        staffProjectileAnimation = new Animation<TextureRegion>(0.3f, staffFrames);

        //Initialize sprite when object is created
        this.sprite = new Sprite(staffFrames[0]);
        this.sprite.setSize(32, 32);
        this.sprite.setBounds(1, 1, 16, 16);
    }

    public void update(){
        this.sprite.setPosition(box2body.getPosition().x - this.sprite.getWidth() / 2, box2body.getPosition().y - this.sprite.getHeight() /2);
        this.sprite.setRegion(staffProjectileAnimation.getKeyFrame(stateTimer, true));
        this.stateTimer = this.stateTimer + Gdx.graphics.getDeltaTime();

        if(this.stateTimer > this.projectileLife){
            world.destroyBody(box2body);
            //Stop drawing projectile sprite on screen
            screen.staffProjectile = null;
            this.stateTimer = 0;
        }
    }

}
