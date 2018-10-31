package com.brian.rpg.Model;

import com.badlogic.gdx.math.Vector2;
import com.brian.rpg.Views.PlayScreen;

public class SpellAcidCloud {
    PlayScreen screen;
    Player player;

    //Spell Stats
    double castDelay = 1.5;

    public SpellAcidCloud(PlayScreen screen, Player player){
        this.screen = screen;
        this.player = player;
    }

    public double castAcidCloud(float createX, float createY, Vector2 velocity){
        AcidCloudProjectile acidCloudProjectile;

        if(player.level == 1 || player.level == 2){
            acidCloudProjectile = new AcidCloudProjectile(screen, createX, createY, velocity, 6, 45, 3);
            screen.projectilesToRender(acidCloudProjectile);
        }

        if(player.level >= 3){
            acidCloudProjectile = new AcidCloudProjectile(screen, createX, createY, velocity, 6, 65, 4);
            screen.projectilesToRender(acidCloudProjectile);
        }


        return castDelay;
    }
}
