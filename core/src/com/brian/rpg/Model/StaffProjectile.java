package com.brian.rpg.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.brian.rpg.RPG;
import com.brian.rpg.Views.PlayScreen;

public class StaffProjectile extends Projectile {
    private static final int FRAME_COLS = 8;
    private static final int FRAME_ROWS = 8;

    Boolean deleteFlag = false;




    Animation<TextureRegion> staffProjectileAnimation;

    public StaffProjectile(PlayScreen screen, float createX, float createY, Vector2 projectileVelocity) {
        super(screen, createX, createY, projectileVelocity, 5);
        this.stateTimer = 0;
        this.projectileLife = 3;
        this.projectileSpeed = 1000f;
        this.fixture.setUserData(this);
        texture = screen.getGameManager().get("sprites/vortex_spritesheet.png", Texture.class);

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

        //Initialize animation
        staffProjectileAnimation = new Animation<TextureRegion>(0.3f, staffFrames);

        //Initialize sprite when object is created
        this.sprite = new Sprite(staffFrames[0]);
        this.sprite.setSize(projectileSize + 10, projectileSize + 10);
        this.sprite.setBounds(1, 1, projectileSize + 10, projectileSize + 10);

        //Play projectile sound
        screen.getGameManager().get("Sounds/magic1.wav", Sound.class).play();

        //Move Projectile
        this.box2body.setLinearVelocity(projectileVelocity.scl(projectileSpeed));

    }

    public void update(){
        this.sprite.setPosition(box2body.getPosition().x - this.sprite.getWidth() / 2, box2body.getPosition().y - this.sprite.getHeight() /2);
        this.sprite.setRegion(staffProjectileAnimation.getKeyFrame(stateTimer, true));
        this.stateTimer = this.stateTimer + Gdx.graphics.getDeltaTime();

        //Destroy projectile body when done
        if(this.stateTimer > this.projectileLife){
            world.destroyBody(this.box2body);
            //Remove this projectile from screen staffProjectiles list, stops drawing and updating object
            screen.staffProjectiles.remove(this);
            this.stateTimer = 0;
        }
    }

    public void onHit(){
        //If not a player destroy the projectile
        if(box2body != null && !deleteFlag) {
            screen.bodiesToDelete.add(box2body);
            this.deleteFlag = true;
            screen.staffProjectiles.remove(this);
        }
    }

}
