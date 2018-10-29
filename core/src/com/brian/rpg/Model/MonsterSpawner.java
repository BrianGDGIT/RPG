package com.brian.rpg.Model;

import com.badlogic.gdx.graphics.Color;
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

    private float timeSinceCreation = 0;
    private int spawnInterval = 3;
    private int totalSpawns = 0;
    private int spawnAmount;
    private int spawnerDistanceMin;
    private int monsterSize;
    String spawnerType;

    public MonsterSpawner(PlayScreen screen, Vector2 spawnPoint, String spawnerType, int spawnerDistanceMin, int spawnAmount){
        this.screen = screen;
        this.world = screen.getWorld();
        this.spawnPoint = spawnPoint;
        this.spawnerType = spawnerType;
        this.spawnerDistanceMin = spawnerDistanceMin;
        this.spawnAmount = spawnAmount;
        monsterType = randomizeMonsterType();
        createMonsterSpawner();
    }

    public MonsterSpawner(PlayScreen screen, Vector2 spawnPoint, String spawnerType, int spawnerDistanceMin, String monsterType, int spawnAmount, int monsterSize){
        this.screen = screen;
        this.world = screen.getWorld();
        this.spawnPoint = spawnPoint;
        this.spawnerType = spawnerType;
        this.spawnerDistanceMin = spawnerDistanceMin;
        this.spawnAmount = spawnAmount;
        if(!monsterType.equals("Any")) {
            this.monsterType = monsterType;
        }else{
            this.monsterType = randomizeMonsterType();
        }
        this.monsterSize = monsterSize;
        createMonsterSpawner();
    }

    public void update(float delta){
        Vector2 spawnerPos = box2body.getPosition();
        Vector2 playerPos = screen.getPlayer().box2body.getPosition();

        //Increase spawn counter every frame
        timeSinceCreation += delta;

        if(spawnerType.equals("Normal")) {
            if (spawnerPos.dst(playerPos) > spawnerDistanceMin && spawnerPos.dst(playerPos) < 450 && timeSinceCreation >= spawnInterval && totalSpawns < spawnAmount) {
                timeSinceCreation = 0;
                totalSpawns++;
                if (monsterType == "Skeleton") {
                    SkeletonEnemy skeleton = new SkeletonEnemy(screen, 4, 0, "Monster", new Vector2(box2body.getPosition().x, box2body.getPosition().y));
                    screen.creaturesToRender(skeleton);
                } else if (monsterType == "Orc") {
                    OrcEnemy orc = new OrcEnemy(screen, 6, 0, "Monster", new Vector2(box2body.getPosition().x, box2body.getPosition().y));
                    screen.creaturesToRender(orc);
                } else if(monsterType == "Zombie"){
                    SkeletonEnemy zombie = new SkeletonEnemy(screen, 10, 0, "Monster", new Vector2(box2body.getPosition().x, box2body.getPosition().y), 10, Color.GREEN);
                    screen.creaturesToRender(zombie);
                }
            }
        }

        if (spawnerType.equals("Boss") && spawnerPos.dst(playerPos) > spawnerDistanceMin  && spawnerPos.dst(playerPos) < 350 && totalSpawns < spawnAmount) {
            totalSpawns++;
            if (monsterType == "Skeleton") {
                SkeletonEnemy skeleton = new SkeletonEnemy(screen, 50, 0, "Monster", new Vector2(box2body.getPosition().x, box2body.getPosition().y), monsterSize);
                screen.creaturesToRender(skeleton);
            }else if (monsterType == "Orc") {
                OrcEnemy orc = new OrcEnemy(screen, 70, 0, "Monster", new Vector2(box2body.getPosition().x, box2body.getPosition().y), monsterSize);
                screen.creaturesToRender(orc);
            }else if(monsterType == "Zombie"){
                SkeletonEnemy zombie = new SkeletonEnemy(screen, 100, 0, "Monster", new Vector2(box2body.getPosition().x, box2body.getPosition().y), monsterSize, Color.GREEN);
                screen.creaturesToRender(zombie);
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

    private String randomizeMonsterType(){
        int number = MathUtils.random(2);
        if(number == 0){
            return "Skeleton";
        }else if(number == 1){
            return "Orc";
        }else{
            return "Zombie";
        }
    }



}
