package com.brian.rpg.Model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.brian.rpg.Views.PlayScreen;

public class SkeletonEnemy extends Creature {
    private static final int FRAME_COLS = 8;
    private static final int FRAME_ROWS = 8;

    Texture texture;

    public SkeletonEnemy(PlayScreen screen, int hp, int mana, String gameClass){
        super(screen, hp, mana, gameClass);

        texture = new Texture("maps/tilesets/0x72_DungeonTilesetII_v1.1.png");

        //Use split function to create an array of Textures
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / FRAME_COLS,
                texture.getHeight() / FRAME_ROWS);

        //Place the regions into an array
        TextureRegion[] skeletonFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for(int i = 0; i < FRAME_ROWS; i++){
            for(int j = 0; j < FRAME_COLS; j++){
                skeletonFrames[index++] = tmp[i][j];
            }
        }
    }
}
