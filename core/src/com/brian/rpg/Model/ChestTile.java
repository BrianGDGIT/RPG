package com.brian.rpg.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.brian.rpg.RPG;
import com.brian.rpg.Views.PlayScreen;

public class ChestTile extends InteractiveTile{
    boolean itemFlag = false;

    public ChestTile(World world, PlayScreen screen, TiledMap map, Rectangle rect){
        super(world, screen, map, rect);
        this.fixture.setUserData(this);
        //Sets category bit to wall using abstract class method
        setCategoryFilter(RPG.CHEST_BIT);
    }

    public void onContact(){
        final float playerX = screen.getPlayer().box2body.getPosition().x;
        final float playerY = screen.getPlayer().box2body.getPosition().y;
        getCell().setTile(map.getTileSets().getTile(433));

        if(!itemFlag){
            //This allows this method to run after the world simulation in a different thread
           Gdx.app.postRunnable(new Runnable(){
               @Override
               public void run(){
                   Item item = new Item(screen, new Vector2(playerX + 16, playerY));
               }
            });
        }

    }

    private TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x / 16), (int)(body.getPosition().y / 16));
    }

}
