package com.brian.rpg.Model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.brian.rpg.RPG;
import com.brian.rpg.Views.PlayScreen;

public class AreaTransitionTile extends InteractiveTile {
    public AreaTransitionTile(World world, PlayScreen screen, TiledMap map, Rectangle rect) {
        super(world, screen, map, rect);
        this.fixture.setUserData(this);
        //Sets category bit to wall using abstract class method
        setCategoryFilter(RPG.AREATRANSITION_BIT);
    }

    public void onContact(){
        screen.playerBody = screen.getPlayer().box2body;
    }

    private TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        return layer.getCell((int)(body.getPosition().x / 16), (int)(body.getPosition().y / 16));
    }

}
