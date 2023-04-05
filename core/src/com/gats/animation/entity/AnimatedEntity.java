package com.gats.animation.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Verhält sich wie ein SpriteEntity mit dem Unterschied, dass beim draw() Aufruf
 * der korrekte Schritt einer Animation an der absoluten Position des Entity gerendert wird
 */
public class AnimatedEntity extends Entity {
    private Animation<TextureRegion> animation;
    private boolean flipped = false;


    private boolean rotate = false;
    /**
     * When mirror is set to true, the character will be flipped once the angle is 180 or higher on the x axis
     */

    private boolean mirror = false;
    private float accTime = 0;

    private Vector2 size;

    private Vector2 origin = new Vector2();

    public AnimatedEntity(Animation<TextureRegion> animation, Vector2 size) {
        this.animation = animation;
        this.size = size;
    }

    public AnimatedEntity(Animation<TextureRegion> animation, Vector2 size, boolean rotate, boolean mirror) {
        this.animation = animation;
        this.size = size;
        this.rotate = rotate;
        this.mirror = mirror;
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
        //Todo laser not drawn at the proper position when flipped
        //call draw angle to also calculate the flipped value
        float drawAngle = getRotationAngle();
        TextureRegion keyFrame = animation.getKeyFrame(accTime);
        //ToDo: fix rendering
        batch.draw(keyFrame, getPos().x + (flipped ? size.x + origin.x : -origin.x), getPos().y -origin.y, 0, 0, keyFrame.getRegionWidth() , keyFrame.getRegionHeight(), flipped ?-1:1, 1,  drawAngle);
    }


    public void setAnimation(Animation<TextureRegion> animation) {
        if (animation == this.animation) return;
        this.animation = animation;
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public boolean isFlipped() {
        return flipped;
    }

    private void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }

    public void setMirror(boolean mirror) {
        this.mirror = mirror;
    }

    /**
     * Sets the angle of this entity
     *
     * @param angle angle in degrees
     */
    @Override
    public void setRotationAngle(float angle) {
        if (angle >= 90f && angle <= 270f) {
            super.setRotationAngle(rotate ? angle - 180 : 0);
            setFlipped(!mirror);
        } else {
            super.setRotationAngle(rotate ? angle : 0);
            setFlipped(mirror);
        }
    }


    public Vector2 getSize() {
        return new Vector2(size);
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2 origin) {
        this.origin = origin;
    }
}
