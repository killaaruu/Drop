package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    private final Drop game;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Player playerCharacter;
    private List<Monster> monsters;
    private Texture levelTexture;
    private BitmapFont font;
    private Music backgroundMusic;
    private boolean bossSpawned;
    private int waveCount;
    private float elapsedTime;
    private CameraController cameraController;

    public GameScreen(final Drop game) {
        this.game = game;
        this.world = new World(new Vector2(0, -9.8f), true);
        this.debugRenderer = new Box2DDebugRenderer();
        this.playerCharacter = new Player();
        this.monsters = new ArrayList<>();
        this.levelTexture = new Texture("C:\\Users\\Ruslan\\Documents\\Drop\\assets\\mountains.png");
        this.font = new BitmapFont();
        this.backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("C:\\Users\\Ruslan\\Documents\\Drop\\assets\\Track3.mp3"));
        this.bossSpawned = false;
        this.waveCount = 0;
        this.elapsedTime = 0f;
        this.cameraController = new CameraController(game.batch, playerCharacter);

        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        elapsedTime += delta;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        playerCharacter.update(delta);
        for (Monster monster : monsters) {
            monster.update(delta, playerCharacter.getPosition());
        }

        cameraController.update();

        game.batch.setProjectionMatrix(cameraController.getCamera().combined);
        game.batch.begin();
        game.batch.draw(levelTexture, 0, 0);
        playerCharacter.render(game.batch);
        for (Monster monster : monsters) {
            monster.render(game.batch);
        }
        game.batch.end();

        debugRenderer.render(world, cameraController.getCamera().combined);

        // Draw UI elements
        game.batch.begin();
        font.draw(game.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 10);
        font.draw(game.batch, "Time: " + (int) elapsedTime, Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 10);
        game.batch.end();

        // Check if it's time to spawn the boss
        if (waveCount >= 3 && !bossSpawned) {
            spawnBoss();
            bossSpawned = true;
        }

        world.step(1 / 60f, 6, 2);
    }

    @Override
    public void resize(int width, int height) {
        cameraController.resize(width, height);
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
        playerCharacter.dispose();
        levelTexture.dispose();
        font.dispose();
        backgroundMusic.dispose();
        for (Monster monster : monsters) {
            monster.dispose();
        }
    }

    private void spawnNextWave() {
        if (waveCount < 3) {
            monsters.clear();
            for (int i = 0; i < 5; i++) {
                monsters.add(new Skeleton(new Vector2(200 + i * 150, 300)));
                monsters.add(new Mushroom(new Vector2(250 + i * 150, 300)));
                monsters.add(new Goblin(new Vector2(300 + i * 150, 300)));
                monsters.add(new FlyingEye(new Vector2(350 + i * 150, 300)));
            }
            waveCount++;
        }
    }

    private void spawnBoss() {
        monsters.clear();
        monsters.add(new MainBoss(new Vector2(1000, 300)));
    }
}
