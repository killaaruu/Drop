package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Player {
    private Vector2 position;
    private float speed;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runAnimation;
    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> fallAnimation;
    private Animation<TextureRegion> attack1Animation;
    private Animation<TextureRegion> attack2Animation;
    private Animation<TextureRegion> takeHitAnimation;
    private Animation<TextureRegion> deathAnimation;
    private float stateTime;
    private boolean facingRight;
    private int health;
    private int stamina;
    private boolean isAttacking;
    private boolean isJumping;

    public Player() {
        this.position = new Vector2(100, 300);
        this.speed = 200;
        this.idleAnimation = createAnimation(new Texture("C:\\Users\\Ruslan\\Documents\\Drop\\assets\\Martial Hero\\Sprites\\Idle.png"), 8, 1, 0.1f);
        this.runAnimation = createAnimation(new Texture("C:\\Users\\Ruslan\\Documents\\Drop\\assets\\Martial Hero\\Sprites\\Run.png"), 8, 1, 0.1f);
        this.jumpAnimation = createAnimation(new Texture("C:\\Users\\Ruslan\\Documents\\Drop\\assets\\Martial Hero\\Sprites\\Jump.png"), 4, 1, 0.1f);
        this.fallAnimation = createAnimation(new Texture("C:\\Users\\Ruslan\\Documents\\Drop\\assets\\Martial Hero\\Sprites\\Fall.png"), 4, 1, 0.1f);
        this.attack1Animation = createAnimation(new Texture("C:\\Users\\Ruslan\\Documents\\Drop\\assets\\Martial Hero\\Sprites\\Attack1.png"), 6, 1, 0.1f);
        this.attack2Animation = createAnimation(new Texture("C:\\Users\\Ruslan\\Documents\\Drop\\assets\\Martial Hero\\Sprites\\Attack2.png"), 6, 1, 0.1f);
        this.takeHitAnimation = createAnimation(new Texture("C:\\Users\\Ruslan\\Documents\\Drop\\assets\\Martial Hero\\Sprites\\Take Hit.png"), 4, 1, 0.1f);
        this.deathAnimation = createAnimation(new Texture("C:\\Users\\Ruslan\\Documents\\Drop\\assets\\Martial Hero\\Sprites\\Death.png"), 6, 1, 0.1f);
        this.stateTime = 0f;
        this.facingRight = true;
        this.health = 100;
        this.stamina = 100;
        this.isAttacking = false;
        this.isJumping = false;
    }

    private Animation<TextureRegion> createAnimation(Texture texture, int frameCols, int frameRows, float frameDuration) {
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

    public void update(float delta) {
        stateTime += delta;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            position.x -= speed * delta;
            facingRight = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            position.x += speed * delta;
            facingRight = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && stamina > 0) {
            speed = 300;
            stamina -= 1;
        } else {
            speed = 200;
            if (stamina < 100) stamina += 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !isJumping) {
            isJumping = true;
            position.y += speed * delta; // simple jump logic, should be replaced with proper physics
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            isAttacking = true;
        } else {
            isAttacking = false;
        }

        // Update y position for gravity effect
        if (position.y > 100) {
            position.y -= speed * delta * 0.5f;
        } else {
            isJumping = false;
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime, true);

        if (isAttacking) {
            currentFrame = attack1Animation.getKeyFrame(stateTime, true);
        } else if (isJumping) {
            currentFrame = jumpAnimation.getKeyFrame(stateTime, true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            currentFrame = runAnimation.getKeyFrame(stateTime, true);
        }

        if (facingRight) {
            batch.draw(currentFrame, position.x, position.y);
        } else {
            batch.draw(currentFrame, position.x + currentFrame.getRegionWidth(), position.y, -currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public void dispose() {
        // Dispose textures and animations
    }
}
