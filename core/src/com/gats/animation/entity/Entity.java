package com.gats.animation.entity;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Animations-relevantes Objekt.
 * Dient der Strukturierung der animierten Welt und der einfachen Positionierung geschachtelter Objekte
 */
public class Entity {

    private final List<Entity> children = new ArrayList<>();

    protected Entity parent = null;
    private Vector2 scale = new Vector2(1, 1);
    private Vector2 pos = new Vector2(0, 0);
    private Vector2 relPos = new Vector2(0, 0);

/**
     * rotation angle of the Entity from 0 - 360
     */
    private float angle = 0f;
    private float relAngle = 0f;

    public Entity(){}

    public Entity(Entity... children ){
        addAll(children);
    }


    /**
     * Asks the Entity to execute all required actions, required for drawing its contents.
     * Forwards the call to all of its children.
     * @param batch
     */

    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        for (Entity child : children) {
            child.draw(batch, deltaTime, parentAlpha);
        }
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
            setAngle(parent.angle + relAngle);
        }
    }

    protected void setPos(Vector2 pos) {
        this.pos.set(pos);
    }

    public void updatePos(){
        if (parent == null) setPos(relPos);
        else {
            setPos(parent.getPos().cpy().add(relPos.cpy().rotateDeg(parent.getRotationAngle())));
        }
    }

    public void setRelPos(Vector2 pos) {
        //set the new relative position
        this.relPos.set(pos.cpy());
        updatePos();
    }


    public void addAll(Iterable<Entity> children){
        children.forEach(this::add);
    }

    public void addAll(Entity... children){
        for (Entity cur:children)
            add(cur);
    }

    public void add(Entity child){
        if (child == null) return;
        if (child.parent != null) child.parent.remove(this);
        child.setParent(this);
        children.add(child);
    }

    public void remove(Entity child) {
        if (child == null) return;
        if(children.remove(child)){
            child.setParent(null);
        }
    }

    public void clear(){
        for (Entity cur:
                children) {
            cur.setParent(null);
        }
        children.clear();
    }

    public void setParent(Entity parent){
        this.parent = parent;
        updatePos();
    }

    public Entity getParent() {
        return parent;
    }

}
