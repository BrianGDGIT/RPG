package com.brian.rpg.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.brian.rpg.Views.PlayScreen;

public class HorridWiltingProjectile extends Projectile {
    private static final int FRAME_COLS = 8;
    private static final int FRAME_ROWS = 8;
    Animation<TextureRegion> wiltingProjectileAnimation;
    Animation<TextureRegion> wiltingExplosionAnimation;

    //Explosion related variables
    Boolean hasExploded = false;
    Float explosionTimer = 0f;

    public HorridWiltingProjectile(PlayScreen screen, float createX, float createY, Vector2 projectileVelocity, int projectileSize, int damage){
        super(screen, createX, createY, projectileVelocity, projectileSize);
        stateTimer = 0;
        this.damage = damage;
        projectileLife = 5;
        projectileSpeed = 100f;
        fixture.setUserData(this);
        fixture.setSensor(true);

        texture = screen.getGameManager().get("sprites/11_fire_spritesheet.png", Texture.class);

        TextureRegion[] wiltingProjectileFrames = createAnimationFrames(texture);

        texture = screen.getGameManager().get("sprites/16_sunburn_spritesheet.png", Texture.class);
        TextureRegion[] wiltingExplosionFrames = createAnimationFrames(texture);

        wiltingProjectileAnimation = new Animation<TextureRegion>(0.3f, wiltingProjectileFrames);
        wiltingExplosionAnimation = new Animation<TextureRegion>(0.3f, wiltingExplosionFrames);

        //Initialize sprite when object is created
        sprite = new Sprite(wiltingProjectileFrames[0]);
        sprite.setSize(projectileSize * 3, projectileSize * 3);
        sprite.setBounds(1, 1, projectileSize * 3, projectileSize * 3);
        sprite.setColor(Color.GREEN);

        //Play sound
        screen.getGameManager().get("Sounds/Fireball.wav", Sound.class).play();

        //Move Projectile
        this.box2body.setLinearVelocity(projectileVelocity.scl(projectileSpeed));
    }

    public void update(){
        sprite.setPosition(box2body.getPosition().x - sprite.getWidth() / 2, box2body.getPosition().y - sprite.getHeight() /2);
        if(!hasExploded) {
            sprite.setRegion(wiltingProjectileAnimation.getKeyFrame(stateTimer, true));
        }
        stateTimer = stateTimer + Gdx.graphics.getDeltaTime();

        //Destroy projectile body when done
        if(this.stateTimer > this.projectileLife){
            world.destroyBody(this.box2body);
            //Remove this projectile from screen staffProjectiles list, stops drawing and updating object
            screen.staffProjectiles.remove(this);
            stateTimer = 0;
        }

        //Destroy fireball after sometime after explosion
        if(hasExploded){
            sprite.setRegion(wiltingExplosionAnimation.getKeyFrame(stateTimer, true));
            sprite.setColor(Color.GREEN);
            fixture.getShape().setRadius(projectileSize * 1.5f);
            explosionTimer += Gdx.graphics.getDeltaTime();
            if(explosionTimer >= 1f){
                destroyAfterExplosion();
            }
        }
    }

    @Override
    public void onHit(){
        //Increase sprite size as fireball explodes
        sprite.setSize(projectileSize * 5, projectileSize * 5);
        sprite.setBounds(1, 1,projectileSize * 5, projectileSize * 5);
        box2body.setLinearVelocity(0, 0);
        box2body.setAngularVelocity(0);
        hasExploded = true;
        screen.getGameManager().get("Sounds/Explosion.wav", Sound.class).play();
    }

    private void destroyAfterExplosion(){
        if (box2body != null && !screen.bodiesToDelete.contains(box2body)) {
            screen.bodiesToDelete.add(box2body);

            if(screen.staffProjectiles.contains(this)) {
                screen.staffProjectiles.remove(this);
            }

        }
    }

    private TextureRegion[] createAnimationFrames(Texture texture){
        //Use split function to create an array of Textures
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / FRAME_COLS,
                texture.getHeight() / FRAME_ROWS);

        //Place the regions into an array
        TextureRegion[] animationFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                animationFrames[index++] = tmp[i][j];
            }
        }

        return animationFrames;
    }
}
