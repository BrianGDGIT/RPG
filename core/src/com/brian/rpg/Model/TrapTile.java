package com.brian.rpg.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.brian.rpg.RPG;
import com.brian.rpg.Views.PlayScreen;

public class TrapTile extends InteractiveTile {
    public TrapTile(World world, PlayScreen screen, TiledMap map, Rectangle rect){
        super(world, screen, map, rect);
        this.fixture.setUserData(this);

        //setCategoryFilter(RPG.CHEST_BIT);
    }

    public void onContact(){
        final float playerX = screen.getPlayer().box2body.getPosition().x;
        final float playerY = screen.getPlayer().box2body.getPosition().y;
        //Setting tile to null to show trap beneath. I can't get spikes tile id for some reason.
        getCell().setTile(null);
        screen.getPlayer().currentState = Creature.State.DEAD;
    }

    private TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x / 16), (int)(body.getPosition().y / 16));
    }
}
