package com.gats.animation.entity;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

/**
 * Animations-relevantes Objekt.
 * Dient der Strukturierung der animierten Welt und der einfachen Positionierung geschachtelter Objekte
 */
public class Entity {
    private Vector2 scale = new Vector2(1, 1);
    private Vector2 pos = new Vector2(0, 0);
    private Vector2 relPos = new Vector2(0, 0);
    /**
     * rotation angle of the Entity from 0 - 360
     */
    private float angle = 0f;
    private float relAngle = 0f;


    protected Parent parent = null;


    /**
     * Fordert das Entity auf alle nötigen Operationen für das Rendering des aktuellen Frames auszuführen.
     * Das Standardverhalten besteht darin nichts zu tun und dient als Interface zur Implementierung in Sub-Klassen.
     *
     * @param batch
     */

    public void draw(Batch batch, float deltaTime, float parentAlpha) {
    }

    public Vector2 getPos() {
        return pos;
    }

    public Vector2 getRelPos() {
        return relPos;
    }
    public float getRotationAngle() {
        return angle;
    }
    public float getRelRotationAngle() {
        return relAngle;
    }

    public void setRelPos(float x, float y) {
        setRelPos(new Vector2(x, y));
    }

    public Vector2 getScale() {
        return scale;
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
    }


    /**
     * Sets the angle of this entity
     * @param angle
     */
    public void setRelRotationAngle(float angle) {
        this.relAngle = angle;
        updateAngle();
    }

    protected void setAngle(float angle) {
        this.angle = angle;
    }

    public void updateAngle(){
        if (parent == null) setAngle(relAngle);
        else {
            setAngle(parent.asEntity().angle + relAngle);
        }
    }

    protected void setPos(Vector2 pos) {
        this.pos.set(pos);
    }

    public void updatePos(){
        if (parent == null) setPos(relPos);
        else {
            Entity parentEntity = parent.asEntity();
            setPos(parentEntity.getPos().cpy().add(relPos.cpy().rotateDeg(parentEntity.getRotationAngle())));
        }
    }

    public void setRelPos(Vector2 pos) {
        //set the new relative position
        this.relPos.set(pos.cpy());
        updatePos();
    }

    public void setParent(Parent parent){
        this.parent = parent;
        updatePos();
    }

    public Parent getParent() {
        return parent;
    }

}
