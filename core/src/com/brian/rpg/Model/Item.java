package com.brian.rpg.Model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.brian.rpg.Views.PlayScreen;

public class Item extends Sprite {
    private PlayScreen screen;
    private World world;
    private Body box2body;
    private Fixture fixture;
    private TextureAtlas itemAtlas;

    private Vector2 spawnPoint;
    private int itemType;

    public Item(PlayScreen screen, Vector2 spawnPoint){
        this.screen = screen;
        this.world = screen.getWorld();
        this.spawnPoint = spawnPoint;
        this.itemAtlas = new TextureAtlas("sprites/Items1.pack");
    }

    private void generateItem(){
        //Pick item
        if(MathUtils.random(1) == 0){
            this.set(new Sprite(itemAtlas.findRegion("book_02c")));
            this.itemType = 0;
        }
        BodyDef bdef = new BodyDef();
        bdef.position.set(spawnPoint);
        bdef.type = BodyDef.BodyType.StaticBody;
        box2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5);

        fdef.shape = shape;
        fixture = box2body.createFixture(fdef);
    }

    public void onContact(){

    }

    public int getItemType(){return itemType;}
}
