package com.brian.rpg.Controller;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.*;
import com.brian.rpg.Model.*;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //Determine when a projectile and a creature collide
        if(fixA.getUserData() != null && fixB.getUserData() != null) {
            if (fixA.getUserData() instanceof Projectile || fixB.getUserData() instanceof Projectile) {
                Fixture projectile = fixA.getUserData() instanceof Projectile ? fixA : fixB;
                Fixture object = projectile == fixA ? fixB : fixA;

                //In a collision between a projectile and some other object
                //Determine if the other object is a creature
                //If it is call creatures onHit method to damage/kill it
                if (object.getUserData() != null && Creature.class.isAssignableFrom(object.getUserData().getClass()) && object.getUserData() instanceof Player != true) {
                    ((Creature) object.getUserData()).onHit(((Projectile) projectile.getUserData()).getDamage());
                    ((Projectile) projectile.getUserData()).onHit();
                }

                //Destroy projectile on collision with wall
                if(object.getUserData() != null && object.getUserData() instanceof WallTile){
                    ((Projectile) projectile.getUserData()).onHit();
                }

            }
        }

        //Determine when player and creature collide
        if(fixA.getUserData() != null && fixB.getUserData() != null){
            if(fixA.getUserData() instanceof Player || fixB.getUserData() instanceof Player){
                Fixture player = fixA.getUserData() instanceof Player ? fixA : fixB;
                Fixture object = player == fixA ? fixB : fixA;

                //Determine if other object is a creature
                if(object.getUserData() != null && Creature.class.isAssignableFrom(object.getUserData().getClass())){
                    ((Player) player.getUserData()).onHit();
                }

                if(object.getUserData() != null && Item.class.isAssignableFrom(object.getUserData().getClass())){
                    ((Item) object.getUserData()).onContact();
                }

                //Chest collision
                if(object.getUserData() != null && object.getUserData() instanceof ChestTile){
                    ((ChestTile) object.getUserData()).onContact();
                }

                //Area Transition collion
                if(object.getUserData() != null && object.getUserData() instanceof AreaTransitionTile){
                    ((AreaTransitionTile) object.getUserData()).onContact();
                }

                //Trap Tile collision
                if(object.getUserData() != null && object.getUserData() instanceof TrapTile){
                    ((TrapTile) object.getUserData()).onContact();
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
