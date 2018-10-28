package com.brian.rpg.Model;

import com.badlogic.gdx.math.Vector2;
import com.brian.rpg.Views.PlayScreen;

public class SpellHorridWilting {
    PlayScreen screen;
    Player player;

    //Spell Stats
    double castDelay = 1.5;

    public SpellHorridWilting(PlayScreen screen, Player player){
        this.screen = screen;
        this.player = player;
    }

    public double castHorridWilting(float createX, float createY, Vector2 velocity){
        HorridWiltingProjectile horridWiltingProjectile;

        if(player.level == 1 || player.level == 2){
            horridWiltingProjectile = new HorridWiltingProjectile(screen, createX, createY, velocity, 10, 6);
            screen.projectilesToRender(horridWiltingProjectile);
        }

        if(player.level >= 3){
            horridWiltingProjectile = new HorridWiltingProjectile(screen, createX, createY, velocity, 10, 6);
            screen.projectilesToRender(horridWiltingProjectile);
        }


        return castDelay;
    }
}
