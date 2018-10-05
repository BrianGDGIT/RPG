package com.brian.rpg.Model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    public Projectile(World world, PlayScreen screen){
        this.world = world;
        this.screen = screen;
    }

    public void createProjectile(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(RPG.V_WIDTH / 2, RPG.V_HEIGHT / 2);
        bdef.type = BodyDef.BodyType.DynamicBody;
        box2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5);

        fdef.shape = shape;
        box2body.createFixture(fdef);
    }
}
