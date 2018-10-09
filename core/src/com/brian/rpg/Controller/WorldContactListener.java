package com.brian.rpg.Controller;

import com.badlogic.gdx.physics.box2d.*;
import com.brian.rpg.Model.Creature;
import com.brian.rpg.Model.SkeletonEnemy;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //Determine when a projectile and a creature collide
        if(fixA.getUserData() != null && fixB.getUserData() != null) {
            if (fixA.getUserData().equals("projectile") || fixB.getUserData().equals("projectile")) {
                Fixture projectile = fixA.getUserData() == "projectile" ? fixA : fixB;
                Fixture object = projectile == fixA ? fixB : fixA;

                //In a collision between a creature and some other object
                //Determine if the other object is a projectile
                //If it is call creatures onHit method to damage/kill it
                if (object.getUserData() != null && Creature.class.isAssignableFrom(object.getUserData().getClass())) {
                    ((Creature) object.getUserData()).onHit();
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
