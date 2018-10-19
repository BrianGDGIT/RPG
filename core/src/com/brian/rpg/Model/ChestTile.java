package com.brian.rpg.Model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.brian.rpg.RPG;

public class ChestTile extends InteractiveTile{
    public ChestTile(World world, TiledMap map, Rectangle rect){
        super(world, map, rect );
        this.fixture.setUserData(this);
        //Sets category bit to wall using abstract class method
        setCategoryFilter(RPG.CHEST_BIT);
    }

    public void onContact(){
        getCell().setTile(map.getTileSets().getTile(433));
    }

    private TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x / 16), (int)(body.getPosition().y / 16));
    }

}
