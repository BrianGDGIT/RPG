package com.brian.rpg.Model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.brian.rpg.Views.PlayScreen;

public class StaffProjectile extends Projectile {

    public StaffProjectile(World world, PlayScreen screen){
        super(world, screen);
        texture = new Texture("sprites/vortex_spritesheet.png");
        //textureRegion = new TextureRegion()
    }

}
