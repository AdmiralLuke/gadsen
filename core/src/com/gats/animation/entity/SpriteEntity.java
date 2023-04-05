package com.gats.animation.entity;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Verhält sich wie ein Entity mit dem Unterschied, dass beim draw() Aufruf
 * eine Textur mit der absoluten Position des Entity gerendert wird
 */
public class SpriteEntity extends Entity {

    private TextureRegion textureRegion;

    private Color color = null;
    private Vector2 scale = new Vector2(1, 1);
    private Vector2 size = new Vector2(1, 1);

    private boolean flipped = false;


    private boolean rotate = false;
    /**
     * When mirror is set to true, the character will be flipped once the angle is 180 or higher on the x axis
     */

    private boolean mirror = false;

    private Vector2 origin = new Vector2();

    public SpriteEntity(TextureRegion textureRegion) {
        super();
        this.textureRegion = textureRegion;
    }

    public SpriteEntity(TextureRegion textureRegion, Vector2 relPos, Vector2 size) {
        super();
        this.textureRegion = textureRegion;
        setRelPos(relPos);
        setSize(size);
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        super.draw(batch, deltaTime, parentAlpha);
        if (color != null)
            batch.setColor(color);

        batch.draw(textureRegion,
                getPos().x -origin.x,
                getPos().y -origin.y,
                origin.x,
                origin.y,
                size.x,
                size.y,
                scale.x,
                (flipped ? -1 : 1 ) * (scale.y),
                getRotationAngle());

        //    batch.draw(textureRegion, getPos().x, getPos().y, 0, 0, size.x, size.y, scale.x, scale.y, getRotationAngle());

        if (color != null)
            batch.setColor(Color.WHITE);
    }


    public Vector2 getScale() {
        return scale;
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
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


    /**
     * Returns the center of the rendered Sprite as a Vector.
     * This is calculated with the {@link SpriteEntity#scale};
     *
     * @return Vector of the sprite center.
     */
    public Vector2 getSpriteCenter() {
        if (this.textureRegion != null) {
            return new Vector2(size.x / 2f, size.y / 2f);
        }
        return new Vector2(0, 0);
    }

    /**
     * Will tint the sprite with the specified color during rendering.
     * Null will not tint the sprite.
     *
     * @param color specified tint of the sprite
     */
    public void setColor(Color color) {
        this.color = color;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    /**
     * @return specified tint of the sprite
     */
    public Color getColor() {
        return color;
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
        angle = ((angle % 360) + 360) % 360;
        super.setRotationAngle(rotate ? angle : 0);
        if (angle >= 90f && angle <= 270f) {
            setFlipped(mirror);
        } else {
            setFlipped(false);
        }
    }

}
