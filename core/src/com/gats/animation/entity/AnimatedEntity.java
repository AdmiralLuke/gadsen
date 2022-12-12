package com.gats.animation.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gats.animation.entity.Entity;

/**
 * Verhält sich wie ein SpriteEntity mit dem Unterschied, dass beim draw() Aufruf
 * der korrekte Schritt einer Animation an der absoluten Position des Entity gerendert wird
 */
public class AnimatedEntity extends Entity {
    private final Animation<TextureRegion> animation;
    private float accTime = 0;

    public AnimatedEntity(Animation<TextureRegion> animation) {
        this.animation = animation;
    }

    /**
     * Rendert den korrekten Schritt der Animation
     * @param batch The Batch to draw to
     * @param deltaTime Time passed since the last frame
     * @param parentAlpha The transparency inherited from its parent
     */
    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        super.draw(batch, deltaTime, parentAlpha);
        accTime += deltaTime;
        batch.draw(animation.getKeyFrame(accTime), getPos().x, getPos().y);
    }
    public TextureRegion getTextureRegion(){
        return animation.getKeyFrame(0);
    }
}
