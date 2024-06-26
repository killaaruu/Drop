package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class   Monster implements Enemy {
    protected Vector2 position;
    protected Animation<TextureRegion> idleAnimation;
    protected Animation<TextureRegion> walkAnimation;
    protected Animation<TextureRegion> attackAnimation;
    protected Animation<TextureRegion> takeHitAnimation;
    protected Animation<TextureRegion> deathAnimation;
    protected float stateTime;
    protected boolean facingRight;
    protected boolean isAttacking;
    protected boolean isWalking;
    protected int health;
    protected boolean isDead;

    public Monster(Vector2 position, int health) {
        this.position = position;
        this.health = health;
        this.stateTime = 0f;
        this.isAttacking = false;
        this.isWalking = false;
        this.facingRight = true;
        this.isDead = false;
    }

    protected Animation<TextureRegion> createAnimation(Texture texture, int frameCols, int frameRows, float frameDuration) {
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / frameCols, texture.getHeight() / frameRows);
        TextureRegion[] frames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        return new Animation<>(frameDuration, new Array<>(frames));
    }

    public void update(float delta, Vector2 playerPosition) {
        stateTime += delta;

        if (position.dst(playerPosition) < 300 && !isDead) {
            // Simple AI: move towards player
            if (playerPosition.x < position.x) {
                position.x -= 100 * delta;
                facingRight = false;
            } else {
                position.x += 100 * delta;
                facingRight = true;
            }
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime, true);

        if (facingRight) {
            batch.draw(currentFrame, position.x, position.y);
        } else {
            batch.draw(currentFrame, position.x + currentFrame.getRegionWidth(), position.y, -currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        }
    }

    @Override
    public boolean isDead() {
        return isDead;
    }

    @Override
    public void takeHit(int damage) {
        health -= damage;
        if (health <= 0) {
            isDead = true;
        }
    }

    @Override
    public void dispose() {
        // Dispose textures and animations
    }

    }

