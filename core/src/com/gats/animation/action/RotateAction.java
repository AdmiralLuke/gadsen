package com.gats.animation.action;

import com.badlogic.gdx.math.Vector2;
import com.gats.animation.entity.Entity;
import com.gats.simulation.Path;

/**
 * Simple Animator Action that sets the angle of a Entity.
 */
public class RotateAction extends Action{

    private Entity target;
    private Vector2 angle;
    private float endTime;
    private Path path;

    public RotateAction(float start,Entity target, Vector2 angle) {
        super(start);
        this.target = target;
        this.endTime = start;
        this.angle = angle;
    }
    public RotateAction(float start, Entity target,float duration, Path path){
        super(start);
        this.target = target;
        this.path = path;
        this.endTime = duration+start;
    }

    @Override
    protected void runAction(float oldTime, float current) {

        if(target!=null) {
            if (path != null) {
                float time = Math.min(endTime,current);
                this.target.setRotationAngle(path.getDir(time).angleDeg());
            } else {
                target.setRotationAngle(this.angle.angleDeg());
                endAction(oldTime);
            }
        }

        if(current>endTime){endAction(endTime);}
    }

    public void setTarget(Entity target) {
    this.target = target;
    }
}
