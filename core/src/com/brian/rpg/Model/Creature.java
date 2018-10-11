package com.brian.rpg.Model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.brian.rpg.RPG;
import com.brian.rpg.Views.PlayScreen;

public abstract class Creature{
    int hp;
    int mana;
    String gameClass;
    Sprite sprite;
    Vector2 spawnPoint;

    //Animation states
    enum State {IDLE, WALKING, ATTACKING, DEAD};
    enum Direction {LEFT, RIGHT};

    State currentState;
    State previousState;
    Direction currentDirection;
    Direction previousDirection;

    //Used to for collisions
    //Sets fixture to the userdata
    Fixture fixture;

    Boolean deleteFlag = false;


    public World world;
    public PlayScreen screen;
    public Body box2body;

    public Creature(PlayScreen screen, int hp, int mana, String gameClass, Vector2 spawnPoint){
        this.screen = screen;
        this.world = screen.getWorld();
        this.hp = hp;
        this.mana = mana;
        this.gameClass = gameClass;
        this.spawnPoint = spawnPoint;
        this.createCreature();
    }

    //Sets all Box2Body properties on a creature
    public void createCreature(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(spawnPoint);
        bdef.type = BodyDef.BodyType.DynamicBody;
        box2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5);

        fdef.shape = shape;
        fixture = box2body.createFixture(fdef);
    }

    public void onHit(){
        //If not a player destroy the creature
        if(box2body != null){
            screen.bodiesToDelete.add(box2body);
            deleteFlag = true;
            screen.spawnedCreatures.remove(this);
        }

    }

    public abstract void update(float delta);



    public Sprite getSprite(){
        return this.sprite;
    }



}
