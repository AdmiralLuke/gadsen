package com.gats.animation.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Gruppiert mehrere Entities.
 * Dabei sollen die absoluten Positionen aller Kinder angepasst werden, wenn sich die Position der Gruppe ändert.
 */
public class EntityGroup extends Entity implements Parent{

    private final List<Entity> children = new ArrayList<>();

    public EntityGroup(Entity... children) {
        for (Entity cur:
             children) {
            add(cur);
        }
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        super.draw(batch, deltaTime, parentAlpha);
        for (Entity child : children) {
            child.draw(batch, deltaTime, parentAlpha);
        }
    }

    @Override
    protected void setPos(Vector2 pos) {
        super.setPos(pos);
        for (Entity child : children) {
            child.updatePos();
        }
    }

    @Override
    public void setRelPos(Vector2 pos) {
        super.setRelPos(pos);
    }


    @Override
    public void add(Entity child){
        if (child == null) return;
        if (child.parent != null) child.parent.remove(this);
        child.setParent(this);
        children.add(child);
    }

    @Override
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

    @Override
    public Entity asEntity() {
        return this;
    }
}
