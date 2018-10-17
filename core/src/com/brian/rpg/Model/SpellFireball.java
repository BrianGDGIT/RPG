package com.brian.rpg.Model;

import com.badlogic.gdx.math.Vector2;
import com.brian.rpg.Views.PlayScreen;

public class SpellFireball {
    PlayScreen screen;
    Player player;

    public SpellFireball(PlayScreen screen, Player player){
        this.screen = screen;
        this.player = player;
    }

    public void castFireball(float createX, float createY, Vector2 velocity){
        FireballProjectile fireballProjectile;

        if(player.level >= 1){
            fireballProjectile = new FireballProjectile(screen, createX, createY, velocity);
            screen.projectilesToRender(fireballProjectile);
        }
    }
}
