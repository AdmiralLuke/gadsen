package com.gats.animation.action;

import com.badlogic.gdx.math.Vector2;
import com.gats.animation.entity.Entity;
import com.gats.animation.entity.SpriteEntity;

/**
 * Simple animator action to scale a SpriteEntity
 */
public class ScaleAction extends Action{
    private Vector2 scale;
    private Entity target;

    public ScaleAction(float start, Entity target, Vector2 scale) {
        super(start);
        this.target = target;
        this.scale = scale;
    }

    @Override
    protected void runAction(float oldTime, float current) {

        if(target!=null){target.setScale(scale);}
        endAction(oldTime);

    }
}
