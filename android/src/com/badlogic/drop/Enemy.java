package com.badlogic.drop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public interface Enemy {
    boolean isDead();
    void takeHit(int damage);
    void update(float delta, Vector2 playerPosition);
    void render(SpriteBatch batch);
    void dispose();
}
