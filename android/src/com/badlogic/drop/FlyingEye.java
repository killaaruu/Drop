package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class FlyingEye extends Monster {
    public FlyingEye(Vector2 position) {
        super(position, 60);
        idleAnimation = createAnimation(new Texture("C:\\Users\\Ruslan\\Documents\\Drop\\assets\\Flying eye\\Flight.png"), 8, 1, 0.1f);
        walkAnimation = createAnimation(new Texture("C:\\Users\\Ruslan\\Documents\\Drop\\assets\\Flying eye\\Flight.png"), 8, 1, 0.1f);
        attackAnimation = createAnimation(new Texture("C:\\Users\\Ruslan\\Documents\\Drop\\assets\\Flying eye\\Attack.png"), 8, 1, 0.1f);
        takeHitAnimation = createAnimation(new Texture("C:\\Users\\Ruslan\\Documents\\Drop\\assets\\Flying eye\\Take Hit.png"), 4, 1, 0.1f);
        deathAnimation = createAnimation(new Texture("C:\\Users\\Ruslan\\Documents\\Drop\\assets\\Flying eye\\Death.png"), 4, 1, 0.1f);
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime, true);

        if (isAttacking) {
            currentFrame = attackAnimation.getKeyFrame(stateTime, true);
        } else if (isWalking) {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        }

        if (facingRight) {
            batch.draw(currentFrame, position.x, position.y);
        } else {
            batch.draw(currentFrame, position.x + currentFrame.getRegionWidth(), position.y, -currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        }
    }
}
