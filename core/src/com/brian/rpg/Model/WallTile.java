package com.brian.rpg.Model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.brian.rpg.RPG;

public class WallTile extends InteractiveTile {

    public WallTile(World world, TiledMap map, Rectangle rect){
        super(world, map, rect );
        this.fixture.setUserData(this);
        //Sets category bit to wall using abstract class method
        setCategoryFilter(RPG.WALL_BIT);
    }

}
