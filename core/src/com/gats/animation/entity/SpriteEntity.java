package com.gats.animation.entity;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gats.animation.entity.Entity;

/**
 * Verhält sich wie ein Entity mit dem Unterschied, dass beim draw() Aufruf
 * eine Textur mit der absoluten Position des Entity gerendert wird
 */
public class SpriteEntity extends Entity {

    private float angle = 0;
    private TextureRegion textureRegion;
    private Vector2 scale = new Vector2(1, 1);
    private Vector2 size = new Vector2(1, 1);

    private Vector2 relPos = new Vector2().Zero;

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
        batch.draw(textureRegion, getPos().x, getPos().y,0,0, size.x,
                size.y, scale.x, scale.y,angle);
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

    public void setRelPos(Vector2 newRelPos){
        this.relPos = new Vector2(newRelPos);
    }

    @Override
    public Vector2 getRelPos() {
        return new Vector2(relPos);
    }

   public Vector2 getPos(){
        return new Vector2(getRelPos().add(super.getPos()));
   }
   public float getRotationAngle() {
        return angle;
    }

    /**
     * Returns the center of the rendered Sprite as a Vector.
     * This is calculated with the {@link SpriteEntity#scale};
     * @return Vector of the sprite center.
     */
    public Vector2 getSpriteCenter()
    {
        if(this.textureRegion!=null){
            return new Vector2(size.x/2f,size.y/2f);
        }
        return new Vector2(0,0);
    }

    public void setRotationAngle(float angle) {
        this.angle = angle;
    }
}
