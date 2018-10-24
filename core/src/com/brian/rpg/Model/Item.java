package com.brian.rpg.Model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.brian.rpg.RPG;
import com.brian.rpg.Views.PlayScreen;

public class Item {
    private PlayScreen screen;
    private World world;
    private Body box2body;
    private Fixture fixture;
    private TextureAtlas itemAtlas;

    Sprite sprite;

    private Vector2 spawnPoint;
    private int itemType;

    public Item(PlayScreen screen, Vector2 spawnPoint){
        this.screen = screen;
        this.world = screen.getWorld();
        this.spawnPoint = spawnPoint;
        itemAtlas = new TextureAtlas("sprites/Items1.pack");
        this.sprite = new Sprite();
        this.sprite.setSize(10, 10);
        if(MathUtils.random(0) == 0){
            generateItem();
        }

    }

    private void generateItem(){
        if(MathUtils.random(1) == 0) {
            this.sprite.setRegion(itemAtlas.findRegion("book_03f"));
            this.itemType = 0;
        }else{
            this.sprite.setRegion(itemAtlas.findRegion("book_02b"));
            this.itemType = 1;
        }

        BodyDef bdef = new BodyDef();
        bdef.position.set(spawnPoint);
        bdef.type = BodyDef.BodyType.StaticBody;
        box2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5);

        fdef.shape = shape;
        fdef.filter.categoryBits = RPG.ITEM_BIT;
        fdef.filter.maskBits = RPG.PLAYER_BIT;

        fixture = box2body.createFixture(fdef);
        fixture.setUserData(this);

        //Sets sprite position to center of box2body position so the sprite and the physics body are in the same space
        this.sprite.setPosition(box2body.getPosition().x - this.sprite.getWidth() / 2, box2body.getPosition().y - this.sprite.getHeight() / 2);

        //Put generated item into PlayScreen array for rendering
        screen.itemsToRender(this);
    }

    public void onContact(){
        if(box2body != null && !screen.bodiesToDelete.contains(box2body)){
            screen.bodiesToDelete.add(box2body);

            if(screen.spawnedItems.contains(this)){
                screen.spawnedItems.remove(this);
            }

            //Give item to player on contact
            switch(this.itemType){
                case 0:
                    if(!screen.getPlayer().spellBook.contains("Fireball")){
                        screen.getPlayer().spellBook.add("Fireball");
                    }
                case 1:
                    if(!screen.getPlayer().spellBook.contains("Acid Blast")){
                        screen.getPlayer().spellBook.add("Acid Blast");
                    }

            }


        }
    }

    public int getItemType(){return this.itemType;}

    public Sprite getSprite(){return this.sprite;}
}
