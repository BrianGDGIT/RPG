package com.brian.rpg.Model;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.brian.rpg.Views.PlayScreen;

public class OrcEnemy extends Creature {
    float stateTimer = 0;
    float aiTimer = 5;

    //Animations
    private Animation<TextureRegion> orcWalk;

    //Velocity equals target position - current position
    Vector2 velocity;

    public OrcEnemy(PlayScreen screen, int hp, int mana, String gameClass, Vector2 spawnPoint){
        super(screen, hp, mana, gameClass, spawnPoint);
        experienceValue = 10;
        speed = 0.5f;
        fixture.setUserData(this);
        //Setting sprite
        sprite = new Sprite(screen.getMonsters1SpriteAtlas().findRegion("Orc"));
        //Setting Animation
        orcWalk = new Animation<TextureRegion>(0.3f, screen.getMonsters1SpriteAtlas().findRegions("Orc"));
    }

    public OrcEnemy(PlayScreen screen, int hp, int mana, String gameClass, Vector2 spawnPoint, int size){
        super(screen, hp, mana, gameClass, spawnPoint);
        experienceValue = 10 + size;
        speed = 0.5f;
        this.size = size;
        fixture.getShape().setRadius(size / 2.3f);
        fixture.setUserData(this);
        //Setting sprite
        sprite = new Sprite(screen.getMonsters1SpriteAtlas().findRegion("Orc"));
        sprite.setSize(size, size);
        //Setting Animation
        orcWalk = new Animation<TextureRegion>(0.3f, screen.getMonsters1SpriteAtlas().findRegions("Orc"));
    }

    @Override
    public void update(float delta) {
        Vector2 orcPosition = new Vector2(box2body.getPosition().x, box2body.getPosition().y);
        Vector2 playerPosition = new Vector2(screen.getPlayer().box2body.getPosition().x, screen.getPlayer().box2body.getPosition().y);

        //Sets sprite position to center of box2body position so the sprite and the physics body are in the same space
        sprite.setPosition(orcPosition.x - sprite.getWidth() / 2, orcPosition.y - sprite.getHeight() / 2);

        //Wander AI
        if(aiTimer >= 5 && currentState != State.ATTACKING){
            int wanderRangeX = MathUtils.random(100);
            int wanderRangeY = MathUtils.random(100);
            int randomDirectionCheck = MathUtils.random(3);
            //Depending on direction check move the enemy different directions
            //This insures the enemy moves in a variety of directions
            if(randomDirectionCheck == 0) {
                velocity = new Vector2(orcPosition.x + wanderRangeX - orcPosition.x, orcPosition.y + wanderRangeY - orcPosition.y);
            }else if(randomDirectionCheck == 1){
                velocity = new Vector2(orcPosition.x - wanderRangeX - orcPosition.x, orcPosition.y - wanderRangeY - orcPosition.y);
            }else if(randomDirectionCheck == 2){
                velocity = new Vector2(orcPosition.x + wanderRangeX - orcPosition.x, orcPosition.y - wanderRangeY - orcPosition.y);
            }else{
                velocity = new Vector2(orcPosition.x - wanderRangeX - orcPosition.x, orcPosition.y + wanderRangeY - orcPosition.y);
            }
            box2body.setLinearVelocity(velocity.scl(speed));
            aiTimer = 0;
        }

        //Attack AI
        if(orcPosition.dst(playerPosition) < 100) {
            currentState = State.ATTACKING;
            velocity = new Vector2(playerPosition.x - orcPosition.x, playerPosition.y - orcPosition.y);
            box2body.setLinearVelocity(velocity.scl(speed));
        }else if(currentState != State.HASBEENATTACKED){
            currentState = State.IDLE;
        }

        //Update Animation every frame
        sprite.setRegion(orcWalk.getKeyFrame(stateTimer, true));

        stateTimer += delta;
        aiTimer += delta;
    }

    @Override
    public void onHit(int damage){
        //If not a player destroy the creature
        if(box2body != null){
            hp -= damage;
            screen.getGameManager().get("Sounds/orc-34-hit.wav", Sound.class).play();
            if(this.hp <= 0) {
                screen.bodiesToDelete.add(box2body);
                deleteFlag = true;
                screen.spawnedCreatures.remove(this);
                screen.getGameManager().get("Sounds/orc-32-death.wav", Sound.class).play();

                //Award Experience
                screen.getPlayer().awardExperience(this.experienceValue);
                screen.getPlayer().increaseKillCount();
            }
        }
        //Attack player when damaged
        //Prevents player from sniping monsters using Wander AI
        if(currentState != State.ATTACKING){
            Vector2 orcPosition = new Vector2(box2body.getPosition().x, box2body.getPosition().y);
            Vector2 playerPosition = new Vector2(screen.getPlayer().box2body.getPosition().x, screen.getPlayer().box2body.getPosition().y);

            currentState = State.HASBEENATTACKED;
            velocity = new Vector2(playerPosition.x - orcPosition.x, playerPosition.y - orcPosition.y);
            box2body.setLinearVelocity(velocity.scl(speed));
        }
    }
}
