package com.brian.rpg.Model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.brian.rpg.RPG;
import com.brian.rpg.Views.PlayScreen;

public abstract class Creature extends Actor {
    int hp;
    int mana;
    String gameClass;
    Sprite sprite;

    //Animation states
    enum State {IDLE, WALKING, ATTACKING};
    enum Direction {LEFT, RIGHT};

    State currentState;
    State previousState;
    Direction currentDirection;
    Direction previousDirection;


    public World world;
    public PlayScreen screen;
    public Body box2body;

    public Creature(PlayScreen screen, int hp, int mana, String gameClass){
        this.screen = screen;
        this.world = screen.getWorld();
        this.hp = hp;
        this.mana = mana;
        this.gameClass = gameClass;
        this.createCreature();
    }

    //Sets all Box2Body properties on a creature
    public void createCreature(){
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

    public Sprite getSprite(){
        return this.sprite;
    }



}
