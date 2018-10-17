package com.brian.rpg.Controller;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.brian.rpg.Model.WallTile;
import com.brian.rpg.RPG;

public class Box2dWorldGenerator {
    public World world;


    public Box2dWorldGenerator(World world, TiledMap map){
        this.world = world;


        //Create wall bodies, so walls have physics
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new WallTile(world, map, rect);
        }
    }
}
