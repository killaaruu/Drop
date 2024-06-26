package com.badlogic.drop;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class Monster {
    protected Animation<TextureRegion> idleAnimation;
    protected Animation<TextureRegion> walkAnimation;
    protected Animation<TextureRegion> attackAnimation;
    protected Animation<TextureRegion> shieldAnimation;
    protected Animation<TextureRegion> takeHitAnimation;
    protected Animation<TextureRegion> deathAnimation;
    protected float stateTime;
    protected Vector2 position;
    protected boolean facingRight;
    protected int health;
    protected MonsterState state;

    protected enum MonsterState {
        IDLE, WALKING, ATTACKING, SHIELDING, TAKING_HIT, DYING
    }

    public Monster(Vector2 position) {
        this.position = position;
        this.facingRight = true;
        this.health = 100;
        this.state = MonsterState.IDLE;
        this.stateTime = 0f;
    }

    public void update(float delta) {
        stateTime += delta;

        // Implement monster specific AI in subclasses

        if (state == MonsterState.DYING && deathAnimation.isAnimationFinished(stateTime)) {
            // Handle monster death (e.g., remove from game)
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = null;

        switch (state) {
            case IDLE:
                currentFrame = idleAnimation.getKeyFrame(stateTime, true);
                break;
            case WALKING:
                currentFrame = walkAnimation.getKeyFrame(stateTime, true);
                break;
            case ATTACKING:
                currentFrame = attackAnimation.getKeyFrame(stateTime, true);
                break;
            case SHIELDING:
                currentFrame = shieldAnimation.getKeyFrame(stateTime, true);
                break;
            case TAKING_HIT:
                currentFrame = takeHitAnimation.getKeyFrame(stateTime, true);
                break;
            case DYING:
                currentFrame = deathAnimation.getKeyFrame(stateTime, false);
                break;
        }

        if (currentFrame != null) {
            batch.draw(facingRight ? currentFrame : new TextureRegion(currentFrame.getTexture(), currentFrame.getRegionX() + currentFrame.getRegionWidth(), currentFrame.getRegionY(), -currentFrame.getRegionWidth(), currentFrame.getRegionHeight()), position.x, position.y);
        }
    }

    public void takeDamage(int damage) {
        if (state != MonsterState.DYING) {
            health -= damage;
            if (health <= 0) {
                state = MonsterState.DYING;
                stateTime = 0;
            } else {
                state = MonsterState.TAKING_HIT;
                stateTime = 0;
            }
        }
    }

    public abstract void dispose();
}
