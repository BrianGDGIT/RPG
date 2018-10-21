package com.brian.rpg.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.brian.rpg.Controller.Box2dWorldGenerator;
import com.brian.rpg.Controller.WorldContactListener;
import com.brian.rpg.Model.*;
import com.brian.rpg.RPG;

import java.util.ArrayList;

public class PlayScreen implements Screen {
    //Reference to RPG game used to set screens
    private RPG game;

    //Screens
    InventoryScreen inventory;
    HUD hud;

    //Texture Resources
    private TextureAtlas wizardSpriteAtlas;
    private TextureAtlas monsters1SpriteAtlas;

    //Objects Rendered
    //Reference to player object
    private Player player;
    MonsterSpawner monsterSpawner1;
    MonsterSpawner monsterSpawner2;
    MonsterSpawner monsterSpawner3;
    MonsterSpawner monsterSpawner4;
    MonsterSpawner monsterSpawner5;
    MonsterSpawner monsterSpawner6;
    MonsterSpawner monsterSpawner7;
    MonsterSpawner monsterSpawner8;

    Item item;
    Item item2;

    //Spawn lists
    public ArrayList<Projectile> staffProjectiles = new ArrayList<Projectile>();
    public ArrayList<Creature> spawnedCreatures = new ArrayList<Creature>();
    public ArrayList<Item> spawnedItems = new ArrayList<Item>();
    public ArrayList<Body> bodiesToDelete = new ArrayList<Body>();

    //Camera and view variables
    private OrthographicCamera gameCamera;
    private Viewport viewPort;

    //Box2d
    private World world;
    private Box2dWorldGenerator worldGenerator;
    private Box2DDebugRenderer b2dr;

    //Player body for area transition
    public Body playerBody;

    //Map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    public PlayScreen(RPG game){
        this.game = game;

        //Initializing Texture resources
        wizardSpriteAtlas = new TextureAtlas("sprites/Wizard.pack");
        monsters1SpriteAtlas = new TextureAtlas("sprites/Monsters1.pack");

