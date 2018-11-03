package com.brian.rpg.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.brian.rpg.Views.PlayScreen;

public class AcidCloudProjectile extends Projectile {
    private static final int FRAME_COLS = 8;
    private static final int FRAME_ROWS = 8;
    Animation<TextureRegion> acidCloudProjectileAnimation;
    Animation<TextureRegion> acidCloudExplosionAnimation;


    //Explosion related variables
    Boolean hasHit = false;
    Boolean hasExploded = false;
    Float explosionTimer = 0f;
    int explosionSize;

    public AcidCloudProjectile(PlayScreen screen, float createX, float createY, Vector2 projectileVelocity, int projectileSize, int explosionSize, int damage){
        super(screen, createX, createY, projectileVelocity, projectileSize);
        stateTimer = 0;
        this.damage = damage;
        projectileLife = 5;
        projectileSpeed = 100f;
        this.explosionSize = explosionSize;
        fixture.setUserData(this);
        fixture.setSensor(true);

        texture = screen.getGameManager().get("sprites/11_fire_spritesheet.png", Texture.class);

        TextureRegion[] wiltingProjectileFrames = createAnimationFrames(texture);

        texture = screen.getGameManager().get("sprites/16_sunburn_spritesheet.png", Texture.class);
        TextureRegion[] wiltingExplosionFrames = createAnimationFrames(texture);

        acidCloudProjectileAnimation = new Animation<TextureRegion>(0.3f, wiltingProjectileFrames);
        acidCloudExplosionAnimation = new Animation<TextureRegion>(0.3f, wiltingExplosionFrames);

        //Initialize sprite when object is created
        sprite = new Sprite(wiltingProjectileFrames[0]);
        sprite.setSize(30, 30);
        sprite.setBounds(1, 1, 30, 30);

        rotateSprite();

        sprite.setColor(Color.GREEN);

        //Play sound
        screen.getGameManager().get("Sounds/Fireball.wav", Sound.class).play();

        //Move Projectile
        this.box2body.setLinearVelocity(projectileVelocity.scl(projectileSpeed));
    }

    public void update(){
        sprite.setPosition(box2body.getPosition().x - sprite.getWidth() / 2, box2body.getPosition().y - sprite.getHeight() / 2);
        if(!hasExploded) {
            sprite.setRegion(acidCloudProjectileAnimation.getKeyFrame(stateTimer, true));
        }
        stateTimer = stateTimer + Gdx.graphics.getDeltaTime();

        //Destroy projectile body when done
        if(this.stateTimer > this.projectileLife){
            world.destroyBody(this.box2body);
            //Remove this projectile from screen staffProjectiles list, stops drawing and updating object
            screen.staffProjectiles.remove(this);
            stateTimer = 0;
        }

        //Explode Acid Cloud
        //Ensures that explosion only happens once
        if(hasHit && !hasExploded){
            hasExploded = true;
            explode();
        }

        //Destroy AcidCloud sometime after explosion
        if(hasExploded){
            sprite.setRegion(acidCloudExplosionAnimation.getKeyFrame(stateTimer, true));
            explosionTimer += Gdx.graphics.getDeltaTime();
            box2body.setLinearVelocity(0, 0);
            box2body.setAngularVelocity(0);
            if(explosionTimer >= 10f){
                destroyAfterExplosion();
            }
        }
    }

    @Override
    public void onHit(){
        //Increase sprite size as projectile explodes
        sprite.setSize(explosionSize * 4, explosionSize * 4);
        sprite.setBounds(1, 1,explosionSize * 4, explosionSize * 4);

        box2body.setLinearVelocity(0, 0);
        box2body.setAngularVelocity(0);
        hasHit = true;
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

    private void rotateSprite(){
        //Rotates sprite based on direction
        sprite.setRotation(MathUtils.atan2(projectileVelocity.y, projectileVelocity.x) * MathUtils.radiansToDegrees + 75);
        //Keeps sprite centered with body after rotation
        sprite.setOriginCenter();
    }

    private void explode(){
        sprite.setRotation(0);
        fixture.getShape().setRadius(explosionSize);
    }

}
