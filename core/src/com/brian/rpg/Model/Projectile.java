package com.brian.rpg.Model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.brian.rpg.RPG;
import com.brian.rpg.Views.PlayScreen;

public class Projectile {
    Texture texture;
    TextureRegion textureRegion;
    Sprite sprite;

    public World world;
    public PlayScreen screen;
    public Body box2body;

    float stateTimer = 0;
    float createX;
    float createY;

    //Projectile attributes
    int projectileDelay;
    int projectileLife;
    int projectileSpeed;
    Vector3 projectileTarget;

    public Projectile(World world, PlayScreen screen, float createX, float createY, Vector3 projectileTarget){
        this.world = world;
        this.screen = screen;
        this.createX = createX;
        this.createY = createY;
        this.projectileTarget = projectileTarget;
        createProjectile();
    }

    public void createProjectile(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(createX, createY);
        bdef.type = BodyDef.BodyType.DynamicBody;
        box2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5);

        fdef.shape = shape;
        box2body.createFixture(fdef);
    }


    public Sprite getSprite(){
        return sprite;
    }

}