        //Create camera
        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, RPG.V_WIDTH, RPG.V_HEIGHT);

        //This limits the view of the world to 800, 600 so everything is zoomed in
        //enough to see the 16x16 tiles
        //Keeps the screen scaled properly based on the size of the screen
        viewPort = new FitViewport(296, 144, gameCamera);

        //Create new maploader
        mapLoader = new TmxMapLoader();

        //Create new map
        map = mapLoader.load("maps/level1.tmx");

        //Create mapRenderer, load map file
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        //Initialize Box2d world
        Box2D.init();
        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();
        world.setContactListener(new WorldContactListener());

        //Create player
        player = new Player(this,10, 10, "Wizard", new Vector2(RPG.V_WIDTH / 2, RPG.V_HEIGHT / 2));

        //Create Inventory Screen
        inventory = new InventoryScreen(game, player, game.batch);

        //Create HUD
        hud = new HUD(this);

        //Create MonsterSpawner
        monsterSpawner1 = new MonsterSpawner(this, new Vector2(RPG.V_WIDTH / 2, RPG.V_HEIGHT / 2 + 151), "Normal;");
        monsterSpawner2 = new MonsterSpawner(this, new Vector2(RPG.V_WIDTH / 2 - 300, RPG.V_HEIGHT / 2), "Normal");
        monsterSpawner3 = new MonsterSpawner(this, new Vector2(947, 70), "Normal");
        monsterSpawner4 = new MonsterSpawner(this, new Vector2(331, 589), "Boss");
        monsterSpawner5 = new MonsterSpawner(this, new Vector2(1141, 888), "Normal");
        monsterSpawner6 = new MonsterSpawner(this, new Vector2(1890, 98), "Normal");
        monsterSpawner7 = new MonsterSpawner(this, new Vector2(1818, 147), "Normal");
        monsterSpawner8 = new MonsterSpawner(this, new Vector2(1822, 28), "Boss");

        item = new Item(this, new Vector2(1238, 60));
        item2 = new Item(this, new Vector2(1112,650));

        worldGenerator = new Box2dWorldGenerator(world,this, map);


        gameCamera.update();
    }



    public void update(float delta){
        //Calls Player object method to handle player input every frame
        player.handleInput(delta);

        //Box2 handles physics
        world.step(1/60f, 6, 2);

        //Remove all bodies that need deleted
        removeBodies();

        movePlayer(playerBody);

        //Make game camera follow player
        gameCamera.position.x = player.box2body.getPosition().x;
        gameCamera.position.y = player.box2body.getPosition().y;

        //Update camera every render frame
        gameCamera.update();

        //Update player sprite position every frame
        player.update(delta);

        //Update MonsterSpawners
        monsterSpawner1.update(delta);
        monsterSpawner2.update(delta);
        monsterSpawner3.update(delta);
        monsterSpawner4.update(delta);
        monsterSpawner5.update(delta);
        monsterSpawner6.update(delta);
        monsterSpawner7.update(delta);
        monsterSpawner8.update(delta);

        //Update projectiles
        //Can't use advanced forloop here because update() removes items from the list, which causes concurrentModificationException
        //Only the normal for loop can do this
        if(staffProjectiles != null) {
            for (int i = 0; i < staffProjectiles.size(); i++ ) {
                staffProjectiles.get(i).update();
            }
        }

        //Update creatures
        if(spawnedCreatures != null){
            for(int i = 0; i < spawnedCreatures.size(); i++){
                spawnedCreatures.get(i).update(delta);
            }
        }

        //Render only parts of the map that the camera can currently see
        mapRenderer.setView(gameCamera);


    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Render game map
        mapRenderer.render();

        //Render Box2d debug lines
        b2dr.render(world, gameCamera.combined);

        game.batch.setProjectionMatrix(gameCamera.combined);
        game.batch.begin();

        //Draw the player sprite
        player.getSprite().draw(game.batch);

        //Render projectiles
        if(staffProjectiles != null){
            for(Projectile projectile : staffProjectiles){
                projectile.getSprite().draw(game.batch);
            }

        }

        //Render creatures
        if(spawnedCreatures != null){
            for(Creature creature : spawnedCreatures){
                creature.getSprite().draw(game.batch);
            }
        }

        //Render Items
        if(!spawnedItems.isEmpty()){
            for(Item item : spawnedItems){
                item.getSprite().draw(game.batch);
            }
        }

        game.batch.end();

        //Render Inventory Screen
        if(player.inventoryDisplayed){
            inventory.showCharacterScreen();
            inventory.stage.draw();
        }

        //Render Spellbook
        if(player.spellbookDisplayed){
            inventory.showSpellbook();
            inventory.stage.draw();
        }

        //Render HUD
        hud.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        hud.stage.draw();
        hud.checkHudClicks();
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height);
        hud.stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        wizardSpriteAtlas.dispose();
        monsters1SpriteAtlas.dispose();
        mapRenderer.dispose();
        map.dispose();
    }

    public void removeBodies(){
        if(bodiesToDelete != null){
            for(int i = 0; i < bodiesToDelete.size(); i++){
                world.destroyBody(bodiesToDelete.get(i));
                bodiesToDelete.get(i).equals(null);
                bodiesToDelete.remove(i);
            }
        }
    }

    //Player area transition implementation
    //So player can move after world step
    public void movePlayer(Body playerBody){
        if(playerBody != null){
            playerBody.setTransform(1790, 95, playerBody.getAngle());
            this.playerBody = null;
        }

    }

    public void projectilesToRender(Projectile staffProjectile){
        this.staffProjectiles.add(staffProjectile);
    }

    public void creaturesToRender(Creature creature){
        this.spawnedCreatures.add(creature);
    }

    public void itemsToRender(Item item){
        this.spawnedItems.add(item);
    }

    public TextureAtlas getWizardSpriteAtlas(){
        return wizardSpriteAtlas;
    }

    public TextureAtlas getMonsters1SpriteAtlas(){return monsters1SpriteAtlas;}

    public OrthographicCamera getGameCamera(){
        return gameCamera;
    }

    public Box2dWorldGenerator getWorldGenerator() { return worldGenerator;}

    public World getWorld(){return world;}

    public Player getPlayer(){return player;}

    public AssetManager getGameManager(){return game.getManager();}

    public RPG getGame(){return game;}

    public HUD getHud(){return hud;}
}
