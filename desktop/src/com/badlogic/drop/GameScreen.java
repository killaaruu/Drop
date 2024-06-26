package com.badlogic.drop;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class GameScreen implements Screen {
    private MyGame game;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Body playerBody;
    private Player playerCharacter;
    private OrthographicCamera camera;
    private Texture levelTexture;

    public GameScreen(MyGame game) {
        this.game = game;
        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();

        playerCharacter = new Player();
        createPlayer();
        createPlatform();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800 / PPM, 480 / PPM);

        levelTexture = new Texture(Gdx.files.internal("level.png"));
    }

    private void createPlayer() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(100 / PPM, 300 / PPM);
        playerBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(16 / PPM, 32 / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.0f;

        playerBody.createFixture(fixtureDef);
        shape.dispose();
    }

    private void createPlatform() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        bodyDef.position.set(100 / PPM, 100 / PPM);
        Body platform = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(100 / PPM, 10 / PPM);

        platform.createFixture(shape, 0.0f);
        shape.dispose();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(1 / 60f, 6, 2);

        playerCharacter.update(delta);

        camera.position.set(playerBody.getPosition().x, playerBody.getPosition().y, 0);
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(levelTexture, 0, 0);
        playerCharacter.render(game.batch);
        game.batch.end();

        debugRenderer.render(world, camera.combined.cpy().scale(PPM, PPM, 0));
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / PPM;
        camera.viewportHeight = height / PPM;
        camera.update();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
        levelTexture.dispose();
        playerCharacter.dispose();
    }

    private static final float PPM = 100;
}
