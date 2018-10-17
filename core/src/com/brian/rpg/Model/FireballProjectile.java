package com.brian.rpg.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.brian.rpg.Views.PlayScreen;

public class FireballProjectile extends Projectile {
    private static final int FRAME_COLS = 8;
    private static final int FRAME_ROWS = 8;
    Animation<TextureRegion> fireballAnimation;

    //Explosion related variables
    Boolean hasExploded = false;
    Float explosionTimer = 0f;

    public FireballProjectile(PlayScreen screen, float createX, float createY, Vector2 projectileVelocity){
        super(screen, createX, createY, projectileVelocity, 10);
        this.stateTimer = 0;
        this.projectileLife = 5;
        this.projectileSpeed = 1000f;
        this.fixture.setUserData(this);
        this.box2body.setType(BodyDef.BodyType.KinematicBody);
        texture = screen.getGameManager().get("sprites/16_sunburn_spritesheet.png", Texture.class);

        //Use split function to create an array of Textures
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / FRAME_COLS,
                texture.getHeight() / FRAME_ROWS);

        //Place the regions into an array
        TextureRegion[] staffFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                staffFrames[index++] = tmp[i][j];
            }
        }

        fireballAnimation = new Animation<TextureRegion>(0.3f, staffFrames);

        //Initialize sprite when object is created
        this.sprite = new Sprite(staffFrames[0]);
        this.sprite.setSize(projectileSize + 30, projectileSize + 30);
        this.sprite.setBounds(1, 1, projectileSize + 30, projectileSize + 30);

        //Move Projectile
        this.box2body.setLinearVelocity(projectileVelocity.scl(projectileSpeed));
    }

    public void update(){
        this.sprite.setPosition(box2body.getPosition().x - this.sprite.getWidth() / 2, box2body.getPosition().y - this.sprite.getHeight() /2);
        this.sprite.setRegion(fireballAnimation.getKeyFrame(stateTimer, true));
        this.stateTimer = this.stateTimer + Gdx.graphics.getDeltaTime();

        //Destroy projectile body when done
        if(this.stateTimer > this.projectileLife){
            world.destroyBody(this.box2body);
            //Remove this projectile from screen staffProjectiles list, stops drawing and updating object
            screen.staffProjectiles.remove(this);
            this.stateTimer = 0;
        }

        //Destroy fireball after sometime after explosion
        if(hasExploded){
            explosionTimer += Gdx.graphics.getDeltaTime();
            if(explosionTimer >= 1f){
                destroyAfterExplosion();
            }
        }
    }

    @Override
    public void onHit(){
        //Increase sprite size as fireball explodes
        this.sprite.setSize(projectileSize + 50, projectileSize + 50);
        this.sprite.setBounds(1, 1,projectileSize + 50, projectileSize + 50);
        this.box2body.setLinearVelocity(0, 0);
        this.box2body.setAngularVelocity(0);
        hasExploded = true;
    }

    private void destroyAfterExplosion(){
        if (box2body != null && !screen.bodiesToDelete.contains(box2body)) {
            screen.bodiesToDelete.add(box2body);

            if(screen.staffProjectiles.contains(this)) {
                screen.staffProjectiles.remove(this);
            }

        }
    }
}
