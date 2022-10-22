package com.gats.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Verhält sich wie ein SpriteEntity mit dem Unterschied, dass beim draw() Aufruf
 * der korrekte Schritt einer Animation an der absoluten Position des Entity gerendert wird
 */
public class AnimatedEntity extends Entity{
    private Animation<TextureRegion> animation;

    /**
     * Rendert den korrekten Schritt der Animation
     * @param batch
     */
    @Override
    void draw(Batch batch) {
        super.draw(batch);
        batch.draw(animation.getKeyFrame(System.currentTimeMillis()), getPos().x, getPos().y);
    }
}
