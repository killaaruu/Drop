package com.badlogic.drop;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private static final int FRAME_COLS = 4, FRAME_ROWS = 2;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> attackAnimation;
    private Texture walkSheet;
    private Texture jumpSheet;
    private Texture attackSheet;
    private float stateTime;
    private Vector2 position;
    private boolean facingRight;
    private PlayerState state;

    private enum PlayerState {
        WALKING, JUMPING, ATTACKING, IDLE
    }

    public Player() {
        walkSheet = new Texture(Gdx.files.internal("walk_animation.png"));
        jumpSheet = new Texture(Gdx.files.internal("jump_animation.png"));
        attackSheet = new Texture(Gdx.files.internal("attack_animation.png"));

        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);
        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation<>(0.1f, walkFrames);

        // Similarly initialize jumpAnimation and attackAnimation

        stateTime = 0f;
        position = new Vector2(100, 300);
        facingRight = true;
        state = PlayerState.IDLE;
    }

    public void update(float delta) {
        stateTime += delta;

        // Handle input and update player state and position here
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            position.x += 200 * delta;
            state = PlayerState.WALKING;
            facingRight = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            position.x -= 200 * delta;
            state = PlayerState.WALKING;
            facingRight = false;
        } else if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            state = PlayerState.JUMPING;
        } else if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            state = PlayerState.ATTACKING;
        } else {
            state = PlayerState.IDLE;
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = null;

        switch (state) {
            case WALKING:
                currentFrame = walkAnimation.getKeyFrame(stateTime, true);
                break;
            case JUMPING:
                currentFrame = jumpAnimation.getKeyFrame(stateTime, true);
                break;
            case ATTACKING:
                currentFrame = attackAnimation.getKeyFrame(stateTime, true);
                break;
            case IDLE:
                currentFrame = walkAnimation.getKeyFrame(0); // Assuming idle is the first frame of walk animation
                break;
        }

        if (currentFrame != null) {
            batch.draw(facingRight ? currentFrame : new TextureRegion(currentFrame.getTexture(), currentFrame.getRegionX() + currentFrame.getRegionWidth(), currentFrame.getRegionY(), -currentFrame.getRegionWidth(), currentFrame.getRegionHeight()), position.x, position.y);
        }
    }

    public void dispose() {
        walkSheet.dispose();
        jumpSheet.dispose();
        attackSheet.dispose();
    }
}
