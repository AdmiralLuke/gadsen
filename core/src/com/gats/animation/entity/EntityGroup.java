package com.gats.animation.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Gruppiert mehrere Entities.
 * Dabei sollen die absoluten Positionen aller Kinder angepasst werden, wenn sich die Position der Gruppe ändert.
 */
public class EntityGroup extends Entity implements Parent{

    private final List<Entity> children = new ArrayList<>();


    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        super.draw(batch, deltaTime, parentAlpha);
        for (Entity child : children) {
            child.draw(batch, deltaTime, parentAlpha);
        }
    }

    @Override
    public void setRelPos(Vector2 pos) {
        super.setRelPos(pos);
        for (Entity child : children) {
            child.setPos(new Vector2(this.getPos()).add(child.getRelPos()));
        }
    }


    public void add(Entity child){
        if (child.parent != null) child.parent.remove(child);
        children.add(child);
        child.parent = this;
    }

    @Override
    public void remove(Entity child) {
        children.remove(child);
    }
}