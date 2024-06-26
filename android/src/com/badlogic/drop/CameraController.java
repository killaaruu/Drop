package com.badlogic.drop;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class CameraController {
    private OrthographicCamera camera;
    private Player player;

    public CameraController(SpriteBatch batch, Player player) {
        this.camera = new OrthographicCamera();
        this.player = player;
    }

    public void update() {
        Vector2 playerPos = player.getPosition();
        camera.position.set(playerPos.x, playerPos.y, 0);
        camera.update();
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
