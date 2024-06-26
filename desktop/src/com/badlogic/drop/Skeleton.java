package com.badlogic.drop;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Skeleton extends Monster {
    private static final int FRAME_COLS = 4;
    private static final int FRAME_ROWS = 2;

    public Skeleton(Vector2 position) {
        super(position);
        loadAnimations();
    }

    private void loadAnimations() {
        Texture idleSheet = new Texture(Gdx.files.internal("skeleton_idle.png"));
        Texture walkSheet = new Texture(Gdx.files.internal("skeleton_walk.png"));
        Texture attackSheet = new Texture(Gdx.files.internal("skeleton_attack.png"));
        Texture shieldSheet = new Texture(Gdx.files.internal("skeleton_shield.png"));
        Texture takeHitSheet = new Texture(Gdx.files.internal("skeleton_take_hit.png"));
        Texture deathSheet = new Texture(Gdx.files.internal("skeleton_death.png"));

        idleAnimation = createAnimation(idleSheet, 0.1f, 4, 1);
        walkAnimation = createAnimation(walkSheet, 0.1f, 4, 1);
        attackAnimation = createAnimation(attackSheet, 0.1f, 8, 1);
        shieldAnimation = createAnimation(shieldSheet, 0.1f, 4, 1);
        takeHitAnimation = createAnimation(takeHitSheet, 0.1f, 4, 1);
        deathAnimation = createAnimation(deathSheet, 0.1f, 4, 1);
    }

    private Animation<TextureRegion> createAnimation(Texture sheet, float frameDuration, int cols, int rows) {
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / cols, sheet.getHeight() / rows);
        TextureRegion[] frames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        return new Animation<>(frameDuration, frames);
    }

    @Override
    public void dispose() {
        // Dispose textures
    }
}
