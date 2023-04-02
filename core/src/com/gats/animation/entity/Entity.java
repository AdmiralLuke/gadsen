package com.gats.animation.entity;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Animations-relevantes Objekt.
 * Dient der Strukturierung der animierten Welt und der einfachen Positionierung geschachtelter Objekte
 */
public class Entity {
    private Vector2 pos = new Vector2(0, 0);
    private Vector2 relPos = new Vector2(0, 0);
/**
     * rotation angle of the Entity from 0 - 360
     */
    private float angle = 0f;


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

    public void setRelPos(float x, float y) {
        setRelPos(new Vector2(x, y));
    }

    /**
     * Sets the angle of this entity
     * @param angle
     */
    public void setRotationAngle(float angle) {
        this.angle = angle;
    }

    protected void setPos(Vector2 pos) {
        System.out.println("Setting pos to" + pos.toString());
        this.pos.set(pos);
    }

    public void setRelPos(Vector2 pos) {

        //move the absolute position by how much the relative position was altered
        this.pos.add(new Vector2(pos).sub(relPos));
        //set the new relative position
        this.relPos.set(new Vector2(pos));
    }

    public Parent getParent() {
        return parent;
    }

}
