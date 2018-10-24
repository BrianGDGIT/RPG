package com.brian.rpg.Model;

import com.badlogic.gdx.math.Vector2;
import com.brian.rpg.Views.PlayScreen;

public class SpellAcidBlast {
    PlayScreen screen;
    Player player;

    //Spell Stats
    double castDelay = 1.5;

    public SpellAcidBlast(PlayScreen screen, Player player){
        this.screen = screen;
        this.player = player;
    }

    public double castAcidBlast(float createX, float createY, Vector2 velocity){
        AcidBlastProjectile acidBlastProjectile;

        if(player.level >= 1){
            acidBlastProjectile = new AcidBlastProjectile(screen, createX, createY, velocity);
            screen.projectilesToRender(acidBlastProjectile);
        }

        return castDelay;
    }
}
