package com.gats.animation.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gats.animation.entity.Entity;

import java.util.Random;

/**
 * Verhält sich wie ein SpriteEntity mit dem Unterschied, dass beim draw() Aufruf
 * der korrekte Schritt einer Animation an der absoluten Position des Entity gerendert wird
 */
public class AnimatedEntity extends Entity {
    private Animation<TextureRegion> animation;
    private boolean flipped = false;
    private float accTime = 0;

    private Vector2 size;

    public AnimatedEntity(Animation<TextureRegion> animation, Vector2 size) {
        this.animation = animation;
        this.size = size;
    }

    /**
     * Rendert den korrekten Schritt der Animation
     *
     * @param batch       The Batch to draw to
     * @param deltaTime   Time passed since the last frame
     * @param parentAlpha The transparency inherited from its parent
     */
    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        super.draw(batch, deltaTime, parentAlpha);
        accTime += deltaTime;
            batch.draw(animation.getKeyFrame(accTime), getPos().x + (flipped ? size.x : 0), getPos().y, flipped ? -size.x: size.x, size.y);
    }


    public void setAnimation(Animation<TextureRegion> animation) {
        if (animation == this.animation) return;
        accTime = 0;
        this.animation = animation;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }
}
