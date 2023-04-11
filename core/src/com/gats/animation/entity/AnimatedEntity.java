package com.gats.animation.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gats.animation.entity.SpriteEntity;

/**
 * Verhält sich wie ein SpriteEntity mit dem Unterschied, dass beim draw() Aufruf
 * der korrekte Schritt einer Animation an der absoluten Position des Entity gerendert wird
 */
public class AnimatedEntity extends SpriteEntity {
    private Animation<TextureRegion> animation;
    private float accTime = 0;



    public AnimatedEntity(Animation<TextureRegion> animation) {
        super(null);
        this.animation = animation;
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
        accTime += deltaTime;
        super.setTextureRegion(animation.getKeyFrame(accTime));
        super.draw(batch, deltaTime, parentAlpha);
    }


    public void setAnimation(Animation<TextureRegion> animation) {
        if (animation == this.animation) return;
        this.animation = animation;
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }


}
