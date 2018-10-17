package com.brian.rpg.Model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.brian.rpg.RPG;
import com.brian.rpg.Views.PlayScreen;

public class MonsterSpawner {
    //Create coords
    Vector2 spawnPoint;
    String monsterType = "";

    public World world;
    public PlayScreen screen;
    public Body box2body;

    float timeSinceCreation = 0;
    int spawnInterval = 3;
    int totalSpawns = 0;

    public MonsterSpawner(PlayScreen screen, Vector2 spawnPoint){
        this.screen = screen;
        this.world = screen.getWorld();
        this.spawnPoint = spawnPoint;
        if(MathUtils.random(1) == 0){
            monsterType = "Skeleton";
        }else{
            monsterType = "Orc";
        }
        createMonsterSpawner();
    }

    public void update(float delta){
        Vector2 spawnerPos = box2body.getPosition();
        Vector2 playerPos = screen.getPlayer().box2body.getPosition();

        //Increase spawn counter every frame
        timeSinceCreation += delta;

        if(spawnerPos.dst(playerPos) > 150 && spawnerPos.dst(playerPos) < 450  && timeSinceCreation >= spawnInterval && totalSpawns < 20){
            timeSinceCreation = 0;
            totalSpawns++;
            if(monsterType == "Skeleton") {
                SkeletonEnemy skeleton = new SkeletonEnemy(screen, 10, 0, "Monster", new Vector2(box2body.getPosition().x, box2body.getPosition().y));
                screen.creaturesToRender(skeleton);
            }else if(monsterType == "Orc"){
                OrcEnemy orc = new OrcEnemy(screen, 10, 0, "Monster", new Vector2(box2body.getPosition().x, box2body.getPosition().y));
                screen.creaturesToRender(orc);
            }


        }
    }

    public void createMonsterSpawner(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(spawnPoint);
        bdef.type = BodyDef.BodyType.StaticBody;
        box2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5);

        fdef.shape = shape;
        //So nothing can interact or collide with it
        fdef.isSensor = true;
        box2body.createFixture(fdef);
    }



}
