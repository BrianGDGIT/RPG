package com.brian.rpg.Controller;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.brian.rpg.Model.AreaTransitionTile;
import com.brian.rpg.Model.ChestTile;
import com.brian.rpg.Model.TrapTile;
import com.brian.rpg.Model.WallTile;
import com.brian.rpg.RPG;
import com.brian.rpg.Views.PlayScreen;

public class Box2dWorldGenerator {
    public World world;
    public PlayScreen screen;


    public Box2dWorldGenerator(World world, PlayScreen screen, TiledMap map){
        this.world = world;
        this.screen = screen;


        //Create wall bodies, so walls have physics
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new WallTile(world, screen, map, rect);
        }

        //Create chest bodies
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new ChestTile(world, screen, map, rect);
        }

        //Create Area Transition bodies
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new AreaTransitionTile(world, screen, map, rect);
        }

        //Create Trap tile bodies
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new TrapTile(world, screen, map, rect);
        }
    }
}
