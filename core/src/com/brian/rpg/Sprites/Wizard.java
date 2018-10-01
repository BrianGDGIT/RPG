package com.brian.rpg.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.brian.rpg.RPG;

public class Wizard extends Sprite {
    public World world;
    public Body box2body;

    public Wizard(World world){
        this.world = world;
        createWizard();
    }

    public void createWizard(){
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
