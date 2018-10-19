package com.brian.rpg.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.brian.rpg.Views.PlayScreen;

public class SpellMagicMissile {
    private PlayScreen screen;
    private Player player;

    //Spell related
    private double castDelay = 1;

    public SpellMagicMissile(PlayScreen screen, Player player){
        this.screen = screen;
        this.player = player;
    }

    public double castMagicMissile(float createX, float createY, Vector2 velocity){
        StaffProjectile staffProjectile;


        //Projectile 1
        if(player.level == 1) {
            staffProjectile = new StaffProjectile(screen, createX, createY, velocity);
            screen.projectilesToRender(staffProjectile);
        }

        if(player.level == 2){
            staffProjectile = new StaffProjectile(screen, createX, createY, velocity);
            screen.projectilesToRender(staffProjectile);

            staffProjectile = new StaffProjectile(screen, createX + 5, createY, velocity);
            screen.projectilesToRender(staffProjectile);
        }

        if(player.level >= 3){
            //Projectile 1
            staffProjectile = new StaffProjectile(screen, createX + 5, createY, velocity);
            screen.projectilesToRender(staffProjectile);
            //Projectile2
            staffProjectile = new StaffProjectile(screen, createX + 7, createY, velocity);
            screen.projectilesToRender(staffProjectile);

            //Projectile3
            staffProjectile = new StaffProjectile(screen, createX + 9, createY, velocity);
            screen.projectilesToRender(staffProjectile);
        }

        return castDelay;
    }
}
