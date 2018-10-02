package com.brian.rpg.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.brian.rpg.RPG;
import com.brian.rpg.Sprites.WizardSprite;

public class PlayScreen implements Screen {
    //Reference to RPG game used to set screens
    private RPG game;
    private TextureAtlas wizardSpriteAtlas;

    //Reference to player sprite
    private WizardSprite player;

    //Camera and view variables
    private OrthographicCamera gameCamera;
    private Viewport viewPort;

    //Box2d
    private World world;
    private Box2DDebugRenderer b2dr;

    //Map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    public PlayScreen(RPG game){
        this.game = game;

        wizardSpriteAtlas = new TextureAtlas("sprites/Wizard.pack");

        //Create camera
        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, RPG.V_WIDTH, RPG.V_HEIGHT);

        //This limits the view of the world to 800, 600 so everything is zoomed in
        //enough to see the 16x16 tiles
        //Keeps the screen scaled properly based on the size of the screen
        viewPort = new FitViewport(400, 300, gameCamera);

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

        //Create player
        player = new WizardSprite(world, this );

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //Create wall bodies, so walls have physics
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            body.createFixture(fdef);
        }


        gameCamera.update();
    }

    public void handleInput(float delta){
        //Keyboard controls
        if(Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S)){
            player.box2body.applyLinearImpulse(new Vector2(0, 50), player.box2body.getWorldCenter(), true);
        }else{
            if(!Gdx.input.isKeyPressed(Input.Keys.S)){
                //player.box2body.setLinearVelocity(vel.x, 0);
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)){
            player.box2body.applyLinearImpulse(new Vector2(50, 0), player.box2body.getWorldCenter(), true);
        }else{
            if(!Gdx.input.isKeyPressed(Input.Keys.A)){
                player.box2body.setLinearVelocity(0, player.box2body.getLinearVelocity().y);
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.W)){
            player.box2body.applyLinearImpulse(new Vector2(0, -50), player.box2body.getWorldCenter(), true);
        }else{
            if(!Gdx.input.isKeyPressed(Input.Keys.W)) {
                player.box2body.setLinearVelocity(player.box2body.getLinearVelocity().x, 0);
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)){
            player.box2body.applyLinearImpulse(new Vector2(-50, 0), player.box2body.getWorldCenter(), true);
        }else{
            if(!Gdx.input.isKeyPressed(Input.Keys.D)) {
                player.box2body.setLinearVelocity(0, player.box2body.getLinearVelocity().y);
            }
        }

    }

    public void update(float delta){
        handleInput(delta);

        //Box2 handles physics
        world.step(1/60f, 6, 2);

        //Make game camera follow player
        gameCamera.position.x = player.box2body.getPosition().x;
        gameCamera.position.y = player.box2body.getPosition().y;

        //Update camera every render frame
        gameCamera.update();

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
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height);
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

    }

    public TextureAtlas getWizardSpriteAtlasAtlas(){
        return wizardSpriteAtlas;
    }
}
