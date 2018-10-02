package com.brian.rpg.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.brian.rpg.RPG;
import com.brian.rpg.Views.PlayScreen;

public class WizardSprite extends Sprite {
    public World world;
    public Body box2body;
    //Textures
    private TextureRegion wizardIdle1;

    public WizardSprite(World world, PlayScreen screen){
        super(screen.getWizardSpriteAtlas().findRegion("idle"));
        this.world = world;
        createWizard();
        //Create Sprite Textures
        wizardIdle1 = new TextureRegion(getTexture(), 1078, 850, 342, 354);
        setBounds(1,1, 342, 354);
        setRegion(wizardIdle1);
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

    public void update(float delta){
        setPosition(box2body.getPosition().x - getWidth() / 2, box2body.getPosition().y - getHeight() /2);
    }
}
