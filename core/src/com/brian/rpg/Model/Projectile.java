package com.brian.rpg.Model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.brian.rpg.RPG;
import com.brian.rpg.Views.PlayScreen;

public abstract class Projectile {
    Texture texture;
    Sprite sprite;

    public World world;
    public PlayScreen screen;
    public Body box2body;

    float stateTimer = 0;
    float createX;
    float createY;

    //Projectile attributes
    int projectileLife;
    int projectileSize;
    float projectileSpeed;
    Vector2 projectileVelocity;

    Fixture fixture;

    public Projectile(PlayScreen screen, float createX, float createY, Vector2 projectileVelocity, int projectileSize){
        this.world = screen.getWorld();
        this.screen = screen;
        this.createX = createX;
        this.createY = createY;
        this.projectileVelocity = projectileVelocity;
        this.projectileSize = projectileSize;
        createProjectile();
    }

    public void createProjectile(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(createX, createY);
        bdef.type = BodyDef.BodyType.DynamicBody;
        box2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(projectileSize);

        fdef.shape = shape;
        fdef.filter.categoryBits = RPG.PROJECTILE_BIT;
        fdef.filter.maskBits = RPG.WALL_BIT | RPG.CREATURE_BIT;
        fixture = box2body.createFixture(fdef);
    }

    public abstract void onHit();

    public Sprite getSprite(){
        return sprite;
    }

}
