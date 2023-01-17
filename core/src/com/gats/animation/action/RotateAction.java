package com.gats.animation.action;

import com.badlogic.gdx.math.Vector2;
import com.gats.animation.entity.Entity;

/**
 * Simple Animator Action that sets the angle of a Entity.
 */
public class RotateAction extends Action{

    private Entity target;
    private Vector2 angle;

    public RotateAction(float start,Entity target, Vector2 angle) {
        super(start);
        this.target = target;
        this.angle = angle;
    }

    @Override
    protected void runAction(float oldTime, float current) {

        if(target!=null){target.setRotationAngle(this.angle.angleDeg());}
        endAction(oldTime);
    }
}
