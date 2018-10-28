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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.brian.rpg.RPG;
import com.brian.rpg.Views.PlayScreen;

public class HorridWiltingProjectile extends Projectile {
    private static final int FRAME_COLS = 8;
    private static final int FRAME_ROWS = 8;
    Animation<TextureRegion> wiltingExplosionAnimation;


    //Explosion related variables
    Boolean hasExploded = false;
    Float explosionTimer = 0f;


    public HorridWiltingProjectile(PlayScreen screen, float createX, float createY, Vector2 projectileVelocity, int projectileSize, int damage){
        super(screen, createX, createY, projectileVelocity, projectileSize);
        stateTimer = 0;
        this.damage = damage;
        projectileLife = 5;

        fixture.setUserData(this);
        fixture.setSensor(true);

        texture = screen.getGameManager().get("sprites/16_sunburn_spritesheet.png", Texture.class);
        TextureRegion[] wiltingExplosionFrames = createAnimationFrames(texture);


        wiltingExplosionAnimation = new Animation<TextureRegion>(0.3f, wiltingExplosionFrames);

        //Initialize sprite when object is created
        sprite = new Sprite(screen.getGameManager().get("sprites/skull.png", Texture.class));
        sprite.setSize(projectileSize * 3, projectileSize * 3);
        sprite.setBounds(1, 1, projectileSize * 3, projectileSize * 3);

        sprite.setColor(Color.GREEN);

        //Play sound
        screen.getGameManager().get("Sounds/Fireball.wav", Sound.class).play();

    }

    public void update(){
        sprite.setPosition(box2body.getPosition().x - sprite.getWidth() / 2, box2body.getPosition().y - sprite.getHeight() / 2);
        sprite.setPosition(box2body.getPosition().x - sprite.getWidth() / 2, box2body.getPosition().y - sprite.getHeight() / 2);

        stateTimer = stateTimer + Gdx.graphics.getDeltaTime();

        //Destroy projectile body when done
        if(this.stateTimer > this.projectileLife){
            world.destroyBody(this.box2body);
            //Remove this projectile from screen staffProjectiles list, stops drawing and updating object
            screen.staffProjectiles.remove(this);
            stateTimer = 0;
        }

        if(stateTimer > 1 && !hasExploded){
            hasExploded = true;
            explode();
        }

        //Destroy fireball after sometime after explosion
        if(hasExploded){
            sprite.setRegion(wiltingExplosionAnimation.getKeyFrame(stateTimer, true));
            explosionTimer += Gdx.graphics.getDeltaTime();
            if(explosionTimer >= 1f){
                destroyAfterExplosion();
            }
        }
    }

    @Override
    public void onHit(){

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

    private void explode(){
        //Increase sprite size of Horrid Wilting as it explodes
        sprite.setSize(projectileSize * 20, projectileSize * 20);
        sprite.setBounds(1, 1,projectileSize * 20, projectileSize * 20);

        screen.getGameManager().get("Sounds/Explosion.wav", Sound.class).play();

        sprite.setColor(Color.GREEN);
        Gdx.app.postRunnable(new Runnable(){
            @Override
            public void run(){
                FixtureDef fdef = new FixtureDef();
                fdef.shape = fixture.getShape();
                fdef.shape.setRadius(projectileSize * 5);
                fdef.filter.categoryBits = RPG.PROJECTILE_BIT;
                fdef.filter.maskBits = RPG.WALL_BIT | RPG.CREATURE_BIT;
                fixture = box2body.createFixture(fdef);
                fixture.setSensor(true);
            }
        });

    }

}
