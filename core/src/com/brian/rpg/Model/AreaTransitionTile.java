package com.brian.rpg.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.brian.rpg.RPG;
import com.brian.rpg.Views.PlayScreen;

public class AreaTransitionTile extends InteractiveTile {
    Player player = screen.getPlayer();

    public AreaTransitionTile(World world, PlayScreen screen, TiledMap map, Rectangle rect) {
        super(world, screen, map, rect);
        this.fixture.setUserData(this);
        //Sets category bit to wall using abstract class method
        setCategoryFilter(RPG.AREATRANSITION_BIT);
    }

    public void onContact(){
        if(body.getPosition().dst(new Vector2(1143, 943)) < 10 ){
            Gdx.app.postRunnable(new Runnable(){
                @Override
                public void run(){
                    player.box2body.setTransform(23, 95, player.box2body.getAngle());
                }
            });
        }else if(body.getPosition().dst(new Vector2(18, 378)) < 10){
            Gdx.app.postRunnable(new Runnable(){
                @Override
                public void run(){
                    player.box2body.setTransform(1798, 748, player.box2body.getAngle());
                }
            });
        }else if(body.getPosition().dst(new Vector2(1801, 755)) < 10){
            Gdx.app.postRunnable(new Runnable(){
                @Override
                public void run(){
                    player.box2body.setTransform(25, 378, player.box2body.getAngle());
                }
            });
        }

    }

    private TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        return layer.getCell((int)(body.getPosition().x / 16), (int)(body.getPosition().y / 16));
    }

}
